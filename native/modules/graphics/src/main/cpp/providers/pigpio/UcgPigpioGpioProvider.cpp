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
#include "UcgPigpioGpioProvider.h"
#include <pigpio.h>
#include <pigpiod_if2.h>
#include <iostream>

#define AUX_SPI (1<<8)
#define AUX_BITS(x) ((x)<<16)

UcgPigpioGpioProvider::UcgPigpioGpioProvider(UcgIOProvider *provider) : UcgGpioProvider(provider) {

}

UcgPigpioGpioProvider::~UcgPigpioGpioProvider() = default;

void UcgPigpioGpioProvider::init(int pin, UcgGpioProvider::GpioMode mode) {
    checkHandle();
    UcgGpioProvider::init(pin, mode);

    if (pin < 0)
        return;

    if (mode == GpioMode::MODE_ASIS) {
        int pigpioMode = get_mode(this->getProvider()->getHandle(), pin);
        mode = pigpioToGpioMode(pigpioMode);
    }

    int res = -1;

    if (this->getProvider()->getType() == PIGPIO_TYPE_DAEMON)
        res = set_mode(this->getProvider()->getHandle(), pin, gpioModeToPigpio(mode));
    else if (this->getProvider()->getType() == PIGPIO_TYPE_STANDALONE)
        res = gpioSetMode(pin, gpioModeToPigpio(mode));

    if (res < 0) {
        if (res == PI_BAD_GPIO) {
            std::stringstream ss;
            ss << "init() : Invalid GPIO pin (" << std::to_string(pin) << "). Must be between 0 and 53";
            throw GpioModeException(ss.str());
        } else if (res == PI_BAD_MODE) {
            throw GpioModeException("init() : Invalid mode number. Must be between 0 to 7 (See http://abyz.me.uk/rpi/pigpio/pdif2.html#set_mode)");
        } else if (res == PI_NOT_PERMITTED) {
            throw GpioModeException("init() : GPIO operation not permitted");
        } else {
            throw GpioModeException("init() : Unknown error");
        }
    }

    this->getProvider()->getInfo()->log->debug("init_gpio() : [PIGPIO] Pin = {}, Mode = {}", pin, std::to_string(mode));
}

void UcgPigpioGpioProvider::write(int pin, uint8_t value) {
    checkHandle();
    if (pin < 0)
        return;

    int res = -1;
    if (this->getProvider()->getType() == PIGPIO_TYPE_DAEMON)
        res = gpio_write(this->getProvider()->getHandle(), pin, value);
    else if (this->getProvider()->getType() == PIGPIO_TYPE_STANDALONE)
        res = gpioWrite(pin, value);

    if (res == PI_BAD_GPIO) {
        throw GpioWriteException(std::string("write() : Invalid GPIO pin (") + std::to_string(pin) + std::string("). Must be between 0 and 53"));
    } else if (res == PI_BAD_LEVEL) {
        throw GpioWriteException("write() : Invalid level. Must be either 0 or 1");
    } else if (res == PI_NOT_PERMITTED) {
        throw GpioWriteException("write() : GPIO operation not permitted");
    } else {
        throw GpioWriteException("write() : Unknown error");
    }
}

void UcgPigpioGpioProvider::checkHandle() {
    if (this->getProvider()->getType() == PIGPIO_TYPE_DAEMON && (this->getProvider()->getHandle() < 0))
        throw GpioException(std::string("checkHandle() : Invalid pigpio handle: ") + std::to_string(this->getProvider()->getHandle()));
}

bool UcgPigpioGpioProvider::isModeSupported(const UcgGpioProvider::GpioMode &mode) {
    return (mode >= 0 && mode <= 8);
}

UcgGpioProvider::GpioMode UcgPigpioGpioProvider::pigpioToGpioMode(int mode) {
    switch (mode) {
        case PI_INPUT: {
            return GpioMode::MODE_INPUT;
        }
        case PI_OUTPUT: {
            return GpioMode::MODE_OUTPUT;
        }
        case PI_ALT0: {
            return GpioMode::MODE_ALT0;
        }
        case PI_ALT1: {
            return GpioMode::MODE_ALT1;
        }
        case PI_ALT2: {
            return GpioMode::MODE_ALT2;
        }
        case PI_ALT3: {
            return GpioMode::MODE_ALT3;
        }
        case PI_ALT4: {
            return GpioMode::MODE_ALT4;
        }
        case PI_ALT5: {
            return GpioMode::MODE_ALT5;
        }
        default: {
            std::stringstream ss;
            ss << "modeToInt() : Unable to perform mode conversion. Reason: mode is invalid or not supported: " << std::to_string(mode);
            throw GpioModeException(ss.str());
        }
    }
}

unsigned int UcgPigpioGpioProvider::gpioModeToPigpio(UcgGpioProvider::GpioMode mode) {
    switch (mode) {
        case GpioMode::MODE_INPUT: {
            return PI_INPUT;
        }
        case GpioMode::MODE_OUTPUT: {
            return PI_OUTPUT;
        }
        case GpioMode::MODE_ALT0: {
            return PI_ALT0;
        }
        case GpioMode::MODE_ALT1: {
            return PI_ALT1;
        }
        case GpioMode::MODE_ALT2: {
            return PI_ALT2;
        }
        case GpioMode::MODE_ALT3: {
            return PI_ALT3;
        }
        case GpioMode::MODE_ALT4: {
            return PI_ALT4;
        }
        case GpioMode::MODE_ALT5: {
            return PI_ALT5;
        }
        default: {
            std::stringstream ss;
            ss << "modeToInt() : Unable to perform mode conversion. Reason: mode is invalid or not supported: " << std::to_string(mode);
            throw GpioModeException(ss.str());
        }
    }
}

UcgPigpioProvider *UcgPigpioGpioProvider::getProvider() {
    return dynamic_cast<UcgPigpioProvider *>(UcgProviderBase::getProvider());
}

void UcgPigpioGpioProvider::close() {
    //no-op
}
