#ifndef UCGD_MOD_GRAPHICS_U8G2TESTHAL_H
#define UCGD_MOD_GRAPHICS_U8G2TESTHAL_H

#include <u8g2.h>
#include <cstdint>
#include <memory>
#include <map>
#include <functional>
#include <iostream>
#include <UcgdTypes.h>

/**
 * Initialize the device pins to be used by the HAL
 */
void u8g2_rpi_hal_init(std::shared_ptr<ucgd_t> param);

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

std::shared_ptr<ucgd_t> U8g2Util_GetDisplayDeviceInfo(uintptr_t addr);

uint8_t ByteCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

uint8_t GpioCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

u8g2_cb_t *U8g2Util_ToRotation(int rotation);

#endif //UCGD_MOD_GRAPHICS_U8G2TESTHAL_H
