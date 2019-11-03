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

struct cp_spi_handle {
    int fd;

    struct {
        int c_errno;
        char errmsg[96];
    } error;
};

UcgCperSpiProvider::UcgCperSpiProvider(UcgIOProvider *provider) : UcgSpiProvider(provider) {
    m_SPI = std::unique_ptr<cp_spi_t>(cp_spi_new());
}

UcgCperSpiProvider::~UcgCperSpiProvider() {
    _close();
};

void UcgCperSpiProvider::open() {
    printDebugInfo();

    std::string devicePath = this->getOptionValueString(OPT_DEVICE_SPI_PATH, DEFAULT_SPI_DEV_PATH);
    int speed = this->getOptionValueInt(OPT_DEVICE_SPEED, DEFAULT_SPI_SPEED);
    int flags = this->getOptionValueInt(OPT_SPI_FLAGS, DEFAULT_SPI_FLAGS);
    cp_spi_bit_order bit_order = static_cast<cp_spi_bit_order>(this->getOptionValueInt(OPT_SPI_BIT_ORDER, DEFAULT_SPI_BIT_ORDER));
    uint8_t bits_per_word = this->getOptionValueInt(OPT_SPI_BITS_PER_WORD, DEFAULT_SPI_BITS_PER_WORD);
    int mode = this->getOptionValueInt(OPT_SPI_MODE, DEFAULT_SPI_MODE);

    this->getProvider()->getInfo()->log->debug("open() : [C-PERIPHERY] Bit Order = {}, Bits Per Word = {}, Mode = {}", bit_order, bits_per_word, mode);

    if (devicePath.empty()) {
        throw SpiOpenException("open() : [C-PERIPHERY] SPI Device path not specified");
    }

    //mode = 0,1,2,3
    if (cp_spi_open_advanced(m_SPI.get(), devicePath.c_str(), mode, speed, bit_order, bits_per_word, flags) < 0) {
        std::stringstream ss;
        ss << "open() : Error initializing spi device. Reason: " << std::string(cp_spi_errmsg(m_SPI.get()));
        throw SpiOpenException(ss.str());
    }
}

int UcgCperSpiProvider::write(uint8_t *buffer, int count) {
    int retval;
    if ((retval = cp_spi_transfer(m_SPI.get(), buffer, buffer, count)) < 0) {
        throw SpiWriteException(std::string("write() : Failed to write to spi device. Reason: \"") + std::string(cp_spi_errmsg(m_SPI.get())) + std::string("\""));
    }
    return retval;
}

UcgCperipheryProvider *UcgCperSpiProvider::getProvider() {
    return dynamic_cast<UcgCperipheryProvider *>(UcgProviderBase::getProvider());
}

void UcgCperSpiProvider::close() {
    _close();
}

void UcgCperSpiProvider::_close() {
    cp_spi_close(m_SPI.get());
    cp_spi_free(m_SPI.get());
}
