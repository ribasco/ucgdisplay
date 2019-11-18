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
#include <UcgdCperipheryProvider.h>
#include <UcgdCperSpiPeripheral.h>
#include <UcgdCperI2CPeripheral.h>
#include <UcgdCperGpioPeripheral.h>

UcgdCperipheryProvider::UcgdCperipheryProvider() : UcgdProvider(PROVIDER_CPERIPHERY) {
}

UcgdCperipheryProvider::~UcgdCperipheryProvider() {
    debug("UcgCperipheryProvider : destructor");
}

void UcgdCperipheryProvider::open(const std::shared_ptr<ucgd_t>& context) {
    log.debug("init_cper() : [C-PERIPHERY] No initialization is needed for this provider");
    setInitialized(true);
}

std::string UcgdCperipheryProvider::getLibraryName() {
    //this provider is statically linked to this library, so we do not have a name to return
    return std::string();
}

bool UcgdCperipheryProvider::supportsGpio() const {
    return true;
}

bool UcgdCperipheryProvider::supportsSPI() const {
    return true;
}

bool UcgdCperipheryProvider::supportsI2C() const {
    return true;
}

bool UcgdCperipheryProvider::isProvided() {
    return true;
}

std::shared_ptr<UcgdGpioPeripheral> UcgdCperipheryProvider::createGpioPeripheral() {
    return std::make_shared<UcgdCperGpioPeripheral>(getPointer());
}

std::shared_ptr<UcgdI2CPeripheral> UcgdCperipheryProvider::createI2CPeripheral() {
    return std::make_shared<UcgdCperI2CPeripheral>(getPointer());
}

std::shared_ptr<UcgdSpiPeripheral> UcgdCperipheryProvider::createSpiPeripheral() {
    return std::make_shared<UcgdCperSpiPeripheral>(getPointer());
}

std::shared_ptr<UcgdCperipheryProvider> UcgdCperipheryProvider::getPointer() {
    return this->shared_from_this();
}
