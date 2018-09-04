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
 * Setup and initialize the display. This method must be called first before calling any other U8G2 methods.
 *
 * @param setup_proc_name The u8g2 setup procedure
 * @param commInt
 * @param commType Communications type (e.g. COMINT_4WSPI)
 * @param address Device address
 * @param rotation Display rotation
 * @param pin_config Pin mapping configuration
 * @param emulation Set to true to activate emulator mode
 * @return
 */
shared_ptr<u8g2_info_t> u8g2util_SetupAndInitDisplay(string setup_proc_name, int commInt, int commType, int address, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config, bool emulation = false);

/**
 * Retrieves the device details from the cache
 * @param addr  The pointer address to lookup
 * @return A shared_ptr of u8g2_info_t
 */
shared_ptr<u8g2_info_t> u8g2util_GetDisplayDeviceInfo(uintptr_t addr);

uint8_t u8g2util_SetupHelperByte(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

uint8_t u8g2util_SetupHelperGpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

#endif //PIDISP_U8G2UTILS_H
