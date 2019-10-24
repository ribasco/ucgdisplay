/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgGpio.h
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
//
// Created by raffy on 22/10/2019.
//

#ifndef UCGD_MOD_GRAPHICS_UCGGPIO_H
#define UCGD_MOD_GRAPHICS_UCGGPIO_H

#include <utility>
#include "U8g2Hal.h"

class GpioException: public std::exception
{
    virtual const char* what() const noexcept
    {
        return "GPIO line init exception";
    }
};

struct u8g2_info_t;

class UcgGpio {

public:
    enum GpioDirection : int {
        DIR_INPUT = 0,
        DIR_OUTPUT = 1,
        DIR_ASIS = 2
    };

    UcgGpio(std::shared_ptr<u8g2_info_t> info) : info(std::move(info)) { }
    virtual void initLine(int pin, GpioDirection direction) = 0;
    virtual void digitalWrite(int pin, uint8_t value) = 0;

protected:
    std::shared_ptr<u8g2_info_t> info;
};

#endif //UCGD_MOD_GRAPHICS_UCGGPIO_H
