/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpiodGpioProvider.cpp
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
#include "UcgdPigpiodGpioPeripheral.h"
#include <pigpiod_if2.h>
#include <iostream>
#include <UcgdPigpioCommon.h>

#define AUX_SPI (1<<8)
#define AUX_BITS(x) ((x)<<16)

UcgdPigpiodGpioPeripheral::UcgdPigpiodGpioPeripheral(const std::shared_ptr<UcgdProvider>& provider) : UcgdGpioPeripheral(provider) {

}

UcgdPigpiodGpioPeripheral::~UcgdPigpiodGpioPeripheral() {
    debug("UcgPigpiodGpioProvider : destructor called");
};

void UcgdPigpiodGpioPeripheral::init(const std::shared_ptr<ucgd_t> &context, int pin, UcgdGpioPeripheral::GpioMode mode) {
    checkHandle();
    UcgdGpioPeripheral::init(context, pin, mode);

    if (pin < 0)
        return;

    auto provider = std::dynamic_pointer_cast<UcgdPigpiodProvider>(getProvider());

    if (mode == GpioMode::MODE_ASIS) {
        int pigpioMode = get_mode(provider->getHandle(), pin);
        mode = UcgdPigpioCommon::pigpioToGpioMode(pigpioMode);
    }

    int res = set_mode(provider->getHandle(), pin, UcgdPigpioCommon::gpioModeToPigpio(mode));
    if (res < 0) {
        //TODO: Move all error messages to UcgPigpioCommon::getErrorMsg and use it instead
        if (res == PI_BAD_GPIO) {
            std::stringstream ss;
            ss << "init() : [PIGPIOD] Invalid GPIO pin (" << std::to_string(pin) << "). Must be between 0 and 53";
            throw GpioModeException(ss.str());
        } else if (res == PI_BAD_MODE) {
            throw GpioModeException("init() : [PIGPIOD] Invalid mode number. Must be between 0 to 7 (See http://abyz.me.uk/rpi/pigpio/pdif2.html#set_mode)");
        } else if (res == PI_NOT_PERMITTED) {
            throw GpioModeException("init() : [PIGPIOD] GPIO operation not permitted");
        } else {
            throw GpioModeException("init() : [PIGPIOD] Unknown error");
        }
    }

    log.debug("init_gpio() : [PIGPIOD] Pin = {}, Mode = {}", pin, std::to_string(mode));
}

void UcgdPigpiodGpioPeripheral::write(int pin, uint8_t value) {
    checkHandle();
    if (pin < 0)
        return;

    auto provider = std::dynamic_pointer_cast<UcgdPigpiodProvider>(getProvider());
    int res = gpio_write(provider->getHandle(), pin, value);

    //TODO: Move all error messages to UcgPigpioCommon::getErrorMsg and use it instead
    if (res == PI_BAD_GPIO) {
        throw GpioWriteException(std::string("write() : [PIGPIOD] Invalid GPIO pin (") + std::to_string(pin) + std::string("). Must be between 0 and 53"));
    } else if (res == PI_BAD_LEVEL) {
        throw GpioWriteException("write() : [PIGPIOD] Invalid level. Must be either 0 or 1");
    } else if (res == PI_NOT_PERMITTED) {
        throw GpioWriteException("write() : [PIGPIOD] GPIO operation not permitted");
    } else {
        throw GpioWriteException("write() : [PIGPIOD] Unknown error");
    }
}

void UcgdPigpiodGpioPeripheral::checkHandle() {
    auto provider = std::dynamic_pointer_cast<UcgdPigpiodProvider>(getProvider());
    if (provider->getHandle() < 0) {
        throw GpioException(std::string("checkHandle() : [PIGPIOD] Invalid pigpio handle: ") +
                            std::to_string(provider->getHandle()));
    }
}

bool UcgdPigpiodGpioPeripheral::isModeSupported(const UcgdGpioPeripheral::GpioMode &mode) {
    return (mode >= 0 && mode <= 8);
}
