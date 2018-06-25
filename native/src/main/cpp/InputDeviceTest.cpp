//
// Created by raffy on 6/4/18.
//

#include <string>
#include <jni.h>
#include <poll.h>
#include <utility>
#include <algorithm>
#include <iostream>
#include <list>
#include <vector>
#include <memory>
#include <map>
#include <functional>
#include <shared_mutex>
#include <dirent.h>
#include <cstring>
#include <fcntl.h>
//#include <zconf.h>
#include <sys/stat.h>
#include <queue>
#include <fstream>

#ifdef _LIBUDEV_H_
#include <libudev.h>
#endif

#include <set>
//#include <event2/event.h>
//#include <event2/thread.h>
#include <sys/inotify.h>

using namespace std;

#include "InputDevHelper.h"
#include "InputEventManager.h"

bool filterDevice(int fd, const string &path);

void inputHandler(device_input_event evt);

void inputMonitorFunction_Test(future<void> futureObj);

void onDeviceAdded_Test(device_entry *entry);

void onDeviceRemoved_Test(int fd, const string &device);

InputEventManager evMan(inputHandler, filterDevice);

// Reads the list of files in |directory_path| into |filenames|.
void listDirectory(const string &directory_path, vector<string> *filenames) {

    if (!filenames) {
        printf("Error: filenames is null.\n");
        exit(1);
    }
    DIR *directory = opendir(directory_path.c_str());
    if (!directory) {
        perror("err");
        return;
    }
    struct dirent *entry = nullptr;
    while ((entry = readdir(directory))) {
        if (strcmp(entry->d_name, ".") != 0 && strcmp(entry->d_name, "..") != 0) {
            filenames->push_back(directory_path + "/" + entry->d_name);
        }
    }
}

bool filterDevice(int fd, const string &path) {
    //cout << "+ Checking Device: " << path << ", FD: " << fd << endl;
    return (HasEventType(fd, EV_KEY) && HasSpecificKey(fd, KEY_B)) || HasEventType(fd, EV_REL) || HasEventType(fd, EV_ABS);
    //return (HasEventType(fd, EV_KEY) && HasSpecificKey(fd, KEY_B)) || HasEventType(fd, EV_ABS);
}

void inputMonitorFunction_Test(future<void> futureObj) {
    if (evMan.size() <= 0) {
        cerr << "No devices available for processing, Make sure the cache has been initialized" << endl;
        return;
    }
    cout << hex << this_thread::get_id() << " Starting monitor from thread" << endl;
    evMan.startMonitor();
}

void inputHandler(device_input_event evt) {
    if (evt.type != EV_REL)
        cout << hex << this_thread::get_id() << dec << " [" << evt.device << "] Type: " << evt.typeName << ", Code: " << evt.codeName << ", Value: " << evt.value << ", Repeat Count: "
             << evt.repeatCount << endl;
}

void onDeviceAdded_Test(device_entry *entry) {
    cout << "DEVICE ADDED: " << entry->path << endl;
}

void onDeviceRemoved_Test(int fd, const string &device) {
    cout << "DEVICE REMOVED: " << fd << ", Path: " << device << endl;
}

void onDeviceRejected_Test(const string &device) {
    cerr << "DEVICE REJECTED: " << device << endl;
}

#ifdef _LIBUDEV_H_
static void processDeviceStateChange(struct udev_device *dev) {
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

    cout << hex << this_thread::get_id() <<  " Device: " << device_name << ", Action: " << a << ", Dev Type: " << devtype << ", Dev Path: " << devpath << endl;

    if (action == "add") {
        evMan.add(device_name, true);
    } else if (action == "remove") {
        evMan.remove(device_name);
    }
}
#endif

#define EVENT_SIZE  ( sizeof (struct inotify_event) )
#define BUF_LEN     ( 1024 * ( EVENT_SIZE + 16 ) )
#define DEVINPUT_DIR_PATH "/dev/input"

void dsm_inotify(future<void> futureObj) {
    int fd, wd, length, i = 0;
    char buffer[BUF_LEN];

    fd = inotify_init();

    if (fd < 0) {
        perror("inotify_init");
        exit(errno);
    }

    wd = inotify_add_watch(fd, DEVINPUT_DIR_PATH, IN_ATTRIB);
    auto pfd = pollfd{fd, POLLIN, 0};

    while (futureObj.wait_for(chrono::milliseconds(1)) == future_status::timeout) {
        poll(&pfd, 1, 200);

        if (pfd.revents & POLLIN) {
            //Read input event
            length = static_cast<int>(read(fd, buffer, BUF_LEN));

            if (length < 0) {
                cerr << "There was a problem reading file descriptor" << endl;
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
                            if (!is_readable(devicePath)) {
                                cerr << "File not readable: " << devicePath << endl;
                                continue;
                            }
                            cout << "Adding: " << devicePath << flush << endl;
                            if (!evMan.exists(devicePath)) {
                                evMan.add(devicePath, true);
                            } else {
                                cerr << "Skipping add...Device is already in the cache: " << devicePath << flush << endl;
                            }
                        } else {
                            //if (!evMan.isValid(devicePath)) {
                            if (evMan.exists(devicePath)) {
                                cout << "Removing: " << devicePath << endl;
                                evMan.remove(devicePath);
                            }
                            //}
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
}

#ifdef _LIBUDEV_H_
void deviceStateMonitorUdev(future<void> futureObj) {
    struct udev *udev = udev_new();
    if (!udev) {
        fprintf(stderr, "udev_new() failed\n");
        return;
    }

    struct udev_monitor *mon = udev_monitor_new_from_netlink(udev, "udev");

    udev_monitor_filter_add_match_subsystem_devtype(mon, "input", nullptr);
    udev_monitor_enable_receiving(mon);

    int fd = udev_monitor_get_fd(mon);

    while (futureObj.wait_for(chrono::milliseconds(1)) == future_status::timeout) {
        fd_set fds;
        FD_ZERO(&fds);
        FD_SET(fd, &fds);

        int ret = select(fd + 1, &fds, nullptr, nullptr, nullptr);
        if (ret <= 0)
            break;

        if (FD_ISSET(fd, &fds)) {
            struct udev_device *dev = udev_monitor_receive_device(mon);
            if (dev) {
                if (udev_device_get_devnode(dev))
                    processDeviceStateChange(dev);
                udev_device_unref(dev);
            }
        }
    }
}
#endif

int main() {
    promise<void> exSig;
    promise<void> exSig_dsm;
    vector<string> filenames;

    evMan.setOnDeviceAdded(onDeviceAdded_Test);
    evMan.setOnDeviceRemoved(onDeviceRemoved_Test);
    evMan.setOnDeviceRejected(onDeviceRejected_Test);

    listDirectory("/dev/input", &filenames);

    //Populate cache
    for (const auto &f : filenames) {
        evMan.add(f);
    }

    cout << "There are a total of '" << evMan.size() << "' entries added to the cache" << endl;

    //Fetch std::future object associated with promise
    future<void> futureObj = exSig.get_future();
    future<void> futDsm = exSig_dsm.get_future();

    cout << "Starting input monitor thread..." << endl;
    thread devInputMonitorThread(inputMonitorFunction_Test, move(futureObj));

    cout << "Starting device state monitor" << endl;
    thread devStateMonitorThread(dsm_inotify, move(futDsm));

    devInputMonitorThread.join();
    devStateMonitorThread.join();

    return 0;
}