/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgSpiProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGDSPIPERIPHERAL_H
#define UCGD_MOD_GRAPHICS_UCGDSPIPERIPHERAL_H

#include <iostream>
#include <UcgdPeripheral.h>

#define DEFAULT_SPI_SPEED 1000000
#define DEFAULT_SPI_CHANNEL SPI_RPI_CHANNEL_CE0
#define DEFAULT_SPI_PERIPHERAL SPI_PERIPHERAL_MAIN
#define DEFAULT_SPI_FLAGS 0
#define DEFAULT_SPI_DEV_PATH "/dev/spidev0.0"
#define DEFAULT_SPI_BITS_PER_WORD 8
#define DEFAULT_SPI_MODE 0
#define DEFAULT_SPI_BIT_ORDER 0 //MSB First

class SpiException : public std::runtime_error {
public:
    explicit SpiException(const std::string &arg) : runtime_error(arg) {}

    explicit SpiException(const char *string) : runtime_error(string) {}

    explicit SpiException(const runtime_error &error) : runtime_error(error) {}
};

class SpiOpenException : public SpiException {
public:
    explicit SpiOpenException(const std::string &arg) : SpiException(arg) {}

    explicit SpiOpenException(const char *string) : SpiException(string) {}

    explicit SpiOpenException(const runtime_error &error) : SpiException(error) {}
};

class SpiWriteException : public SpiException {
public:
    explicit SpiWriteException(const std::string &arg) : SpiException(arg) {}

    explicit SpiWriteException(const char *string) : SpiException(string) {}

    explicit SpiWriteException(const runtime_error &error) : SpiException(error) {}
};

class UcgdSpiPeripheral : public UcgdPeripheral {

public:
    explicit UcgdSpiPeripheral(const std::shared_ptr<UcgdProvider>& provider) : UcgdPeripheral(provider) {

    }

    ~UcgdSpiPeripheral() override {
        debug("UcgdSpiPeripheral: destructor");
    };

    virtual int write(const std::shared_ptr<ucgd_t> &context, uint8_t *buffer, int count) = 0;

protected:

    static std::string buildSPIDevicePath(const std::shared_ptr<ucgd_t>& context) {
        Log& log = ServiceLocator::getInstance().getLogger();
        int peripheral = context->getOptionInt(OPT_SPI_BUS);
        int channel = context->getOptionInt(OPT_SPI_CHANNEL);
        std::string path = std::string("/dev/spidev") + std::to_string(peripheral) + std::string(".") + std::to_string(channel);
        log.debug(std::string("buildSPIDevicePath() : Building device path: BUS = ") + std::to_string(peripheral) + std::string(", CHANNEL = ") + std::to_string(channel) + std::string(", PATH = ") + path);
        return path;
    }

    void printDebugInfo(const std::shared_ptr<ucgd_t>& context) {
        Log& log = ServiceLocator::getInstance().getLogger();

        std::string devicePath = buildSPIDevicePath(context);
        int peripheral = context->getOptionInt(OPT_SPI_BUS, DEFAULT_SPI_PERIPHERAL);
        int speed = context->getOptionInt(OPT_BUS_SPEED, DEFAULT_SPI_SPEED);
        int channel = context->getOptionInt(OPT_SPI_CHANNEL, DEFAULT_SPI_CHANNEL);
        int flags = context->getOptionInt(OPT_SPI_FLAGS, 0);

        log.debug("=====================================================================");
        log.debug("SPI Configuration Parameters");
        log.debug("=====================================================================");
        log.debug(" -   Provider: {}" , this->getProvider()->getName());
        log.debug(" -       Path: {}" , devicePath);
        log.debug(" - Peripheral: {}" , std::to_string(peripheral));
        log.debug(" -      Speed: {}" , std::to_string(speed));
        log.debug(" -    Channel: {}" , std::to_string(channel));
        log.debug(" -      Flags: {}" , std::to_string(flags));
        log.debug("=====================================================================");
    }
};

#endif
