/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2Hal.cpp
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

#include <unistd.h>
#include <iomanip>
#include <Global.h>
#include "U8g2Hal.h"

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

#include <system_error>

#endif

static u8g2_setup_func_map_t u8g2_setup_functions; //NOLINT
static u8g2_lookup_font_map_t u8g2_font_map; //NOLINT

#define DEFAULT_SPI_SPEED 1000000

void u8g2hal_Init() {
    //Initialize lookup tables
    u8g2hal_InitSetupFunctions(u8g2_setup_functions);
    u8g2hal_InitFonts(u8g2_font_map);
}

u8g2_setup_func_t u8g2hal_GetSetupProc(const std::string &function_name) {
    if (u8g2_setup_functions.empty())
        return nullptr;
    auto it = u8g2_setup_functions.find(function_name);
    if (it != u8g2_setup_functions.end()) {
        return it->second;
    }
    return nullptr;
}

uint8_t *u8g2hal_GetFontByName(const std::string &font_name) {
    if (u8g2_font_map.empty())
        return nullptr;
    auto it = u8g2_font_map.find(font_name);
    if (it != u8g2_font_map.end()) {
        return const_cast<uint8_t *>(it->second);
    }
    return nullptr;
}

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

/**
 * 4-wire SPI Hardware Callback Routine (ARM)
 */
uint8_t cb_byte_spi_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            auto *buf = (uint8_t *) arg_ptr;
            if (spi_transfer(info->spi.get(), buf, buf, arg_int) < 0) {
                fprintf(stderr, "spi_transfer(): %s\n", spi_errmsg(info->spi.get()));
                return 0;
            }
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            //disable chip-select
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);
            int speed = info->device_speed <= -1 ? DEFAULT_SPI_SPEED : info->device_speed;
            if (spi_open(info->spi.get(), info->transport_device.c_str(), 0, speed) < 0) {
                fprintf(stderr, "spi_open(): %s\n", spi_errmsg(info->spi.get()));
                return 0;
            }
            break;
        }
        case U8X8_MSG_BYTE_START_TRANSFER: {
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_enable_level);
            u8x8->gpio_and_delay_cb(u8x8, U8X8_MSG_DELAY_NANO, u8x8->display_info->post_chip_enable_wait_ns, nullptr);
            break;
        }
        case U8X8_MSG_BYTE_END_TRANSFER: {
            u8x8->gpio_and_delay_cb(u8x8, U8X8_MSG_DELAY_NANO, u8x8->display_info->pre_chip_disable_wait_ns, nullptr);
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);
            break;
        }
        case U8X8_MSG_BYTE_SET_DC: {
            u8x8_gpio_SetDC(u8x8, arg_int);
        }
        default:
            return 0;
    }
    return 1;
}

/**
 * I2C Hardware Callback Routine (ARM)
 */
uint8_t cb_byte_i2c_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    uint8_t *data;

    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            data = (uint8_t *) arg_ptr;
            __u16 addr = u8x8_GetI2CAddress(u8x8);
            struct i2c_msg i2cMsg = {.addr = addr, .flags = 0, .len = arg_int, .buf = data};
            if (i2c_transfer(info->i2c.get(), &i2cMsg, 1) < 0) {
                fprintf(stderr, "i2c_transfer(): %s\n", i2c_errmsg(info->i2c.get()));
                return 0;
            }
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            if (i2c_open(info->i2c.get(), info->transport_device.c_str()) < 0) {
                fprintf(stderr, "i2c_open(): %s\n", i2c_errmsg(info->i2c.get()));
                return 0;
            }
            break;
        }
        case U8X8_MSG_BYTE_START_TRANSFER: {
            break;
        }
        case U8X8_MSG_BYTE_END_TRANSFER: {
            break;
        }
        default:
            return 0;
    }
    return 1;
}

