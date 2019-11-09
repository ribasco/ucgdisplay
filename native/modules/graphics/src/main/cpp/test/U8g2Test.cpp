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
#include <UcgPigpioProvider.h>
#include "U8g2TestHal.h"
#include <sstream>

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

void initializeProviders() {
    //Initialize service locator and providers
    ServiceLocator &locator = ServiceLocator::getInstance();
    std::unique_ptr<Log> log = std::make_unique<Log>(nullptr);
    //Initialize Logger
    locator.setLogger(log);

    //Initialize Device Manager
    locator.setDeviceManager(std::make_unique<DeviceManager>());

    //Initialize Providers
    locator.setProviderManager(std::make_unique<ProviderManager>());
    auto &pMan = locator.getProviderManager();

    //Register supported providers
    if (!pMan->isRegistered(PROVIDER_CPERIPHERY))
        pMan->registerProvider(std::make_unique<UcgCperipheryProvider>());
    if (!pMan->isRegistered(PROVIDER_PIGPIO))
        pMan->registerProvider(std::make_unique<UcgPigpioProvider>());
    if (!pMan->isRegistered(PROVIDER_PIGPIOD))
        pMan->registerProvider(std::make_unique<UcgPigpiodProvider>("", ""));
    if (!pMan->isRegistered(PROVIDER_LIBGPIOD))
        pMan->registerProvider(std::make_unique<UcgLibgpiodProvider>());
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

u8g2_t *setupDisplay(const std::string &provider) {
    initializeProviders();

    const std::unique_ptr<DeviceManager> &devMgr = ServiceLocator::getInstance().getDeviceManager();
    std::shared_ptr<ucgd_t> &info = devMgr->createDevice();

    u8g2_pin_map_t pin_config = {};
    /*pin_config.d0 = 11;
    pin_config.d1 = 10;
    pin_config.cs = 8;*/

    //Initialize device info details
    //info->u8g2 = std::make_unique<u8g2_t>();
    info->pin_map = pin_config;
    info->rotation = const_cast<u8g2_cb_t *>(U8G2_R0);
    info->flag_virtual = false;
    info->comm_int = COMINT_ST7920SPI;
    info->comm_type = COMTYPE_HW;

    //Populate options
    info->options[OPT_BUS_SPEED] = DEFAULT_SPI_SPEED;
    info->options[OPT_PROVIDER] = std::string(PROVIDER_CPERIPHERY);
    info->options[OPT_SPI_BUS] = SPI_PERIPHERAL_MAIN;
    info->options[OPT_SPI_CHANNEL] = SPI_RPI_CHANNEL_CE1;
    info->options[OPT_I2C_BUS] = 1;
    info->options[OPT_PROVIDER] = provider;
    info->provider = ServiceLocator::getInstance().getProviderManager()->getProvider(info);
    info->provider->initialize(info);

    //Make sure the provider supports the current configuration setup
    //e.g. SPI & I2C capability
    if (info->comm_type == COMTYPE_HW) {
        if ((info->comm_int == COMINT_4WSPI || info->comm_int == COMINT_3WSPI || info->comm_int == COMINT_ST7920SPI) && !info->provider->supportsSPI()) {
            throw std::runtime_error(std::string("Your current setup is configured for Hardware SPI but the provider you selected '") + info->provider->getName() + std::string("' does not have hardware SPI capability"));
        } else if ((info->comm_int == COMINT_I2C) && !info->provider->supportsI2C()) {
            throw std::runtime_error(std::string("Your current setup is configured for Hardware I2C but the provider you selected '") + info->provider->getName() + std::string("' does not have hardware I2C capability"));
        }
    } else if (info->comm_type == COMTYPE_SW) {
        //make sure the current provider supports GPIO for bit-bang implementations
        if (!info->provider->supportsGpio()) {
            throw std::runtime_error(std::string("Your current setup is configured for software bit-bang implementation but the provider you selected does '") + info->provider->getName() + std::string("' not have GPIO capability"));
        }
    }

    //Assign the i2c addres if applicable
    if (info->comm_type == COMINT_I2C) {
        std::any addr = info->options[OPT_I2C_ADDRESS];
        if (addr.has_value()) {
            int intVal = std::any_cast<int>(addr);
            u8g2_SetI2CAddress(info->u8g2.get(), intVal);
        }
    }

    std::cout << "initializeProviders : DONE" << std::endl;

    //Retrieve the byte callback function based on the commInt and commType arguments
    u8g2_msg_func_info_t cb_byte = U8g2Util_GetByteCb(info->comm_int, info->comm_type);

    if (cb_byte == nullptr) {
        throw std::runtime_error(std::string("No available byte callback procedures for CommInt = ") + std::to_string(info->comm_int) + std::string(", CommType = ") + std::to_string(info->comm_type));
    }

    //Init callbacks
    info->byte_cb = [info, cb_byte](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        try {
            return cb_byte(info, u8x8, msg, arg_int, arg_ptr);
        } catch (std::exception &e) {
            std::stringstream ss;
            ss << "byte_cb() : " << std::string(e.what()) << std::endl;
            throw std::runtime_error(ss.str());
        }
        return cb_byte(info, u8x8, msg, arg_int, arg_ptr);
    };

    info->gpio_cb = [info](u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) -> uint8_t {
        try {
            return cb_gpio_delay(info, u8x8, msg, arg_int, arg_ptr);
        } catch (std::exception &e) {
            std::stringstream ss;
            ss << "gpio_cb() : " << std::string(e.what()) << std::endl;
            throw std::runtime_error(ss.str());
        }
    };

    //Obtain the raw pointer
    u8g2_t *pU8g2 = info->u8g2.get();

    //for bit bang implementations, you can use the pre-built callbacks available in the u8g2 library
    //see https://github.com/olikraus/u8g2/wiki/Porting-to-new-MCU-platform#communication-callback-eg-u8x8_byte_hw_i2c
    //ex: u8x8_byte_4wire_sw_spi
    u8g2_Setup_st7920_s_128x64_f(pU8g2, U8G2_R2, ByteCallbackWrapper, GpioCallbackWrapper);;

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
    if (provider == PROVIDER_PIGPIO || provider == PROVIDER_PIGPIOD || provider == PROVIDER_CPERIPHERY || provider == PROVIDER_LIBGPIOD)
        return true;
    return false;
}

void printUsage(char *argv[]) {
    std::cout << "Usage: " << std::string(argv[0]) << " [-d] <provider>" << std::endl;
    exit(1);
}

int main(int argc, char *argv[]) {
    // Register signal and signal handler
    signal(SIGINT, signal_callback_handler);

    std::string provider;

    if (argc == 2) {
        if (strcmp("-d", argv[1]) == 0) {
            provider = std::string(PROVIDER_PIGPIO);
        } else {
            provider = std::string(argv[1]);
        }
    } else {
        printUsage(argv);
    }

    //validate input
    if (!validProvider(provider))
        printUsage(argv);

    std::cout << "=========================================" << std::endl;
    std::cout << "Using Providers" << std::endl;
    std::cout << "=========================================" << std::endl;
    std::cout << "PROVIDER = " << provider << std::endl;
    std::cout << "=========================================" << std::endl;

    u8g2_t *u8g2 = setupDisplay(provider);

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