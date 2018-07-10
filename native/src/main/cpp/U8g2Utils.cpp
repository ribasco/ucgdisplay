//
// Created by raffy on 7/7/18.
//

#include <iostream>
#include <memory>
#include <functional>
#include <u8g2.h>
#include <jni.h>

#include "U8g2Utils.h"
#include "U8g2Hal.h"

static map<uintptr_t, shared_ptr<u8g2_info_t>> u8g2_device_cache; // NOLINT
static map<int, string> pinNameIndexMap; //NOLINT

u8g2_t *toU8g2(jlong address) {
    auto *u8g2 = reinterpret_cast<u8g2_t *>(address);
    return u8g2;
}

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

u8g2_cb_t *toRotation(int rotation) {
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

/**
u8x8_byte_4wire_sw_spi	Standard 8-bit SPI communication with "four pins" (SCK, MOSI, DC, CS)
u8x8_byte_3wire_sw_spi	9-bit communication with "three pins" (SCK, MOSI, CS)
u8x8_byte_8bit_6800mode	Parallel interface, 6800 format
u8x8_byte_8bit_8080mode	Parallel interface, 8080 format
u8x8_byte_sw_i2c	    Two wire, I2C communication
u8x8_byte_ks0108	    Special interface for KS0108 controller
 */
u8g2_msg_func_info_t get_byte_cb(int commInt, int commType) {
    u8g2_msg_func_t tmpcb;

    //Wrap lambda around existing functions for compatability
    switch (commInt) {
        case COMINT_3WSPI: {
            if (commType == COMTYPE_HW) {
                //no HW support for 3-wire interface?
                return nullptr;
            } else {
                tmpcb = u8x8_byte_3wire_sw_spi;
            }
            break;
        }
        case COMINT_4WSPI: {
            if (commType == COMTYPE_HW) {
                tmpcb = u8x8_byte_3wire_sw_spi;
            } else {
                return cb_rpi_byte_spi_hw;
            }
            break;
        }
            //Similar to 4W_SPI
        case COMINT_ST7920SPI: {
            if (commType == COMTYPE_HW) {
                return cb_rpi_byte_spi_hw;
            }
            //software impl?
            return nullptr; //TODO: ?
        }
        case COMINT_I2C: {
            if (commType == COMTYPE_HW) {
                return cb_rpi_byte_i2c_hw;
            } else {
                tmpcb = u8x8_byte_sw_i2c;
            }
            break;
        }
        case COMINT_6800: {
            if (commType == COMTYPE_HW) {
                //TODO: Maybe in future, we could allow I2C/SPI/Shift register expansions to use the 6800 and 8080 modes
                return nullptr;
            } else {
                tmpcb = u8x8_byte_8bit_6800mode;
            }
            break;
        }
        case COMINT_8080: {
            if (commType == COMTYPE_HW) {
                //TODO: Maybe in future, we could allow I2C/SPI/Shift register expansions to use the 6800 and 8080 modes
                return nullptr;
            } else {
                tmpcb = u8x8_byte_8bit_8080mode;
            }
            break;
        }
        case COMINT_UART: {
            //TODO: Not yet sure how to implement this
            /*if (commType == COMTYPE_HW) {

            } else {
                //TODO: ?
            }*/
            //SEE: u8g2_Setup_a2printer_384x240_f()
            return nullptr;
        }
            //similar to 6800 mode
        case COMINT_KS0108: {
            if (commType == COMTYPE_HW) {
                return nullptr;
            } else {
                tmpcb = u8x8_byte_ks0108;
            }
            break;
        }
        case COMINT_SED1520: {
            if (commType == COMTYPE_HW) {
                return nullptr;
            } else {
                tmpcb = u8x8_byte_sed1520;
            }
            break;
        }
        default:
            break;
    }
    if (tmpcb != nullptr) {
        return [&tmpcb](u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
            return tmpcb(u8x8, msg, arg_int, arg_ptr);
        };
    }
    return nullptr;
}

shared_ptr<u8g2_info_t> setupAndInitDisplay(string setup_proc_name, int commInt, int commType, const u8g2_cb_t *rotation, u8g2_pin_map_t pin_config) {
    shared_ptr<u8g2_info_t> info = make_shared<u8g2_info_t>();

    //Initialize device info details
    info->u8g2 = make_shared<u8g2_t>();
    info->pin_map = pin_config;
    info->rotation = const_cast<u8g2_cb_t *>(rotation);

    //Get the setup procedure callback
    u8g2_setup_func_t setup_proc_callback = u8g2hal_get_setupproc(setup_proc_name);

    //Verify that we have found a callback
    if (setup_proc_callback == nullptr) {
        cerr << "u8g2 setup procedure not found: '" << setup_proc_name << "'" << endl;
        return nullptr;
    }

    info->setup_proc_name = setup_proc_name;
    info->setup_cb = setup_proc_callback;

    //TODO: Dynamically select callback
    info->byte_cb = [info, commInt, commType](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        u8g2_msg_func_info_t cb_byte = get_byte_cb(commInt, commType);
        if (cb_byte == nullptr)
            return 0;
        return cb_byte(info.get(), u8x8, msg, arg_int, arg_ptr);//cb_rpi_byte_spi_hw(info.get(), u8x8, msg, arg_int, arg_ptr);
    };

    info->gpio_cb = [info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        return cb_rpi_gpio_delay(info.get(), u8x8, msg, arg_int, arg_ptr);
    };

    //Obtain the raw pointer
    u8g2_t *pU8g2 = info->u8g2.get();

    //Insert device info in cache
    auto it = u8g2_device_cache.insert(make_pair((uintptr_t) pU8g2, info));

    //Call the setup procedure
    info->setup_cb(pU8g2, rotation, u8g2_setup_helper_byte, u8g2_setup_helper_gpio);

    //Initialize the display
    u8g2_InitDisplay(pU8g2);
    u8g2_SetPowerSave(pU8g2, 0);
    u8g2_ClearDisplay(pU8g2);

    return info;
}

shared_ptr<u8g2_info_t> getDisplayDeviceInfo(uintptr_t addr) {
    auto it = u8g2_device_cache.find(addr);
    if (it != u8g2_device_cache.end())
        return it->second;
    return nullptr;
}

uint8_t u8g2_setup_helper_byte(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    shared_ptr<u8g2_info_t> info = getDisplayDeviceInfo(addr);
    if (info == nullptr) {
        cerr << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << to_string(addr) << endl;
        exit(-1);
    }
    return info->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t u8g2_setup_helper_gpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    shared_ptr<u8g2_info_t> info = getDisplayDeviceInfo(addr);
    if (info == nullptr) {
        cerr << "[u8g2_setup_helper_gpio] Unable to obtain display device info for address: " << to_string(addr) << endl;
        exit(-1);
    }
    return info->gpio_cb(u8x8, msg, arg_int, arg_ptr);
}

static bool _pins_initialized = false;

string getPinIndexDesc(int index) {
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

void throwNativeDriverException(JNIEnv *env, string msg) {
    env->ThrowNew(clsGlcdNativeDriverException, msg.c_str());
}

void copyjByteArray(JNIEnv *env, jbyteArray arr, uint8_t *buffer, int length) {
    if (length <= 0) {
        throwNativeDriverException(env, "Invalid array length");
        return;
    }
    if (buffer == nullptr) {
        throwNativeDriverException(env, "Buffer is null");
        return;
    }
    jbyte *body = env->GetByteArrayElements(arr, nullptr);
    for (int i = 0; i < length; i++) {
        buffer[i] = static_cast<uint8_t>(body[i]);
    }
    env->ReleaseByteArrayElements(arr, body, 0);
}
