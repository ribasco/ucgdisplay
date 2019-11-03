/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgdTypes.h
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
#ifndef UCGD_MOD_GRAPHICS_TYPES_H
#define UCGD_MOD_GRAPHICS_TYPES_H

#include <cstring>
#include <functional>
#include <map>
#include <iostream>
#include <Log.h>

//The <any> header for Mac OSX i386 is only available from the experimental namespace
#if defined(__APPLE__) && defined(__MAC_10_13_4) && !defined(__MAC_10_15)
#include <experimental/any>
typedef std::map<std::string, std::experimental::any> option_map_t;
#else

#include <any>

typedef std::map<std::string, std::any> option_map_t;
#endif

extern "C" {
#include <u8g2.h>
}

//Global macros
#define PROVIDER_LIBGPIOD "libgpiod"
#define PROVIDER_CPERIPHERY "cperiphery"
#define PROVIDER_PIGPIO "pigpio"

/**
 * C linked against pigpio. Fastest code, slowest development.
 * Only one program linked against the pigpio library can be running at a time (the program in effect becomes the pigpio daemon).
 * Program can only run on the Raspberry Pi.
 *
 * @see https://github.com/joan2937/pigpio/issues/129#issuecomment-300442282
 */
#define PIGPIO_TYPE_STANDALONE 0

/**
 * C linked against pigpiod_if2 (via pigpio daemon): Fast code, fast development. Requires the pigpio daemon to be running.
 * Many programs can be running at any one time talking to the pigpio daemon.
 * Programs can be compiled and run on any machine with a suitable compiler, e.g. Linux, Windows, Mac.
 *
 * @see https://github.com/joan2937/pigpio/issues/129#issuecomment-300442282
 */
#define PIGPIO_TYPE_DAEMON 1

//Options
#define OPT_ROTATION "rotation"
#define OPT_DEVICE_SPEED "device_speed"
#define OPT_DEVICE_SPI_PATH "device_path_spi"
#define OPT_DEVICE_I2C_PATH "device_path_i2c"
#define OPT_DEVICE_GPIO_PATH "device_path_gpio"
#define OPT_PROVIDER "default_provider"

#define OPT_PROVIDER_GPIO "provider_gpio"
#define OPT_PROVIDER_SPI "provider_spi"
#define OPT_PROVIDER_I2C "provider_i2c"

#define OPT_SPI_CHANNEL "spi_channel"
#define OPT_SPI_PERIPHERAL "spi_peripheral"
#define OPT_SPI_FLAGS "spi_flags"
#define OPT_SPI_MODE "spi_mode"
#define OPT_SPI_BIT_ORDER "spi_bit_order"
#define OPT_SPI_BITS_PER_WORD "spi_bits_per_word"

#define OPT_I2C_BUS "i2c_bus_number"
#define OPT_I2C_FLAGS "i2c_flags"
#define OPT_I2C_ADDRESS "i2c_address"

//Pigpio specific options
#define OPT_PIGPIO_TYPE "pigpio_mode"
#define OPT_PIGPIO_ADDR "pigpio_addr"
#define OPT_PIGPIO_PORT "pigpio_port"

/*
 * -------------------------------------------------------------------------------------------------------------
 * Note: To enable SPI Auxillary channel on the Raspberry Pi, you need to enable it on the device tree overlay
 * -------------------------------------------------------------------------------------------------------------
 * 1. Add the following to /boot/config.txt
 *
 * # enable spi1 with a single CS line
 *    dtoverlay=spi1-1cs*
 *
 * # enable spi1 with two CS lines
 *    dtoverlay=spi1-2cs*
 *
 * # enable spi1 with three CS lines
 *    dtoverlay=spi1-3cs
 *
 * 2. Verify
 *
 *   lsmod | grep spi
 * -------------------------------------------------------------------------------------------------------------
*/

//Available SPI Peripherals (Main & Auxillary) on the Raspberry Pi
#define SPI_PERIPHERAL_MAIN 0
#define SPI_PERIPHERAL_AUX  1

//Fixed Raspberry Pi Pins for Main SPI Peripheral
#define SPI_RPI_PIN_MAIN_MOSI 10
#define SPI_RPI_PIN_MAIN_MISO 9
#define SPI_RPI_PIN_MAIN_SCLK 11
#define SPI_RPI_PIN_MAIN_CE0 8
#define SPI_RPI_PIN_MAIN_CE1 7

//Fixed Raspberry Pi Pins for Auxillary SPI Peripheral
#define SPI_RPI_PIN_AUX_MOSI 20
#define SPI_RPI_PIN_AUX_MISO 19
#define SPI_RPI_PIN_AUX_SCLK 21
#define SPI_RPI_PIN_AUX_CE0 18
#define SPI_RPI_PIN_AUX_CE1 17
#define SPI_RPI_PIN_AUX_CE2 16

