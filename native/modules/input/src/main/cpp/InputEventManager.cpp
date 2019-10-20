/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Input
 * Filename: InputEventManager.cpp
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

#include "UcgiConfig.h"
#include "InputEventManager.h"

InputEventManager::InputEventManager(device_input_event_cb eventCallback, device_filter_cb filter) {
    m_InputEventCallback = move(eventCallback);
    if (filter)
        m_DeviceFilter = move(filter);
    _base = event_base_new();
    if (evthread_use_pthreads() == -1) {
        fprintf(stderr, "evthread_use_pthreads(): %s\n", strerror(errno));
        return;
    }
}

void InputEventManager::startMonitor() {
    if (!_started) {
        _started = true;
        if (event_base_loop(_base, EVLOOP_NO_EXIT_ON_EMPTY) == -1) {
            perror("Unable to start event loop");
            _started = false;
            return;
        }
    }
}

void InputEventManager::stopMonitor() {
    if (_started) {
        event_base_loopbreak(_base);
        _started = false;
    }
}

event *InputEventManager::add(const string &devicePath, bool replace) {
    if (devicePath.empty())
        return nullptr;
    if (exists(devicePath)) {
        if (!replace)
            return nullptr;
        remove(devicePath);
    }
    int fd = open(devicePath.c_str(), O_RDONLY);
    if (fd == -1) {
        cerr << "Could not open file: " << devicePath << " (" << string(strerror(errno)) << ")" << endl;
        return nullptr;
    }

    if (m_DeviceFilter != nullptr && !m_DeviceFilter(fd, devicePath)) {
        if (m_DeviceRejected)
            m_DeviceRejected(devicePath);
        close(fd);
        return nullptr;
    }

    lock_guard<std::shared_timed_mutex> writerLock(m_Mutex, adopt_lock);
    struct event *ev = event_new(_base, fd, EV_READ | EV_PERSIST, InputEventManager::processInput, this);
    if (event_add(ev, nullptr) == 0) {
        _cache.insert(make_pair(devicePath, device_entry{devicePath, ev, queue<input_event>()}));
        if (m_DeviceAdded)
            m_DeviceAdded(_get_entry_from_fd(fd));
        return ev;
    } else {
        cerr << "Could not register event: " << devicePath << endl;
    }
    _cache.erase(devicePath);
    return nullptr;
}

void InputEventManager::remove(const string &devicePath) {
    lock_guard<std::shared_timed_mutex> writerLock(m_Mutex, adopt_lock);
    device_entry *ev = get(devicePath);
    if (ev != nullptr) {
        string devPath = ev->path;
        int fd = ev->evt->ev_fd;
        if (event_del(ev->evt) == 0) {
            _cache.erase(devicePath);
            if (m_DeviceRemoved)
                m_DeviceRemoved(fd, devPath);
        } else {
            cerr << "Could not unregister event: " << ev->path << endl;
        }
    } /*else {
        cerr << "Device has already been removed: " << devicePath << endl;
    }*/
}

void InputEventManager::remove(int fd) {
    device_entry *e = _get_entry_from_fd(fd);
    if (e != nullptr)
        remove(e->path);
}

bool InputEventManager::exists(int fd) {
    return _get_entry_from_fd(fd) != nullptr;
}

bool InputEventManager::exists(const string &devicePath) {
    shared_lock<shared_timed_mutex> readerLock(m_Mutex, try_to_lock);
    return _get(devicePath) != _cache.end();
}

device_entry *InputEventManager::get(const string &devicePath) {
    shared_lock<shared_timed_mutex> readerLock(m_Mutex, try_to_lock);
    auto it = _get(devicePath);
    if (it != _cache.end()) {
        return &it->second;
    }
    //It doesn't exist, create new entry
    return nullptr;
}

device_entry *InputEventManager::get(int fd) {
    shared_lock<shared_timed_mutex> readerLock(m_Mutex, try_to_lock);
    return _get_entry_from_fd(fd);
}

int InputEventManager::size() {
    return static_cast<int>(_cache.size());
}

void InputEventManager::processInput(int fd, short kind, void *arg) {
    auto *em = static_cast<InputEventManager *>(arg);
    struct input_event event{};
    ssize_t n = read(fd, &event, sizeof(event));

    device_entry *entry = em->get(fd);

    if (n == -1) {
        em->invalidate(entry);
        return;
    }

    queue<input_event> *event_queue = &entry->eventQueue;

    // Don't process it yet as there might be other events in that report.
    if (event.type != EV_SYN && event.type != EV_MSC) {
        event_queue->push(event);
    }

    if (event.type == EV_SYN && event.code == SYN_REPORT) {
        while (!event_queue->empty()) {
            input_event evt = event_queue->front();
            //cout << "input event =\tType: " << typname(evt.type) << "\tCode: " << codename(evt.type, evt.code) << "\tValue: " << evt.value << endl;
            if (em->m_InputEventCallback)
                em->m_InputEventCallback(device_input_event{entry->path, typname(evt.type), codename(evt.type, evt.code), evt.type, evt.code, evt.value, evt.time,
                                                            ((evt.value == 2) ? entry->repeat++ : entry->repeat = 1)
                });
            event_queue->pop();
        }
    }
}

void InputEventManager::setOnDeviceAdded(device_state_added_cb callback) {
    m_DeviceAdded = move(callback);
}

void InputEventManager::setOnDeviceRemoved(device_state_removed_cb callback) {
    m_DeviceRemoved = move(callback);
}

void InputEventManager::setOnDeviceRejected(device_state_rejected_cb callback) {
    m_DeviceRejected = move(callback);
}

void InputEventManager::clear() {
    if (size() > 0) {
        lock_guard<std::shared_timed_mutex> writerLock(m_Mutex, adopt_lock);
        for (auto it = _cache.cbegin(); it != _cache.cend();) {
            remove(it->first);
            it++;
        }
    }
}

map<string, device_entry> &InputEventManager::entries() {
    return _cache;
}

void InputEventManager::invalidate(device_entry *entry) {
    if (entry == nullptr)
        return;
    event_del(entry->evt);
    entry->evt->ev_fd = -1;
}

bool InputEventManager::isValid(device_entry *entry) {
    if (entry == nullptr)
        return false;
    if (entry->path.empty())
        return false;
    return is_valid_fd(entry->evt->ev_fd) != 0;
}

bool InputEventManager::isValid(const string &device) {
    device_entry *e = get(device);
    if (e != nullptr)
        return isValid(e);
    return false;
}
