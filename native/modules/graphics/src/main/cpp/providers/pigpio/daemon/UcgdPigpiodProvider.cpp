/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioProvider.cpp
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
#include <UcgdPigpiodProvider.h>

#include <iostream>
#include <pigpiod_if2.h>
#include <UcgdPigpiodSpiPeripheral.h>
#include <UcgdPigpiodI2CPeripheral.h>
#include <UcgdPigpiodGpioPeripheral.h>

UcgdPigpiodProvider::UcgdPigpiodProvider() : UcgdPigpioProviderBase(PROVIDER_PIGPIOD, PigpioType::TYPE_DAEMON), m_Handle(-1) {

}

UcgdPigpiodProvider::UcgdPigpiodProvider(const std::string &address, const std::string &port) : UcgdPigpiodProvider() {
    //log.debug("construct() : [PIGPIOD] Using address = {}, port = {}", address, port);
    this->address = address;
    this->port = port;
}

UcgdPigpiodProvider::~UcgdPigpiodProvider() {
    debug("UcgPigpiodProvider : destructor called");
    if (m_Handle >= 0) {
        pigpio_stop(this->m_Handle);
        debug("UcgPigpiodProvider: successfully closed pigpiod");
    }
}

void UcgdPigpiodProvider::open(const std::shared_ptr<ucgd_t> &context) {
    try {
        log.debug("init_pigpiod() : [PIGPIOD] Initializing pigpio provider (DAEMON)");

        ServiceLocator &locator = ServiceLocator::getInstance();

        char *cAddr = nullptr;
        char *cPort = nullptr;

        if (!this->address.empty())
            cAddr = const_cast<char *>(this->address.c_str());

        if (!this->port.empty())
            cPort = const_cast<char *>(this->port.c_str());

        log.debug("init_pigpiod() : [PIGPIOD] Connecting to daemon (Address: {}, Port: {})", cAddr == nullptr ? PI_DEFAULT_SOCKET_ADDR_STR : cAddr, cPort == nullptr ? PI_DEFAULT_SOCKET_PORT_STR : cPort);

        //Connect to pigpio daemon
        this->m_Handle = pigpio_start(cAddr, cPort);

        if (this->m_Handle < 0)
            throw PigpiodInitException("init_pigpiod() : [PIGPIOD] Failed to connect to pigpio daemon");

        log.debug("init_pigpiod() : [PIGPIOD] Successfully connected to daemon (Address: {}, Port: {}, Handle: {})", cAddr == nullptr ? PI_DEFAULT_SOCKET_ADDR_STR : cAddr, cPort == nullptr ? PI_DEFAULT_SOCKET_PORT_STR : cPort, this->m_Handle);

        setInitialized(true);
    } catch (std::exception &e) {
        setInitialized(false);
        throw UcgProviderInitException(e, this);
    }
}

int UcgdPigpiodProvider::getHandle() const {
    return m_Handle;
}

std::string UcgdPigpiodProvider::getLibraryName() {
    return "libpigpiod_if2.so";
}

bool UcgdPigpiodProvider::supportsGpio() const {
    return true;
}

bool UcgdPigpiodProvider::supportsSPI() const {
    return true;
}

bool UcgdPigpiodProvider::supportsI2C() const {
    return true;
}

std::shared_ptr<UcgdGpioPeripheral> UcgdPigpiodProvider::createGpioPeripheral() {
    return std::make_shared<UcgdPigpiodGpioPeripheral>(getPointer());
}

std::shared_ptr<UcgdI2CPeripheral> UcgdPigpiodProvider::createI2CPeripheral() {
    return std::make_shared<UcgdPigpiodI2CPeripheral>(getPointer());
}

std::shared_ptr<UcgdSpiPeripheral> UcgdPigpiodProvider::createSpiPeripheral() {
    return std::make_shared<UcgdPigpiodSpiPeripheral>(getPointer());
}

std::shared_ptr<UcgdPigpiodProvider> UcgdPigpiodProvider::getPointer() {
    return this->shared_from_this();
}