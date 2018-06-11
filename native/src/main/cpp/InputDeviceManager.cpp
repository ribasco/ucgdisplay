//
// Created by raffy on 5/29/18.
//



#include <shared_mutex>
#include <utility>
#include <vector>
#include <dirent.h>
#include <set>
#include <sys/inotify.h>
#include <queue>
#include <libudev.h>

#include "InputDeviceManager.h"
#include "InputDevHelper.h"
#include "InputEventManager.h"

#define JNI_VERSION JNI_VERSION_1_8
using namespace std;

static map<string, jobject> deviceInfoCache;
volatile bool initialized = false;
volatile bool stopping = false;
static JavaVM *cachedJVM;
static jobject jThreadGroup;
static JavaVMAttachArgs jAttachArgs;

unique_ptr<promise<void>> inputMonitorPromise;
unique_ptr<promise<void>> deviceMonitorPromise;
unique_ptr<InputEventManager> eventManager;

//Function prototypes
bool deviceInputFilter(int fd, const string &path);

void deviceInputEventHandler(device_input_event event);

void onDeviceAdded(device_entry *entry);

void onDeviceRemoved(int fd, const string &path);

void refreshDevices();

void inputMonitorFunction(future<void> futureObj);

void deviceStateMonitorFunction(future<void> futureObj);

void processDeviceStateChange(struct udev_device *dev);

jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    cachedJVM = jvm;

    JNIEnv *env;
    jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

    jclass clsThreadGroup = env->FindClass(CLS_THREADGROUP);
    jmethodID midThreadGroupCtr = env->GetMethodID(clsThreadGroup, "<init>", "(Ljava/lang/String;)V");
    jThreadGroup = env->NewGlobalRef(env->NewObject(clsThreadGroup, midThreadGroupCtr));

    jAttachArgs.version = JNI_VERSION_1_8;
    jAttachArgs.name = const_cast<char *>("input-event-monitor");
    jAttachArgs.group = jThreadGroup;

    inputMonitorPromise = make_unique<promise<void>>();
    deviceMonitorPromise = make_unique<promise<void>>();

    //Initialize input event manager
    eventManager = make_unique<InputEventManager>(deviceInputEventHandler, deviceInputFilter);
    eventManager->setOnDeviceAdded(onDeviceAdded);
    eventManager->setOnDeviceRemoved(onDeviceRemoved);

    //cout << "Populating input device cache for the first time" << endl;

    //Populate devices
    refreshDevices();

    //cout << "Total Devices Found: " << eventManager->size() << endl;

    return JNI_VERSION;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);
    eventManager->clear();
}

jobject Java_com_ibasco_pidisplay_core_system_InputDeviceManager_queryDevice(JNIEnv *env, jclass cls, jstring devPath) {
    string devicePath = string(env->GetStringUTFChars(devPath, 0));
    auto res = deviceInfoCache.find(devicePath);
    if (res != deviceInfoCache.end())
        return res->second;
    return nullptr;
}

jobjectArray Java_com_ibasco_pidisplay_core_system_InputDeviceManager_getInputDevices(JNIEnv *env, jclass cls) {
    if (deviceInfoCache.empty())
        return nullptr;

    jclass clsInputDev = env->FindClass(CLS_INPUT_DEVICE);
    jobjectArray result = env->NewObjectArray(static_cast<jsize>(deviceInfoCache.size()), clsInputDev, nullptr);

    int ctr = 0;
    for (auto cachedEntry : deviceInfoCache) {
        env->SetObjectArrayElement(result, ctr++, cachedEntry.second);
    }
    return result;
}

void Java_com_ibasco_pidisplay_core_system_InputDeviceManager_startInputEventMonitor(JNIEnv *env, jclass cls) {
    if (initialized) {
        throwIOException(env, string("Monitor already started"));
        return;
    }

    //Fetch std::future object associated with promise
    future<void> futDim = inputMonitorPromise->get_future();
    future<void> futDsm = deviceMonitorPromise->get_future();

    // Starting Thread & move the future object in lambda function by reference
    thread monitorThread(inputMonitorFunction, move(futDim));
    monitorThread.detach();

    //Initialize device state monitor service
    thread devStateMonitorThread(deviceStateMonitorFunction, move(futDsm));
    devStateMonitorThread.detach();

    initialized = true;
}

void Java_com_ibasco_pidisplay_core_system_InputDeviceManager_stopInputEventMonitor(JNIEnv *env, jclass cls) {
    if (initialized && !stopping) {
        stopping = true;
        eventManager->stopMonitor();
        inputMonitorPromise->set_value();
        deviceMonitorPromise->set_value();
    }
}

void Java_com_ibasco_pidisplay_core_system_InputDeviceManager_refreshDeviceCache(JNIEnv *env, jclass cls) {
    refreshDevices();
}

