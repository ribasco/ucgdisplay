/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgLibgpiodGpioProvider.cpp
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

#include <Global.h>
#include <UcgLibgpiodGpioProvider.h>
#include <UcgLibgpiodProvider.h>

#include <gpiod.hpp>
#include <iostream>
#include <sstream>

UcgLibgpiodGpioProvider::UcgLibgpiodGpioProvider(UcgIOProvider *provider) : UcgGpioProvider(provider) {
}

UcgLibgpiodGpioProvider::~UcgLibgpiodGpioProvider() = default;

void UcgLibgpiodGpioProvider::init(int pin, GpioMode direction) {
    //Do not process unassinged
    if (pin <= -1)
        return;

    auto provider = this->getProvider();
    const std::shared_ptr<gpiod::chip> &chip = provider->getChip();

    if (chip == nullptr)
        throw GpioModeException("GPIO m_Chip has not yet been initialized");

    gpiod::line *gpio_line = findGpioLine(pin);
    if (gpio_line == nullptr) {
        auto it = this->m_LineMap.insert(std::make_pair(pin, chip->get_line(pin)));
        gpio_line = &it.first->second;
    }

    if (gpio_line == nullptr)
        throw GpioModeException("Gpio line instance not found");

    //Release if used
    if (gpio_line->is_used()) {
        gpio_line->release();
    }

    gpio_line->request({GPIOUS_CONSUMER, dirToInt(direction), 0});

    this->getProvider()->getInfo()->log->debug("init_gpio() : [LIBGPIOD] Pin = {}, Mode = {}", pin, std::to_string(direction));
}

void UcgLibgpiodGpioProvider::write(int pin, uint8_t value) {
    //Ignore pins < 0
    if (pin <= -1)
        return;
    //GPIO Userspace code
    gpiod::line *gpio_line = findGpioLine(pin);
    if (gpio_line == nullptr) {
        std::stringstream ss;
        ss << "Could not obtain line reference for line offset: " << std::to_string(pin);
        throw GpioWriteException(ss.str());
    }
    gpio_line->set_value(value);
}

gpiod::line *UcgLibgpiodGpioProvider::findGpioLine(int pin) {
    auto res = this->m_LineMap.find(pin);
    gpiod::line *gpio_line = nullptr;
    if (res != this->m_LineMap.end()) {
        gpio_line = &res->second;
    }
    return gpio_line;
}

int UcgLibgpiodGpioProvider::dirToInt(GpioMode direction) {
    switch (direction) {
        case GpioMode::MODE_ASIS : {
            return gpiod::line_request::DIRECTION_AS_IS;
        }
        case MODE_INPUT: {
            return gpiod::line_request::DIRECTION_INPUT;
        }
        case MODE_OUTPUT: {
            return gpiod::line_request::DIRECTION_OUTPUT;
        }
        default: {
            return gpiod::line_request::DIRECTION_AS_IS;
        }
    }
}

bool UcgLibgpiodGpioProvider::isModeSupported(const UcgGpioProvider::GpioMode &mode) {
    switch (mode) {
        case GpioMode::MODE_INPUT:
        case GpioMode::MODE_OUTPUT:
        case GpioMode::MODE_ASIS:
            return true;
        default:
            return false;
    }
}

void UcgLibgpiodGpioProvider::close() {

}
