/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Graphics
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

#ifndef UCGDISP_U8G2UTILS_H
#define UCGDISP_U8G2UTILS_H

#include <UcgdTypes.h>

#define U8G2_BYTE_SEND_INIT 28

class UcgdSetupException : public std::runtime_error {
public:
    explicit UcgdSetupException(const std::string &arg) : std::runtime_error(arg) {};

    explicit UcgdSetupException(const char *string) : std::runtime_error(string) {};

    explicit UcgdSetupException(const runtime_error &error) : std::runtime_error(error) {};
};

class UcgdByteCallbackException : public std::runtime_error {
public:
    explicit UcgdByteCallbackException(const std::string &arg) : std::runtime_error(arg) {};

    explicit UcgdByteCallbackException(const char *string) : std::runtime_error(string) {};

    explicit UcgdByteCallbackException(const runtime_error &error) : std::runtime_error(error) {};
};

class UcgdGpioCallbackException : public std::runtime_error {
public:
    explicit UcgdGpioCallbackException(const std::string &arg) : std::runtime_error(arg) {};

    explicit UcgdGpioCallbackException(const char *string) : std::runtime_error(string) {};

    explicit UcgdGpioCallbackException(const runtime_error &error) : std::runtime_error(error) {};
};

class UcgdByteCallbackWrapperException : public std::runtime_error {
public:
    explicit UcgdByteCallbackWrapperException(const std::string &arg) : std::runtime_error(arg) {};

    explicit UcgdByteCallbackWrapperException(const char *string) : std::runtime_error(string) {};

    explicit UcgdByteCallbackWrapperException(const runtime_error &error) : std::runtime_error(error) {};
};

class UcgdGpioCallbackWrapperException : public std::runtime_error {
public:
    explicit UcgdGpioCallbackWrapperException(const std::string &arg) : std::runtime_error(arg) {};

    explicit UcgdGpioCallbackWrapperException(const char *string) : std::runtime_error(string) {};

    explicit UcgdGpioCallbackWrapperException(const runtime_error &error) : std::runtime_error(error) {};
};

/**
 * Load the Utils Module
 * @param env
 */
void U8gUtils_Load(JNIEnv *env);

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

/**
 * Initialize all supported providers
 *
 * @param info The project descriptor
 */
//void U8g2Util_InitializeProviders(const std::shared_ptr<ucgd_t> &info);

#endif

/**
 * Retrieves the name/description of the Pin Number
 *
 * @param index The pin number
 * @return Name/Description of the Pin
 */
std::string U8g2Util_GetPinIndexDesc(int index);

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
u8g2_cb_t *U8g2Util_ToRotation(int rotation);

/**
 * Setup and initialize the display. This method must be called first before calling any other U8G2 methods.
 *
 * @param setup_proc_name The u8g2 setup procedure
 * @param commInt
 * @param commType Communications type (e.g. COMINT_4WSPI)
 * @param device_address Device address
 * @param transport_device The transport device path (e.g. /dev/i2c-1)
 * @param gpio_device The gpio chip path
 * @param rotation Display rotation
 * @param pin_config Pin mapping configuration
 * @param virtualMode Set to true to activate emulator mode
 * @return
 */
std::shared_ptr<ucgd_t>& U8g2Util_SetupAndInitDisplay(const std::string &setup_proc_name, int commInt, int commType, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config, option_map_t &options, bool virtualMode = false);

/**
 * Byte callback wrapper for u8g2 setup procedures
 */
extern "C" uint8_t U8g2Util_ByteCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Gpio callback wrapper for u8g2 setup procedures
 */
extern "C" uint8_t U8g2Util_GpioCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

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
