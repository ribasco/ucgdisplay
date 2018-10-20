/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native Library
 * Filename: InputDeviceManager.cpp
 * 
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
#include <shared_mutex>
#include <utility>
#include <vector>
#include <dirent.h>
#include <queue>

#define USE_UDEV false

#if defined(__linux__)

#endif

#if USE_UDEV
#include <libudev.h>
#else
#include <sys/inotify.h>
#endif

#include "InputDeviceManager.h"
#include "InputDevHelper.h"
#include "InputEventManager.h"
#include "Global.h"

#define EVENT_SIZE  ( sizeof (struct inotify_event) )
#define BUF_LEN     ( 1024 * ( EVENT_SIZE + 16 ) )
#define DEVINPUT_DIR_PATH "/dev/input"

using namespace std;

static map<string, jobject> deviceInfoCache;
JavaVMAttachArgs jAttachArgs;

volatile bool initialized = false;
volatile bool stopping = false;

static unique_ptr<promise<void>> inputMonitorPromise;
static unique_ptr<promise<void>> deviceMonitorPromise;
static unique_ptr<InputEventManager> eventManager;

//Cached objects
static jobject jThreadGroup;

//Cached classes
static jclass clsThreadGroup;
static jclass clsInputDevManager;
static jclass clsRawInputEvent;
static jclass clsInputEventType;
static jclass clsInputEventCode;
static jclass clsArrayList;
static jclass clsHashMap;
static jclass clsInteger;
static jclass clsDeviceStateEvent;
static jclass clsInputDev;

//Cached method ids
static jmethodID midArrayListCtr;
static jmethodID midArrayListAdd;
static jmethodID midEventTypeCtr;
static jmethodID midEventCodeCtr1;
static jmethodID midEventCodeCtr2;
static jmethodID midHashMapCtr;
static jmethodID midHashMapPut;
static jmethodID midIntegerCtr;
static jmethodID midRawInputEventCtr;
static jmethodID midInputEventCallback;
static jmethodID midInputDevCtr;
static jmethodID midDevStateEventCtr;
static jmethodID midDevStateEventCb;

//Cached field ids
static jfieldID fidAbsData;

//Function prototypes
bool deviceInputFilter(int fd, const string &path);

void deviceInputEventHandler(device_input_event event);

void onDeviceAdded(device_entry *entry);

void onDeviceRemoved(int fd, const string &path);

void refreshDevices();

void inputMonitorFunction(future<void> futureObj);

void deviceStateMonitor(future<void> futureObj);

void processDeviceStateChange(struct udev_device *dev);

void invokeDeviceStateCallback(JNIEnv *env, const string &device, const string &action);

jobject createInputDeviceInfo(JNIEnv *env, int fd, const string &devicePath);

