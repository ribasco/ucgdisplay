//
// Created by raffy on 7/7/18.
//

#ifndef PIDISP_U8G2UTILS_H
#define PIDISP_U8G2UTILS_H

#include "U8g2Hal.h"

#define CLS_GlcdNativeDriverException "com/ibasco/pidisplay/drivers/glcd/exceptions/GlcdNativeDriverException"

extern jclass clsGlcdNativeDriverException;

/**
 * Retrieves the name/description of the Pin Number
 *
 * @param index The pin number
 * @return Name/Description of the Pin
 */
string getPinIndexDesc(int index);

/**
 * Converts a long pointer address to a u8g2_t instance
 * @param address The pointer address in long format
 * @return The u8g2_t instance pointer
 */
u8g2_t *to_ptr(jlong address);

uint8_t u8g2_setup_helper_byte(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

uint8_t u8g2_setup_helper_gpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Initialize Display Device
 *
 * @param pin_config Contains pin mapping information that will be used by the byte and gpio callbacks
 * @return A shared_ptr of u8g2_info_t
 */
shared_ptr<u8g2_info_t> SetupAndInitDisplay(string setup_procedure, u8g2_pin_map_t pin_config, const u8g2_cb_t *rotation);

/**
 * Retrieves the device details from the cache
 * @param addr  The pointer address to lookup
 * @return A shared_ptr of u8g2_info_t
 */
shared_ptr<u8g2_info_t> GetDisplayDeviceInfo(uintptr_t addr);

void throwNativeDriverException(JNIEnv *env, string msg);

void copyjByteArrayToPrimitive(JNIEnv *env, jbyteArray arr, uint8_t* buffer, int length);

#endif //PIDISP_U8G2UTILS_H
