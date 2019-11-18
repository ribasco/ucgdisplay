#include "U8g2TestHal.h"

#include <iostream>
#include <unistd.h>
#include <iomanip>
#include <utility>
#include <Common.h>
#include <UcgdGpioPeripheral.h>
#include <UcgdSpiPeripheral.h>
#include <UcgdI2CPeripheral.h>

static std::shared_ptr<ucgd_t> u8g2_rpi_hal;
static std::map<uintptr_t, std::shared_ptr<ucgd_t>> u8g2_device_cache; // NOLINT
void initializeGpio(const std::shared_ptr<ucgd_t>& info, const std::shared_ptr<UcgdGpioPeripheral>& gpio);
void initializeGpioAllOut(const std::shared_ptr<ucgd_t>& info, const std::shared_ptr<UcgdGpioPeripheral>& gpio);
bool tryGetAndInitProvider(const std::shared_ptr<ucgd_t> &context, const std::string& providerName, std::shared_ptr<UcgdProvider>& provider);

uint8_t cb_byte_spi_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    std::shared_ptr<UcgdSpiPeripheral> spi = info->provider->getSpiProvider();

    switch (msg) {
        case U8X8_MSG_BYTE_INIT: {
            spi->open(info);
            break;
        }
        case U8X8_MSG_BYTE_SEND: {
            auto *buf = (uint8_t *) arg_ptr;
            spi->write(info, buf, arg_int);
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

uint8_t cb_byte_i2c_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    uint8_t *data;

    std::shared_ptr<UcgdI2CPeripheral> i2c = info->provider->getI2CProvider();

    switch (msg) {
        case U8X8_MSG_BYTE_INIT: {
            i2c->open(info);
            break;
        }
        case U8X8_MSG_BYTE_SEND: {
            i2c->write(info, u8x8_GetI2CAddress(u8x8), (uint8_t *) arg_ptr, arg_int);
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
uint8_t cb_gpio_delay(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {

    std::shared_ptr<UcgdGpioPeripheral> gpio = info->provider->getGpioProvider();

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

void initializeGpio(const std::shared_ptr<ucgd_t>& info, const std::shared_ptr<UcgdGpioPeripheral>& gpio) {
    Log log = ServiceLocator::getInstance().getLogger();

    int &comm_int = info->comm_int;
    int &comm_type = info->comm_type;

    log.debug("hal_init_gpio() : Detected System \"{}\"", get_soc_description());

    //Check if we are using Hardware or Software Implementation
    if (comm_type == COMTYPE_HW) {
        log.debug("hal_init_gpio() : Communication type is HARDWARE");

        //If this is a raspberry pi system, we will TRY use the pigpio provider to initialize the hardware specific mode sets.
        //This assumes that you are using a Pi model with the Standard J8 header (would not bother supporting the older versions)
        if (is_soc_raspberrypi()) {
            log.debug("hal_init_gpio() : Raspberry Pi system detected. Attempting to automatically configure pins for hardware capability");
            std::unique_ptr<ProviderManager> &pMan = ServiceLocator::getInstance().getProviderManager();

            //Try and retrieve provider for configuring special pin modes specific to the Raspberry Pi
            std::shared_ptr<UcgdProvider> pigpioProvider;

            if (gpio->getProvider()->getName() == PROVIDER_PIGPIO || gpio->getProvider()->getName() == PROVIDER_PIGPIOD) {
                log.debug("hal_init_gpio() : Using existing pigpio provider for configuring pin modes");
                pigpioProvider = pMan->getProvider(gpio->getProvider()->getName());
                if (pigpioProvider->isInitialized()) {
                    log.debug("YEAAA");
                }
            } else {
                //Attempt #1: Try Pigpio Daemon mode
                log.debug("hal_init_gpio() : Attempting to retrieve and connect to the Pigpio daemon...");
                bool havePigpiod = tryGetAndInitProvider(info, PROVIDER_PIGPIOD, pigpioProvider);

                if (!havePigpiod) {
                    //Attempt #2: Try Pigpio Standalone mode
                    log.debug("hal_init_gpio() : Looks like PIGPIO Daemon is unavailable, trying Pigpio standalone...");
                    bool havePigpio = tryGetAndInitProvider(info, PROVIDER_PIGPIO, pigpioProvider);

                    if (!havePigpio) {
                        //Attempt no fucks given: Do nothing
                        log.warn("hal_init_gpio() : Failed to retrieve/initialize both pigpio daemon and standalone. Falling back to default routine.");
                        return;
                    }
                }
            }

            std::shared_ptr<UcgdGpioPeripheral> pigpioGpio = pigpioProvider->getGpioProvider();

            //Check which hardware peripheral device we need to configure
            if (comm_int == COMINT_3WSPI || comm_int == COMINT_4WSPI || comm_int == COMINT_ST7920SPI) {
                int spi_bus_number = info->getOptionInt(OPT_SPI_BUS);
                if (spi_bus_number == SPI_PERIPHERAL_MAIN) {
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_MISO, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //MISO
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_MOSI, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //MOSI
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_SCLK, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //SCLK
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_CE1, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //CE1
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_CE0, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //CE0
                    log.debug("hal_init_gpio() : Pins initialized for MAIN SPI Hardware Peripheral");
                } else if (spi_bus_number == SPI_PERIPHERAL_AUX) {
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_MISO, UcgdGpioPeripheral::GpioMode::MODE_ALT4); //MISO
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_MOSI, UcgdGpioPeripheral::GpioMode::MODE_ALT4); //MOSI
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_SCLK, UcgdGpioPeripheral::GpioMode::MODE_ALT4); //SCLK
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_CE0, UcgdGpioPeripheral::GpioMode::MODE_ALT4); //CE0
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_CE1, UcgdGpioPeripheral::GpioMode::MODE_ALT4); //CE1
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_CE2, UcgdGpioPeripheral::GpioMode::MODE_ALT4); //CE2
                    log.debug("hal_init_gpio() : Pins initialized for AUXILLARY SPI Hardware Peripheral");
                } else {
                    throw std::runtime_error("hal_init_gpio() : SPI bus number not supported");
                }
            } else if (comm_int == COMINT_I2C) {
                pigpioGpio->init(info, I2C_RPI_PIN_SDA, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //Data / SDA
                pigpioGpio->init(info, I2C_RPI_PIN_SCL, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //Clock / SCL
            } else if (comm_int == COMINT_UART) {
                pigpioGpio->init(info, UART_RPI_PIN_TXD, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //Transmit / TXD
                pigpioGpio->init(info, UART_RPI_PIN_RXD, UcgdGpioPeripheral::GpioMode::MODE_ALT0); //Receive / RXD
            } else {
                //Other implementations, just init everything to OUT
                initializeGpioAllOut(info, gpio);
            }
        } else {
            log.warn("hal_init_gpio() : Unrecognized system. Using default provider for initializing all GPIO pins to OUT.");
            initializeGpioAllOut(info, gpio);
        }
    }
        //Software Implementation - Make all GPIO output
    else if (info->comm_type == COMTYPE_SW) {
        log.debug("hal_init_gpio() : Communication type is SOFTWARE");
        initializeGpioAllOut(info, gpio);
    } else {
        throw std::runtime_error("Invalid/Unrecognized comm type");
    }
}

/**
 * Initialize all gpio lines to OUTPUT
 * @param info The ucgdisplay descriptor
 * @param gpio The default gpio provider
 */
void initializeGpioAllOut(const std::shared_ptr<ucgd_t> &info, const std::shared_ptr<UcgdGpioPeripheral> &gpio) {
    Log &log = ServiceLocator::getInstance().getLogger();
    log.debug("Initializing all gpio lines to OUTPUT for Comm Int {}, Comm Type = {}, Provider: {}} ", info->comm_int, info->comm_type, gpio->getProvider()->getName());

    // called once during init phase of u8g2/u8x8, can be used to setup pins
    gpio->init(info, info->pin_map.d0, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d1, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.sda, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.scl, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d2, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d3, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d4, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d5, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d6, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d7, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.dc, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.cs, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.cs1, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.cs2, UcgdGpioPeripheral::GpioMode::MODE_OUTPUT);
}

bool tryGetAndInitProvider(const std::shared_ptr<ucgd_t> &context, const std::string& providerName, std::shared_ptr<UcgdProvider>& provider) {
    Log log = ServiceLocator::getInstance().getLogger();
    std::unique_ptr<ProviderManager> &pMan = ServiceLocator::getInstance().getProviderManager();

    //Try and retrieve provider for configuring special pin modes specific to the Raspberry Pi
    std::shared_ptr<UcgdProvider>& tmpProvider = pMan->getProvider(providerName);

    //Check if installed
    if (!tmpProvider->isAvailable()) {
        log.debug("tryGetAndInitProvider() : Provider '{}' not installed on the system", providerName);
        return false;
    }

    //Try to initialize
    if (!tmpProvider->isInitialized()) {
        try {
            tmpProvider->open(context);
        } catch (std::runtime_error& e) {
            log.warn("tryGetAndInitProvider() : Provider '{}' failed to start (Reason: {})", providerName, std::string(e.what()));
            return false;
        }
    }

    provider = tmpProvider;

    return true;
}

void addToDeviceCache(u8g2_t *ptr, const std::shared_ptr<ucgd_t> &info) {
    u8g2_device_cache.insert(std::make_pair((uintptr_t) ptr, info));
}

std::shared_ptr<ucgd_t> U8g2Util_GetDisplayDeviceInfo(uintptr_t addr) {
    auto it = u8g2_device_cache.find(addr);
    if (it != u8g2_device_cache.end())
        return it->second;
    return nullptr;
}

uint8_t ByteCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<ucgd_t>& info = ServiceLocator::getInstance().getDeviceManager()->getDevice(addr);
    return info->byte_cb(u8x8, msg, arg_int, arg_ptr);
}

uint8_t GpioCallbackWrapper(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    auto addr = (uintptr_t) u8x8;
    std::shared_ptr<ucgd_t>& info = ServiceLocator::getInstance().getDeviceManager()->getDevice(addr);
    return info->gpio_cb(u8x8, msg, arg_int, arg_ptr);
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

/*
 * ============================================================================================================
 *
 * Below are the wrapper functions for the built-in U8G2 bit-bang implementations (Hardware and Software)
 *
 * ============================================================================================================
 */

/**
 * Wrapper for 'u8x8_byte_sw_i2c'
 */
uint8_t cb_byte_sw_i2c(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_sw_i2c(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_4wire_sw_spi'
 */
uint8_t cb_byte_4wire_sw_spi(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_4wire_sw_spi(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_3wire_sw_spi'
 */
uint8_t cb_byte_3wire_sw_spi(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_3wire_sw_spi(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_8bit_6800mode'
 */
uint8_t cb_byte_8bit_6800mode(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_8bit_6800mode(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_8bit_8080mode'
 */
uint8_t cb_byte_8bit_8080mode(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_8bit_8080mode(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_ks0108'
 */
uint8_t cb_byte_ks0108(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_ks0108(u8x8, msg, arg_int, arg_ptr);
}

/**
 * Wrapper for 'u8x8_byte_sed1520'
 */
uint8_t cb_byte_sed1520(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return u8x8_byte_sed1520(u8x8, msg, arg_int, arg_ptr);
}