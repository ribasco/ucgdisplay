//
// Created by raffy on 5/29/18.
//

#include "InputDeviceManager.h"
#include "InputDevHelper.h"

#define CLS_INPUT_DEVICE "com/ibasco/pidisplay/core/system/InputDevice"
#define CLS_INPUT_EVENT_TYPE "com/ibasco/pidisplay/core/system/InputEventType"
#define CLS_INPUT_EVENT_CODE "com/ibasco/pidisplay/core/system/InputEventCode"
#define CLS_INPUT_DEVICE_MGR "com/ibasco/pidisplay/core/system/InputDeviceManager"
#define CLS_RAW_INPUT_EVENT "com/ibasco/pidisplay/core/system/RawInputEvent"

#define CLS_IOEXCEPTION "java/io/IOException"
#define CLS_ARRAYLIST "java/util/ArrayList"
#define CLS_HASHMAP  "java/util/HashMap"
#define CLS_INTEGER "java/lang/Integer"
#define CLS_THREADGROUP "java/lang/ThreadGroup"
//#define CLS_STRING "java/lang/String"

#define FSIG_INPUTEVENTCODE_ABSDATA "Ljava/util/Map;"
#define MIDSIG_INPUTDEVICE_CTR "(Ljava/lang/String;Ljava/lang/String;[SLjava/lang/String;Ljava/util/List;)V"
#define MIDSIG_INPUTEVENTTYPE_CTR1 "(Ljava/lang/String;ILjava/util/List;Z)V"
#define MIDSIG_INPUTEVENTCODE_CTR1 "(Ljava/lang/String;I)V"
#define MIDSIG_INPUTEVENTCODE_CTR2 "(Ljava/lang/String;II)V"
#define MIDSIG_INPUTDEVMGR_CALLBACK "(Lcom/ibasco/pidisplay/core/system/RawInputEvent;)V"
#define MIDSIG_RAWINPUTEVENT_CTR "(Lcom/ibasco/pidisplay/core/system/InputDevice;JIIILjava/lang/String;Ljava/lang/String;)V"
#define MIDSIG_HASHMAP_PUT "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
#define MIDSIG_HASHMAP_CTR "()V"

using namespace std;

static map<string, int> inputDeviceHandles;
static map<string, jobject> inputDeviceCache;

// Create a std::promise object
promise<void> exSig;
JavaVM *cachedJVM;
static jobject jThreadGroup;
volatile bool initialized = false;
volatile bool stopping = false;

jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    cachedJVM = jvm;

    JNIEnv *env;
    jvm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_8);

    jclass clsThreadGroup = env->FindClass(CLS_THREADGROUP);
    jmethodID midThreadGroupCtr = env->GetMethodID(clsThreadGroup, "<init>", "(Ljava/lang/String;)V");
    jThreadGroup = env->NewGlobalRef(env->NewObject(clsThreadGroup, midThreadGroupCtr));

    return JNI_VERSION_1_8;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
    if (!inputDeviceCache.empty()) {
        JNIEnv* env;
        vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_8);

        auto it = inputDeviceCache.begin();
        while (it != inputDeviceCache.end()) {
            env->DeleteGlobalRef(it->second);
        }
    }
}

jobject Java_com_ibasco_pidisplay_core_system_InputDeviceManager_queryDevice(JNIEnv *env, jclass cls, jstring devPath) {
    const char *devicePath = env->GetStringUTFChars(devPath, 0);
    return getInputDevice(env, string(devicePath));
}

jint Java_com_ibasco_pidisplay_core_system_InputDeviceManager_open(JNIEnv *env, jclass cls, jstring devPath) {
    const char *devicePath = env->GetStringUTFChars(devPath, 0);
    int fd = openDevice(env, string(devicePath));//open(devicePath, O_RDONLY);
    if (fd < 0) {
        string msg = string("Unable to open file (Error ") + to_string(fd) + string(")");
        throwIOException(env, msg);
        return fd;
    }
    return fd;
}

void Java_com_ibasco_pidisplay_core_system_InputDeviceManager_close(JNIEnv *env, jclass cls, jint fd) {
    if (fd >= 0)
        closeDevice(env, fd);
}

jobjectArray Java_com_ibasco_pidisplay_core_system_InputDeviceManager_getInputDevices(JNIEnv *env, jclass cls) {
    vector<string> devices;
    int devCount = getDevices(devices);

    if (devCount <= 0)
        return nullptr;

    jclass clsInputDev = env->FindClass(CLS_INPUT_DEVICE);
    jobjectArray result = env->NewObjectArray(devCount, clsInputDev, nullptr);

    int ctr = 0;
    for (int i = 0; i < devices.size(); i++) {
        string devicePath = devices.at(i);
        jobject obj = getInputDevice(env, devicePath);
        if (obj != nullptr)
            env->SetObjectArrayElement(result, ctr++, obj);
    }

    return result;
}

