/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioI2CProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGDPIGPIOI2CPERIPHERAL_H
#define UCGD_MOD_GRAPHICS_UCGDPIGPIOI2CPERIPHERAL_H

#include <UcgdI2CPeripheral.h>
#include <UcgdPigpioProvider.h>

class UcgdPigpioI2CPeripheral : public UcgdI2CPeripheral {
public:
    explicit UcgdPigpioI2CPeripheral(const std::shared_ptr<UcgdProvider>& provider);

    ~UcgdPigpioI2CPeripheral() override;

    void open(const std::shared_ptr<ucgd_t> &context) override;

    int write(const std::shared_ptr<ucgd_t> &context, unsigned short address, const uint8_t *buffer, unsigned short length) override;

private:
    static std::string _get_errmsg(int val);
};


#endif //UCGD_MOD_GRAPHICS_UCGDPIGPIOI2CPERIPHERAL_H
