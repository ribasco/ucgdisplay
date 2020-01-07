/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Graphics
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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
#include "DeviceManager.h"
#include <UcgdTypes.h>

DeviceManager::~DeviceManager() {
    ::debug("DeviceManager : destructor");
};

DeviceManager::DeviceManager() = default;

auto DeviceManager::createDevice() -> std::shared_ptr<ucgd_t> & {
    std::shared_ptr dev = std::make_shared<ucgd_t>();
    dev->u8g2 = std::make_unique<u8g2_t>();
    auto it = m_DeviceMap.insert(make_pair(dev->address(), std::move(dev)));
    return it.first->second;
}

auto DeviceManager::deleteDevice(const uintptr_t &addr) -> void {
    if (!isRegistered(addr))
        throw DeviceNotFoundException(std::string("Device with address '") + std::to_string(addr) + std::string("' not found in cache."), addr);
    m_DeviceMap.erase(addr);
}

auto DeviceManager::getDevice(const uintptr_t &addr) -> std::shared_ptr<ucgd_t> & {
    auto it = m_DeviceMap.find(addr);
    if (it != m_DeviceMap.end())
        return it->second;
    throw DeviceNotFoundException(std::string("Device with address '") + std::to_string(addr) + std::string("' not found in cache."), addr);
}

auto DeviceManager::isRegistered(const uintptr_t &addr) -> bool {
    return m_DeviceMap.find(addr) != m_DeviceMap.end();
}

auto DeviceManager::getAllDevices() -> const std::map<uintptr_t, std::shared_ptr<ucgd_t>>&{
    return m_DeviceMap;
}

uintptr_t DeviceNotFoundException::getAddress() const {
    return address;
}