//SPI Channels for the Raspberry Pi (Chip-Select)
#define SPI_RPI_CHANNEL_CE0 0
#define SPI_RPI_CHANNEL_CE1 1
#define SPI_RPI_CHANNEL_CE2 2

//Fixed Raspberry Pi Pins for I2C Peripheral
#define I2C_RPI_PIN_SDA 2
#define I2C_RPI_PIN_SCL 3

//Fixed Raspberry Pi Pins for UART Peripheral
#define UART_RPI_PIN_TXD 14
#define UART_RPI_PIN_RXD 15

//The communication method (Hardware or Software)
#define COMTYPE_HW 0
#define COMTYPE_SW 1

//The communication interfaces (name/value pairs are idential to the u8g2 macros)
#define COMINT_4WSPI 0x0001
#define COMINT_3WSPI 0x0002
#define COMINT_6800  0x0004
#define COMINT_8080 0x0008
#define COMINT_I2C  0x0010
#define COMINT_ST7920SPI 0x0020     /* mostly identical to COM_4WSPI, but does not use DC */
#define COMINT_UART 0x0040
#define COMINT_KS0108  0x0080        /* mostly identical to 6800 mode, but has more chip select lines */
#define COMINT_SED1520  0x0100

//Uncomment to enable debbugging
#define DEBUG_UCGD

class OptionNotFoundException : public std::runtime_error {
public:
    explicit OptionNotFoundException(const std::string &arg) : runtime_error(arg) {}

    explicit OptionNotFoundException(const char *string) : runtime_error(string) {}

    explicit OptionNotFoundException(const runtime_error &error) : runtime_error(error) {}
};

class ProviderNotFoundException : public std::runtime_error {
public:
    explicit ProviderNotFoundException(const std::string &arg) : runtime_error(arg) {};

    explicit ProviderNotFoundException(const char *string) : runtime_error(string) {}

    explicit ProviderNotFoundException(const runtime_error &error) : runtime_error(error) {}
};

//Forward declarations
class UcgIOProvider;

class UcgSpiProvider;

class UcgI2CProvider;

class UcgGpioProvider;

struct u8g2_info_t;

typedef std::function<uint8_t(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr)> u8g2_msg_func_t;

typedef std::function<void(u8g2_t *u8g2, const u8g2_cb_t *rotation, u8x8_msg_cb byte_cb, u8x8_msg_cb gpio_and_delay_cb)> u8g2_setup_func_t;

typedef std::map<std::string, u8g2_setup_func_t> u8g2_setup_func_map_t;

typedef std::map<std::string, const uint8_t *> u8g2_lookup_font_map_t;

typedef std::function<uint8_t(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr)> u8g2_msg_func_info_t;

typedef struct {
    //pin configuration
    int d0 = -1; //spi-clock
    int d1 = -1; //spi-data
    int d2 = -1;
    int d3 = -1;
    int d4 = -1;
    int d5 = -1;
    int d6 = -1;
    int d7 = -1;
    int en = -1;
    int cs = -1;
    int dc = -1;
    int reset = -1;
    int scl = -1;
    int sda = -1;
    int cs1 = -1;
    int cs2 = -1;
} u8g2_pin_map_t;

/**
 * Our main u8g2 descriptor
 */
struct u8g2_info_t {
    u8g2_pin_map_t pin_map;
    std::unique_ptr<u8g2_t> u8g2;
    std::string setup_proc_name;
    u8g2_setup_func_t setup_cb;
    u8g2_msg_func_t byte_cb;
    u8g2_msg_func_t gpio_cb;
    u8g2_cb_t *rotation;
    bool flag_font;
    bool flag_virtual;
    int comm_int;
    int comm_type;
    bool debug;
    std::shared_ptr<Log> log;

    uintptr_t address() {
        return (uintptr_t) u8g2.get();
    }

//Only available on ARM 32/64 bit platforms
#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    std::map<std::string, std::shared_ptr<UcgIOProvider>> providers;
    std::shared_ptr<UcgIOProvider> provider;
    std::map<std::string, std::any> options;

    std::any &getOption(const std::string &key) {
        auto it = this->options.find(key);
        if (it != this->options.end()) {
            return it->second;
        }
        throw OptionNotFoundException(std::string("Key '") + key + std::string("' not found in options"));
    }

    std::string getOptionString(const std::string &key, std::string defaultVal = "") {
        try {
            std::any value = getOption(key);
            return std::any_cast<std::string>(value);
        } catch (OptionNotFoundException &e) {
            return defaultVal;
        }
    }

    int getOptionInt(const std::string &key, int defaultVal = 0) {
        try {
            std::any value = getOption(key);
            return std::any_cast<int>(value);
        } catch (OptionNotFoundException &e) {
            return defaultVal;
        }
    }

#endif
};


#endif //UCGD_MOD_GRAPHICS_TYPES_H
