/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioGpioProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGPIGPIOGPIOPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGPIGPIOGPIOPROVIDER_H

#include <UcgGpioProvider.h>
#include "UcgPigpioProvider.h"

class UcgPigpioGpioProvider : public UcgGpioProvider {
public:
    explicit UcgPigpioGpioProvider(UcgIOProvider *provider);

    ~UcgPigpioGpioProvider() override;

    void init(const std::shared_ptr<ucgd_t> &context, int pin, GpioMode direction) override;

    void write(int pin, uint8_t value) override;

    UcgPigpioProvider *getProvider() override;

protected:
    bool isModeSupported(const GpioMode &mode) override;
};

#endif //UCGD_MOD_GRAPHICS_UCGPIGPIOGPIOPROVIDER_H
