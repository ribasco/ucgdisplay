/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgCperipheryProvider.cpp
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
#include <UcgCperipheryProvider.h>
#include <UcgCperSpiProvider.h>
#include <UcgCperI2CProvider.h>
#include <UcgCperGpioProvider.h>

UcgCperipheryProvider::UcgCperipheryProvider() : UcgIOProvider(PROVIDER_CPERIPHERY) {
}

void UcgCperipheryProvider::initialize(const std::shared_ptr<ucgd_t>& context) {
    log.debug("init_cper() : [C-PERIPHERY] No initialization is needed for this provider");
    setInitialized(true);
}

std::string UcgCperipheryProvider::getLibraryName() {
    //this provider is statically linked to this library, so we do not have a name to return
    return std::string();
}

bool UcgCperipheryProvider::supportsGpio() const {
    return true;
}

bool UcgCperipheryProvider::supportsSPI() const {
    return true;
}

bool UcgCperipheryProvider::supportsI2C() const {
    return true;
}

const std::shared_ptr<UcgSpiProvider> &UcgCperipheryProvider::getSpiProvider() {
    if (!isInitialized()) {
        initialize(nullptr);
    }
    if (UcgIOProvider::getSpiProvider() == nullptr) {
        log.debug("init_cper() : [C-PERIPHERY] Initializing system SPI provider");
        setSPIProvider(std::make_shared<UcgCperSpiProvider>(this));
    }
    return UcgIOProvider::getSpiProvider();
}

const std::shared_ptr<UcgI2CProvider> &UcgCperipheryProvider::getI2CProvider() {
    if (UcgIOProvider::getI2CProvider() == nullptr) {
        log.debug("init_cper() : [C-PERIPHERY] Initializing system I2C provider");
        setI2CProvider(std::make_shared<UcgCperI2CProvider>(this));
    }
    return UcgIOProvider::getI2CProvider();
}

const std::shared_ptr<UcgGpioProvider> &UcgCperipheryProvider::getGpioProvider() {
    if (UcgIOProvider::getGpioProvider() == nullptr) {
        log.debug("init_cper() : [C-PERIPHERY] Initializing system GPIO provider");
        setGPIOProvider(std::make_shared<UcgCperGpioProvider>(this));
    }
    return UcgIOProvider::getGpioProvider();
}

bool UcgCperipheryProvider::isProvided() {
    return true;
}
