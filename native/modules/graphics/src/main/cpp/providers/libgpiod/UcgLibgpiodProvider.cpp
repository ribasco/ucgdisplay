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

UcgLibgpiodProvider::UcgLibgpiodProvider() : UcgIOProvider(PROVIDER_LIBGPIOD) {

}

void UcgLibgpiodProvider::initialize(const std::shared_ptr<ucgd_t>& context) {
    log.debug("init_libgpiod() : [LIBGPIOD] Initializing libgpiod provider");
    try {
        std::string devicePath = UcgGpioProvider::buildGpioDevicePath(context);
        this->m_Chip = std::make_shared<gpiod::chip>(devicePath);
        setInitialized(true);
    } catch (const std::exception &e) {
        setInitialized(false);
        throw UcgProviderInitException(e, this);
    }
}

std::string UcgLibgpiodProvider::getLibraryName() {
    return "libgpiodcxx.so";
}

const std::shared_ptr<UcgSpiProvider> &UcgLibgpiodProvider::getSpiProvider() {
    return UcgIOProvider::getSpiProvider();
}

const std::shared_ptr<UcgI2CProvider> &UcgLibgpiodProvider::getI2CProvider() {
    return UcgIOProvider::getI2CProvider();
}

const std::shared_ptr<UcgGpioProvider> &UcgLibgpiodProvider::getGpioProvider() {
    if (UcgIOProvider::getGpioProvider() == nullptr) {
        log.debug("init_libgpiod() : [LIBGPIOD] Initializing libgpiod GPIO provider");
        setGPIOProvider(std::make_shared<UcgLibgpiodGpioProvider>(this));
    }
    return UcgIOProvider::getGpioProvider();
}

bool UcgLibgpiodProvider::supportsGpio() const {
    return true;
}

bool UcgLibgpiodProvider::supportsSPI() const {
    return false;
}

bool UcgLibgpiodProvider::supportsI2C() const {
    return false;
}

void UcgLibgpiodProvider::close() {

}
