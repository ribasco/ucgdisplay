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
#include <UcgdLibgpiodProvider.h>
#include <UcgdLibgpiodGpioPeripheral.h>
#include <iostream>

UcgdLibgpiodProvider::UcgdLibgpiodProvider() : UcgdProvider(PROVIDER_LIBGPIOD) {

}

UcgdLibgpiodProvider::~UcgdLibgpiodProvider() {
    debug("UcgLibgpiodProvider");
};

const std::shared_ptr<gpiod::chip> &UcgdLibgpiodProvider::getChip() const {
    return m_Chip;
}

void UcgdLibgpiodProvider::open(const std::shared_ptr<ucgd_t>& context) {
    log.debug("init_libgpiod() : [LIBGPIOD] Initializing libgpiod provider");
    try {
        std::string devicePath = UcgdGpioPeripheral::buildGpioDevicePath(context);
        this->m_Chip = std::make_shared<gpiod::chip>(devicePath);
        setInitialized(true);
    } catch (const std::exception &e) {
        setInitialized(false);
        throw UcgProviderInitException(e, this);
    }
}

std::string UcgdLibgpiodProvider::getLibraryName() {
    return "libgpiodcxx.so";
}

std::shared_ptr<UcgdGpioPeripheral> UcgdLibgpiodProvider::createGpioPeripheral() {
    return std::make_shared<UcgdLibgpiodGpioPeripheral>(getPointer());
}

std::shared_ptr<UcgdI2CPeripheral> UcgdLibgpiodProvider::createI2CPeripheral() {
    throw std::runtime_error("I2C peripheral not supported by this provider");
}

std::shared_ptr<UcgdSpiPeripheral> UcgdLibgpiodProvider::createSpiPeripheral() {
    throw std::runtime_error("SPI peripheral not supported by this provider");
}

bool UcgdLibgpiodProvider::supportsGpio() const {
    return true;
}

bool UcgdLibgpiodProvider::supportsSPI() const {
    return false;
}

bool UcgdLibgpiodProvider::supportsI2C() const {
    return false;
}

std::shared_ptr<UcgdLibgpiodProvider> UcgdLibgpiodProvider::getPointer() {
    return this->shared_from_this();
}
