/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgI2CProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGI2CPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGI2CPROVIDER_H

#include <UcgProviderBase.h>

#define DEFAULT_I2C_DEVICE_PATH "/dev/i2c-1"
#define DEFAULT_I2C_BUS 1

class I2CException : public std::runtime_error {
public:
    explicit I2CException(const std::string &arg) : runtime_error(arg) {}

    explicit I2CException(const char *string) : runtime_error(string) {}

    explicit I2CException(const runtime_error &error) : runtime_error(error) {}
};

class I2COpenException : public I2CException {
public:
    explicit I2COpenException(const std::string &arg) : I2CException(arg) {}

    explicit I2COpenException(const char *string) : I2CException(string) {}

    explicit I2COpenException(const runtime_error &error) : I2CException(error) {}
};

class I2CWriteException : public I2CException {
public:
    explicit I2CWriteException(const std::string &arg) : I2CException(arg) {}

    explicit I2CWriteException(const char *string) : I2CException(string) {}

    explicit I2CWriteException(const runtime_error &error) : I2CException(error) {}
};

class UcgI2CProvider : public UcgProviderBase {

public:
    explicit UcgI2CProvider(UcgIOProvider *provider) : UcgProviderBase(provider) {}

    ~UcgI2CProvider() override = default;;

    virtual int write(const std::shared_ptr<ucgd_t>& context, unsigned short address, const uint8_t *buffer, unsigned short length) {};

protected:

    static std::string buildI2CDevicePath(const std::shared_ptr<ucgd_t>& context) {
        return std::string("/dev/i2c-") + std::to_string(context->getOptionInt(OPT_I2C_BUS, 1));
    }

    void printDebugInfo(const std::shared_ptr<ucgd_t>& context) {
        //std::string devicePath = context->getOptionString(OPT_DEVICE_I2C_PATH, DEFAULT_I2C_DEVICE_PATH);
        std::string devicePath = buildI2CDevicePath(context);
        int bus = context->getOptionInt(OPT_I2C_BUS, 1);
        int flags = context->getOptionInt(OPT_I2C_FLAGS, 0);
        Log log = ServiceLocator::getInstance().getLogger();
        log.debug("=====================================================================");
        log.debug("I2C Setup Info");
        log.debug("=====================================================================");
        log.debug(" - Provider: {}", this->getProvider()->getName());
        log.debug(" -     Path: {}", devicePath);
        log.debug(" -      Bus: {}", std::to_string(bus));
        log.debug(" -    Flags: {}", std::to_string(flags));
        log.debug("=====================================================================");
    }
};

#endif
