/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: ServiceLocator.cpp
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
#include "ServiceLocator.h"

#include <memory>
#include <iostream>

ServiceLocator::ServiceLocator() : m_Logger(nullptr) {
}

ServiceLocator::~ServiceLocator() = default;

auto ServiceLocator::getInstance() -> ServiceLocator & {
    static auto instance = std::unique_ptr<ServiceLocator>(nullptr);
    // Initialized once - lazy initialization
    if (!instance) {
        instance.reset(new ServiceLocator()); // NOLINT(modernize-make-unique)
    }
    return *instance;
}

auto ServiceLocator::getLogger() -> Log & {
    if (m_Logger == nullptr)
        throw std::runtime_error("Logger not set. Instance is NULL");
    return *m_Logger;
}

void ServiceLocator::setLogger(std::unique_ptr<Log>& log) {
    this->m_Logger = std::move(log);
}

auto ServiceLocator::getDeviceManager() -> std::unique_ptr<DeviceManager> & {
    if (m_DeviceManager == nullptr) {
        throw std::runtime_error("Device manager not set. Instance is NULL");
    }
    return m_DeviceManager;
}

void ServiceLocator::setDeviceManager(std::unique_ptr<DeviceManager> mDeviceManager) {
    m_DeviceManager = std::move(mDeviceManager);
}

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

void ServiceLocator::setProviderManager(std::unique_ptr<ProviderManager> mProviderManager) {
    m_ProviderManager = std::move(mProviderManager);
}

auto ServiceLocator::getProviderManager() -> std::unique_ptr<ProviderManager> & {
    if (m_ProviderManager == nullptr) {
        throw std::runtime_error("Provider manager not set. Instance is NULL");
    }
    return m_ProviderManager;
}

#endif