void Java_com_ibasco_pidisplay_core_system_InputDeviceManager_startMonitor(JNIEnv *env, jclass cls) {
    if (initialized) {
        throwIOException(env, string("Monitor already started"));
        return;
    }

    //Fetch std::future object associated with promise
    future<void> futureObj = exSig.get_future();

    // Starting Thread & move the future object in lambda function by reference
    thread monitorThread(inputMonitorFunction, move(futureObj));
    monitorThread.detach();

    initialized = true;
}

void Java_com_ibasco_pidisplay_core_system_InputDeviceManager_stopMonitor(JNIEnv *env, jclass cls) {
    if (initialized && !stopping) {
        stopping = true;
        exSig.set_value();
    }
}

void throwIOException(JNIEnv *env, string msg) {
    jclass clsEx = env->FindClass(CLS_IOEXCEPTION);
    env->ThrowNew(clsEx, msg.c_str());
}

// Reads the list of files in |directory_path| into |filenames|.
void listDirectory(JNIEnv *env, const string &directory_path, vector<string> *filenames) {
    if (!filenames) {
        printf("Error: filenames is null.\n");
        exit(1);
    }
    DIR *directory = opendir(directory_path.c_str());
    if (!directory) {
        throwIOException(env, string("Failed to to open directory: ") + directory_path);
        return;
    }
    struct dirent *entry = NULL;
    while ((entry = readdir(directory))) {
        if (strcmp(entry->d_name, ".") && strcmp(entry->d_name, "..")) {
            filenames->push_back(directory_path + "/" + entry->d_name);
        }
    }
}

string getDevicePathFromFd(int fd) {
    auto findResult = find_if(begin(inputDeviceHandles), end(inputDeviceHandles), [&](const pair<string, int> &pair) {
        return pair.second == fd;
    });

    string foundKey; // You might want to initialise this to a value you know is invalid in your map
    //int foundValue = -1;
    if (findResult != std::end(inputDeviceHandles)) {
        foundKey = findResult->first;
        //foundValue = findResult->second;
    }
    return foundKey;
}

int openDevice(JNIEnv *env, string devicePath) {
    if ((inputDeviceHandles.find(devicePath) != inputDeviceHandles.end()) && inputDeviceHandles.at(devicePath) > 0) {
        return inputDeviceHandles.at(devicePath);
    }

    int fd = open(devicePath.c_str(), O_RDONLY);
    if (fd < 0) {
        jclass clsEx = env->FindClass(CLS_IOEXCEPTION);
        char msg[256];
        sprintf(msg, "Unable to open file '%s' (Error %d)", devicePath, fd);
        env->ThrowNew(clsEx, msg);
        return -1;
    }

    inputDeviceHandles.insert(pair<string, int>(devicePath, fd));

    return fd;
}

void closeDevice(JNIEnv *env, int fd) {
    if (close(fd) < 0) {
        throwIOException(env, string("Unable to close device"));
    }
    string devicePath = getDevicePathFromFd(fd);
    if (devicePath.empty()) {
        throwIOException(env, string("Could not locate device path of the file resource in the cache"));
        return;
    }
    inputDeviceHandles.erase(devicePath);
}

/**
 * Retrieves an InputDevice instance based on the specified device path.
 *
 * @param env
 * @param devicePath
 * @return
 */
jobject getInputDevice(JNIEnv *env, const string &devicePath) {
    //Does the object exist in the cache?
    if (inputDeviceCache.find(devicePath) != inputDeviceCache.end()) {
        return inputDeviceCache.at(devicePath);
    }
    int fd = openDevice(env, devicePath);
    jobject ret = getInputDevice(env, fd);
    closeDevice(env, fd);
    return ret;
}

/**
 * Retrieves the InputDevice instance from the File Descriptor.
 *
 * @param env
 * @param fd
 * @return
 */