void InputDevManager_Load(JNIEnv *env) {
    //START: Initialize global references for classes and methods
    jclass tmpThreadGroup = env->FindClass(CLS_THREADGROUP);
    clsThreadGroup = (jclass) env->NewGlobalRef(tmpThreadGroup);
    env->DeleteLocalRef(tmpThreadGroup);

    jclass tmpInputDevMan = env->FindClass(CLS_INPUT_DEVICE_MGR);
    clsInputDevManager = (jclass) env->NewGlobalRef(tmpInputDevMan);
    env->DeleteLocalRef(tmpInputDevMan);

    jclass tmpRawInputEvent = env->FindClass(CLS_RAW_INPUT_EVENT);
    clsRawInputEvent = (jclass) env->NewGlobalRef(tmpRawInputEvent);
    env->DeleteLocalRef(tmpRawInputEvent);

    jclass tmpInputEventType = env->FindClass(CLS_INPUT_EVENT_TYPE);
    clsInputEventType = (jclass) env->NewGlobalRef(tmpInputEventType);
    env->DeleteLocalRef(tmpInputEventType);

    jclass tmpInputEventCode = env->FindClass(CLS_INPUT_EVENT_CODE);
    clsInputEventCode = (jclass) env->NewGlobalRef(tmpInputEventCode);
    env->DeleteLocalRef(tmpInputEventCode);

    jclass tmpArrayList = env->FindClass(CLS_ARRAYLIST);
    clsArrayList = (jclass) env->NewGlobalRef(tmpArrayList);
    env->DeleteLocalRef(tmpArrayList);

    jclass tmpHashMap = env->FindClass(CLS_HASHMAP);
    clsHashMap = (jclass) env->NewGlobalRef(tmpHashMap);
    env->DeleteLocalRef(tmpHashMap);

    jclass tmpInteger = env->FindClass(CLS_INTEGER);
    clsInteger = (jclass) env->NewGlobalRef(tmpInteger);
    env->DeleteLocalRef(tmpInteger);

    jclass tmpDevStateEvent = env->FindClass(CLS_DEVICE_STATE_EVENT);
    clsDeviceStateEvent = (jclass) env->NewGlobalRef(tmpDevStateEvent);
    env->DeleteLocalRef(tmpDevStateEvent);

    jclass tmpInputDev = env->FindClass(CLS_INPUT_DEVICE);
    clsInputDev = (jclass) env->NewGlobalRef(tmpInputDev);
    env->DeleteLocalRef(tmpInputDev);

    midRawInputEventCtr = env->GetMethodID(clsRawInputEvent, "<init>", MIDSIG_RAWINPUTEVENT_CTR);
    midInputEventCallback = env->GetStaticMethodID(clsInputDevManager, "inputEventCallback", MIDSIG_INPUTDEVMGR_CALLBACK);
    midInputDevCtr = env->GetMethodID(clsInputDev, "<init>", MIDSIG_INPUTDEVICE_CTR);

    midArrayListCtr = env->GetMethodID(clsArrayList, "<init>", "()V");
    midArrayListAdd = env->GetMethodID(clsArrayList, "add", "(Ljava/lang/Object;)Z");
    midEventTypeCtr = env->GetMethodID(clsInputEventType, "<init>", MIDSIG_INPUTEVENTTYPE_CTR1);
    midEventCodeCtr1 = env->GetMethodID(clsInputEventCode, "<init>", MIDSIG_INPUTEVENTCODE_CTR1);
    midEventCodeCtr2 = env->GetMethodID(clsInputEventCode, "<init>", MIDSIG_INPUTEVENTCODE_CTR2);
    midHashMapCtr = env->GetMethodID(clsHashMap, "<init>", MIDSIG_HASHMAP_CTR);
    midHashMapPut = env->GetMethodID(clsHashMap, "put", MIDSIG_HASHMAP_PUT);
    midIntegerCtr = env->GetMethodID(clsInteger, "<init>", "(I)V");

    midDevStateEventCtr = env->GetMethodID(clsDeviceStateEvent, "<init>", MIDSIG_DEVSTATEVT_CTR);
    midDevStateEventCb = env->GetStaticMethodID(clsInputDevManager, "deviceStateEventCallback", MIDSIG_INPUTDEVMGR_DEVEVENTCB);
    jmethodID midThreadGroupCtr = env->GetMethodID(clsThreadGroup, "<init>", "(Ljava/lang/String;)V");

    fidAbsData = env->GetFieldID(clsInputEventCode, "absData", FSIG_INPUTEVENTCODE_ABSDATA);
    //END: Initialize global references for classes and methods

    jstring threadGroupName = env->NewStringUTF("pi-disp-input");
    jobject lThreadGroup = env->NewObject(clsThreadGroup, midThreadGroupCtr, threadGroupName);
    jThreadGroup = env->NewGlobalRef(lThreadGroup);
    env->DeleteLocalRef(lThreadGroup);

    jAttachArgs.version = JNI_VERSION_1_8;
    jAttachArgs.name = const_cast<char *>("input-event-monitor");
    jAttachArgs.group = jThreadGroup;

    inputMonitorPromise = make_unique<promise<void>>();
    deviceMonitorPromise = make_unique<promise<void>>();

    //Initialize input event manager
    eventManager = make_unique<InputEventManager>(deviceInputEventHandler, deviceInputFilter);
    eventManager->setOnDeviceAdded(onDeviceAdded);
    eventManager->setOnDeviceRemoved(onDeviceRemoved);

    refreshDevices();
}

void InputDevManager_UnLoad(JNIEnv *env) {
    eventManager->clear();
    env->DeleteGlobalRef(jThreadGroup);
    env->DeleteGlobalRef(clsInputDevManager);
    env->DeleteGlobalRef(clsRawInputEvent);

    for (auto it : deviceInfoCache) {
        env->DeleteGlobalRef(it.second);
    }

    deviceInfoCache.clear();
}

