//
// Created by raffy on 5/30/18.
//

#include "InputDevHelper.h"

uint8_t HasEventType(int fd, int type) {
    unsigned long evbit = 0;
    // Get the bit field of available event types.
    ioctl(fd, EVIOCGBIT(0, sizeof(evbit)), &evbit);
    return evbit & (1 << type);
}

// Returns true iff the device reports EV_KEY events.
uint8_t HasKeyEvents(int device_fd) {
    unsigned long evbit = 0;
    // Get the bit field of available event types.
    ioctl(device_fd, EVIOCGBIT(0, sizeof(evbit)), &evbit);
    return evbit & (1 << EV_KEY);
}

// Returns true iff the given device has |key|.
uint8_t HasSpecificKey(int device_fd, unsigned int key) {
    size_t nchar = KEY_MAX / 8 + 1;
    unsigned char bits[nchar];
    // Get the bit fields of available keys.
    ioctl(device_fd, EVIOCGBIT(EV_KEY, sizeof(bits)), &bits);
    return bits[key / 8] & (1 << (key % 8));
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

string codename(unsigned int type, unsigned int code) {
    if (!names.count(type))
        return "?";
    if (!names.at(type).count(code))
        return "?";
    return ((type <= EV_MAX) && (code <= maxval.at(type)) && (!names.at(type).at(code).empty())) ? names.at(type).at(
            code) : string("?");
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

int getDevices(vector<string> &entries) {
    struct dirent **namelist;
    int ndev = scandir(DEV_INPUT_EVENT, &namelist, is_event_device, versionsort);
    for (int i = 0; i < ndev; i++) {
        char devicePath[267];
        snprintf(devicePath, sizeof(devicePath), "%s/%s", DEV_INPUT_EVENT, namelist[i]->d_name);
        entries.emplace_back(devicePath);
    }
    return ndev;
}