jobject getInputDevice(JNIEnv *env, int fd) {
    string devicePath = getDevicePathFromFd(fd);

    if (devicePath.empty()) {
        throwIOException(env,
                         string("Unable to find device path from the cache. Make sure to open the device resource via openDevice()"));
        return nullptr;
    }

    if (fd < 0) {
        char msg[256];
        sprintf(msg, "Invalid file handle (Error %d)", fd);
        throwIOException(env, string(msg));
        return nullptr;
    }

    char deviceName[256] = "Unknown";
    ioctl(fd, EVIOCGNAME(sizeof(deviceName)), deviceName);
    short ids[4];
    ioctl(fd, EVIOCGID, ids);
    int driverVersion;
    ioctl(fd, EVIOCGVERSION, &driverVersion);

    unsigned long bit[EV_MAX][NBITS(KEY_MAX)];
    unsigned long state[KEY_CNT] = {0};

    memset(bit, 0, sizeof(bit));
    ioctl(fd, EVIOCGBIT(0, EV_MAX), bit[0]);

    int have_state, stateval;
    unsigned int type, code;

    jclass clsInputEventType = env->FindClass(CLS_INPUT_EVENT_TYPE);
    jclass clsInputEventCode = env->FindClass(CLS_INPUT_EVENT_CODE);
    jclass clsArrayList = env->FindClass(CLS_ARRAYLIST);
    jclass clsHashMap = env->FindClass(CLS_HASHMAP);
    jclass clsInteger = env->FindClass(CLS_INTEGER);

    jmethodID midArrayListCtr = env->GetMethodID(clsArrayList, "<init>", "()V");
    jmethodID midArrayListAdd = env->GetMethodID(clsArrayList, "add", "(Ljava/lang/Object;)Z");
    jmethodID midEventTypeCtr = env->GetMethodID(clsInputEventType, "<init>", MIDSIG_INPUTEVENTTYPE_CTR1);
    jmethodID midEventCodeCtr1 = env->GetMethodID(clsInputEventCode, "<init>", MIDSIG_INPUTEVENTCODE_CTR1);
    jmethodID midEventCodeCtr2 = env->GetMethodID(clsInputEventCode, "<init>", MIDSIG_INPUTEVENTCODE_CTR2);
    jmethodID midHashMapCtr = env->GetMethodID(clsHashMap, "<init>", MIDSIG_HASHMAP_CTR);
    jfieldID fidAbsData = env->GetFieldID(clsInputEventCode, "absData", FSIG_INPUTEVENTCODE_ABSDATA);
    jmethodID midHashMapPut = env->GetMethodID(clsHashMap, "put", MIDSIG_HASHMAP_PUT);
    jmethodID midIntegerCtr = env->GetMethodID(clsInteger, "<init>", "(I)V");
    jobject jEventTypeList = env->NewObject(clsArrayList, midArrayListCtr);

    for (type = 0; type < EV_MAX; type++) {
        //Check if the type is supported by the device
        if (test_bit(type, bit[0]) && (type != EV_REP)) {

            have_state = (get_state(fd, type, state, sizeof(state)) == 0);

            if (type == EV_SYN)
                continue;
            ioctl(fd, EVIOCGBIT(type, KEY_MAX), bit[type]);

            jobject jaEventCodeList = env->NewObject(clsArrayList, midArrayListCtr);

            for (code = 0; code < KEY_MAX; code++) {
                if (test_bit(code, bit[type])) {
                    jobject jEventCode;
                    jstring codeName = env->NewStringUTF(codename(type, code).c_str());

                    if (have_state) {
                        stateval = test_bit(code, state);
                        jEventCode = env->NewObject(clsInputEventCode, midEventCodeCtr2, codeName, code, stateval);
                    } else {
                        jEventCode = env->NewObject(clsInputEventCode, midEventCodeCtr1, codeName, code);
                    }

                    //Do we have ABS data?
                    if (type == EV_ABS) {
                        int abs[6] = {0};
                        ioctl(fd, EVIOCGABS(code), abs);
                        //Create hashmap
                        jobject jAbsDataMap = env->NewObject(clsHashMap, midHashMapCtr);

                        //Store the abs key/value pair to the hashmap
                        for (int k = 0; k < 6; k++) {
                            if ((k < 3) || abs[k]) {
                                jstring absKey = env->NewStringUTF(absval.at(k).c_str());
                                jobject absValue = env->NewObject(clsInteger, midIntegerCtr, abs[k]);
                                //Put abs key/value data
                                env->CallObjectMethod(jAbsDataMap, midHashMapPut, absKey, absValue);
                            }
                        }
                        //Store the abs data map to the event code instance
                        env->SetObjectField(jEventCode, fidAbsData, jAbsDataMap);
                    }

                    env->CallBooleanMethod(jaEventCodeList, midArrayListAdd, jEventCode);
                }
            }

            jstring jTypeName = env->NewStringUTF(typname(type).c_str());
            //New InputEventType
            jobject jInputEventType = env->NewObject(clsInputEventType, midEventTypeCtr, jTypeName, type,
                                                     jaEventCodeList, have_state);
            //Add to eventType list
            env->CallBooleanMethod(jEventTypeList, midArrayListAdd, jInputEventType);
        }
    }

    if (test_bit(EV_REP, bit[0])) {

    }

    char versionStr[32];
    sprintf(versionStr, "%d.%d.%d", driverVersion >> 16, (driverVersion >> 8) & 0xff, driverVersion & 0xff);

    jstring jPath = env->NewStringUTF(devicePath.c_str());
    jstring jName = env->NewStringUTF(deviceName);
    jshortArray jIds = env->NewShortArray(4);
    env->SetShortArrayRegion(jIds, 0, 4, ids);
    jstring jDriverVersion = env->NewStringUTF(versionStr);

    jclass clsInputDev = env->FindClass(CLS_INPUT_DEVICE);
    jmethodID midInputDevCtr = env->GetMethodID(clsInputDev, "<init>", MIDSIG_INPUTDEVICE_CTR);
    jobject objInputDevice = env->NewObject(clsInputDev, midInputDevCtr, jName, jPath, jIds, jDriverVersion,
                                            jEventTypeList);

    //Create a global reference so that we can cache it
    jobject jGInputDevice = env->NewGlobalRef(objInputDevice);
    inputDeviceCache.insert(pair<string, jobject>(devicePath, jGInputDevice));

    return jGInputDevice;
}

