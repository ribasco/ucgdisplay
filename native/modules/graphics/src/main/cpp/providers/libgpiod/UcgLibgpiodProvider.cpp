/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgLibgpiodProvider.cpp
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
#include <utility>
#include <system_error>
#include <UcgLibgpiodProvider.h>
#include <UcgLibgpiodGpioProvider.h>
#include <iostream>

UcgLibgpiodProvider::~UcgLibgpiodProvider() = default;

const std::shared_ptr<gpiod::chip> &UcgLibgpiodProvider::getChip() const {
    return m_Chip;
}

UcgLibgpiodProvider::UcgLibgpiodProvider(const std::shared_ptr<u8g2_info_t> &info) : UcgIOProvider(info, PROVIDER_LIBGPIOD) {
    info->log->debug("init_provider() : [LIBGPIOD] Initializing provider");
    try {
        std::string devicePath = info->getOptionString(OPT_DEVICE_GPIO_PATH);
        this->m_Chip = std::make_shared<gpiod::chip>(devicePath);
        setGPIOProvider(std::make_shared<UcgLibgpiodGpioProvider>(this));
    } catch (const std::system_error &e) {
        throw UcgProviderInitException(e);
    }
}