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
#include <zconf.h>
#include <sys/stat.h>
#include "InputDevHelper.h"
#include <sys/inotify.h>
#include <queue>
#include <fstream>
#include <experimental/filesystem>
#include <boost/smart_ptr/scoped_array.hpp>
#include <libudev.h>
#include <set>
#include <event2/event.h>
#include <event2/thread.h>

using namespace std;

#include "InputEventManager.h"

bool filterDevice(int fd, const string &path);
void inputHandler(device_input_event evt);
void inputMonitorFunction_Test(future<void> futureObj);
void onDeviceAdded_Test(device_entry* entry);
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

void initEventProcessor() {
    // enable pthread
    if (evthread_use_pthreads() == -1) {
        printf("error while evthread_use_pthreads(): %s\n", strerror(errno));
        return;
    }

    struct event_base *eb;
    if((eb = event_base_new()) == nullptr) {
        printf("error while event_base_new(): %s\n", strerror(errno));
        return;
    }
}

int listCb(const struct event_base* eb, const struct event* evt, void *args) {
    cout << "Event List: " << evt->ev_fd << endl;
    return 0;
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

    cout << hex << this_thread::get_id() << dec << " [" << evt.device << "] Type: " << evt.typeName << ", Code: " << evt.codeName << ", Value: " << evt.value << ", Repeat Count: "
         << evt.repeatCount  << endl;
}

void onDeviceAdded_Test(device_entry* entry) {
    cout << "DEVICE ADDED: " << entry->path << endl;
}

void onDeviceRemoved_Test(int fd, const string &device) {
    cout << "DEVICE REMOVED: " << fd << ", Path: " << device << endl;
}

void onDeviceRejected_Test(const string& device) {
    cerr << "DEVICE REJECTED: " << device << endl;
}

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

    cout << "Starting monitor thread..." << endl;

    // Starting Thread & move the future object in lambda function by reference
    thread devInputMonitorThread(inputMonitorFunction_Test, move(futureObj));

    cout << "Starting device state monitor" << endl;
    thread devStateMonitorThread(deviceStateMonitorUdev, move(futDsm));

    devInputMonitorThread.join();
    devStateMonitorThread.join();

    return 0;
}