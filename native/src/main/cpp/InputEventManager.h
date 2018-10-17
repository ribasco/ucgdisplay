//
// Created by raffy on 6/8/18.
//

#ifndef UCGDISP_INPUTEVENTMANAGER_H
#define UCGDISP_INPUTEVENTMANAGER_H

#include <string>
#include <map>
#include <memory>
#include <unistd.h>
#include <linux/input.h>
#include <thread>
#include <queue>
#include <fcntl.h>
#include <iostream>
#include <cstring>
#include <shared_mutex>
#include <mutex>
#include <event.h>
#include <event2/event.h>
#include <event2/thread.h>
#include "InputDevHelper.h"

using namespace std;

struct device_entry {
    string path;
    event *evt;
    queue<input_event> eventQueue;
    int repeat;
};

struct device_input_event {
    string device;
    string typeName;
    string codeName;
    unsigned short type;
    unsigned short code;
    int value;
    timeval time;
    int repeatCount;
};

typedef function<void(device_entry*)> device_state_added_cb;
typedef function<void(int, string)> device_state_removed_cb;
typedef function<void(device_input_event)> device_input_event_cb;
typedef function<bool(int, string)> device_filter_cb;
typedef function<void(string)> device_state_rejected_cb;

class InputEventManager {
public:
    explicit InputEventManager(device_input_event_cb eventCallback, device_filter_cb filter = nullptr);

    void startMonitor();

    void stopMonitor();

    void setOnDeviceAdded(device_state_added_cb callback);

    void setOnDeviceRemoved(device_state_removed_cb callback);

    void setOnDeviceRejected(device_state_rejected_cb callback);

    event *add(const string &devicePath, bool replace = false);

    device_entry *get(const string &devicePath);

    device_entry* get(int fd);

    void remove(const string &devicePath);

    void remove(int fd);

    void invalidate(device_entry* entry);

    bool isValid(const string &device);

    bool isValid(device_entry* entry);

    void clear();

    bool exists(const string &devicePath);

    bool exists(int fd);

    bool started() {
        return _started;
    };

    map<string, device_entry>& entries();

    int size();

    static void processInput(int fd, short kind, void *arg);


private:
    struct event_base *_base;
    map<string, device_entry> _cache;
    volatile bool _started = false;
    device_filter_cb m_DeviceFilter;
    device_input_event_cb m_InputEventCallback;
    device_state_added_cb m_DeviceAdded;
    device_state_removed_cb m_DeviceRemoved;
    device_state_rejected_cb m_DeviceRejected;

    shared_timed_mutex m_Mutex;

    device_entry* _get_entry_from_fd(int fd) {
        auto result = find_if(_cache.begin(), _cache.end(), [fd](const std::pair<string, device_entry> &v) {
            return v.second.evt->ev_fd == fd;
        });
        if (result != _cache.end())
            return &result->second;
        return nullptr;
    }

    string _get_path_from_fd(int fd) {
        auto result = find_if(_cache.begin(), _cache.end(), [fd](const std::pair<string, device_entry> &v) {
            return v.second.evt->ev_fd == fd;
        });
        if (result != _cache.end())
            return result->first;
        return string();
    }

    map<string, device_entry>::iterator _get(const string &devicePath) {
        return _cache.find(devicePath);
    };
};


#endif //UCGDISP_INPUTEVENTMANAGER_H
