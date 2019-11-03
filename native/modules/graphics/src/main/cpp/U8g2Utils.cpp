/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2Utils.cpp
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

#include <memory>
#include <iomanip>
#include <iostream>
#include <system_error>
#include <Global.h>
#include <Common.h>

#include "UcgdConfig.h"
#include "U8g2Hal.h"
#include "U8g2Utils.h"

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

#include <UcgLibgpiodProvider.h>
#include <UcgPigpioProvider.h>
#include <UcgCperipheryProvider.h>

#endif

std::map<uintptr_t, std::shared_ptr<u8g2_info_t>> deviceMap; // NOLINT
std::map<int, std::string> pinNameIndexMap; //NOLINT

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

u8g2_cb_t *U8g2util_ToRotation(int rotation) {
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

u8g2_msg_func_info_t U8g2Util_GetByteCb(int commInt, int commType) {
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

std::shared_ptr<u8g2_info_t> U8g2Util_SetupAndInitDisplay(const std::string &setup_proc_name, int commInt, int commType, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config, option_map_t &options, std::shared_ptr<Log> &logger, bool virtualMode) {
    JNIEnv *env;
    GETENV(env);

    std::shared_ptr<u8g2_info_t> info = std::make_shared<u8g2_info_t>();
    info->log = logger;

    //read this: https://floating.io/2017/07/lambda-shared_ptr-memory-leak/
    std::weak_ptr<u8g2_info_t> weak_info(info);

    //Initialize device info details
    info->u8g2 = std::make_unique<u8g2_t>();
    info->pin_map = pin_config;
    info->rotation = const_cast<u8g2_cb_t *>(rotation);
    info->flag_virtual = virtualMode;
    info->comm_int = commInt;
    info->comm_type = commType;

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    //Copy options data
    info->options = std::map(options);

    //Initialize all providers
    U8g2Util_InitializeProviders(info);

    //Make sure the provider supports the current configuration setup
    //e.g. SPI & I2C capability
    if (commType == COMTYPE_HW) {
        if ((commInt == COMINT_4WSPI || commInt == COMINT_3WSPI || commInt == COMINT_ST7920SPI) && !info->provider->supportsSPI()) {
            throw std::runtime_error(std::string("Your current setup is configured for Hardware SPI but the provider you selected '") + info->provider->getName() + std::string("' does not have hardware SPI capability"));
        } else if ((commInt == COMINT_I2C) && !info->provider->supportsI2C()) {
            throw std::runtime_error(std::string("Your current setup is configured for Hardware I2C but the provider you selected '") + info->provider->getName() + std::string("' does not have hardware I2C capability"));
        }
    } else if (commType == COMTYPE_SW) {
        //make sure the current provider supports GPIO for bit-bang implementations
        if (!info->provider->supportsGpio()) {
            throw std::runtime_error(std::string("Your current setup is configured for software bit-bang implementation but the provider you selected does '") + info->provider->getName() + std::string("' not have GPIO capability"));
        }
    }

    //Assign the i2c addres if applicable
    if (commType == COMINT_I2C) {
        std::any addr = info->options[OPT_I2C_ADDRESS];
        if (addr.has_value()) {
            int intVal = std::any_cast<int>(addr);
            u8g2_SetI2CAddress(info->u8g2.get(), intVal);
        }
    }
#endif

    //Get the setup procedure callback
    u8g2_setup_func_t setup_proc_callback = U8g2hal_GetSetupProc(setup_proc_name);

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
    u8g2_msg_func_info_t cb_byte = U8g2Util_GetByteCb(commInt, commType);

    if (cb_byte == nullptr) {
        JNI_ThrowNativeLibraryException(env, std::string("No available byte callback procedures for CommInt = ") + std::to_string(commInt) + std::string(", CommType = ") + std::to_string(commType));
        return nullptr;
    }

    //Byte callback
    info->byte_cb = [cb_byte, weak_info, virtualMode](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        auto info = weak_info.lock();
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
        try {
            return cb_byte(info, u8x8, msg, arg_int, arg_ptr);
        } catch (std::exception &e) {
            JNIEnv *lenv;
            GETENV(lenv);
            std::stringstream ss;
            ss << "byte_cb() : " << std::string(e.what()) << std::endl;
            JNI_ThrowNativeLibraryException(lenv, ss.str());
            return 0;
        }
    };

    //Gpio callback
    info->gpio_cb = [virtualMode, weak_info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        auto info = weak_info.lock();
        if (virtualMode) {
            JNIEnv *lenv;
            GETENV(lenv);
            JNI_FireGpioEvent(lenv, info->address(), msg, arg_int);
            return 1;
        }
        try {
            uint8_t retval = cb_gpio_delay(info, u8x8, msg, arg_int, arg_ptr);
            return retval;
        } catch (std::exception &e) {
            JNIEnv *lenv;
            GETENV(lenv);
            std::stringstream ss;
            ss << "gpio_cb() : " << std::string(e.what()) << std::endl;
            JNI_ThrowNativeLibraryException(lenv, ss.str());
            return 0;
        }
    };

    //Obtain the raw pointer
    u8g2_t *pU8g2 = info->u8g2.get();

    //Insert device info in cache
    deviceMap.insert(make_pair((uintptr_t) pU8g2, info));

    //Call the setup procedure
    info->setup_cb(pU8g2, rotation, U8g2Util_ByteCallbackWrapper, U8g2Util_GpioCallbackWrapper);

    //Initialize the display
    u8g2_InitDisplay(pU8g2);
    u8g2_SetPowerSave(pU8g2, 0);
    u8g2_ClearDisplay(pU8g2);

    return info;
}

std::shared_ptr<u8g2_info_t> U8g2Util_GetDisplayDeviceInfo(uintptr_t addr) {
    auto it = deviceMap.find(addr);
    if (it != deviceMap.end())
        return it->second;
    return nullptr;
}

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

void U8g2Util_InitializeProviders(const std::shared_ptr<u8g2_info_t> &info) {
    info->log->debug("init_providers() : Initializing providers");

    try {
        //Initialize all available providers
        auto pigpio_it = info->providers.insert(make_pair(PROVIDER_PIGPIO, std::make_shared<UcgPigpioProvider>(info)));
        auto libgpiod_it = info->providers.insert(make_pair(PROVIDER_LIBGPIOD, std::make_shared<UcgLibgpiodProvider>(info)));
        auto cper_it = info->providers.insert(make_pair(PROVIDER_CPERIPHERY, std::make_shared<UcgCperipheryProvider>(info)));

        //Retrieve the default provider
        std::string defaultProvider = info->getOptionString(OPT_PROVIDER);
        info->log->debug("init_providers() : Found default provider = {}", defaultProvider);

        if (defaultProvider.empty()) {
            info->log->warn("init_providers() : No default provider was specified. Defaulting to SYSTEM (c-periphery) provider");
            defaultProvider = PROVIDER_CPERIPHERY;
        }

        if (defaultProvider == PROVIDER_PIGPIO) {
            info->provider = pigpio_it.first->second;
        } else if (defaultProvider == PROVIDER_LIBGPIOD) {
            info->provider = libgpiod_it.first->second;
        } else if (defaultProvider == PROVIDER_CPERIPHERY) {
            info->provider = cper_it.first->second;
        } else {
            throw std::runtime_error(std::string("Provider '") + defaultProvider + std::string("' not supported"));
        }
    } catch (OptionNotFoundException &e) {
        info->log->debug("init_providers() : No default provider found");
        throw e;
    }
}

#endif

uint8_t U8g2Util_ByteCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<u8g2_info_t> info = U8g2Util_GetDisplayDeviceInfo(addr);
    if (info == nullptr) {
        JNIEnv *env;
        GETENV(env);
        std::stringstream ss;
        ss << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        JNI_ThrowNativeLibraryException(env, ss.str());
    }
    return info->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t U8g2Util_GpioCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<u8g2_info_t> info = U8g2Util_GetDisplayDeviceInfo(addr);
    if (info == nullptr) {
        JNIEnv *env;
        GETENV(env);
        std::stringstream ss;
        ss << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        JNI_ThrowNativeLibraryException(env, ss.str());
    }
    return info->gpio_cb(u8x8, msg, arg_int, arg_ptr);
}

std::string U8g2Util_GetPinIndexDesc(int index) {
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
