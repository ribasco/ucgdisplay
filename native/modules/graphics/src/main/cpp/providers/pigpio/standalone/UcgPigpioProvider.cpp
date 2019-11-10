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
#include <UcgPigpioProvider.h>

#include <iostream>
#include <pigpio.h>
#include <UcgPigpioSpiProvider.h>
#include <UcgPigpioI2CProvider.h>
#include <UcgPigpioGpioProvider.h>
#include <unistd.h>
#include <fcntl.h>
#include <Global.h>

UcgPigpioProvider::UcgPigpioProvider() : UcgPigpioProviderBase(PROVIDER_PIGPIO, PigpioType::TYPE_STANDALONE) {

}

extern "C" void pigpioSignalIntHandler(int signal, void* data) {
    gpioTerminate();
}

// Returns:
// (positive) PID of instance of daemon already running!
// 0 if there is no lockfile
// -1 if there is a lockfile but cannot make sense of contents
extern "C" int checkPigpiod(void)
{
    int fd;
    int count;
    int pid = 0;
    char str[20];

    fd = open( PI_LOCKFILE, O_RDONLY );
    if( fd != -1 ) {
        pid = -1;
        count = read(fd, str, sizeof(str)-1);
        if( count ) {
            pid = atoi( str );
        }
        close( fd );
    }
    return pid;
}

void UcgPigpioProvider::initialize(const std::shared_ptr<ucgd_t> &context) {
    try {
        log.debug("init_pigpio() : [PIGPIO] Initializing pigpio provider (STANDALONE)");

        if (getuid()) {
            throw std::runtime_error("You need to be running as ROOT to use the Standalone mode of PIGPIO");
        }

        int lockFilePid = checkPigpiod();
        if( lockFilePid > 0 ) {
            std::string msg = std::string("init_pigpio() : [PIGPIO] An instance of pigpiod is already running (Process: ") + std::to_string(lockFilePid) + std::string("). Please shutdown the daemon and try again.");
            log.warn(msg);
            throw PigpioInitException(msg);
        }
        else if( lockFilePid == -1 ) {
            log.warn("An instance of pigpio daemon is already running. Please shutdown the daemon and try again.");
            throw PigpioInitException("init_pigpio() : [PIGPIO] An instance of pigpio daemon is already running. Please shutdown the daemon and try again.");
        }

        if (gpioInitialise() <= PI_INIT_FAILED)
            throw PigpioInitException("init_pigpio() : Failed to initialize pigpio (STANDALONE)");

        void* data = (void*) context.get();
        gpioSetSignalFuncEx(2, pigpioSignalIntHandler, data);
        gpioSetSignalFuncEx(15, pigpioSignalIntHandler, data);
        gpioSetSignalFuncEx(11, pigpioSignalIntHandler, data);

        log.debug("init_pigpio() : [PIGPIO] Registered signal interrupt handler");

        log.debug("init_pigpio() : [PIGPIO] Initialized pigpio standalone mode");
        setInitialized(true);
    } catch (std::exception &e) {
        setInitialized(false);
        throw UcgProviderInitException(e, this);
    }
}

UcgPigpioProvider::~UcgPigpioProvider() {
    if (isInitialized()) {
        gpioTerminate();
    }
}

std::string UcgPigpioProvider::getLibraryName() {
    return "libpigpio.so";
}

const std::shared_ptr<UcgSpiProvider> &UcgPigpioProvider::getSpiProvider() {
    if (UcgIOProvider::getSpiProvider() == nullptr) {
        //Initialize spi m_Provider
        log.debug("init_pigpiod() : [PIGPIO] Initializing pigpio SPI provider");
        setSPIProvider(std::make_shared<UcgPigpioSpiProvider>(this));
    }
    return UcgIOProvider::getSpiProvider();
}

const std::shared_ptr<UcgI2CProvider> &UcgPigpioProvider::getI2CProvider() {
    if (UcgIOProvider::getI2CProvider() == nullptr) {
        //Initialize i2c m_Provider
        log.debug("init_pigpiod() : [PIGPIO] Initializing pigpio I2C provider");
        setI2CProvider(std::make_shared<UcgPigpioI2CProvider>(this));
    }
    return UcgIOProvider::getI2CProvider();
}

const std::shared_ptr<UcgGpioProvider> &UcgPigpioProvider::getGpioProvider() {
    if (UcgIOProvider::getGpioProvider() == nullptr) {
        //Initialize gpio m_Provider
        log.debug("init_pigpiod() : [PIGPIO] Initializing pigpio GPIO provider");
        setGPIOProvider(std::make_shared<UcgPigpioGpioProvider>(this));
    }
    return UcgIOProvider::getGpioProvider();
}

bool UcgPigpioProvider::supportsGpio() const {
    return true;
}

bool UcgPigpioProvider::supportsSPI() const {
    return true;
}

bool UcgPigpioProvider::supportsI2C() const {
    return true;
}

void UcgPigpioProvider::close() {
    setInitialized(false);
}
