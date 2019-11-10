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
#include <UcgPigpiodProvider.h>

#include <iostream>
#include <pigpiod_if2.h>
#include <UcgPigpiodSpiProvider.h>
#include <UcgPigpiodI2CProvider.h>
#include <UcgPigpiodGpioProvider.h>

UcgPigpiodProvider::UcgPigpiodProvider() : UcgPigpioProviderBase(PROVIDER_PIGPIOD, PigpioType::TYPE_DAEMON), m_Handle(-1) {

}

UcgPigpiodProvider::UcgPigpiodProvider(const std::string &address, const std::string &port) : UcgPigpiodProvider() {
    log.debug("construct() : [PIGPIOD] Using address = {}, port = {}", address, port);
    this->address = address;
    this->port = port;
}

void UcgPigpiodProvider::initialize(const std::shared_ptr<ucgd_t> &context) {
    try {
        log.debug("init_pigpiod() : [PIGPIOD] Initializing pigpio provider (DAEMON)");

        ServiceLocator &locator = ServiceLocator::getInstance();

        char *cAddr = nullptr;
        char *cPort = nullptr;

        if (!this->address.empty())
            cAddr = const_cast<char *>(this->address.c_str());

        if (!this->port.empty())
            cPort = const_cast<char *>(this->port.c_str());

        log.debug("init_pigpiod() : [PIGPIOD] Connecting to daemon (Address: {}, Port: {})", cAddr, cPort);

        //Connect to pigpio daemon
        this->m_Handle = pigpio_start(cAddr, cPort);

        if (this->m_Handle < 0)
            throw PigpiodInitException("init_pigpiod() : [PIGPIOD] Failed to initialize pigpio (DAEMON)");

        log.debug("init_pigpiod() : [PIGPIOD] Successfully connected to daemon (Address: {}, Port: {}, Handle: {})", cAddr, cPort, this->m_Handle);

        setInitialized(true);
    } catch (std::exception &e) {
        setInitialized(false);
        throw UcgProviderInitException(e, this);
    }
}

int UcgPigpiodProvider::getHandle() const {
    return m_Handle;
}

UcgPigpiodProvider::~UcgPigpiodProvider() {
    if (m_Handle >= 0)
        pigpio_stop(this->m_Handle);
}

std::string UcgPigpiodProvider::getLibraryName() {
    return "libpigpiod_if2.so";
}

const std::shared_ptr<UcgSpiProvider> &UcgPigpiodProvider::getSpiProvider() {
    if (UcgIOProvider::getSpiProvider() == nullptr) {
        //Initialize spi m_Provider
        log.debug("init_pigpiod() : [PIGPIOD] Initializing pigpio SPI provider");
        setSPIProvider(std::make_shared<UcgPigpiodSpiProvider>(this));
    }
    return UcgIOProvider::getSpiProvider();
}

const std::shared_ptr<UcgI2CProvider> &UcgPigpiodProvider::getI2CProvider() {
    if (UcgIOProvider::getI2CProvider() == nullptr) {
        //Initialize i2c m_Provider
        log.debug("init_pigpiod() : [PIGPIOD] Initializing pigpio I2C provider");
        setI2CProvider(std::make_shared<UcgPigpiodI2CProvider>(this));
    }
    return UcgIOProvider::getI2CProvider();
}

const std::shared_ptr<UcgGpioProvider> &UcgPigpiodProvider::getGpioProvider() {
    if (UcgIOProvider::getGpioProvider() == nullptr) {
        //Initialize gpio m_Provider
        log.debug("init_pigpiod() : [PIGPIOD] Initializing pigpio GPIO provider");
        setGPIOProvider(std::make_shared<UcgPigpiodGpioProvider>(this));
    }
    return UcgIOProvider::getGpioProvider();
}

bool UcgPigpiodProvider::supportsGpio() const {
    return true;
}

bool UcgPigpiodProvider::supportsSPI() const {
    return true;
}

bool UcgPigpiodProvider::supportsI2C() const {
    return true;
}

void UcgPigpiodProvider::close() {

}
