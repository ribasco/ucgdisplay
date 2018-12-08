/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2Utils.cpp
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

#include <sstream>
#include <memory>
#include <iomanip>
#include <iostream>
#include <system_error>
#include <Global.h>

#include "U8g2Utils.h"

static std::map<uintptr_t, std::shared_ptr<u8g2_info_t>> u8g2_device_cache; // NOLINT
static std::map<int, std::string> pinNameIndexMap; //NOLINT

#define COMINT_4WSPI 0x0001
#define COMINT_3WSPI 0x0002
#define COMINT_6800  0x0004
#define COMINT_8080 0x0008
#define COMINT_I2C  0x0010
#define COMINT_ST7920SPI 0x0020     /* mostly identical to COM_4WSPI, but does not use DC */
#define COMINT_UART 0x0040
#define COMINT_KS0108  0x0080        /* mostly identical to 6800 mode, but has more chip select lines */
#define COMINT_SED1520  0x0100

#define COMTYPE_HW 0
#define COMTYPE_SW 1

static bool _pins_initialized = false;

jclass clsU8g2GpioEvent;
jclass clsU8g2EventDispatcher;
jmethodID midU8g2EventDispatcher_onGpioEvent;
jmethodID midU8g2EventDispatcher_onByteEvent;
jmethodID midU8g2GpioEventCtr;

void U8gUtils_Load(JNIEnv *env) {
    //START: Cache Class/methods
    JNI_MakeGlobal(env, CLS_U8g2GpioEvent, clsU8g2GpioEvent);
    JNI_MakeGlobal(env, CLS_U8g2EventDispatcher, clsU8g2EventDispatcher);
    midU8g2EventDispatcher_onGpioEvent = env->GetStaticMethodID(clsU8g2EventDispatcher, "onGpioEvent", "(JII)V");
    midU8g2EventDispatcher_onByteEvent = env->GetStaticMethodID(clsU8g2EventDispatcher, "onByteEvent", "(JII)V");
    midU8g2GpioEventCtr = env->GetMethodID(clsU8g2GpioEvent, "<init>", "(II)V");
    //END
}

void JNI_FireGpioEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value) {
    env->CallStaticVoidMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_onGpioEvent, (jlong) id, msg, value);
}

void JNI_FireByteEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value) {
    env->CallStaticVoidMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_onByteEvent, (jlong) id, msg, value);
}

u8g2_cb_t *u8g2util_ToRotation(int rotation) {
    switch (rotation) {
        case 0: {
            return const_cast<u8g2_cb_t *>(U8G2_R0);
        }
        case 1: {
            return const_cast<u8g2_cb_t *>(U8G2_R1);
        }
        case 2: {
            return const_cast<u8g2_cb_t *>(U8G2_R2);
        }
        case 3: {
            return const_cast<u8g2_cb_t *>(U8G2_R3);
        }
        case 4: {
            return const_cast<u8g2_cb_t *>(U8G2_MIRROR);
        }
        default:
            break;
    }
    return nullptr;
}

u8g2_msg_func_info_t u8g2util_GetByteCb(int commInt, int commType) {
    switch (commInt) {
        case COMINT_3WSPI: {
            if (commType == COMTYPE_HW) {
                //no HW support for 3-wire interface?
                return nullptr;
            }
            return cb_byte_3wire_sw_spi;
        }
        case COMINT_4WSPI: {
            if (commType == COMTYPE_HW) {
                return cb_byte_spi_hw;
            }
            return cb_byte_4wire_sw_spi;
        }
            //Similar to 4W_SPI
        case COMINT_ST7920SPI: {
            if (commType == COMTYPE_HW) {
                return cb_byte_spi_hw;
            }
            return cb_byte_4wire_sw_spi;
        }
        case COMINT_I2C: {
            if (commType == COMTYPE_HW) {
                return cb_byte_i2c_hw;
            }
            return cb_byte_sw_i2c;
        }
        case COMINT_6800: {
            if (commType == COMTYPE_HW) {
                return nullptr;
            }
            return cb_byte_8bit_6800mode;
        }
        case COMINT_8080: {
            if (commType == COMTYPE_HW) {
                return nullptr;
            }
            return cb_byte_8bit_8080mode;
        }
        case COMINT_UART: {
            //TODO: Not yet sure how to implement this
            //SEE: u8g2_Setup_a2printer_384x240_f()
            return nullptr;
        }
            //similar to 6800 mode
        case COMINT_KS0108: {
            if (commType == COMTYPE_HW) {
                return nullptr;
            }
            return cb_byte_ks0108;
        }
        case COMINT_SED1520: {
            if (commType == COMTYPE_HW) {
                return nullptr;
            }
            return cb_byte_sed1520;
        }
        default:
            break;
    }
    return nullptr;
}

