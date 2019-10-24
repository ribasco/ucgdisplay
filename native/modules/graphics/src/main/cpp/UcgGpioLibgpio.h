/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgGpioLibgpio.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGGPIOLIBGPIO_H
#define UCGD_MOD_GRAPHICS_UCGGPIOLIBGPIO_H

#include "UcgGpio.h"

class UcgGpioLibgpio : public UcgGpio {
public:
    explicit UcgGpioLibgpio(const std::shared_ptr<u8g2_info_t> &info);

    void initLine(int pin, GpioDirection direction) override;

    void digitalWrite(int pin, uint8_t value) override;

private:
    gpiod::line* get_gpio_line(int pin);
    static int convert_dir(GpioDirection direction);
};


#endif //UCGD_MOD_GRAPHICS_UCGGPIOLIBGPIO_H
