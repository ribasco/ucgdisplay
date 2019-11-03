/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioI2CProvider.cpp
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
#include "UcgPigpioI2CProvider.h"
#include <pigpiod_if2.h>
#include <sstream>

UcgPigpioI2CProvider::UcgPigpioI2CProvider(UcgIOProvider *provider) : UcgI2CProvider(provider), m_PigpioHandle(-1), m_Handle(-1) {}

UcgPigpioI2CProvider::~UcgPigpioI2CProvider() {
    _close();
};

int UcgPigpioI2CProvider::open() {
    printDebugInfo();

    if (m_PigpioHandle <= -1) {
        m_PigpioHandle = this->getProvider()->getHandle();
    }

    int busNumber = getOptionValueInt(OPT_I2C_BUS, DEFAULT_I2C_BUS);
    int address = getOptionValueInt(OPT_I2C_ADDRESS);
    int flags = getOptionValueInt(OPT_I2C_FLAGS);

    if (this->getProvider()->getType() == PIGPIO_TYPE_DAEMON) {
        //Note: Returns a handle (>=0) if OK, otherwise PI_BAD_I2C_BUS, PI_BAD_I2C_ADDR, PI_BAD_FLAGS, PI_NO_HANDLE, or PI_I2C_OPEN_FAILED.
        m_Handle = i2c_open(m_PigpioHandle, busNumber, address, flags);
    } else if (this->getProvider()->getType() == PIGPIO_TYPE_STANDALONE) {
        m_Handle = i2cOpen(busNumber, address, flags);
    }

    if (m_Handle < 0) {
        std::stringstream ss;
        ss << "Failed to open I2C device. Reason: " << _get_errmsg(m_Handle);
        throw I2COpenException(ss.str());
    }

    return m_Handle;
}

int UcgPigpioI2CProvider::write(unsigned short address, const uint8_t *buffer, unsigned short length) {
    int retval = -1;
    if (this->getProvider()->getType() == PIGPIO_TYPE_DAEMON) {
        retval = i2c_write_device(m_PigpioHandle, m_Handle, (char *) buffer, length);
    } else if (this->getProvider()->getType() == PIGPIO_TYPE_STANDALONE) {
        retval = i2cWriteDevice(m_Handle, (char *) buffer, length);
    }

    if (retval < 0) {
        std::stringstream ss;
        ss << "Failed to write to I2C device. Reason: " << _get_errmsg(retval);
        throw I2CWriteException(ss.str());
    }
    return retval;
}

int UcgPigpioI2CProvider::close() {
    return _close();
}

UcgPigpioProvider *UcgPigpioI2CProvider::getProvider() {
    return dynamic_cast<UcgPigpioProvider *>(UcgProviderBase::getProvider());
}

int UcgPigpioI2CProvider::_close() {
    int retval = -1;

    if (this->getProvider()->getType() == PIGPIO_TYPE_DAEMON) {
        retval = i2c_close(m_PigpioHandle, m_Handle);
    } else if (this->getProvider()->getType() == PIGPIO_TYPE_STANDALONE) {
        retval = i2cClose(m_Handle);
    }

    if (retval < 0) {
        std::stringstream ss;
        ss << "Failed to close I2C device. Reason: " << _get_errmsg(retval);
        throw I2CException(ss.str());
    }
    m_PigpioHandle = -1;
    m_Handle = -1;
    return retval;
}

std::string UcgPigpioI2CProvider::_get_errmsg(int val) {
    std::string reason;
    switch (m_Handle) {
        case PI_BAD_I2C_BUS:
            reason = "Bad I2C Bus";
            break;
        case PI_BAD_I2C_ADDR:
            reason = "Bad I2C Address";
            break;
        case PI_BAD_FLAGS:
            reason = "Bad I2C open flags";
            break;
        case PI_NO_HANDLE:
            reason = "No handle available";
            break;
        case PI_I2C_OPEN_FAILED: {
            int busNumber = getOptionValueInt(OPT_I2C_BUS, DEFAULT_I2C_BUS);
            reason = std::string("Can't open I2C device: ") + std::to_string(busNumber);
            break;
        }
        case PI_BAD_HANDLE: {
            reason = "Unknown handle";
            break;
        }
        case PI_BAD_PARAM: {
            reason = "Bad i2c/spi/ser parameter";
            break;
        }
        case PI_I2C_WRITE_FAILED: {
            reason = "I2C write failed";
            break;
        }
        default: {
            reason = "Unknown error";
            break;
        }
    }
    return reason;
}


