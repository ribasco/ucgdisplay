//
// Created by raffy on 21/10/2019.
//

#ifndef UCGD_MOD_GRAPHICS_U8G2TESTHAL_H
#define UCGD_MOD_GRAPHICS_U8G2TESTHAL_H

#include <spi.h>
#include <i2c.h>
#include <gpiod.hpp>
#include <u8g2.h>
#include <cstdint>
#include <memory>
#include <map>
#include <functional>

#define COMTYPE_HW 0
#define COMTYPE_SW 1

#define COMINT_4WSPI 0x0001
#define COMINT_3WSPI 0x0002
#define COMINT_6800  0x0004
#define COMINT_8080 0x0008
#define COMINT_I2C  0x0010
#define COMINT_ST7920SPI 0x0020     /* mostly identical to COM_4WSPI, but does not use DC */
#define COMINT_UART 0x0040
#define COMINT_KS0108  0x0080        /* mostly identical to 6800 mode, but has more chip select lines */
#define COMINT_SED1520  0x0100

typedef std::function<uint8_t(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr)> u8g2_msg_func_t;
typedef std::function<void(u8g2_t *u8g2, const u8g2_cb_t *rotation, u8x8_msg_cb byte_cb, u8x8_msg_cb gpio_and_delay_cb)> u8g2_setup_func_t;

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

typedef struct {
    u8g2_pin_map_t pin_map;
    std::shared_ptr<u8g2_t> u8g2;
    std::string setup_proc_name;
    u8g2_cb_t *rotation;
    std::shared_ptr<spi_t> spi;
    std::shared_ptr<i2c_t> i2c;
    std::string transport_device;
    std::string gpio_device;
    int device_speed;
    std::shared_ptr<gpiod::chip> gpio_chip;
    std::map<int , std::shared_ptr<gpiod::line>> gpio;
    std::map<int , gpiod::line> gpio_tmp;
    bool flag_font;
    bool flag_virtual;
    u8g2_setup_func_t setup_cb;
    u8g2_msg_func_t byte_cb;
    u8g2_msg_func_t gpio_cb;
    uintptr_t address() {
        return (uintptr_t) u8g2.get();
    }
} u8g2_info_t;

typedef std::function<uint8_t(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr)> u8g2_msg_func_info_t;

/**
 * Initialize the device pins to be used by the HAL
 */
void u8g2_rpi_hal_init(std::shared_ptr<u8g2_info_t> param);

/**
 * Byte communications callback for I2C Hardware on the Raspberry Pi
 */
uint8_t cb_byte_i2c_hw(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Byte communications callback for SPI Hardware on the Raspberry Pi
 */
uint8_t cb_byte_spi_hw(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * GPIO and Delay callback for Raspberry Pi
 */
uint8_t cb_gpio_delay_rpi(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr);

/**
 * Hardware I2C byte communication callback (Using c-periphery)
 */
uint8_t cb_byte_i2c_hw(const std::shared_ptr<u8g2_info_t>& info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Hardware SPI byte communications callback (Using c-periphery)
 */
uint8_t cb_byte_spi_hw(const std::shared_ptr<u8g2_info_t>& info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * GPIO and Delay callback (Using libgpiod/linux userspace)
 */
uint8_t cb_gpio_delay(const std::shared_ptr<u8g2_info_t>& info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr);

#endif //UCGD_MOD_GRAPHICS_U8G2TESTHAL_H
