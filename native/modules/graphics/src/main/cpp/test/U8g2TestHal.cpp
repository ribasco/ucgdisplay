#include "U8g2TestHal.h"

#include <iostream>
#include <unistd.h>
#include <iomanip>
#include <utility>
#include <Common.h>
#include <UcgGpioProvider.h>
#include <UcgSpiProvider.h>
#include <UcgI2CProvider.h>

static std::shared_ptr<u8g2_info_t> u8g2_rpi_hal;
static std::map<uintptr_t, std::shared_ptr<u8g2_info_t>> u8g2_device_cache; // NOLINT
void initializeGpio(const std::shared_ptr<u8g2_info_t>& info, const std::shared_ptr<UcgGpioProvider>& gpio);
void initializeGpioAllOut(const std::shared_ptr<u8g2_info_t>& info, const std::shared_ptr<UcgGpioProvider>& gpio);

uint8_t cb_byte_spi_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    std::shared_ptr<UcgSpiProvider> spi = info->provider->getSpiProvider();

    switch (msg) {
        case U8X8_MSG_BYTE_INIT: {
            spi->open();
            break;
        }
        case U8X8_MSG_BYTE_SEND: {
            auto *buf = (uint8_t *) arg_ptr;
            spi->write(buf, arg_int);
            break;
        }
        case U8X8_MSG_BYTE_START_TRANSFER: {
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_enable_level);
            u8x8->gpio_and_delay_cb(u8x8, U8X8_MSG_DELAY_NANO, u8x8->display_info->post_chip_enable_wait_ns, nullptr);
            break;
        }
        case U8X8_MSG_BYTE_END_TRANSFER: {
            u8x8->gpio_and_delay_cb(u8x8, U8X8_MSG_DELAY_NANO, u8x8->display_info->pre_chip_disable_wait_ns, nullptr);
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);
            break;
        }
        case U8X8_MSG_BYTE_SET_DC: {
            u8x8_gpio_SetDC(u8x8, arg_int);
        }
        default:
            return 0;
    }
    return 1;
}

uint8_t cb_byte_i2c_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    uint8_t *data;

    std::shared_ptr<UcgI2CProvider> i2c = info->provider->getI2CProvider();

    switch (msg) {
        case U8X8_MSG_BYTE_INIT: {
            i2c->open();
            break;
        }
        case U8X8_MSG_BYTE_SEND: {
            i2c->write(u8x8_GetI2CAddress(u8x8), (uint8_t *) arg_ptr, arg_int);
            break;
        }
        case U8X8_MSG_BYTE_START_TRANSFER: {
            break;
        }
        case U8X8_MSG_BYTE_END_TRANSFER: {
            break;
        }
        default:
            return 0;
    }
    return 1;
}