jobject Java_com_ibasco_ucgdisplay_core_input_InputDeviceManager_queryDevice(JNIEnv *env, jclass cls, jstring devPath) {
    string devicePath = string(env->GetStringUTFChars(devPath, 0));
    auto res = deviceInfoCache.find(devicePath);
    if (res != deviceInfoCache.end())
        return res->second;
    return nullptr;
}

jobjectArray Java_com_ibasco_ucgdisplay_core_input_InputDeviceManager_getInputDevices(JNIEnv *env, jclass cls) {
    if (deviceInfoCache.empty())
        return nullptr;

    jobjectArray result = env->NewObjectArray(static_cast<jsize>(deviceInfoCache.size()), clsInputDev, nullptr);
    int ctr = 0;
    for (auto cachedEntry : deviceInfoCache) {
        env->SetObjectArrayElement(result, ctr++, cachedEntry.second);
    }
    return result;
}

void Java_com_ibasco_ucgdisplay_core_input_InputDeviceManager_startInputEventMonitor(JNIEnv *env, jclass cls) {
    if (initialized) {
        JNI_throwIOException(env, string("Monitor already started"));
        return;
    }

    //Fetch std::future object associated with promise
    future<void> futDim = inputMonitorPromise->get_future();
    future<void> futDsm = deviceMonitorPromise->get_future();

    // Starting Thread & move the future object in lambda function by reference
    thread monitorThread(inputMonitorFunction, move(futDim));
    monitorThread.detach();

    //Initialize device state monitor service
    thread devStateMonitorThread(deviceStateMonitor, move(futDsm));
    devStateMonitorThread.detach();

    initialized = true;
}

void Java_com_ibasco_ucgdisplay_core_input_InputDeviceManager_stopInputEventMonitor(JNIEnv *env, jclass cls) {
   if (initialized && !stopping) {
        stopping = true;
        eventManager->stopMonitor();
        inputMonitorPromise->set_value();
        deviceMonitorPromise->set_value();
    }
}

void Java_com_ibasco_ucgdisplay_core_input_InputDeviceManager_refreshDeviceCache(JNIEnv *env, jclass cls) {
    refreshDevices();
}

/**
 * Filter supported input devices
 *
 * @param fd The file descriptor
 * @param path  The input device path
 * @return True if the device is accepted
 */
bool deviceInputFilter(int fd, const string &path) {
    return (HasEventType(fd, EV_KEY) && HasSpecificKey(fd, KEY_B)) || HasEventType(fd, EV_REL) || HasEventType(fd, EV_ABS);
}

/**
 * Handle Device Input Events
 *
 * @param event
 */
void deviceInputEventHandler(device_input_event event) {

    JNIEnv *env;
    cachedJVM->GetEnv((void **) &env, JNI_VERSION);

    jobject *jInputDevice = nullptr;

    auto cachedDeviceInfo = deviceInfoCache.find(event.device);
    if (cachedDeviceInfo != deviceInfoCache.end()) {
        jInputDevice = &cachedDeviceInfo->second;
    }

    //cout << "EVENT: " << event.typeName << endl;
    jlong jSeconds = event.time.tv_sec;
    jint jType = event.type;
    jint jCode = event.code;
    jint jValue = event.value;

    jstring jCodeName = env->NewStringUTF(event.codeName.c_str());
    jstring jTypeName = env->NewStringUTF(event.typeName.c_str());

    jobject jRawInputEvent = env->NewObject(clsRawInputEvent,
                                            midRawInputEventCtr,
                                            (jInputDevice != nullptr) ? *jInputDevice : nullptr,
                                            jSeconds,
                                            jType,
                                            jCode,
                                            jValue,
                                            jCodeName,
                                            jTypeName,
                                            event.repeatCount);

    env->CallStaticVoidMethod(clsInputDevManager, midInputEventCallback, jRawInputEvent);

    if (env->ExceptionCheck()) {
        cerr << "Error: deviceInputEventHandler -> CallStaticVoidMethod()";
        return;
    }

    env->DeleteLocalRef(jCodeName);
    env->DeleteLocalRef(jTypeName);
    env->DeleteLocalRef(jRawInputEvent);
    //cachedJVM->DetachCurrentThread();*/
}

/**
 * Retrieves the InputDevice instance from the File Descriptor.
 *
 * @param env
 * @param fd
 * @return
 */
