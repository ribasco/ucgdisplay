/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native Library
 * Filename: U8g2Utils.h
 * 
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
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

#ifndef UCGDISP_U8G2UTILS_H
#define UCGDISP_U8G2UTILS_H

#include "U8g2Hal.h"

#define U8G2_BYTE_SEND_INIT 28

void U8gUtils_Load(JNIEnv *env);

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

/**
 * Converts rotation index to U8g2 struct
 *
 * @param rotation The rotation index
 * @return Pointer to u8g2_cb_t struct
 */
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
 * @param virtualMode Set to true to activate emulator mode
 * @return
 */
shared_ptr<u8g2_info_t> u8g2util_SetupAndInitDisplay(string setup_proc_name, int commInt, int commType, int address, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config, bool virtualMode = false);

/**
 * Retrieves the device details from the cache
 * @param addr  The pointer address to lookup
 * @return A shared_ptr of u8g2_info_t
 */
shared_ptr<u8g2_info_t> u8g2util_GetDisplayDeviceInfo(uintptr_t addr);

uint8_t u8g2util_SetupHelperByte(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

uint8_t u8g2util_SetupHelperGpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Fires a GpioEvent to the attached listeners
 *
 * @param env JNIEnv instance
 * @param msg The u8g2 message code
 * @param value The u8g2 messave value
 */
void JNI_FireGpioEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value);

/**
 * Fires a ByteEvent to the attached listeners
 *
 * @param env JNIEnv instance
 * @param value The data associated with the event
 */
void JNI_FireByteEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value);

#endif //UCGDISP_U8G2UTILS_H
