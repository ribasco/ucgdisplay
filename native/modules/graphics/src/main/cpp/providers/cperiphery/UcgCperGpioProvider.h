/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgCperGpioProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGCPERGPIOPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGCPERGPIOPROVIDER_H

#include <UcgGpioProvider.h>
#include <UcgCperipheryProvider.h>
#include <gpio.h>
#include <map>

class UcgCperGpioProvider : public UcgGpioProvider {
public:
    explicit UcgCperGpioProvider(UcgIOProvider *provider);

    ~UcgCperGpioProvider() override;

    UcgCperipheryProvider *getProvider() override;

    void init(const std::shared_ptr<ucgd_t>& context, int pin, GpioMode mode) override;

    void write(int pin, uint8_t value) override;

    void close() override;

protected:
    bool isModeSupported(const GpioMode &mode) override;

private:
    std::map<int, std::shared_ptr<gpio_t>> m_GpioLineCache;
    const std::shared_ptr<gpio_t>& findOrCreateGpioLine(int pin);
    int _close();
};


#endif //UCGD_MOD_GRAPHICS_UCGCPERGPIOPROVIDER_H
