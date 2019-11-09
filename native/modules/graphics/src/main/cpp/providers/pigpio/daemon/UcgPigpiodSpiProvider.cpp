/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpiodSpiProvider.cpp
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
#include "UcgPigpiodSpiProvider.h"

#include <UcgGpioProvider.h>
#include <iostream>
#include <pigpiod_if2.h>

UcgPigpiodSpiProvider::UcgPigpiodSpiProvider(UcgIOProvider *provider) : UcgSpiProvider(provider), m_PigpioHandle(-1) {

}

UcgPigpiodSpiProvider::~UcgPigpiodSpiProvider() = default;;

void UcgPigpiodSpiProvider::open(const std::shared_ptr<ucgd_t> &context) {
    if (context->tp_spi_handle >= 0)
        throw SpiOpenException(std::string("SPI device is already open: ") + std::to_string(context->tp_spi_handle));

    if (m_PigpioHandle <= -1) {
        m_PigpioHandle = this->getProvider()->getHandle();
    }

    int speed = context->getOptionInt(OPT_BUS_SPEED, DEFAULT_SPI_SPEED);
    int peripheral = context->getOptionInt(OPT_SPI_BUS, DEFAULT_SPI_PERIPHERAL);
    int channel = context->getOptionInt(OPT_SPI_CHANNEL, DEFAULT_SPI_CHANNEL);
    int flags = context->getOptionInt(OPT_SPI_FLAGS, DEFAULT_SPI_FLAGS);

    //Update peripheral flag
    if (peripheral == SPI_PERIPHERAL_MAIN) {
        flags &= ~(1 << 8); // NOLINT(hicpp-signed-bitwise)
        log.debug("spi_open() : [PIGPIOD] Using Main SPI Peripheral");
    } else if (peripheral == SPI_PERIPHERAL_AUX) {
        flags |= 1 << 8; // NOLINT(hicpp-signed-bitwise)
        log.debug("spi_open() : [PIGPIOD] Using Auxillary SPI Peripheral");
    } else {
        throw SpiOpenException("spi_open() : [PIGPIOD] Invalid SPI peripheral value. Valid values are 0 = Main, 1 = Auxillary");
    }

    log.debug("spi_open() : [PIGPIOD] SPI Params: Provider = {}, Peripheral = {}, Speed = {}, Channel = {}, Flags = {}",
                                               this->getProvider()->getName(),
                                               peripheral,
                                               speed,
                                               channel,
                                               flags
    );

    context->tp_spi_handle = spi_open(m_PigpioHandle, channel, speed, flags);

    if (context->tp_spi_handle < 0) {
        std::stringstream ss;
        ss << "Failed to open spi device (channel: " << std::to_string(channel)
           << ", speed: " << std::to_string(speed)
           << ", flags: " << std::to_string(flags)
           << ", handle: " << std::to_string(this->getProvider()->getHandle())
           << ", Code: " << std::to_string(context->tp_spi_handle) << ")";
        throw SpiOpenException(ss.str());
    }

    printDebugInfo(context);

    log.debug("spi_open() : [PIGPIOD] Successfully opened SPI device");
}

int UcgPigpiodSpiProvider::write(const std::shared_ptr<ucgd_t> &context, uint8_t *buffer, int count) {
    if (context->tp_spi_handle < 0) {
        throw SpiWriteException("write() : [PIGPIOD] SPI device not open");
    }
    int retval = spi_write(this->getProvider()->getHandle(), context->tp_spi_handle, (char *) buffer, count);
    if (retval < 0) {
        std::string reason;
        switch (retval) {
            case PI_BAD_HANDLE:
                reason = "Unknown handle";
                break;
            case PI_BAD_SPI_COUNT:
                reason = "Bad SPI count";
                break;
            case PI_SPI_XFER_FAILED:
                reason = "SPI xfer/read/write failed";
                break;
            default:
                reason = "Unknown";
                break;
        }
        throw SpiWriteException(reason);
    }

    return retval;
}

UcgPigpiodProvider *UcgPigpiodSpiProvider::getProvider() {
    return dynamic_cast<UcgPigpiodProvider *>(UcgProviderBase::getProvider());
}

void UcgPigpiodSpiProvider::close(const std::shared_ptr<ucgd_t> &context) {
    _close(context);
}

void UcgPigpiodSpiProvider::_close(const std::shared_ptr<ucgd_t> &context) {
    spi_close(this->m_PigpioHandle, context->tp_spi_handle);
    m_PigpioHandle = -1;
    context->tp_spi_handle = -1;
}