/**
 * GPIO and Delay Procedure Routine (ARM)
*/
uint8_t cb_gpio_delay(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {
    std::shared_ptr<UcgGpio> gpio = info->gpio_int;

    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: { // called once during init phase of u8g2/u8x8, can be used to setup pins
            gpio->initLine(info->pin_map.d0, UcgGpio::GpioDirection::DIR_ASIS);
            gpio->initLine(info->pin_map.d1, UcgGpio::GpioDirection::DIR_ASIS);
            gpio->initLine(info->pin_map.sda, UcgGpio::GpioDirection::DIR_ASIS);
            gpio->initLine(info->pin_map.scl, UcgGpio::GpioDirection::DIR_ASIS);

            //Configure pin modes
            gpio->initLine(info->pin_map.d2, UcgGpio::GpioDirection::DIR_OUTPUT);
            gpio->initLine(info->pin_map.d3, UcgGpio::GpioDirection::DIR_OUTPUT);
            gpio->initLine(info->pin_map.d4, UcgGpio::GpioDirection::DIR_OUTPUT);
            gpio->initLine(info->pin_map.d5, UcgGpio::GpioDirection::DIR_OUTPUT);
            gpio->initLine(info->pin_map.d6, UcgGpio::GpioDirection::DIR_OUTPUT);
            gpio->initLine(info->pin_map.d7, UcgGpio::GpioDirection::DIR_OUTPUT);
            gpio->initLine(info->pin_map.dc, UcgGpio::GpioDirection::DIR_OUTPUT);

            gpio->initLine(info->pin_map.cs, UcgGpio::GpioDirection::DIR_ASIS);
            gpio->initLine(info->pin_map.cs1, UcgGpio::GpioDirection::DIR_ASIS);
            gpio->initLine(info->pin_map.cs2, UcgGpio::GpioDirection::DIR_ASIS);
            break;
        }
        case U8X8_MSG_DELAY_NANO: { // delay arg_int * 1 nano second
            //this is important. Removing this will cause garbage data to be displayed.
            usleep(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_100NANO: {       // delay arg_int * 100 nano seconds
            usleep(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_10MICRO: {       // delay arg_int * 10 micro seconds
            usleep(arg_int * 10);
            break;
        }
        case U8X8_MSG_DELAY_MILLI: {           // delay arg_int * 1 milli second
            usleep(arg_int * 1000);
            break;
        }
        case U8X8_MSG_DELAY_I2C: {               // arg_int is the I2C speed in 100KHz, e.g. 4 = 400 KHz
            usleep(arg_int);          // arg_int=1: delay by 5us, arg_int = 4: delay by 1.25us
            break;
        }
        case U8X8_MSG_GPIO_D0: {                // D0 or SPI clock pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_CLOCK)
            gpio->digitalWrite(info->pin_map.d0, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D1: {                // D1 or SPI data pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_DATA)
            gpio->digitalWrite(info->pin_map.d1, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D2: {                // D2 pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.d2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D3: {                // D3 pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.d3, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D4: {                // D4 pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.d4, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D5: {                // D5 pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.d5, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D6: {                // D6 pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.d6, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D7: {                // D7 pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.d7, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_E: {               // E/WR pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.en, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS: {                // CS (chip select) pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.cs, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_DC: {                // DC (data/cmd, A0, register select) pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.dc, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_RESET: {            // Reset pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.reset, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS1: {                // CS1 (chip select) pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.cs1, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS2: {                // CS2 (chip select) pin: Output level in arg_int
            gpio->digitalWrite(info->pin_map.cs2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_I2C_CLOCK: {        // arg_int=0: Output low at I2C clock pin
            gpio->digitalWrite(info->pin_map.scl, arg_int);
            break;                            // arg_int=1: Input dir with pullup high for I2C clock pin
        }
        case U8X8_MSG_GPIO_I2C_DATA: {           // arg_int=0: Output low at I2C data pin
            gpio->digitalWrite(info->pin_map.sda, arg_int);
            break;                            // arg_int=1: Input dir with pullup high for I2C data pin
        }
        default: {
            u8x8_SetGPIOResult(u8x8, 1);            // default return value
            break;
        }
    }
    return 1;
}

#else

/**
 * SPI Callback Routine (virtual mode)
 */
uint8_t cb_byte_spi_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return cb_byte_4wire_sw_spi(info, u8x8, msg, arg_int, arg_ptr);
}

/**
 * HW I2C Wrapper. Uses software bit-bang implementation (virtual mode)
 */
uint8_t cb_byte_i2c_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return cb_byte_sw_i2c(info, u8x8, msg, arg_int, arg_ptr);
}

/**
 * GPIO and Delay Routine (virtual mode)
 */
uint8_t cb_gpio_delay(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: { // called once during init phase of u8g2/u8x8, can be used to setup pins
            //cout << "U8X8_MSG_GPIO_AND_DELAY_INIT" << endl;
            break;
        }
        case U8X8_MSG_DELAY_NANO: { // delay arg_int * 1 nano second
            //cout << "U8X8_MSG_DELAY_NANO" << endl;
            break;
        }
        case U8X8_MSG_DELAY_100NANO: {       // delay arg_int * 100 nano seconds
            //cout << "U8X8_MSG_DELAY_100NANO" << endl;
            break;
        }
        case U8X8_MSG_DELAY_10MICRO: {       // delay arg_int * 10 micro seconds
            //cout << "U8X8_MSG_DELAY_10MICRO" << endl;
            break;
        }
        case U8X8_MSG_DELAY_MILLI: {           // delay arg_int * 1 milli second
            //cout << "U8X8_MSG_DELAY_MILLI" << endl;
            break;
        }
        case U8X8_MSG_DELAY_I2C: {                                  // arg_int is the I2C speed in 100KHz, e.g. 4 = 400 KHz
            //cout << "U8X8_MSG_DELAY_I2C" << endl;          // arg_int=1: delay by 5us, arg_int = 4: delay by 1.25us
            break;
        }
        case U8X8_MSG_GPIO_D0: {                // D0 or SPI clock pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_CLOCK)
            //cout << "U8X8_MSG_GPIO_D0" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D1: {                // D1 or SPI data pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_DATA)
            //cout << "\t- [Bit]: " << to_string(arg_int) << flush << endl;
            break;
        }
        case U8X8_MSG_GPIO_D2: {                // D2 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D2" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D3: {                // D3 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D3" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D4: {                // D4 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D4" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D5: {                // D5 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D5" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D6: {                // D6 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D6" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D7: {                // D7 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D7" << endl;
            break;
        }
        case U8X8_MSG_GPIO_E: {               // E/WR pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_E" << endl;
            break;
        }
        case U8X8_MSG_GPIO_CS: {                // CS (chip select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_CS" << endl;
            break;
        }
        case U8X8_MSG_GPIO_DC: {                // DC (data/cmd, A0, register select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_DC" << endl;
            break;
        }
        case U8X8_MSG_GPIO_RESET: {            // Reset pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_RESET" << endl;
            break;
        }
        case U8X8_MSG_GPIO_CS1: {                // CS1 (chip select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_CS1" << endl;
            break;
        }
        case U8X8_MSG_GPIO_CS2: {                // CS2 (chip select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_CS2" << endl;
            break;
        }
        case U8X8_MSG_GPIO_I2C_CLOCK: {        // arg_int=0: Output low at I2C clock pin
            //cout << "U8X8_MSG_GPIO_I2C_CLOCK" << endl;
            break;                            // arg_int=1: Input dir with pullup high for I2C clock pin
        }
        case U8X8_MSG_GPIO_I2C_DATA: {           // arg_int=0: Output low at I2C data pin
            //cout << "U8X8_MSG_GPIO_I2C_DATA" << endl;
            break;                            // arg_int=1: Input dir with pullup high for I2C data pin
        }
        default: {
            //u8x8_SetGPIOResult(u8x8, 1);            // default return value
            break;
        }
    }

    return 1;
}

#endif

/*
 * ============================================================================================================
 *
 * Below are the wrapper functions for the built-in U8G2 bit-bang implementations (Hardware and Software)
 *
 * ============================================================================================================
 */

/**
 * Wrapper for 'u8x8_byte_sw_i2c'
 */
uint8_t cb_byte_sw_i2c(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_sw_i2c(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_4wire_sw_spi'
 */
uint8_t cb_byte_4wire_sw_spi(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_4wire_sw_spi(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_3wire_sw_spi'
 */
uint8_t cb_byte_3wire_sw_spi(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_3wire_sw_spi(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_8bit_6800mode'
 */
uint8_t cb_byte_8bit_6800mode(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_8bit_6800mode(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_8bit_8080mode'
 */
uint8_t cb_byte_8bit_8080mode(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_8bit_8080mode(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_ks0108'
 */
uint8_t cb_byte_ks0108(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_ks0108(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_sed1520'
 */
uint8_t cb_byte_sed1520(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_sed1520(u8x8, msg, arg_int, arg_ptr);
}
