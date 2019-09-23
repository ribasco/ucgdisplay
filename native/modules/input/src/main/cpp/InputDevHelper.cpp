/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Input
 * Filename: InputDevHelper.cpp
 * 
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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
        entries.emplace_back(devicePath);
    }
    return ndev;
}

bool is_readable(const string &path) {
    if (path.empty())
        return false;
    return access (path.c_str(), R_OK) == 0;
}

string defaultVal(const char *value) {
    if (value == nullptr)
        return string();
    return string(value);
}
