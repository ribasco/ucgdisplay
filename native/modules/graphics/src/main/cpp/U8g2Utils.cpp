/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Graphics
 * %%
 * Copyright (C) 2018 - 2021 Universal Character/Graphics display library
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

#include <UcgdConfig.h>
#include <U8g2Hal.h>
#include <U8g2Utils.h>
#include <ServiceLocator.h>
#include <DeviceManager.h>

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

#include <Utils.h>
#include <UcgdLibgpiodProvider.h>
#include <UcgdPigpioProvider.h>
#include <UcgdCperipheryProvider.h>
#include <UcgdPigpiodProvider.h>

#endif

static std::map<int, std::string> pinNameIndexMap; //NOLINT

static bool _pins_initialized = false;

jclass clsU8g2GpioEvent;
jclass clsU8g2EventDispatcher;
jmethodID midU8g2EventDispatcher_onGpioEvent;
jmethodID midU8g2EventDispatcher_onByteEvent;
jmethodID midU8g2GpioEventCtr;
jmethodID midU8g2EventDispatcher_hasGpioListeners;
jmethodID midU8g2EventDispatcher_hasByteListeners;

void U8gUtils_Load(JNIEnv *env) {
    //START: Cache Class/methods
    JNI_MakeGlobal(env, CLS_U8g2GpioEvent, clsU8g2GpioEvent);
    JNI_MakeGlobal(env, CLS_U8g2EventDispatcher, clsU8g2EventDispatcher);
    midU8g2EventDispatcher_onGpioEvent = env->GetStaticMethodID(clsU8g2EventDispatcher, "onGpioEvent", "(JII)V");
    midU8g2EventDispatcher_onByteEvent = env->GetStaticMethodID(clsU8g2EventDispatcher, "onByteEvent", "(JII)V");
    midU8g2GpioEventCtr = env->GetMethodID(clsU8g2GpioEvent, "<init>", "(II)V");
    midU8g2EventDispatcher_hasByteListeners = env->GetStaticMethodID(clsU8g2EventDispatcher, "hasByteListeners", "()Z");
    midU8g2EventDispatcher_hasGpioListeners = env->GetStaticMethodID(clsU8g2EventDispatcher, "hasGpioListeners", "()Z");
    //END
}

void JNI_FireGpioEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value) {
    env->CallStaticVoidMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_onGpioEvent, (jlong) id, msg, value);
}

void JNI_FireByteEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value) {
    env->CallStaticVoidMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_onByteEvent, (jlong) id, msg, value);
}

bool JNI_HasGpioListeners(JNIEnv *env) {
    return (bool)env->CallStaticBooleanMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_hasGpioListeners);
}

bool JNI_HasByteListeners(JNIEnv *env) {
    return (bool)env->CallStaticBooleanMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_hasByteListeners);
}

