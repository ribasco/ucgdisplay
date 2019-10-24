#include <iostream>
#include <u8g2.h>
#include <unistd.h>
#include <chrono>
#include <gpiod.hpp>

#include "U8g2TestHal.h"

static std::map<uintptr_t, std::shared_ptr<u8g2_info_t>> u8g2_device_cache; // NOLINT
static volatile bool complete = false;

void drawU8G2Logo(u8g2_t *u8g2) {
    u8g2_ClearBuffer(u8g2);
    u8g2_SetFontMode(u8g2, 1);

    u8g2_SetFontDirection(u8g2, 0);
    u8g2_SetFont(u8g2, u8g2_font_inb16_mf);
    u8g2_DrawStr(u8g2, 0, 22, "U");

    u8g2_SetFontDirection(u8g2, 1);
    u8g2_SetFont(u8g2, u8g2_font_inb19_mn);
    u8g2_DrawStr(u8g2, 14, 8, "8");

    u8g2_SetFontDirection(u8g2, 0);
    u8g2_SetFont(u8g2, u8g2_font_inb16_mf);
    u8g2_DrawStr(u8g2, 36, 22, "g");
    u8g2_DrawStr(u8g2, 48, 22, "\xb2");

    u8g2_DrawHLine(u8g2, 2, 25, 34);
    u8g2_DrawHLine(u8g2, 3, 26, 34);
    u8g2_DrawVLine(u8g2, 32, 22, 12);
    u8g2_DrawVLine(u8g2, 33, 23, 12);
    u8g2_SendBuffer(u8g2);
}

void drawBannerText(u8g2_t *u8g2, uint32_t durationSecs) {

    uint8_t x = 255;
    uint32_t prevMillis = 0;

    //Set Font
    u8g2_SetFont(u8g2, u8g2_font_7x13B_mf);
    uint32_t elapsed = 0;

    while (elapsed != durationSecs) {
        if (complete)
            break;

        std::chrono::milliseconds ms = std::chrono::duration_cast<std::chrono::milliseconds >(std::chrono::system_clock::now().time_since_epoch());
        uint32_t curMillis = ms.count();

        //Count the number of elapsed seconds
        if ((curMillis - prevMillis) >= 1000) {
            prevMillis = curMillis;
            elapsed++;
        }

        if (x <= 0)
            x = 255;
        u8g2_ClearBuffer(u8g2);
        std::string text = "UG82 running on Raspberry Pi!";
        u8g2_DrawStr(u8g2, x, 32, text.c_str());
        u8g2_SendBuffer(u8g2);
        x -= 5;
        usleep(500000);
    }
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
    //std::cout << "u8g2util_SetupHelperByte: " << info->gpio_device << std::endl;
    if (info == nullptr) {
        std::cerr << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        return 0;
    }
    return info->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t u8g2util_SetupHelperGpio(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<u8g2_info_t> info = u8g2util_GetDisplayDeviceInfo(addr);
    //std::cout << "u8g2util_SetupHelperGpio: " << info->gpio_device << std::endl;
    if (info == nullptr) {
        std::cerr << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        return 0;
    }
    return info->gpio_cb(u8x8, msg, arg_int, arg_ptr);
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

int runDisplayTest() {
    //Configure the SPI pins
    std::shared_ptr<u8g2_info_t> info = std::make_shared<u8g2_info_t>();
    //gpioInitialise();

    u8g2_pin_map_t pin_config;
    //pin_config.cs = 8;
    /*pin_config.d0 = 11; //spi clock
    pin_config.d1 = 10; //spi data*/

    //Initialize device info details
    info->u8g2 = std::make_shared<u8g2_t>();
    info->pin_map = pin_config;
    info->rotation = const_cast<u8g2_cb_t *>(U8G2_R0);
    info->flag_virtual = false;

    info->transport_device = "/dev/spidev0.0";
    info->gpio_device = "/dev/gpiochip0";
    info->device_speed = 1000000;
    info->spi = std::make_shared<spi_t>();
    info->i2c = std::make_shared<i2c_t>();

    std::cout << "Using gpio device: " << info->gpio_device << std::endl;

    try {
        info->gpio_chip = std::make_shared<gpiod::chip>(info->gpio_device);
    } catch (const std::system_error &e) {
        std::cerr << "Unable to open gpio device (Device: " << info->gpio_device << ", Code: " << e.code() << ", Reason: " << e.what() << ")";
        return -1;
    }

    //Init callbacks

    //Byte callback
    info->byte_cb = [info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        return cb_byte_spi_hw(info, u8x8, msg, arg_int, arg_ptr);
    };

    //Gpio callback
    info->gpio_cb = [info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        return cb_gpio_delay(info, u8x8, msg, arg_int, arg_ptr);
    };

    //Obtain the raw pointer
    u8g2_t *pU8g2 = info->u8g2.get();

    //Insert device info in cache
    u8g2_device_cache.insert(std::make_pair((uintptr_t) pU8g2, info));

    //for bit bang implementations, you can use the pre-built callbacks available in the u8g2 library
    //see https://github.com/olikraus/u8g2/wiki/Porting-to-new-MCU-platform#communication-callback-eg-u8x8_byte_hw_i2c
    //ex: u8x8_byte_4wire_sw_spi
    u8g2_Setup_st7920_s_128x64_f(pU8g2, U8G2_R2, u8g2util_SetupHelperByte, u8g2util_SetupHelperGpio);;

    //Store to static variable
    u8g2_rpi_hal_init(info);

    //Initialize Display
    u8g2_InitDisplay(pU8g2);
    u8g2_SetPowerSave(pU8g2, 0);
    u8g2_ClearDisplay(pU8g2);

    std::cout << "Running display demo...Press Ctrl+C to exit" << std::endl;

    //Run display loop
    while (!complete) {
        ///Draw U8G2 Logo
        drawU8G2Logo(pU8g2);
        usleep(5000000);
        std::cout << "Drawing..." << std::endl;
        //Run Banner Animation
        drawBannerText(pU8g2, 20);
    }

    std::cout << "Exiting demo" << std::endl;
    u8g2_ClearDisplay(pU8g2);

    return 0;
}

int main() {
    runDisplayTest();

    return 0;
}