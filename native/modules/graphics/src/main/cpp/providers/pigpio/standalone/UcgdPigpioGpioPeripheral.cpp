/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioGpioProvider.cpp
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
#include "UcgdPigpioGpioPeripheral.h"
#include <pigpio.h>
#include <iostream>
#include <UcgdPigpioCommon.h>

#define AUX_SPI (1<<8)
#define AUX_BITS(x) ((x)<<16)

UcgdPigpioGpioPeripheral::UcgdPigpioGpioPeripheral(const std::shared_ptr<UcgdProvider>& provider) : UcgdGpioPeripheral(provider) {

}

UcgdPigpioGpioPeripheral::~UcgdPigpioGpioPeripheral() {
    debug("UcgdPigpioGpioPeripheral : destructor");
};

void UcgdPigpioGpioPeripheral::init(const std::shared_ptr<ucgd_t> &context, int pin, UcgdGpioPeripheral::GpioMode mode) {

    UcgdGpioPeripheral::init(context, pin, mode);

    if (pin < 0)
        return;

    if (mode == GpioMode::MODE_ASIS) {
        int pigpioMode = gpioGetMode(pin);
        mode = UcgdPigpioCommon::pigpioToGpioMode(pigpioMode);
    }

    int res = gpioSetMode(pin, UcgdPigpioCommon::gpioModeToPigpio(mode));

    if (res < 0) {
        if (res == PI_BAD_GPIO) {
            std::stringstream ss;
            ss << "init() : [PIGPIO] Invalid GPIO pin (" << std::to_string(pin) << "). Must be between 0 and 53";
            throw GpioModeException(ss.str());
        } else if (res == PI_BAD_MODE) {
            throw GpioModeException("init() : [PIGPIO] Invalid mode number. Must be between 0 to 7 (See http://abyz.me.uk/rpi/pigpio/pdif2.html#set_mode)");
        } else if (res == PI_NOT_PERMITTED) {
            throw GpioModeException("init() : [PIGPIO] GPIO operation not permitted");
        } else {
            throw GpioModeException("init() : [PIGPIO] Unknown error");
        }
    }

    log.debug("init_gpio() : [PIGPIO] Pin = {}, Mode = {}", pin, std::to_string(mode));
}

void UcgdPigpioGpioPeripheral::write(int pin, uint8_t value) {
    if (pin < 0)
        return;

    int res = gpioWrite(pin, value);

    if (res == 0) {
        return;
    } else if (res == PI_BAD_GPIO) {
        throw GpioWriteException(std::string("write() : [PIGPIO] Invalid GPIO pin (") + std::to_string(pin) + std::string("). Must be between 0 and 53"));
    } else if (res == PI_BAD_LEVEL) {
        throw GpioWriteException("write() : [PIGPIO] Invalid level. Must be either 0 or 1");
    } else if (res == PI_NOT_PERMITTED) {
        throw GpioWriteException("write() : [PIGPIO] GPIO operation not permitted");
    } else {
        throw GpioWriteException(std::string("write() : [PIGPIO] Could not initialize GPIO pin. Reason code: ") + std::to_string(res));
    }
}

bool UcgdPigpioGpioPeripheral::isModeSupported(const UcgdGpioPeripheral::GpioMode &mode) {
    return (mode >= 0 && mode <= 8);
}