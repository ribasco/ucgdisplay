/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgCperI2CProvider.cpp
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
#include <UcgdCperI2CPeripheral.h>
#include <sstream>
#include <i2c.h>

UcgdCperI2CPeripheral::UcgdCperI2CPeripheral(const std::shared_ptr<UcgdProvider>& provider) : UcgdI2CPeripheral(provider) {
}

UcgdCperI2CPeripheral::~UcgdCperI2CPeripheral() {
    debug("UcgCperI2CProvider : destructor called");
    for (auto const &device : m_OpenDevices) {
        int retval = cp_i2c_close(device->sys_i2c_handle.get());
        //cp_i2c_free(device->sys_i2c_handle.get());
        device->sys_i2c_handle = nullptr;
    }
};

void UcgdCperI2CPeripheral::open(const std::shared_ptr<ucgd_t> &context) {
    if (context->sys_i2c_handle != nullptr) {
        context->sys_i2c_handle = std::unique_ptr<cp_i2c_t>(cp_i2c_new());
    } else {
        throw I2COpenException("There already is an existing i2c handle that is open for this context");
    }

    std::string devicePath = UcgdI2CPeripheral::buildI2CDevicePath(context);

    printDebugInfo(context);

    if (cp_i2c_open(context->sys_i2c_handle.get(), devicePath.c_str()) < 0) {
        const char *errmsg = cp_i2c_errmsg(context->sys_i2c_handle.get());
        std::stringstream ss;
        ss << "cp_i2c_open() : Failed to open i2c device: " << std::string(devicePath);
        throw I2COpenException(ss.str());
    }

    registerDevice(context);
}

int UcgdCperI2CPeripheral::write(const std::shared_ptr<ucgd_t>& context, unsigned short address, const uint8_t *buffer, unsigned short length) {
    if (context->sys_i2c_handle == nullptr) {
        return -1;
    }
    struct i2c_msg i2cMsg = {.addr = address, .flags = 0, .len = length, .buf = const_cast<__u8 *>(buffer)};
    int retval;
    if ((retval = cp_i2c_transfer(context->sys_i2c_handle.get(), &i2cMsg, 1)) < 0) {
        const char *errmsg = cp_i2c_errmsg(context->sys_i2c_handle.get());
        std::stringstream ss;
        ss << "Failed to write to i2c device: " << std::string(errmsg);
        throw I2CWriteException(ss.str());
    }
    return retval;
}