/**
 * GPIO and Delay Procedure Routine (ARM)
*/
uint8_t cb_gpio_delay(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {

    std::shared_ptr<UcgGpioProvider> gpio = info->provider->getGpioProvider();

    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: { // called once during init phase of u8g2/u8x8, can be used to setup pins
            initializeGpio(info, gpio);
            break;
        }
        case U8X8_MSG_DELAY_NANO: { // delay arg_int * 1 nano second
            //this is important. Removing this will cause garbage data to be displayed.
            usleep(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_100NANO: {       // delay arg_int * 100 nano seconds
            usleep(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_10MICRO: {       // delay arg_int * 10 micro seconds
            usleep(arg_int * 10);
            break;
        }
        case U8X8_MSG_DELAY_MILLI: {           // delay arg_int * 1 milli second
            usleep(arg_int * 1000);
            break;
        }
        case U8X8_MSG_DELAY_I2C: {               // arg_int is the I2C speed in 100KHz, e.g. 4 = 400 KHz
            usleep(arg_int);          // arg_int=1: delay by 5us, arg_int = 4: delay by 1.25us
            break;
        }
        case U8X8_MSG_GPIO_D0: {                // D0 or SPI clock pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_CLOCK)
            gpio->write(info->pin_map.d0, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D1: {                // D1 or SPI data pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_DATA)
            gpio->write(info->pin_map.d1, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D2: {                // D2 pin: Output level in arg_int
            gpio->write(info->pin_map.d2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D3: {                // D3 pin: Output level in arg_int
            gpio->write(info->pin_map.d3, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D4: {                // D4 pin: Output level in arg_int
            gpio->write(info->pin_map.d4, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D5: {                // D5 pin: Output level in arg_int
            gpio->write(info->pin_map.d5, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D6: {                // D6 pin: Output level in arg_int
            gpio->write(info->pin_map.d6, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D7: {                // D7 pin: Output level in arg_int
            gpio->write(info->pin_map.d7, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_E: {               // E/WR pin: Output level in arg_int
            gpio->write(info->pin_map.en, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS: {                // CS (m_Chip select) pin: Output level in arg_int
            gpio->write(info->pin_map.cs, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_DC: {                // DC (data/cmd, A0, register select) pin: Output level in arg_int
            gpio->write(info->pin_map.dc, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_RESET: {            // Reset pin: Output level in arg_int
            gpio->write(info->pin_map.reset, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS1: {                // CS1 (m_Chip select) pin: Output level in arg_int
            gpio->write(info->pin_map.cs1, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS2: {                // CS2 (m_Chip select) pin: Output level in arg_int
            gpio->write(info->pin_map.cs2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_I2C_CLOCK: {        // arg_int=0: Output low at I2C clock pin
            gpio->write(info->pin_map.scl, arg_int);
            break;                            // arg_int=1: Input dir with pullup high for I2C clock pin
        }
        case U8X8_MSG_GPIO_I2C_DATA: {           // arg_int=0: Output low at I2C data pin
            gpio->write(info->pin_map.sda, arg_int);
            break;                            // arg_int=1: Input dir with pullup high for I2C data pin
        }
        default: {
            u8x8_SetGPIOResult(u8x8, 1);            // default return value
            break;
        }
    }
    return 1;
}

void initializeGpio(const std::shared_ptr<u8g2_info_t>& info, const std::shared_ptr<UcgGpioProvider>& gpio) {
    int& comm_int = info->comm_int;
    int& comm_type = info->comm_type;

    //Check if we are using Hardware or Software Implementation
    if (comm_type == COMTYPE_HW) {
        //We are on hardware mode, initialize the pins for hardware capability
        if (is_soc_raspberrypi()) {
            //Since this is a raspberry pi system, we will use the pigpio provider instead to initialize the hardware specific mode sets.
            //This assumes that you are using a Pi model with the Standard J8 header
            std::shared_ptr<UcgGpioProvider> pigpioGpio = info->providers[PROVIDER_PIGPIO]->getGpioProvider();

            //Are we on SPI mode or I2C?
            if (comm_int == COMINT_3WSPI || comm_int == COMINT_4WSPI || comm_int == COMINT_ST7920SPI) {
                int spi_peripheral = info->getOptionInt(OPT_SPI_PERIPHERAL);
                int spi_channel = info->getOptionInt(OPT_SPI_CHANNEL);

                if (spi_peripheral == SPI_PERIPHERAL_MAIN) {
                    pigpioGpio->init(SPI_RPI_PIN_MAIN_MISO, UcgGpioProvider::GpioMode::MODE_ALT0); //MISO
                    pigpioGpio->init(SPI_RPI_PIN_MAIN_MOSI, UcgGpioProvider::GpioMode::MODE_ALT0); //MOSI
                    pigpioGpio->init(SPI_RPI_PIN_MAIN_SCLK, UcgGpioProvider::GpioMode::MODE_ALT0); //SCLK
                    pigpioGpio->init(SPI_RPI_PIN_MAIN_CE1, UcgGpioProvider::GpioMode::MODE_ALT0); //CE1
                    pigpioGpio->init(SPI_RPI_PIN_MAIN_CE0, UcgGpioProvider::GpioMode::MODE_ALT0); //CE0
#ifdef DEBUG_UCGD
                    std::cout << "initializeGpio() : SPI Hardware pins initialized for Main Channel" << std::endl;
#endif
                } else if (spi_peripheral == SPI_PERIPHERAL_AUX) {
                    pigpioGpio->init(SPI_RPI_PIN_AUX_MISO, UcgGpioProvider::GpioMode::MODE_ALT4); //MISO
                    pigpioGpio->init(SPI_RPI_PIN_AUX_MOSI, UcgGpioProvider::GpioMode::MODE_ALT4); //MOSI
                    pigpioGpio->init(SPI_RPI_PIN_AUX_SCLK, UcgGpioProvider::GpioMode::MODE_ALT4); //SCLK
                    pigpioGpio->init(SPI_RPI_PIN_AUX_CE0, UcgGpioProvider::GpioMode::MODE_ALT4); //CE0
                    pigpioGpio->init(SPI_RPI_PIN_AUX_CE1, UcgGpioProvider::GpioMode::MODE_ALT4); //CE1
                    pigpioGpio->init(SPI_RPI_PIN_AUX_CE2, UcgGpioProvider::GpioMode::MODE_ALT4); //CE2
#ifdef DEBUG_UCGD
                    std::cout << "initializeGpio() : SPI Hardware pins initialized for Auxillary Channel" << std::endl;
#endif
                } else {
                    throw std::runtime_error("Unsupported SPI channel mode");
                }
            } else if (comm_int == COMINT_I2C) {
                pigpioGpio->init(I2C_RPI_PIN_SDA, UcgGpioProvider::GpioMode::MODE_ALT0); //Data / SDA
                pigpioGpio->init(I2C_RPI_PIN_SCL, UcgGpioProvider::GpioMode::MODE_ALT0); //Clock / SCL
            } else if (comm_int == COMINT_UART) {
                pigpioGpio->init(UART_RPI_PIN_TXD, UcgGpioProvider::GpioMode::MODE_ALT0); //Transmit / TXD
                pigpioGpio->init(UART_RPI_PIN_RXD, UcgGpioProvider::GpioMode::MODE_ALT0); //Receive / RXD
            }
            //Other implementations, just init everything to OUT
            else {
                initializeGpioAllOut(info, gpio);
            }
        }
#ifdef DEBUG_UCGD
        else {
            std::cerr << "Warning: Full compatibility for this SoC is unknown. No gpio pins have been pre-initialized by the native library." << std::endl;
        }
#endif
    }
    //Software Implementation - Make all GPIO output
    else if (info->comm_type == COMTYPE_SW) {
        std::cout << "Communication type is SOFTWARE" << std::endl;
        initializeGpioAllOut(info, gpio);
    } else {
        throw std::runtime_error("Invalid/Unrecognized comm type");
    }
}

void initializeGpioAllOut(const std::shared_ptr<u8g2_info_t>& info, const std::shared_ptr<UcgGpioProvider>& gpio) {
#ifdef DEBUG_UCGD
    std::cout << "Initializing all gpio lines to OUTPUT for Comm Int = " << std::to_string(info->comm_int) << ", Comm Type = " << std::to_string(info->comm_type) << std::endl;
#endif
    // called once during init phase of u8g2/u8x8, can be used to setup pins
    gpio->init(info->pin_map.d0, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.d1, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.sda, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.scl, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.d2, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.d3, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.d4, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.d5, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.d6, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.d7, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.dc, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.cs, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.cs1, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info->pin_map.cs2, UcgGpioProvider::GpioMode::MODE_OUTPUT);
}

void addToDeviceCache(u8g2_t *ptr, const std::shared_ptr<u8g2_info_t> &info) {
    u8g2_device_cache.insert(std::make_pair((uintptr_t) ptr, info));
}

std::shared_ptr<u8g2_info_t> U8g2Util_GetDisplayDeviceInfo(uintptr_t addr) {
    auto it = u8g2_device_cache.find(addr);
    if (it != u8g2_device_cache.end())
        return it->second;
    return nullptr;
}

uint8_t U8g2Util_ByteCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<u8g2_info_t> info = U8g2Util_GetDisplayDeviceInfo(addr);
    if (info == nullptr) {
        std::cerr << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        return 0;
    }
    return info->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t U8g2Util_GpioCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<u8g2_info_t> info = U8g2Util_GetDisplayDeviceInfo(addr);
    if (info == nullptr) {
        std::cerr << "[u8g2_setup_helper_byte] Unable to obtain display device info for address: " << std::to_string(addr) << std::endl;
        return 0;
    }
    return info->gpio_cb(u8x8, msg, arg_int, arg_ptr);
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