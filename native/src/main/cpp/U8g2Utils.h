//
// Created by raffy on 7/7/18.
//

#ifndef PIDISP_U8G2UTILS_H
#define PIDISP_U8G2UTILS_H

#include "U8g2Hal.h"

/**
 * Retrieves the name/description of the Pin Number
 *
 * @param index The pin number
 * @return Name/Description of the Pin
 */
string u8g2util_GetPinIndexDesc(int index);

/**
 * Converts a long pointer address to a u8g2_t instance
 * @param address The pointer address in long format
 * @return The u8g2_t instance pointer
 */
u8g2_t *toU8g2(jlong address);

u8g2_cb_t *u8g2util_ToRotation(int rotation);

/**
 * Initialize Display Device
 *
 * @param pin_config Contains pin mapping information that will be used by the byte and gpio callbacks
 * @return A shared_ptr of u8g2_info_t
 */
shared_ptr<u8g2_info_t> u8g2util_SetupAndInitDisplay(string setup_proc_name, int commInt, int commType, int address, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config);

/**
 * Retrieves the device details from the cache
 * @param addr  The pointer address to lookup
 * @return A shared_ptr of u8g2_info_t
 */
shared_ptr<u8g2_info_t> u8g2util_GetDisplayDeviceInfo(uintptr_t addr);

/**
 * Copy jByteArray to a byte buffer
 *
 * @param env JNIEnv of the caller
 * @param arr  The source jbyteArray
 * @param buffer  The destination buffer
 * @param length The number of bytes to copy
 */
void JNI_copyjByteArray(JNIEnv *env, jbyteArray arr, uint8_t *buffer, int length);

uint8_t u8g2util_SetupHelperByte(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

uint8_t u8g2util_SetupHelperGpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

#endif //PIDISP_U8G2UTILS_H