std::shared_ptr<u8g2_info_t> u8g2util_SetupAndInitDisplay(const std::string &setup_proc_name, int commInt, int commType, int device_address, const std::string &transport_device, const std::string &gpio_device, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config, bool virtualMode) {
    JNIEnv *env;
    GETENV(env);

    std::shared_ptr<u8g2_info_t> info = std::make_shared<u8g2_info_t>();

    //Initialize device info details
    info->u8g2 = std::make_shared<u8g2_t>();
    info->pin_map = pin_config;
    info->rotation = const_cast<u8g2_cb_t *>(rotation);
    info->flag_virtual = virtualMode;

#if defined(__arm__) && defined(__linux__)
    info->transport_device = transport_device;
    info->gpio_device = gpio_device;
    info->spi = std::make_shared<spi_t>();
    info->i2c = std::make_shared<i2c_t>();
#ifdef USE_GPIOUSERSPACE
    try {
        info->gpio_chip = gpiod::chip(info->gpio_device);
    } catch (const std::system_error &e) {
        std::stringstream ss;
        ss << "Unable to open gpio device (Device: " << info->gpio_device << ", Code: " << e.code() << ", Reason: " << e.what() << ")";
        JNI_ThrowNativeLibraryException(env, ss.str());
        return nullptr;
    }
#endif
#endif

    //Get the setup procedure callback
    u8g2_setup_func_t setup_proc_callback = u8g2hal_GetSetupProc(setup_proc_name);

    //Verify that we have found a callback
    if (setup_proc_callback == nullptr) {
        std::stringstream ss;
        ss << "u8g2 setup procedure not found: '" << setup_proc_name << "'" << std::endl;
        JNI_ThrowNativeLibraryException(env, ss.str());
        return nullptr;
    }

    info->setup_proc_name = setup_proc_name;
    info->setup_cb = setup_proc_callback;

    //Retrieve the byte callback function based on the commInt and commType arguments
    u8g2_msg_func_info_t cb_byte = u8g2util_GetByteCb(commInt, commType);
    if (cb_byte == nullptr) {
        JNI_ThrowNativeLibraryException(env, std::string("No available byte callback procedures for CommInt = ") + std::to_string(commInt) + std::string(", CommType = ") + std::to_string(commType));
        return nullptr;
    }

    //Byte callback
    info->byte_cb = [cb_byte, info, virtualMode, commInt, commType](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        if (virtualMode) {
            JNIEnv *lenv;
            GETENV(lenv);

            if (msg == U8X8_MSG_BYTE_SEND) {
                uint8_t value;
                uint8_t size = arg_int;
                auto *data = (uint8_t *) arg_ptr;
                JNI_FireByteEvent(lenv, info->address(), U8G2_BYTE_SEND_INIT, size); //fire custom event
                while (size > 0) {
                    value = *data;
                    data++;
                    size--;
                    JNI_FireByteEvent(lenv, info->address(), msg, value);
                }
            } else {
                JNI_FireByteEvent(lenv, info->address(), msg, arg_int);
            }
            return 1;
        }
        return cb_byte(info.get(), u8x8, msg, arg_int, arg_ptr);
    };

    //Gpio callback
    info->gpio_cb = [virtualMode, info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        if (virtualMode) {
            JNIEnv *lenv;
            GETENV(lenv);
            JNI_FireGpioEvent(lenv, info->address(), msg, arg_int);
            return 1;
        }
        return cb_gpio_delay(info.get(), u8x8, msg, arg_int, arg_ptr);
    };

    //Obtain the raw pointer
    u8g2_t *pU8g2 = info->u8g2.get();

    //Assign the i2c addres if applicable
    if (commType == COMINT_I2C) {
        u8g2_SetI2CAddress(pU8g2, device_address);
    }

    //Insert device info in cache
    auto it = u8g2_device_cache.insert(make_pair((uintptr_t) pU8g2, info));

    //Call the setup procedure
    info->setup_cb(pU8g2, rotation, u8g2util_SetupHelperByte, u8g2util_SetupHelperGpio);

    //Initialize the display
    u8g2_InitDisplay(pU8g2);
    u8g2_SetPowerSave(pU8g2, 0);
    u8g2_ClearDisplay(pU8g2);

    return info;
}

std::shared_ptr<u8g2_info_t> u8g2util_GetDisplayDeviceInfo(uintptr_t addr) {
    auto it = u8g2_device_cache.find(addr);
    if (it != u8g2_device_cache.end())
        return it->second;
    return nullptr;
}

uint8_t u8g2util_SetupHelperByte(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<u8g2_info_t> info = u8g2util_GetDisplayDeviceInfo(addr);
    if (info == nullptr) {
        JNIEnv *env;
        GETENV(env);
        std::stringstream ss;
        ss << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        JNI_ThrowNativeLibraryException(env, ss.str());
    }
    return info->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t u8g2util_SetupHelperGpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<u8g2_info_t> info = u8g2util_GetDisplayDeviceInfo(addr);
    if (info == nullptr) {
        JNIEnv *env;
        GETENV(env);
        std::stringstream ss;
        ss << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        JNI_ThrowNativeLibraryException(env, ss.str());
    }
    return info->gpio_cb(u8x8, msg, arg_int, arg_ptr);
}

std::string u8g2util_GetPinIndexDesc(int index) {
    if (!_pins_initialized) {
        if (!pinNameIndexMap.empty())
            pinNameIndexMap.clear();
        pinNameIndexMap[0] = "D0 (alt. SPI CLOCK)";
        pinNameIndexMap[1] = "D1 (alt. SPI DATA)";
        pinNameIndexMap[2] = "D2";
        pinNameIndexMap[3] = "D3";
        pinNameIndexMap[4] = "D4";
        pinNameIndexMap[5] = "D5";
        pinNameIndexMap[6] = "D6";
        pinNameIndexMap[7] = "D7";
        pinNameIndexMap[8] = "E";
        pinNameIndexMap[9] = "CS";
        pinNameIndexMap[10] = "DC";
        pinNameIndexMap[11] = "RESET";
        pinNameIndexMap[12] = "I2C CLOCK (SCL)";
        pinNameIndexMap[13] = "I2C DATA (SDA)";
        pinNameIndexMap[14] = "CS1 (Chip Select 1)";
        pinNameIndexMap[15] = "CS2 (Chip Select 2)";
        //Initialize pin names here
        _pins_initialized = true;
    }
    if (index > 15) {
        return nullptr;
    }
    return pinNameIndexMap.at(index);
}

u8g2_t *toU8g2(jlong address) {
    auto *u8g2 = reinterpret_cast<u8g2_t *>(address);
    return u8g2;
}