void inputMonitorFunction(future<void> futureObj) {
    // in the new thread:
    JNIEnv *env;
    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_8;
    args.name = const_cast<char *>("input-monitor");
    args.group = jThreadGroup;
    cachedJVM->AttachCurrentThread((void **) &env, &args);

    jclass clsInputDevManager = env->FindClass(CLS_INPUT_DEVICE_MGR);
    jclass clsRawInputEvent = env->FindClass(CLS_RAW_INPUT_EVENT);
    jmethodID midRawInputEventCtr = env->GetMethodID(clsRawInputEvent, "<init>", MIDSIG_RAWINPUTEVENT_CTR);
    jmethodID midCallback = env->GetStaticMethodID(clsInputDevManager, "inputEventCallback", MIDSIG_INPUTDEVMGR_CALLBACK);

    vector<string> filenames;
    listDirectory(env, DEV_INPUT_EVENT, &filenames);

    vector<pollfd> poll_fds;

    for (auto &filename : filenames) {
        int fd = openDevice(env, filename);

        bool proceed = (HasEventType(fd, EV_KEY) && HasSpecificKey(fd, KEY_B)) || HasEventType(fd, EV_REL) ||
                       HasEventType(fd, EV_ABS);

        if (!proceed)
            continue;

        poll_fds.push_back(pollfd{fd, POLLIN, 0});
    }

    if (poll_fds.empty()) {
        throwIOException(env, string("No valid input devices detected"));
        if (cachedJVM != nullptr)
            cachedJVM->DetachCurrentThread();
        return;
    }

    // Number of repeated events + 1.
    int count = 1;

    // Events accumulated for each device.
    vector<vector<input_event>> events(poll_fds.size());

    //Start polling
    while (futureObj.wait_for(chrono::milliseconds(1)) == future_status::timeout) {
        // Wait for data to be available on one of the file descriptors without  timeout (-1).
        poll(poll_fds.data(), poll_fds.size(), -1);

        for (size_t i = 0; i < poll_fds.size(); i++) {
            if (poll_fds[i].revents & POLLIN) { // NOLINT
                int fd = poll_fds[i].fd;

                jobject jInputDevice = getInputDevice(env, fd);

                struct input_event event{};

                //Read input event
                if (read(fd, &event, sizeof(event)) != sizeof(event)) {
                    throwIOException(env, string("Failed to read an event"));
                    if (cachedJVM != nullptr)
                        cachedJVM->DetachCurrentThread();
                    return;
                }

                // When receiving a key event for B, add it to the list of events.
                // Don't process it yet as there might be other events in that report.
                if (event.type != EV_SYN && event.type != EV_MSC) {
                    events[i].push_back(event);
                }

                // A SYN_REPORT event signals the end of a report, process all the
                // previously accumulated events.
                // At that point we only have events for B in the event vector.
                if (event.type == EV_SYN && event.code == SYN_REPORT) {
                    for (auto &it : events[i]) {
                        jstring jCodeName = env->NewStringUTF(codename(it.type, it.code).c_str());
                        jstring jTypeName = env->NewStringUTF(typname(it.type).c_str());
                        jobject jRawEvent = env->NewObject(clsRawInputEvent, midRawInputEventCtr, jInputDevice, it.time.tv_sec, it.type, it.code, it.value, jCodeName, jTypeName);
                        env->CallStaticVoidMethod(clsInputDevManager, midCallback, jRawEvent);
                    }

                    // Discard all processed events.
                    events[i].clear();
                }
            }
        }
    }

    if (cachedJVM != nullptr) {
        cachedJVM->DetachCurrentThread();
        initialized = false;
        stopping = false;
    }
}