/**
 * Handle Device Input Events
 *
 * @param event
 */
void deviceInputEventHandler(device_input_event event) {
    // in the new thread:
    JNIEnv *env;
    cachedJVM->AttachCurrentThread((void **) &env, &jAttachArgs);

    jclass clsInputDevManager = env->FindClass(CLS_INPUT_DEVICE_MGR);
    jclass clsRawInputEvent = env->FindClass(CLS_RAW_INPUT_EVENT);
    jmethodID midRawInputEventCtr = env->GetMethodID(clsRawInputEvent, "<init>", MIDSIG_RAWINPUTEVENT_CTR);
    jmethodID midCallback = env->GetStaticMethodID(clsInputDevManager, "inputEventCallback", MIDSIG_INPUTDEVMGR_CALLBACK);


    auto cachedDev = deviceInfoCache.find(event.device);

    jobject jInputDevice = nullptr;

    if (cachedDev != deviceInfoCache.end()) {
        jInputDevice = cachedDev->second;
    }

    jstring jCodeName = env->NewStringUTF(event.codeName.c_str());
    jstring jTypeName = env->NewStringUTF(event.typeName.c_str());
    jobject jRawEvent = env->NewObject(clsRawInputEvent,
                                       midRawInputEventCtr,
                                       jInputDevice,
                                       event.time.tv_sec,
                                       event.type,
                                       event.code,
                                       event.value,
                                       jCodeName,
                                       jTypeName,
                                       event.repeatCount);

    //Invoke callback method
    env->CallStaticVoidMethod(clsInputDevManager, midCallback, jRawEvent);
}

void invokeDeviceStateCallback(JNIEnv *env, const string &device, string action) {

    auto cachedDev = deviceInfoCache.find(device);

    //Make sure that the device is valid
    if (cachedDev == deviceInfoCache.end()) {
        return;
    }

    jobject jInputDevice = cachedDev->second;

    jclass clsInputDevManager = env->FindClass(CLS_INPUT_DEVICE_MGR);
    jclass clsDeviceStateEvent = env->FindClass(CLS_DEVICE_STATE_EVENT);
    jmethodID midDevStateEvCtr = env->GetMethodID(clsDeviceStateEvent, "<init>", "(Lcom/ibasco/pidisplay/core/system/InputDevice;Ljava/lang/String;)V");
    jmethodID midCallback = env->GetStaticMethodID(clsInputDevManager, "deviceStateEventCallback", "(Lcom/ibasco/pidisplay/core/system/DeviceStateEvent;)V");

    jstring jAction = env->NewStringUTF(action.c_str());
    jobject jDevEvent = env->NewObject(clsDeviceStateEvent,
                                       midDevStateEvCtr,
                                       jInputDevice,
                                       jAction);

    //Invoke callback method
    env->CallStaticVoidMethod(clsInputDevManager, midCallback, jDevEvent);
}

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

void onDeviceAdded(device_entry *entry) {
    if (deviceInfoCache.count(entry->path) == 0) {
        // in the new thread:
        JNIEnv *env;
        cachedJVM->AttachCurrentThread((void **) &env, &jAttachArgs);
        jobject jDeviceInfo = createInputDeviceInfo(env, entry->evt->ev_fd, entry->path);
        if (jDeviceInfo != nullptr) {
            deviceInfoCache.insert(make_pair(entry->path, env->NewGlobalRef(jDeviceInfo)));
            //cout << "Device '" << entry->path << "' has been ADDED to the cache" << endl;
        }
    }
}

void onDeviceRemoved(int fd, const string &path) {
    // in the new thread:
    JNIEnv *env;
    cachedJVM->AttachCurrentThread((void **) &env, &jAttachArgs);

    auto res = deviceInfoCache.find(path);
    //bool deref = false;
    if (res != deviceInfoCache.end()) {
        if (env->GetObjectRefType(res->second) == jobjectRefType::JNIGlobalRefType) {
            env->DeleteGlobalRef(res->second);
            //deref = true;
        }
        deviceInfoCache.erase(res);
        //cout << "Device '" << path << "' has been REMOVED from the cache. Dereferenced: " << string(deref ? "Yes" : "No") << endl;
    }
}

void refreshDevices() {
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
    // in the new thread:
    JNIEnv *env;
    cachedJVM->AttachCurrentThread((void **) &env, &jAttachArgs);

    //Start monitoring for input
    eventManager->startMonitor();
    futureObj.wait();

    if (cachedJVM != nullptr) {
        cachedJVM->DetachCurrentThread();
        initialized = false;
        stopping = false;
    }
}


void deviceStateMonitorFunction(future<void> futureObj) {
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