//
// Created by raffy on 5/30/18.
//

#include "InputDevHelper.h"

bool file_exists(const string &name) {
    struct stat buffer;
    return (stat (name.c_str(), &buffer) == 0);
}

int is_valid_fd(int fd) {
    return fcntl(fd, F_GETFD) != -1 || errno != EBADF;
}


uint8_t HasEventType(int fd, int type) {
    unsigned long evbit = 0;
    // Get the bit field of available event types.
    ioctl(fd, EVIOCGBIT(0, sizeof(evbit)), &evbit);
    return static_cast<uint8_t>(evbit & (1 << type));
}

// Returns true iff the device reports EV_KEY events.
uint8_t HasKeyEvents(int device_fd) {
    unsigned long evbit = 0;
    // Get the bit field of available event types.
    ioctl(device_fd, EVIOCGBIT(0, sizeof(evbit)), &evbit);
    return static_cast<uint8_t>(evbit & (1 << EV_KEY));
}

// Returns true iff the given device has |key|.
uint8_t HasSpecificKey(int device_fd, unsigned int key) {
    size_t nchar = KEY_MAX / 8 + 1;
    unsigned char bits[nchar];
    // Get the bit fields of available keys.
    ioctl(device_fd, EVIOCGBIT(EV_KEY, sizeof(bits)), &bits);
    return static_cast<uint8_t>(bits[key / 8] & (1 << (key % 8)));
}

int get_state(int fd, unsigned int type, unsigned long *array, size_t size) {
    int rc;

    switch (type) {
        case EV_LED:
            rc = ioctl(fd, EVIOCGLED(size), array);
            break;
        case EV_SND:
            rc = ioctl(fd, EVIOCGSND(size), array);
            break;
        case EV_SW:
            rc = ioctl(fd, EVIOCGSW(size), array);
            break;
        case EV_KEY:
            /* intentionally not printing the value for EV_KEY, let the
             * repeat handle this */
        default:
            return 1;
    }
    if (rc == -1)
        return 1;

    return 0;
}

string typname(unsigned int type) {
    return (type <= EV_MAX && !events.at(type).empty()) ? events.at(type) : string("?");
}

string codename(int type, int code) {
    if (!names.count(type))
        return "?";
    if (!names.at(type).count(code))
        return "?";
    return ((type <= EV_MAX) && (code <= maxval.at(type)) && (!names.at(type).at(code).empty())) ? names.at(type).at(
            code) : string("?");
}


void throwIOException(JNIEnv *env, string msg) {
    jclass clsEx = env->FindClass(CLS_IOEXCEPTION);
    env->ThrowNew(clsEx, msg.c_str());
}

/**
 * Retrieves the InputDevice instance from the File Descriptor.
 *
 * @param env
 * @param fd
 * @return
 */
jobject createInputDeviceInfo(JNIEnv *env, int fd, const string &devicePath) {

    if (!is_valid_fd(fd)) {
        throwIOException(env, string("createInputDeviceInfo: Invalid file descriptor"));
        return nullptr;
    }

    if (devicePath.empty()) {
        throwIOException(env, string("createInputDeviceInfo: Device path must not be empty"));
        return nullptr;
    }

    if (fd < 0) {
        char msg[256];
        sprintf(msg, "createInputDeviceInfo: Invalid file handle (Error %d)", fd);
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

    int have_state;
    unsigned long stateval;
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

    jclass clsInputDev = env->FindClass(CLS_INPUT_DEVICE);
    jmethodID midInputDevCtr = env->GetMethodID(clsInputDev, "<init>", MIDSIG_INPUTDEVICE_CTR);
    jobject objInputDevice = env->NewObject(clsInputDev, midInputDevCtr, jName, jPath, jIds, jDriverVersion, jEventTypeList);

    return objInputDevice;
}

/**
 * Filter for the AutoDevProbe scandir on /dev/input.
 *
 * @param dir The current directory entry provided by scandir.
 *
 * @return Non-zero if the given directory entry starts with "event", or zero
 * otherwise.
 */
static int is_event_device(const struct dirent *dir) {
    return strncmp(EVENT_DEV_NAME, dir->d_name, 5) == 0 || strncmp("mouse", dir->d_name, 5) == 0;
}

/**
 * List all supported input event devices
 *
 * @param entries
 * @return
 */
int listInputDevices(vector<string> &entries) {
    struct dirent **namelist;
    int ndev = scandir(DEV_INPUT_EVENT, &namelist, is_event_device, versionsort);
    for (int i = 0; i < ndev; i++) {
        char devicePath[267];
        snprintf(devicePath, sizeof(devicePath), "%s/%s", DEV_INPUT_EVENT, namelist[i]->d_name);
        entries.push_back(devicePath);
    }
    return ndev;
}

string defaultVal(const char *value) {
    if (value == nullptr)
        return string();
    return string(value);
}
