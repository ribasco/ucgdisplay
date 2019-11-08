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
uint8_t cb_byte_i2c_hw(const std::shared_ptr<ucgd_t>& info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Hardware SPI byte communications callback (Using c-periphery)
 */
uint8_t cb_byte_spi_hw(const std::shared_ptr<ucgd_t>& info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * GPIO and Delay callback (Using libgpiod/linux userspace)
 */
uint8_t cb_gpio_delay(const std::shared_ptr<ucgd_t>& info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr);

void addToDeviceCache(u8g2_t* ptr, const std::shared_ptr<ucgd_t>& info);

std::shared_ptr<ucgd_t> U8g2Util_GetDisplayDeviceInfo(uintptr_t addr);

uint8_t U8g2Util_ByteCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

uint8_t U8g2Util_GpioCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

u8g2_cb_t *U8g2Util_ToRotation(int rotation);

#endif //UCGD_MOD_GRAPHICS_U8G2TESTHAL_H
