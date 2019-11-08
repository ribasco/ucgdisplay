#define UCGTEST

#include <iostream>
#include <u8g2.h>
#include <unistd.h>
#include <chrono>
#include <csignal>
#include <UcgPigpiodProvider.h>
#include <UcgLibgpiodProvider.h>
#include <UcgCperipheryProvider.h>
#include <UcgSpiProvider.h>
#include "U8g2TestHal.h"

static volatile bool complete = false;

#pragma clang diagnostic push
#pragma ide diagnostic ignored "OCDFAInspection"

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

#pragma clang diagnostic pop

void drawBannerText(u8g2_t *u8g2, uint32_t durationSecs) {

    uint8_t x = 255;
    uint32_t prevMillis = 0;

    //Set Font
    u8g2_SetFont(u8g2, u8g2_font_7x13B_mf);
    uint32_t elapsed = 0;

    while (elapsed != durationSecs) {
        if (complete)
            break;

        std::chrono::milliseconds ms = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch());
        uint32_t curMillis = ms.count();

        //Count the number of elapsed seconds
        if ((curMillis - prevMillis) >= 1000) {
            prevMillis = curMillis;
            elapsed++;
        }

        if (x <= 0)
            x = 255;
        u8g2_ClearBuffer(u8g2);
        std::string text = "UCGDisplay v1.5.0-alpha";
        u8g2_DrawStr(u8g2, x, 32, text.c_str());
        u8g2_SendBuffer(u8g2);
        x -= 5;
        usleep(500000);
    }
}

void initializeProviders(const std::shared_ptr<ucgd_t> &info) {
#ifdef DEBUG_UCGD
    std::cout << "U8g2Util_InitializeProviders: Initializing selected provider" << std::endl;
#endif
    try {
        //Initialize all available providers
        auto pigpio_it = info->providers.insert(make_pair(PROVIDER_PIGPIO, std::make_shared<UcgPigpiodProvider>(info)));
        auto libgpiod_it = info->providers.insert(make_pair(PROVIDER_LIBGPIOD, std::make_shared<UcgLibgpiodProvider>(info)));
        auto cper_it = info->providers.insert(make_pair(PROVIDER_CPERIPHERY, std::make_shared<UcgCperipheryProvider>(info)));

        //Retrieve the default provider
        std::string defaultProvider = info->getOptionString(OPT_PROVIDER);
        std::cout << "Found default provider: " << defaultProvider << std::endl;

        std::cout << "Initializing providers: START" << std::endl;
        pigpio_it.first->second->initialize();
        cper_it.first->second->initialize();
        libgpiod_it.first->second->initialize();
        std::cout << "Initializing providers: END" << std::endl;

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
        std::cout << "Default provider not found" << std::endl;
        throw e;
    }
}