jobject createInputDeviceInfo(JNIEnv *env, int fd, const string &devicePath) {

    if (fd < 0 || !is_valid_fd(fd)) {
        JNI_throwIOException(env, string("createInputDeviceInfo: Invalid file descriptor"));
        return nullptr;
    }

    if (devicePath.empty()) {
        JNI_throwIOException(env, string("createInputDeviceInfo: Device path must not be empty"));
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

    int have_state;
    unsigned long stateval;
    unsigned int type, code;

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
            jobject jInputEventType = env->NewObject(clsInputEventType, midEventTypeCtr, jTypeName, type, jaEventCodeList, have_state);
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

    jobject tmpInputDevice = env->NewObject(clsInputDev, midInputDevCtr, jName, jPath, jIds, jDriverVersion, jEventTypeList);
    jobject inputDevice = env->NewGlobalRef(tmpInputDevice);
    env->DeleteLocalRef(tmpInputDevice);

    return inputDevice;
}

void onDeviceAdded(device_entry *entry) {
    //cout << "Device added: " << entry->path << endl;
    if (deviceInfoCache.count(entry->path) == 0) {
        JNIEnv *env;
        cachedJVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);
        jobject deviceInfo = createInputDeviceInfo(env, entry->evt->ev_fd, entry->path);
        if (deviceInfo != nullptr) {
            deviceInfoCache.insert(make_pair(entry->path, deviceInfo));
            //cout << "Device add success " << endl;
        }
        invokeDeviceStateCallback(env, entry->path, "add");
        return;
    }
    //cerr << "onDeviceRemoved: Could not add device to cache" << endl;
}

void onDeviceRemoved(int fd, const string &path) {
    //cout << "Device removed: " << path << endl;
    auto res = deviceInfoCache.find(path);
    if (res != deviceInfoCache.end()) {
        JNIEnv *env;
        cachedJVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);
        //invoke callback before we delete from cache
        invokeDeviceStateCallback(env, path, "remove");

        //Delete global reference
        if (env->GetObjectRefType(res->second) == jobjectRefType::JNIGlobalRefType) {
            env->DeleteGlobalRef(res->second);
        }

        //Delete from cache
        deviceInfoCache.erase(res);

        //cout << "Device remove success " << endl;
        return;
    }
    //cerr << "onDeviceRemoved: Could not delete device from cache" << endl;
}

void refreshDevices() {
    //cout << "Refreshing Input Devices Yo" << endl;
    vector<string> inputDevices;
    listInputDevices(inputDevices);

    if (inputDevices.empty())
        return;

    //Clear existing entries
    if (eventManager->size() > 0) {
        eventManager->clear();
    }

    //Initialize device info cache
    for (const auto &device : inputDevices) {
        eventManager->add(device);
    }
}

/**
 * Monitor incoming input events
 *
 * @param futureObj
 */
void inputMonitorFunction(future<void> futureObj) {
    JNIEnv *env;
    cachedJVM->AttachCurrentThread((void **) &env, &jAttachArgs);
    //Start monitoring for input
    eventManager->startMonitor();
    futureObj.wait();

    initialized = false;
    stopping = false;
    cachedJVM->DetachCurrentThread();
}

/**
 * Invokes the device state change callback of InputDeviceManager
 *
 * @param env
 * @param device
 * @param action
 */
void invokeDeviceStateCallback(JNIEnv *env, const string &device, const string &action) {
    auto cachedDev = deviceInfoCache.find(device);

    //Make sure that the device is valid
    jobject *jInputDevice = nullptr;
    if (cachedDev != deviceInfoCache.end()) {
        jInputDevice = &cachedDev->second;
    }

    jstring jAction = env->NewStringUTF(action.c_str());
    jobject jDevEvent = env->NewObject(clsDeviceStateEvent, midDevStateEventCtr, jInputDevice != nullptr ? *jInputDevice : nullptr, jAction);

    //Invoke callback method
    env->CallStaticVoidMethod(clsInputDevManager, midDevStateEventCb, jDevEvent);

    //Release references
    env->DeleteLocalRef(jAction);
    env->DeleteLocalRef(jDevEvent);
}

