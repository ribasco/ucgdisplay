/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpiodI2CProvider.cpp
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
#include "UcgdPigpiodI2CPeripheral.h"
#include <pigpiod_if2.h>
#include <sstream>
#include <UcgdPigpioCommon.h>

UcgdPigpiodI2CPeripheral::UcgdPigpiodI2CPeripheral(const std::shared_ptr<UcgdProvider>& provider) : UcgdI2CPeripheral(provider), m_PigpioHandle(-1) {

}

UcgdPigpiodI2CPeripheral::~UcgdPigpiodI2CPeripheral() {
    debug("UcgPigpiodI2CProvider : destructor");
    for (auto const &device : m_OpenDevices) {
        int retval = i2c_close(m_PigpioHandle, device->tp_i2c_handle);
        if (retval < 0) {
            debug("UcgdPigpiodI2CPeripheral: failed to close i2c peripheral");
        }
        m_PigpioHandle = -1;
        device->tp_i2c_handle = -1;
    }
}

void UcgdPigpiodI2CPeripheral::open(const std::shared_ptr<ucgd_t> &context) {
    printDebugInfo(context);

    if (m_PigpioHandle <= -1) {
        auto provider = std::dynamic_pointer_cast<UcgdPigpiodProvider>(getProvider());
        m_PigpioHandle = provider->getHandle();
    }

    int busNumber = context->getOptionInt(OPT_I2C_BUS, DEFAULT_I2C_BUS);
    int address = context->getOptionInt(OPT_I2C_ADDRESS);
    int flags = context->getOptionInt(OPT_I2C_FLAGS);

    //Note: Returns a handle (>=0) if OK, otherwise PI_BAD_I2C_BUS, PI_BAD_I2C_ADDR, PI_BAD_FLAGS, PI_NO_HANDLE, or PI_I2C_OPEN_FAILED.
    context->tp_i2c_handle = i2c_open(m_PigpioHandle, busNumber, address, flags);

    if (context->tp_i2c_handle < 0) {
        std::stringstream ss;
        ss << "Failed to open I2C device. Reason: " << _get_errmsg(context->tp_i2c_handle);
        throw I2COpenException(ss.str());
    }

    registerDevice(context);
}

int UcgdPigpiodI2CPeripheral::write(const std::shared_ptr<ucgd_t>& context, unsigned short address, const uint8_t *buffer, unsigned short length) {
    int retval = -1;
    retval = i2c_write_device(m_PigpioHandle, context->tp_i2c_handle, (char *) buffer, length);
    if (retval < 0) {
        std::stringstream ss;
        ss << "Failed to write to I2C device. Reason: " << _get_errmsg(retval);
        throw I2CWriteException(ss.str());
    }
    return retval;
}

std::string UcgdPigpiodI2CPeripheral::_get_errmsg(int val) {
    std::string reason;
    switch (val) {
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
            //int busNumber = context->getOptionInt(OPT_I2C_BUS, DEFAULT_I2C_BUS);
            //reason = std::string("Can't open I2C device: ") + std::to_string(busNumber);
            reason = std::string("Can't open I2C device");
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


