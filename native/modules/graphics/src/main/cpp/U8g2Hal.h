/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Graphics
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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
#ifndef UCGDISP_U8G2CALLBACKS_H
#define UCGDISP_U8G2CALLBACKS_H

#include <iostream>
#include <cstring>
#include <sstream>
#include <memory>
#include <map>
#include <functional>
#include <jni.h>
#include <UcgdTypes.h>

extern "C" {
#include <u8g2.h>
}

class SetupProcNotFoundException : public std::runtime_error {
public:
    explicit SetupProcNotFoundException(const std::string &arg) : runtime_error(arg) {}

    explicit SetupProcNotFoundException(const char *string) : runtime_error(string) {}

    explicit SetupProcNotFoundException(const runtime_error &error) : runtime_error(error) {}
};

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

//#include "UcgIOProvider.h"

#endif

/**
 * Hardware I2C byte communication callback (Using c-periphery)
 */
uint8_t cb_byte_i2c_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Hardware SPI byte communications callback (Using c-periphery)
 */
uint8_t cb_byte_spi_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * GPIO and Delay callback (Using libgpiod/linux userspace)
 */
uint8_t cb_gpio_delay(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr);

/**
 * Wrapper for u8x8_byte_sw_i2c (Software Bit-bang implementation)
*/
uint8_t cb_byte_sw_i2c(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_4wire_sw_spi (Software Bit-bang implementation)
 */
uint8_t cb_byte_4wire_sw_spi(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_3wire_sw_spi (Software Bit-bang implementation)
 */
uint8_t cb_byte_3wire_sw_spi(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_8bit_6800mode (Software Bit-bang implementation)
 */
uint8_t cb_byte_8bit_6800mode(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_8bit_8080mode (Software Bit-bang implementation)
 */
uint8_t cb_byte_8bit_8080mode(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_ks0108 (Software Bit-bang implementation)
 */
uint8_t cb_byte_ks0108(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_sed1520 (Software Bit-bang implementation)
 */
uint8_t cb_byte_sed1520(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Initialize the lookup t ables. This shuld be called prior to calling the other methods found in this file
 */
void U8g2hal_Init();

/**
 * Initialize u8g2 setup lookup table
 */
void U8g2hal_InitSetupFunctions(u8g2_setup_func_map_t &setup_map);

/**
 * Initialize u8g2 font lookup table
 */
void U8g2hal_InitFonts(u8g2_lookup_font_map_t &font_map);

/**
 * Helper utility function to obtain the u8g2 setup callback by name
 *
 * @param function_name The u8g2 setup procedure name
 * @return The function callback if found, otherwise null if not found
 * @throws SetupProcNotFoundException if procedure is not found
 */
u8g2_setup_func_t& U8g2Hal_GetSetupProc(const std::string &function_name);

/**
 * Retrieve font data by name
 * @param font_name The name of the u8g2 font
 * @return Buffer containing the font data
 */
uint8_t *U8g2hal_GetFontByName(const std::string &font_name);

#if !((defined(__arm__) || defined(__aarch64__)) && defined(__linux__))

//Note: The following i2c_* code snippets was copied from the U8G2 source. Credits to olikarus for this.

static void i2c_delay(u8x8_t *u8x8) U8X8_NOINLINE;

static void i2c_delay(u8x8_t *u8x8) {
    //u8x8_gpio_Delay(u8x8, U8X8_MSG_DELAY_10MICRO, u8x8->display_info->i2c_bus_clock_100kHz);
    u8x8_gpio_Delay(u8x8, U8X8_MSG_DELAY_I2C, u8x8->display_info->i2c_bus_clock_100kHz);
}

static void i2c_init(u8x8_t *u8x8) {
    u8x8_gpio_SetI2CClock(u8x8, 1);
    u8x8_gpio_SetI2CData(u8x8, 1);

    i2c_delay(u8x8);
}

static void i2c_read_scl_and_delay(u8x8_t *u8x8) {
    /* set as input (line will be high) */
    u8x8_gpio_SetI2CClock(u8x8, 1);

    i2c_delay(u8x8);
}

static void i2c_clear_scl(u8x8_t *u8x8) {
    u8x8_gpio_SetI2CClock(u8x8, 0);
}

static void i2c_read_sda(u8x8_t *u8x8) {
    /* set as input (line will be high) */
    u8x8_gpio_SetI2CData(u8x8, 1);
}

static void i2c_clear_sda(u8x8_t *u8x8) {
    /* set open collector and drive low */
    u8x8_gpio_SetI2CData(u8x8, 0);
}

static void i2c_start(u8x8_t *u8x8) {
    if (u8x8->i2c_started != 0) {
        /* if already started: do restart */
        i2c_read_sda(u8x8);     /* SDA = 1 */
        i2c_delay(u8x8);
        i2c_read_scl_and_delay(u8x8);
    }
    i2c_read_sda(u8x8);
    /* send the start condition, both lines go from 1 to 0 */
    i2c_clear_sda(u8x8);
    i2c_delay(u8x8);
    i2c_clear_scl(u8x8);
    u8x8->i2c_started = 1;
}

static void i2c_stop(u8x8_t *u8x8) {
    /* set SDA to 0 */
    i2c_clear_sda(u8x8);
    i2c_delay(u8x8);

    /* now release all lines */
    i2c_read_scl_and_delay(u8x8);

    /* set SDA to 1 */
    i2c_read_sda(u8x8);
    i2c_delay(u8x8);
    u8x8->i2c_started = 0;
}

static void i2c_write_bit(u8x8_t *u8x8, uint8_t val) {
    if (val)
        i2c_read_sda(u8x8);
    else
        i2c_clear_sda(u8x8);

    i2c_delay(u8x8);
    i2c_read_scl_and_delay(u8x8);
    i2c_clear_scl(u8x8);
}

static void i2c_read_bit(u8x8_t *u8x8) {
    /* do not drive SDA */
    i2c_read_sda(u8x8);
    i2c_delay(u8x8);
    i2c_read_scl_and_delay(u8x8);
    i2c_read_sda(u8x8);
    i2c_delay(u8x8);
    i2c_clear_scl(u8x8);
}

static void i2c_write_byte(u8x8_t *u8x8, uint8_t b) {
    i2c_write_bit(u8x8, b & 128);
    i2c_write_bit(u8x8, b & 64);
    i2c_write_bit(u8x8, b & 32);
    i2c_write_bit(u8x8, b & 16);
    i2c_write_bit(u8x8, b & 8);
    i2c_write_bit(u8x8, b & 4);
    i2c_write_bit(u8x8, b & 2);
    i2c_write_bit(u8x8, b & 1);

    /* read ack from client */
    /* 0: ack was given by client */
    /* 1: nothing happend during ack cycle */
    i2c_read_bit(u8x8);
}

#endif

#endif //UCGDISP_U8G2CALLBACKS_H