u8g2_cb_t *U8g2Util_ToRotation(int rotation) {
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

std::shared_ptr<ucgd_t> &U8g2Util_SetupAndInitDisplay(const std::string &setup_proc_name, int commInt, int commType, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config, option_map_t &options, uint8_t* buffer, bool virtualMode) {
    JNIEnv *env;
    GETENV(env);

    Log &log = ServiceLocator::getInstance().getLogger();

    const std::unique_ptr<DeviceManager> &devMgr = ServiceLocator::getInstance().getDeviceManager();
    std::shared_ptr<ucgd_t> &context = devMgr->createDevice();

    //read this: https://floating.io/2017/07/lambda-shared_ptr-memory-leak/
    std::weak_ptr<ucgd_t> weak_context(context);

    //Store all device specific properties to the context
    context->pin_map = pin_config;
    context->rotation = const_cast<u8g2_cb_t *>(rotation);
    context->flag_virtual = virtualMode;
    context->comm_int = commInt;
    context->comm_type = commType;

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    std::string defaultProviderName = context->getOptionString(OPT_PROVIDER);
    context->options = std::map(options);
    context->setDefaultProvider(ServiceLocator::getInstance().getProviderManager()->getProvider(context));
    auto defaultProvider = context->getDefaultProvider();

    log.debug("setup_display() : Initializing default provider '{}'", defaultProvider->getName());
    if (!defaultProvider->isInitialized())
        defaultProvider->open(context);

    //Make sure the provider supports the current configuration setup
    //e.g. SPI & I2C capability
    if (commType == COMTYPE_HW) {
        if ((commInt == COMINT_4WSPI || commInt == COMINT_3WSPI || commInt == COMINT_ST7920SPI) && !defaultProvider->supportsSPI()) {
            throw UcgdSetupException(std::string("Your current setup is configured for Hardware SPI but the provider you selected '") + defaultProvider->getName() + std::string("' does not have hardware SPI capability"));
        } else if ((commInt == COMINT_I2C) && !defaultProvider->supportsI2C()) {
            throw UcgdSetupException(std::string("Your current setup is configured for Hardware I2C but the provider you selected '") + defaultProvider->getName() + std::string("' does not have hardware I2C capability"));
        }
    } else if (commType == COMTYPE_SW) {
        //make sure the current provider supports GPIO for bit-bang implementations
        if (!defaultProvider->supportsGpio()) {
            throw UcgdSetupException(std::string("Your current setup is configured for software bit-bang implementation but the provider you selected does '") + defaultProvider->getName() + std::string("' not have GPIO capability"));
        }
    }

    //Assign the i2c addres if applicable
    if (commType == COMINT_I2C) {
        std::any addr = context->options[OPT_I2C_ADDRESS];
        if (addr.has_value()) {
            int intVal = std::any_cast<int>(addr);
            u8g2_SetI2CAddress(context->u8g2.get(), intVal);
        }
    }
#endif

    //Get the setup procedure callback
    u8g2_setup_func_t setup_proc_callback = U8g2Hal_GetSetupProc(setup_proc_name);

    //Verify that we have found a callback
    if (setup_proc_callback == nullptr) {
        std::stringstream ss;
        ss << "setup_display() : u8g2 setup procedure not found: '" << setup_proc_name << "'" << std::endl;
        throw UcgdSetupException(ss.str());
    }

    context->setup_proc_name = setup_proc_name;
    context->setup_cb = setup_proc_callback;

    //Retrieve the byte callback function based on the commInt and commType arguments
    u8g2_msg_func_info_t cb_byte = U8g2Util_GetByteCb(commInt, commType);

    if (cb_byte == nullptr) {
        throw UcgdSetupException(std::string("No available byte callback procedures for CommInt = ") + std::to_string(commInt) + std::string(", CommType = ") + std::to_string(commType));
    }

    //Configure Byte callback
    context->byte_cb = [cb_byte, weak_context, virtualMode](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        auto context = weak_context.lock();

        if (!context)
            throw UcgdByteCallbackException("context->byte_cb() : Context out of scope");

        if (virtualMode) {
            JNIEnv *lenv;
            GETENV(lenv);

            if (!JNI_HasByteListeners(lenv))
                return 1;

            if (msg == U8X8_MSG_BYTE_SEND) {
                uint8_t value;
                uint8_t size = arg_int;
                auto *data = (uint8_t *) arg_ptr;
                JNI_FireByteEvent(lenv, context->address(), U8G2_BYTE_SEND_INIT, size); //fire custom event
                while (size > 0) {
                    value = *data;
                    data++;
                    size--;
                    JNI_FireByteEvent(lenv, context->address(), msg, value);
                }
            } else {
                JNI_FireByteEvent(lenv, context->address(), msg, arg_int);
            }
            return 1;
        }
        try {
            if (g_SignalStatus)
                throw SignalInterruptedException(g_SignalStatus, "Caught signal interrupt");

            return cb_byte(context, u8x8, msg, arg_int, arg_ptr);
         } catch (SignalInterruptedException& e) {
            throw e;
         } catch (std::exception &e) {
            throw UcgdByteCallbackException(std::string("context->byte_cb() : Error thrown from the Byte Callback. Reason: ") + std::string(e.what()));
        }
    };

    //Configure Gpio callback
    context->gpio_cb = [virtualMode, weak_context](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        auto context = weak_context.lock();

        if (!context)
            throw UcgdGpioCallbackException("context->gpio_cb() : Context out of scope");

        if (virtualMode) {
            JNIEnv *lenv;
            GETENV(lenv);
            if (!JNI_HasGpioListeners(lenv))
                return 1;
            JNI_FireGpioEvent(lenv, context->address(), msg, arg_int);
            return 1;
        }

       try {
            if (g_SignalStatus)
                throw SignalInterruptedException(g_SignalStatus, "Caught signal interrupt");

            return cb_gpio_delay(context, u8x8, msg, arg_int, arg_ptr);
       } catch (SignalInterruptedException& e) {
            throw e;
       } catch (std::exception &e) {
            throw UcgdGpioCallbackException(std::string("context->gpio_cb() : Error thrown from the Gpio Callback. Reason: ") + std::string(e.what()));
       }
    };

    //Obtain the u8g2 raw pointer
    u8g2_t *pU8g2 = context->u8g2.get();

    //Call the setup procedure
    context->setup_cb(pU8g2, rotation, U8g2Util_ByteCallbackWrapper, U8g2Util_GpioCallbackWrapper);

    //Allocate dynamic buffer
    u8g2_SetBufferPtr(pU8g2, buffer);
    //log.debug("setup_display() : Allocating pixel buffers dynamically (size: {})", size);

    //Initialize the display
    log.debug("setup_display() : Executing u8g2 startup sequence for '{}'", std::to_string(context->address()));
    u8g2_InitDisplay(pU8g2);
    u8g2_SetPowerSave(pU8g2, 0);
    u8g2_ClearDisplay(pU8g2);

    log.debug("setup_display() : Display start sequence complete");
    return context;
}

uint8_t U8g2Util_ByteCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<ucgd_t> &context = ServiceLocator::getInstance().getDeviceManager()->getDevice(addr);
    return context->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t U8g2Util_GpioCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<ucgd_t> &context = ServiceLocator::getInstance().getDeviceManager()->getDevice(addr);
    return context->gpio_cb(u8x8, msg, arg_int, arg_ptr);
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
        return std::string();
    }
    return pinNameIndexMap.at(index);
}

u8g2_t *toU8g2(jlong address) {
    auto *u8g2 = reinterpret_cast<u8g2_t *>(address);
    return u8g2;
}
