/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Graphics
 * %%
 * Copyright (C) 2018 - 2021 Universal Character/Graphics display library
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
#ifndef UCGD_MOD_GRAPHICS_DEVICEMANAGER_H
#define UCGD_MOD_GRAPHICS_DEVICEMANAGER_H

#include <memory>
#include <map>

class DeviceNotFoundException : public std::runtime_error {
public:
    DeviceNotFoundException(const std::string &arg, uintptr_t address) : runtime_error(arg), address(address) {}

    DeviceNotFoundException(const char *string, uintptr_t address) : runtime_error(string), address(address) {}

    DeviceNotFoundException(const runtime_error &error, uintptr_t address) : runtime_error(error), address(address) {}

    [[nodiscard]] uintptr_t getAddress() const;
private:
    uintptr_t address;
};

struct ucgd_t;

class DeviceManager {
public:
    DeviceManager();

    virtual ~DeviceManager();

    auto createDevice() -> std::shared_ptr<ucgd_t>&;

    auto deleteDevice(const uintptr_t& addr) -> void;

    auto getDevice(const uintptr_t& addr) -> std::shared_ptr<ucgd_t>&;

    auto isRegistered(const uintptr_t& addr) -> bool;

    auto getAllDevices() -> const std::map<uintptr_t, std::shared_ptr<ucgd_t>>&;

private:
    std::map<uintptr_t, std::shared_ptr<ucgd_t>> m_DeviceMap;
};


#endif //UCGD_MOD_GRAPHICS_DEVICEMANAGER_H