u8g2_t *setupDisplay(const std::string &gpioProvider, const std::string &spiProvider, const std::string &i2cProvider) {
    //Configure the SPI pins
    std::shared_ptr<ucgd_t> info = std::make_shared<ucgd_t>();

    u8g2_pin_map_t pin_config = {};
    /*pin_config.d0 = 11;
    pin_config.d1 = 10;
    pin_config.cs = 8;*/

    //Initialize device info details
    info->u8g2 = std::make_unique<u8g2_t>();
    info->pin_map = pin_config;
    info->rotation = const_cast<u8g2_cb_t *>(U8G2_R0);
    info->flag_virtual = false;
    info->comm_int = COMINT_ST7920SPI;
    info->comm_type = COMTYPE_HW;

    //Populate options
    info->options[OPT_DEVICE_SPEED] = DEFAULT_SPI_SPEED;
    info->options[OPT_PROVIDER] = std::string(PROVIDER_CPERIPHERY);
    info->options[OPT_SPI_BUS] = SPI_PERIPHERAL_MAIN;
    info->options[OPT_SPI_CHANNEL] = DEFAULT_SPI_CHANNEL;
    info->options[OPT_DEVICE_GPIO_PATH] = std::string("/dev/gpiochip0");
    info->options[OPT_DEVICE_I2C_PATH] = std::string("/dev/i2c-1");
    info->options[OPT_DEVICE_SPI_PATH] = std::string("/dev/spidev0.0");
    info->options[OPT_I2C_BUS] = 1;

    initializeProviders(info);
    std::cout << "initializeProviders : DONE" << std::endl;

    //Init callbacks
    info->byte_cb = [info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        return cb_byte_spi_hw(info, u8x8, msg, arg_int, arg_ptr);
    };

    info->gpio_cb = [info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        return cb_gpio_delay(info, u8x8, msg, arg_int, arg_ptr);
    };

    //Obtain the raw pointer
    u8g2_t *pU8g2 = info->u8g2.get();

    //Insert device m_Info in cache
    addToDeviceCache(pU8g2, info);

    //for bit bang implementations, you can use the pre-built callbacks available in the u8g2 library
    //see https://github.com/olikraus/u8g2/wiki/Porting-to-new-MCU-platform#communication-callback-eg-u8x8_byte_hw_i2c
    //ex: u8x8_byte_4wire_sw_spi
    u8g2_Setup_st7920_s_128x64_f(pU8g2, U8G2_R2, U8g2Util_ByteCallbackWrapper, U8g2Util_GpioCallbackWrapper);;

    //Initialize Display
    u8g2_InitDisplay(pU8g2);
    u8g2_SetPowerSave(pU8g2, 0);
    u8g2_ClearDisplay(pU8g2);

    return pU8g2;
}

// Define the function to be called when ctrl-c (SIGINT) is sent to process
void signal_callback_handler(int signum) {
    std::cout << "Detected cancel event" << std::endl;
    complete = true;
}

bool validProvider(const std::string &provider) {
    if (provider == PROVIDER_PIGPIO || provider == PROVIDER_CPERIPHERY || provider == PROVIDER_LIBGPIOD)
        return true;
    return false;
}

void printUsage(char *argv[]) {
    std::cout << "Usage: " << std::string(argv[0]) << " [-d] <gpio provider> <spi provider> <i2c provider>" << std::endl;
    exit(1);
}

int main(int argc, char *argv[]) {
    // Register signal and signal handler
    signal(SIGINT, signal_callback_handler);

    std::string gpioProvider;
    std::string spiProvider;
    std::string i2cProvider;

    if (argc == 4) {
        gpioProvider = std::string(argv[1]);
        spiProvider = std::string(argv[2]);
        i2cProvider = std::string(argv[3]);
    } else if (argc >= 2) {
        if (strcmp("-d", argv[1]) == 0) {
            gpioProvider = std::string(PROVIDER_LIBGPIOD);
            spiProvider = std::string(PROVIDER_PIGPIO);
            i2cProvider = std::string(PROVIDER_PIGPIO);
        } else {
            printUsage(argv);
        }
    } else {
        printUsage(argv);
    }

    //validate input
    if (!validProvider(gpioProvider))
        printUsage(argv);
    if (!validProvider(spiProvider))
        printUsage(argv);
    if (!validProvider(i2cProvider))
        printUsage(argv);

    std::cout << "=========================================" << std::endl;
    std::cout << "Using Providers" << std::endl;
    std::cout << "=========================================" << std::endl;
    std::cout << "GPIO PROVIDER = " << gpioProvider << std::endl;
    std::cout << "SPI PROVIDER = " << spiProvider << std::endl;
    std::cout << "I2C PROVIDER = " << i2cProvider << std::endl;
    std::cout << "=========================================" << std::endl;

    u8g2_t *u8g2 = setupDisplay(gpioProvider, spiProvider, i2cProvider);

    std::cout << "Running display demo...Press Ctrl+C to exit" << std::endl;

    //Run display loop
    while (!complete) {
        drawU8G2Logo(u8g2);
        usleep(3000000);
        drawBannerText(u8g2, 20);
    }

    u8g2_ClearDisplay(u8g2);

    std::cout << "Exiting demo" << std::endl;

    return 0;
}