/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2Hal.h
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

#ifndef UCGDISP_U8G2CALLBACKS_H
#define UCGDISP_U8G2CALLBACKS_H

#include <iostream>
#include <cstring>
#include <sstream>
#include <u8g2.h>
#include <memory>
#include <map>
#include <functional>
#include <jni.h>

#if defined(__arm__) && defined(__linux__)

#include <spi.h>
#include <i2c.h>
#include <gpiod.hpp>

#endif

typedef struct {
    //pin configuration
    uint8_t d0; //spi-clock
    uint8_t d1; //spi-data
    uint8_t d2;
    uint8_t d3;
    uint8_t d4;
    uint8_t d5;
    uint8_t d6;
    uint8_t d7;
    uint8_t en;
    uint8_t cs;
    uint8_t dc;
    uint8_t reset;
    uint8_t scl;
    uint8_t sda;
    uint8_t cs1;
    uint8_t cs2;
} u8g2_pin_map_t;

typedef std::function<uint8_t(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr)> u8g2_msg_func_t;

typedef std::function<void(u8g2_t *u8g2, const u8g2_cb_t *rotation, u8x8_msg_cb byte_cb, u8x8_msg_cb gpio_and_delay_cb)> u8g2_setup_func_t;

typedef std::map<std::string, u8g2_setup_func_t> u8g2_setup_func_map_t;

typedef std::map<std::string, const uint8_t *> u8g2_lookup_font_map_t;

typedef struct {
    u8g2_pin_map_t pin_map;
    std::shared_ptr<u8g2_t> u8g2;
    std::string setup_proc_name;
    u8g2_setup_func_t setup_cb;
    u8g2_msg_func_t byte_cb;
    u8g2_msg_func_t gpio_cb;
    u8g2_cb_t *rotation;
#if defined(__arm__) && defined(__linux__)
    spi_t* spi;
    i2c_t* i2c;
    std::string transport_device;
    std::string gpio_device;
    int device_speed;
    std::shared_ptr<gpiod::chip> gpio_chip;
    std::map<int, std::shared_ptr<gpiod::line>> gpio;
#endif
    bool flag_font;
    bool flag_virtual;

    uintptr_t address() {
        return (uintptr_t) u8g2.get();
    }
} u8g2_info_t;

typedef std::function<uint8_t(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr)> u8g2_msg_func_info_t;

/**
 * Hardware I2C byte communication callback (Using c-periphery)
 */
uint8_t cb_byte_i2c_hw(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Hardware SPI byte communications callback (Using c-periphery)
 */
uint8_t cb_byte_spi_hw(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * GPIO and Delay callback (Using libgpiod/linux userspace)
 */
uint8_t cb_gpio_delay(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr);

/**
 * Wrapper for u8x8_byte_sw_i2c (Software Bit-bang implementation)
*/
uint8_t cb_byte_sw_i2c(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_4wire_sw_spi (Software Bit-bang implementation)
 */
uint8_t cb_byte_4wire_sw_spi(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_3wire_sw_spi (Software Bit-bang implementation)
 */
uint8_t cb_byte_3wire_sw_spi(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_8bit_6800mode (Software Bit-bang implementation)
 */
uint8_t cb_byte_8bit_6800mode(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_8bit_8080mode (Software Bit-bang implementation)
 */
uint8_t cb_byte_8bit_8080mode(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_ks0108 (Software Bit-bang implementation)
 */
uint8_t cb_byte_ks0108(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Wrapper for u8x8_byte_sed1520 (Software Bit-bang implementation)
 */
uint8_t cb_byte_sed1520(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Initialize the lookup t ables. This shuld be called prior to calling the other methods found in this file
 */
void u8g2hal_Init();

/**
 * Initialize u8g2 setup lookup table
 */
void u8g2hal_InitSetupFunctions(u8g2_setup_func_map_t &setup_map);

/**
 * Initialize u8g2 font lookup table
 */
void u8g2hal_InitFonts(u8g2_lookup_font_map_t &font_map);

/**
 * Helper utility function to obtain the u8g2 setup callback by name
 *
 * @param function_name The u8g2 setup procedure name
 * @return The function callback if found, otherwise null if not found
 */
u8g2_setup_func_t u8g2hal_GetSetupProc(const std::string &function_name);

uint8_t *u8g2hal_GetFontByName(const std::string &font_name);

#if !(defined(__arm__) && defined(__linux__))

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
