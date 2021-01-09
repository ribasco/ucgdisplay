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
#ifndef UCGD_MOD_GRAPHICS_SERVICELOCATOR_H
#define UCGD_MOD_GRAPHICS_SERVICELOCATOR_H

#include <memory>
#include <iostream>
#include <Global.h>
#include <Log.h>
#include <DeviceManager.h>

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
#include <ProviderManager.h>
#endif

class ServiceLocator {
private:
    //Private constructor
    ServiceLocator();

    // Forbid client code from creating a copy or using the
    // copy constructor.
    ServiceLocator(const ServiceLocator &) {}

public:
    ~ServiceLocator();

private:
    //Services
    std::unique_ptr<Log> m_Logger;

    std::unique_ptr<DeviceManager> m_DeviceManager;

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    std::unique_ptr<ProviderManager> m_ProviderManager;
#endif

public:
    // Return a reference to not allow client code
    // to delete object.
    static auto getInstance() -> ServiceLocator &;

    auto getLogger() -> Log &;

    void setLogger(std::unique_ptr<Log>& log);

    auto getDeviceManager() -> std::unique_ptr<DeviceManager> &;

    void setDeviceManager(std::unique_ptr<DeviceManager> mDeviceManager);

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    auto getProviderManager() -> std::unique_ptr<ProviderManager> &;

    void setProviderManager(std::unique_ptr<ProviderManager> mProviderManager);
#endif

};

#endif //UCGD_MOD_GRAPHICS_SERVICELOCATOR_H
