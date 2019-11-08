/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgGpioProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGGPIOPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGGPIOPROVIDER_H

#include <utility>
#include <iostream>
#include <sstream>
#include <UcgProviderBase.h>

#define DEFAULT_GPIO_DEVICE_PATH "/dev/gpiochip0"

class GpioException : public std::runtime_error {
public:
    explicit GpioException(const std::string &arg) : runtime_error(arg) {}

    explicit GpioException(const runtime_error &error) : runtime_error(error) {}
};

class GpioInitException : public GpioException {
public:
    explicit GpioInitException(const std::string &arg) : GpioException(arg) {}

    explicit GpioInitException(const runtime_error &error) : GpioException(error) {}
};

class GpioWriteException : public GpioException {
public:
    explicit GpioWriteException(const std::string &arg) : GpioException(arg) {}

    explicit GpioWriteException(const runtime_error &error) : GpioException(error) {}
};

class GpioModeException : public GpioException {
public:
    explicit GpioModeException(const std::string &arg) : GpioException(arg) {}

    explicit GpioModeException(const runtime_error &error) : GpioException(error) {}
};

class UcgGpioProvider : public UcgProviderBase {

public:
    enum GpioMode : int {
        MODE_INPUT = 0,
        MODE_OUTPUT = 1,
        MODE_ASIS = 2,
        MODE_ALT0 = 3,
        MODE_ALT1 = 4,
        MODE_ALT2 = 5,
        MODE_ALT3 = 6,
        MODE_ALT4 = 7,
        MODE_ALT5 = 8
    };

    explicit UcgGpioProvider(UcgIOProvider *provider) : UcgProviderBase(provider), log(ServiceLocator::getInstance().getLogger()) {}

    ~UcgGpioProvider() override = default;

    virtual void init(const std::shared_ptr<ucgd_t>& context, int pin, GpioMode mode) {
        if (!isModeSupported(mode)) {
            std::stringstream ss;
            ss << "init() : GPIO Mode '" << std::to_string(mode) << "' is currently not supported by the gpio provider (" << this->getProvider()->getName() << ")";
            throw GpioModeException(ss.str());
        }
    };

    virtual void close() = 0;

    virtual void write(int pin, uint8_t value) = 0;

    static std::string buildGpioDevicePath(const std::shared_ptr<ucgd_t>& context) {
        int chipNum = context->getOptionInt(OPT_GPIO_CHIP, 0);
        return std::string("/dev/gpiochip") + std::to_string(chipNum);
    }
protected:
    Log log;
    virtual bool isModeSupported(const GpioMode& mode) = 0;
};

#endif //UCGD_MOD_GRAPHICS_UCGGPIOPROVIDER_H
