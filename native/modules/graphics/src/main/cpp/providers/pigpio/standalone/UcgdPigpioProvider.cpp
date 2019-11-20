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
#include <UcgdPigpioProvider.h>

#include <iostream>
#include <pigpio.h>
#include <UcgdPigpioSpiPeripheral.h>
#include <UcgdPigpioI2CPeripheral.h>
#include <UcgdPigpioGpioPeripheral.h>
#include <unistd.h>
#include <fcntl.h>

UcgdPigpioProvider::UcgdPigpioProvider() : UcgdPigpioProviderBase(PROVIDER_PIGPIO, PigpioType::TYPE_STANDALONE) {

}

UcgdPigpioProvider::~UcgdPigpioProvider() {
    debug("UcgPigpioProvider : destructor called");
    gpioTerminate();
};

/**
 * @return Positive PID if daemon is already running, 0 if no lockfile or -1 if there is a lock file but invalid content
 */
int UcgdPigpioProvider::checkPigpioDaemonStatus() {
    int fd;
    int count;
    int pid = 0;
    char str[20];

    fd = ::open(PI_LOCKFILE, O_RDONLY);
    if (fd != -1) {
        pid = -1;
        count = read(fd, str, sizeof(str) - 1);
        if (count) {
            pid = atoi(str);
        }
        ::close(fd);
    }
    return pid;
}

void UcgdPigpioProvider::open(const std::shared_ptr<ucgd_t> &context) {
    try {
        log.debug("init_pigpio() : [PIGPIO] Initializing pigpio provider (STANDALONE)");

        if (getuid()) {
            throw std::runtime_error("You need to be running as ROOT to use the Standalone mode of PIGPIO");
        }

        int lockFilePid = checkPigpioDaemonStatus();
        if (lockFilePid > 0) {
            std::string msg =
                    std::string("init_pigpio() : [PIGPIO] An instance of pigpio is already running (Process: ") +
                    std::to_string(lockFilePid) + std::string(", Lock File: " + std::string(PI_LOCKFILE) +
                                                              "). Only one instance could be running at a time.");
            log.warn(
                    "init_pigpio() : [PIGPIO] An instance of pigpio is already running (Process: {}, Lock File: {}). Only one instance could be running at a time.",
                    lockFilePid, PI_LOCKFILE);
            throw PigpioInitException(msg);
        } else if (lockFilePid == -1) {
            std::string msg = "An instance of pigpio daemon is already running. Only one instance could be running at a time.";
            log.warn(msg);
            throw PigpioInitException(msg);
        }

        //Don't use the signal handler of pigpio, it prevents our main signal handler from getting called.
        gpioCfgSetInternals(PI_CFG_NOSIGHANDLER); // NOLINT(hicpp-signed-bitwise)
        //disable all other interface
        gpioCfgInterfaces(PI_DISABLE_SOCK_IF | PI_LOCALHOST_SOCK_IF); //PI_DISABLE_FIFO_IF

        if (gpioInitialise() <= PI_INIT_FAILED)
            throw PigpioInitException("init_pigpio() : Failed to initialize pigpio (STANDALONE)");

        log.debug("init_pigpio() : [PIGPIO] Initialized pigpio standalone mode");
        setInitialized(true);
    } catch (std::exception &e) {
        setInitialized(false);
        throw UcgProviderInitException(e, this);
    }
}

std::string UcgdPigpioProvider::getLibraryName() {
    return "libpigpio.so";
}

bool UcgdPigpioProvider::supportsGpio() const {
    return true;
}

bool UcgdPigpioProvider::supportsSPI() const {
    return true;
}

bool UcgdPigpioProvider::supportsI2C() const {
    return true;
}

std::shared_ptr<UcgdGpioPeripheral> UcgdPigpioProvider::createGpioPeripheral() {
    return std::make_shared<UcgdPigpioGpioPeripheral>(getPointer());
}

std::shared_ptr<UcgdI2CPeripheral> UcgdPigpioProvider::createI2CPeripheral() {
    return std::make_shared<UcgdPigpioI2CPeripheral>(getPointer());
}

std::shared_ptr<UcgdSpiPeripheral> UcgdPigpioProvider::createSpiPeripheral() {
    return std::make_shared<UcgdPigpioSpiPeripheral>(getPointer());
}

std::shared_ptr<UcgdPigpioProvider> UcgdPigpioProvider::getPointer() {
    return shared_from_this();
}
