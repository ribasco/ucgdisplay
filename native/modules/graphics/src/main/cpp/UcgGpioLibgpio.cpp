/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgGpioLibgpio.cpp
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
#include "UcgGpioLibgpio.h"

#include <gpiod.hpp>
#include <Global.h>

UcgGpioLibgpio::UcgGpioLibgpio(const std::shared_ptr<u8g2_info_t> &info) : UcgGpio(info) {}

void UcgGpioLibgpio::initLine(int pin, GpioDirection direction) {
    try {
        //Do not process unassinged
        if (pin <= -1) {
            //std::cout << "> Skipping line initialization" << std::endl;
            return;
        }

        std::cout << "> Initializing pin: " << std::to_string(pin) << std::endl;

        if (info->gpio_chip == nullptr) {
            JNIEnv *env;
            GETENV(env);
            std::stringstream ss;
            ss << "GPIO chip has not yet been initialized";
            JNI_ThrowNativeLibraryException(env, ss.str());
            return;
        }

        gpiod::line *gpio_line = get_gpio_line(pin);
        if (gpio_line == nullptr) {
            auto it = info->gpio.insert(std::make_pair(pin, info->gpio_chip->get_line(pin)));
            gpio_line = &it.first->second;
        }

        if (gpio_line == nullptr)
            throw GpioException();

        //Release if used
        if (gpio_line->is_used()) {
            gpio_line->release();
        }

        int dir = convert_dir(direction);

        gpio_line->request({GPIOUS_CONSUMER, dir, 0});

        std::cout << "> Successfully Initialized pin: " << std::to_string(pin) << std::endl;
    } catch (const std::system_error &e) {
        JNIEnv *env;
        GETENV(env);
        std::stringstream ss;
        ss << "GPIO line initialization failed (Line: " << std::to_string(pin) << ", Code: " << e.code() << ", Reason: " << e.what() << ")";
        JNI_ThrowNativeLibraryException(env, ss.str());
    }
}

void UcgGpioLibgpio::digitalWrite(int pin, uint8_t value) {
    try {
        //Ignore pins < 0
        if (pin <= -1)
            return;
        //GPIO Userspace code
        gpiod::line *gpio_line = get_gpio_line(pin);
        if (gpio_line == nullptr) {
            JNIEnv *env;
            GETENV(env);
            std::stringstream ss;
            ss << "digital_write(): Could not obtain line reference for line offset: " << std::to_string(pin);
            JNI_ThrowNativeLibraryException(env, ss.str());
            return;
        }
        gpio_line->set_value(value);
    } catch (const std::system_error &e) {
        JNIEnv *env;
        GETENV(env);
        std::stringstream ss;
        ss << "Unable to write value to gpio line (Line: " << std::to_string(pin) << ", Code: " << e.code() << ", Reason: " << e.what() << ")";
        JNI_ThrowNativeLibraryException(env, ss.str());
    }
}

gpiod::line *UcgGpioLibgpio::get_gpio_line(int pin) {
    auto res = info->gpio.find(pin);
    gpiod::line *gpio_line = nullptr;
    if (res != info->gpio.end()) {
        gpio_line = &res->second;
    }
    return gpio_line;
}

int UcgGpioLibgpio::convert_dir(GpioDirection direction) {
    switch (direction) {
        case GpioDirection::DIR_ASIS : {
            return gpiod::line_request::DIRECTION_AS_IS;
        }
        case DIR_INPUT: {
            return gpiod::line_request::DIRECTION_INPUT;
        }
        case DIR_OUTPUT: {
            return gpiod::line_request::DIRECTION_OUTPUT;
        }
        default: {
            return gpiod::line_request::DIRECTION_AS_IS;
        }
    }
}


