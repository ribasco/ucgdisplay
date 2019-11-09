/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioSpiProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGPIGPIOSPIPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGPIGPIOSPIPROVIDER_H

#include "UcgSpiProvider.h"
#include "UcgPigpioProvider.h"
#include <memory>
#include <utility>
#include <pigpio.h>
#include <sstream>

class UcgPigpioSpiProvider : public UcgSpiProvider {
public:
    explicit UcgPigpioSpiProvider(UcgIOProvider *provider);

    ~UcgPigpioSpiProvider() override;

    void open(const std::shared_ptr<ucgd_t> &context) override;

    int write(const std::shared_ptr<ucgd_t> &context, uint8_t *buffer, int count) override;

    void close(const std::shared_ptr<ucgd_t> &context) override;

    UcgPigpioProvider *getProvider() override;

private:
    void _close(const std::shared_ptr<ucgd_t> &context);
};


#endif //UCGD_MOD_GRAPHICS_UCGPIGPIOSPIPROVIDER_H
