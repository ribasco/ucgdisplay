/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgCperSpiProvider.cpp
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

#include <UcgCperSpiProvider.h>
#include <spi.h>
#include <sstream>
#include <iostream>

UcgCperSpiProvider::UcgCperSpiProvider(UcgIOProvider *provider) : UcgSpiProvider(provider) {
}

UcgCperSpiProvider::~UcgCperSpiProvider() = default;

void UcgCperSpiProvider::open(const std::shared_ptr<ucgd_t> &context) {

    if (context->sys_spi_handle != nullptr) {
        throw SpiOpenException("SPI device is already open");
    }

    context->sys_spi_handle = std::unique_ptr<cp_spi_t>(cp_spi_new());

    printDebugInfo(context);

    std::string devicePath = UcgSpiProvider::buildSPIDevicePath(context);
    int speed = context->getOptionInt(OPT_BUS_SPEED, DEFAULT_SPI_SPEED);
    int flags = context->getOptionInt(OPT_SPI_FLAGS, DEFAULT_SPI_FLAGS);

    cp_spi_bit_order bit_order = static_cast<cp_spi_bit_order>(context->getOptionInt(OPT_SPI_BIT_ORDER, DEFAULT_SPI_BIT_ORDER));
    uint8_t bits_per_word = context->getOptionInt(OPT_SPI_BITS_PER_WORD, DEFAULT_SPI_BITS_PER_WORD);
    int mode = context->getOptionInt(OPT_SPI_MODE, DEFAULT_SPI_MODE);

    log.debug("open() : [C-PERIPHERY] Bit Order = {}, Bits Per Word = {}, Mode = {}", bit_order, bits_per_word, mode);

    if (devicePath.empty()) {
        throw SpiOpenException("open() : [C-PERIPHERY] Device path is empty");
    }

    //mode = 0,1,2,3
    if (cp_spi_open_advanced(context->sys_spi_handle.get(), devicePath.c_str(), mode, speed, bit_order, bits_per_word, flags) < 0) {
        std::stringstream ss;
        ss << "open() : Error initializing spi device. Reason: " << std::string(cp_spi_errmsg(context->sys_spi_handle.get()));
        throw SpiOpenException(ss.str());
    }
}

int UcgCperSpiProvider::write(const std::shared_ptr<ucgd_t> &context, uint8_t *buffer, int count) {
    int retval;
    if ((retval = cp_spi_transfer(context->sys_spi_handle.get(), buffer, buffer, count)) < 0) {
        throw SpiWriteException(std::string("write() : Failed to write to spi device. Reason: \"") + std::string(cp_spi_errmsg(context->sys_spi_handle.get())) + std::string("\""));
    }
    return retval;
}

UcgCperipheryProvider *UcgCperSpiProvider::getProvider() {
    return dynamic_cast<UcgCperipheryProvider *>(UcgProviderBase::getProvider());
}

void UcgCperSpiProvider::close(const std::shared_ptr<ucgd_t> &context) {
    if (context->sys_spi_handle != nullptr) {
        cp_spi_close(context->sys_spi_handle.get());
        cp_spi_free(context->sys_spi_handle.get());
        context->sys_spi_handle.reset();
        context->sys_spi_handle = nullptr;
    }
}
