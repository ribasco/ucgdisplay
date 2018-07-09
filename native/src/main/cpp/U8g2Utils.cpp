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

u8g2_t *to_ptr(jlong address) {
    auto *u8g2 = reinterpret_cast<u8g2_t *>(address);
    return u8g2;
}

shared_ptr<u8g2_info_t> SetupAndInitDisplay(string setup_procedure, u8g2_pin_map_t pin_config, const u8g2_cb_t *rotation) {
    shared_ptr<u8g2_info_t> info = make_shared<u8g2_info_t>();

    //Initialize device info details
    info->u8g2 = make_shared<u8g2_t>();
    info->pin_map = pin_config;
    info->rotation = const_cast<u8g2_cb_t *>(rotation);

    //Get the setup procedure callback
    u8g2_setup_func_t setup_proc_callback = u8g2hal_get_setupproc(setup_procedure);

    //Verify that we have found a callback
    if (setup_proc_callback == nullptr) {
        cerr << "u8g2 setup procedure not found: '" << setup_procedure << "'" << endl;
        return nullptr;
    }

    info->setup_cb = setup_proc_callback;

    //TODO: Dynamically select callback based on the communication protocol specified
    info->byte_cb = [info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        return cb_rpi_byte_spi_hw(info.get(), u8x8, msg, arg_int, arg_ptr);
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

shared_ptr<u8g2_info_t> GetDisplayDeviceInfo(uintptr_t addr) {
    auto it = u8g2_device_cache.find(addr);
    if (it != u8g2_device_cache.end())
        return it->second;
    return nullptr;
}

uint8_t u8g2_setup_helper_byte(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    shared_ptr<u8g2_info_t> info = GetDisplayDeviceInfo(addr);
    if (info == nullptr) {
        cerr << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << to_string(addr) << endl;
        exit(-1);
    }
    return info->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t u8g2_setup_helper_gpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    shared_ptr<u8g2_info_t> info = GetDisplayDeviceInfo(addr);
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

void copyjByteArrayToPrimitive(JNIEnv *env, jbyteArray arr, uint8_t *buffer, int length) {
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
