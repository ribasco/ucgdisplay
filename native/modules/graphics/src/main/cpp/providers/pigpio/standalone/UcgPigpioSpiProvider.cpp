/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioSpiProvider.cpp
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
#include "UcgPigpioSpiProvider.h"
#include <UcgGpioProvider.h>
#include <iostream>

#include <pigpio.h>

enum SpiFlags : int {
    SPI_FLAG_MODE_0,                     //Mode 0           = 0 0                                                                                                                                            (Bits 0 and 1)
    SPI_FLAG_MODE_1,                     //Mode 1           = 0 1                                                                                                                                            (Bits 0 and 1)
    SPI_FLAG_MODE_2,                     //Mode 2           = 1 0                                                                                                                                            (Bits 0 and 1)
    SPI_FLAG_MODE_3,                     //Mode 3           = 1 1                                                                                                                                            (Bits 0 and 1)
    SPI_FLAG_CE0,                        //0                = active low, 1 = active high                                                                                                                    (Bit 2)
    SPI_FLAG_CE1,                        //0                = active low, 1 = active high                                                                                                                    (Bit 3)
    SPI_FLAG_CE2,                        //0                = active low, 1 = active high                                                                                                                    (Bit 4)
    SPI_FLAG_CE0_RES,                    //1                = reserve GPIO for SPI otherwise, 0 = no reservation                                                                                             (Bit 5)
    SPI_FLAG_CE1_RES,                    //1                = reserve GPIO for SPI otherwise, 0 = no reservation                                                                                             (Bit 6)
    SPI_FLAG_CE2_RES,                    //1                = reserve GPIO for SPI otherwise, 0 = no reservation                                                                                             (Bit 7)
    SPI_FLAG_PERIPHERAL = 1UL << 8,      //0                = Main SPI, 1 = Auxillary SPI                                                                                                                    (Bit 8)
    SPI_FLAG_3WIRE,                      //0                = NOT 3-wire SPI, 1 = 3-wire SPI (Main SPI Only)                                                                                                 (Bit 9)
    SPI_FLAG_NBYTES,                     //n n n n (0 - 15) = the number of bytes (0-15) to write before switching the MOSI line to MISO to read data. This field is ignored if W is not set. Main SPI only. (Bits 10-13)
    SPI_FLAG_AUX_TBITORDER,              //1                = if the least significant bit is transmitted on MOSI first. The default (0) shifts the most significant bit out first. Auxiliary SPI only.      (Bit 14)
    SPI_FLAG_AUX_RBITORDER,              //1                = if the least significant bit is received on MISO first. The default (0) receives the most significant bit first. Auxiliary SPI only.           (Bit 15)
    SPI_FLAG_AUX_WORDSIZE,               //bbbbbb           = defines the word size in bits (0-32). The default (0) sets 8 bits per word. Auxiliary SPI only                                                 (Bit 16-21)
};

UcgPigpioSpiProvider::UcgPigpioSpiProvider(UcgIOProvider *provider) : UcgSpiProvider(provider) {

}

UcgPigpioSpiProvider::~UcgPigpioSpiProvider() = default;;

void UcgPigpioSpiProvider::open(const std::shared_ptr<ucgd_t> &context) {
    if (context->tp_spi_handle >= 0)
        throw SpiOpenException(std::string("SPI device is already open: ") + std::to_string(context->tp_spi_handle));

    int peripheral = context->getOptionInt(OPT_SPI_BUS); //required
    int channel = context->getOptionInt(OPT_SPI_CHANNEL); //required
    int speed = context->getOptionInt(OPT_BUS_SPEED, DEFAULT_SPI_SPEED);
    unsigned int flags = context->getOptionInt(OPT_SPI_FLAGS, DEFAULT_SPI_FLAGS);

    //Update peripheral flag
    if (peripheral == SPI_PERIPHERAL_MAIN) {
        flags &= ~(1u << 8u);
        log.debug("open() : [PIGPIO] Using Main SPI Peripheral");
    } else if (peripheral == SPI_PERIPHERAL_AUX) {
        flags |= 1u << 8u;
        log.debug("open() : [PIGPIO] Using Auxillary SPI Peripheral");
    } else {
        throw SpiOpenException("open() : Invalid SPI peripheral value. Valid values are 0 = Main, 1 = Auxillary");
    }

    log.debug("open() : [PIGPIO] SPI Params: Provider = {}, Peripheral = {}, Speed = {}, Channel = {}, Flags = {}",
                                               this->getProvider()->getName(),
                                               peripheral,
                                               speed,
                                               channel,
                                               flags
    );

    //TODO: spi handle should be placed on the context object
    context->tp_spi_handle = spiOpen(channel, speed, flags);

    if (context->tp_spi_handle < 0) {
        std::stringstream ss;
        ss << "Failed to open spi device (channel: " << std::to_string(channel)
           << ", speed: " << std::to_string(speed)
           << ", flags: " << std::to_string(flags)
           << ", Code: " << std::to_string(context->tp_spi_handle) << ")";
        throw SpiOpenException(ss.str());
    }

    log.debug("open() : [PIGPIO] Successfully opened SPI device");
}

int UcgPigpioSpiProvider::write(const std::shared_ptr<ucgd_t> &context, uint8_t *buffer, int count) {
    if (context->tp_spi_handle < 0) {
        throw SpiWriteException("write() : [PIGPIO] SPI device not open");
    }

    int retval = spiWrite(context->tp_spi_handle, (char *) buffer, count);

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

UcgPigpioProvider *UcgPigpioSpiProvider::getProvider() {
    return dynamic_cast<UcgPigpioProvider *>(UcgProviderBase::getProvider());
}

void UcgPigpioSpiProvider::close(const std::shared_ptr<ucgd_t> &context) {
    _close(context);
}

void UcgPigpioSpiProvider::_close(const std::shared_ptr<ucgd_t> &context) {
    if (context->tp_spi_handle > -1) {
        spiClose(context->tp_spi_handle);
        context->tp_spi_handle = -1;
    }
}
