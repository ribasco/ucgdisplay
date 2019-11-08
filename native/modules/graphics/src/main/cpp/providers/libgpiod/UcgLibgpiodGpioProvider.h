/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgLibgpiodGpioProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGLIBGPIODGPIOPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGLIBGPIODGPIOPROVIDER_H

#include <UcgGpioProvider.h>
#include <UcgLibgpiodProvider.h>
#include <gpiod.hpp>
#include <map>

class UcgLibgpiodGpioProvider : public UcgGpioProvider {
public:
    explicit UcgLibgpiodGpioProvider(UcgIOProvider *provider);

    ~UcgLibgpiodGpioProvider() override;

    void init(const std::shared_ptr<ucgd_t> &context, int pin, GpioMode direction) override;

    void write(int pin, uint8_t value) override;

    void close() override;

    UcgLibgpiodProvider *getProvider() override {
        return dynamic_cast<UcgLibgpiodProvider *>(UcgProviderBase::getProvider());
    }

protected:
    bool isModeSupported(const GpioMode &mode) override;

private:
    std::map<int, gpiod::line> m_LineMap;
    gpiod::line* findGpioLine(int pin);
    static int dirToInt(GpioMode direction);
};


#endif //UCGD_MOD_GRAPHICS_UCGLIBGPIODGPIOPROVIDER_H