#ifdef _LIBUDEV_H_
void processDeviceStateChange(JNIEnv *env, struct udev_device *dev) {
    const char *a = udev_device_get_action(dev);
    if (!a)
        a = "exists";

    const char *v = udev_device_get_sysattr_value(dev, "idVendor");
    if (!v)
        v = "0000";

    const char *p = udev_device_get_sysattr_value(dev, "idProduct");
    if (!p)
        p = "0000";

    string action(a);
    string vendor(v);
    string product(p);
    string devtype(defaultVal(udev_device_get_devtype(dev)));
    string subsystem(udev_device_get_subsystem(dev));
    string devpath(udev_device_get_devpath(dev));
    string syspath(udev_device_get_syspath(dev));
    string device_name(udev_device_get_devnode(dev));

    //cout << hex << this_thread::get_id() << " Device: " << device_name << ", Action: " << a << ", Dev Type: " << devtype << ", Dev Path: " << devpath << endl;

    if (action == "add") {
        eventManager->add(device_name, true);
        invokeDeviceStateCallback(env, device_name, action);
    } else if (action == "remove") {
        if (!eventManager->isValid(device_name)) {
            invokeDeviceStateCallback(env, device_name, action);
            eventManager->remove(device_name);
        }
    }
}

//udev
void deviceStateMonitor(future<void> futureObj) {
    struct udev *udev = udev_new();
    if (!udev) {
        fprintf(stderr, "udev_new() failed\n");
        return;
    }

    struct udev_monitor *mon = udev_monitor_new_from_netlink(udev, "udev");

    udev_monitor_filter_add_match_subsystem_devtype(mon, "input", nullptr);
    udev_monitor_enable_receiving(mon);

    int fd = udev_monitor_get_fd(mon);

    JNIEnv *env;
    cachedJVM->AttachCurrentThread((void **) &env, &jAttachArgs);

    timeval timeout = {1, 0};

    while (futureObj.wait_for(chrono::milliseconds(1)) == future_status::timeout) {
        fd_set fds;
        FD_ZERO(&fds);
        FD_SET(fd, &fds);

        int ret = select(fd + 1, &fds, nullptr, nullptr, &timeout);

        if (ret <= 0) {
            this_thread::sleep_for(chrono::milliseconds(100));
            continue;
        }

        if (FD_ISSET(fd, &fds)) {
            struct udev_device *dev = udev_monitor_receive_device(mon);
            if (dev) {
                if (udev_device_get_devnode(dev))
                    processDeviceStateChange(env, dev);
                udev_device_unref(dev);
            }
        }
    }

    if (cachedJVM != nullptr)
        cachedJVM->DetachCurrentThread();
}
#else

//inotify
void deviceStateMonitor(future<void> futureObj) {
    int fd, wd, length, i = 0;
    char buffer[BUF_LEN];

    fd = inotify_init();

    if (fd < 0) {
        perror("inotify_init");
        exit(errno);
    }

    JNIEnv *env;
    cachedJVM->AttachCurrentThread((void **) &env, &jAttachArgs);

    wd = inotify_add_watch(fd, DEVINPUT_DIR_PATH, IN_ATTRIB);
    auto pfd = pollfd{fd, POLLIN, 0};

    while (futureObj.wait_for(chrono::milliseconds(1)) == future_status::timeout) {
        poll(&pfd, 1, 10);

        if (pfd.revents & POLLIN) {
            //Read input event
            length = static_cast<int>(read(fd, buffer, BUF_LEN));

            if (length < 0) {
                cerr << "Error: There was a problem reading file descriptor (" << string(strerror(errno)) << ")" << endl;
                continue;
            }

            while (i < length) {
                auto *event = (struct inotify_event *) &buffer;
                string devicePath = string(DEVINPUT_DIR_PATH) + "/" + string(event->name);

                if (event->len) {
                    if (event->mask & IN_ISDIR)
                        continue;
                    if (event->mask & IN_ATTRIB) {
                        if (file_exists(devicePath)) {
                            if (!is_readable(devicePath))
                                continue;
                            if (!eventManager->exists(devicePath)) {
                                eventManager->add(devicePath, true);
                            }
                        } else {
                            if (!eventManager->isValid(devicePath) || eventManager->exists(devicePath)) {
                                eventManager->remove(devicePath);
                            }
                        }
                    }
                }
                i += EVENT_SIZE + event->len;
            }
            i = 0;
        }
    } //while

    (void) inotify_rm_watch(fd, wd);
    (void) close(fd);

    if (cachedJVM != nullptr)
        cachedJVM->DetachCurrentThread();
}

#endif

