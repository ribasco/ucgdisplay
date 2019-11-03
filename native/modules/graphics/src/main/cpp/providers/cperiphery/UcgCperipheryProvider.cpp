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

UcgCperipheryProvider::UcgCperipheryProvider(const std::shared_ptr<u8g2_info_t> &info) : UcgIOProvider(info, PROVIDER_CPERIPHERY) {
    info->log->debug("init_provider() : [C-PERIPHERY] Initializing provider");
    setSPIProvider(std::make_shared<UcgCperSpiProvider>(this));
    setI2CProvider(std::make_shared<UcgCperI2CProvider>(this));
    setGPIOProvider(std::make_shared<UcgCperGpioProvider>(this));
}
