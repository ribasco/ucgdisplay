/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2Hal.cpp
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

#include <unistd.h>
#include <iomanip>
#include <Global.h>
#include <UcgdConfig.h>
#include <U8g2Hal.h>
#include <Common.h>

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

#include <ProviderManager.h>
#include <UcgSpiProvider.h>
#include <UcgI2CProvider.h>
#include <UcgGpioProvider.h>
#include <system_error>

#endif

static u8g2_setup_func_map_t u8g2_setup_functions; //NOLINT
static u8g2_lookup_font_map_t u8g2_font_map; //NOLINT

void initializeGpio(const std::shared_ptr<ucgd_t> &info, const std::shared_ptr<UcgGpioProvider> &gpio);

void initializeGpioAllOut(const std::shared_ptr<ucgd_t> &info, const std::shared_ptr<UcgGpioProvider> &gpio);

bool tryGetAndInitProvider(const std::shared_ptr<ucgd_t> &context, const std::string& providerName, std::shared_ptr<UcgIOProvider>& provider);

/**
 * Initialize the HAL
 */
void U8g2hal_Init() {
    //Initialize lookup tables
    U8g2hal_InitSetupFunctions(u8g2_setup_functions);
    U8g2hal_InitFonts(u8g2_font_map);
}

/**
 * Returns the u8g2 setup callback function based on the provided name
 *
 * @param function_name The name of the u8g2 setup procedure
 * @return The setup callback function
 */
u8g2_setup_func_t &U8g2Hal_GetSetupProc(const std::string &function_name) {
    if (u8g2_setup_functions.empty())
        throw SetupProcNotFoundException(std::string("U8g2hal_GetSetupProc : Could not find setup procedure '") + function_name + std::string("'"));
    auto it = u8g2_setup_functions.find(function_name);
    if (it != u8g2_setup_functions.end()) {
        return it->second;
    }
    throw SetupProcNotFoundException(std::string("U8g2hal_GetSetupProc : Could not find setup procedure '") + function_name + std::string("'"));
}

/**
 * Retrieve the font data based on the provided argument
 * @param font_name The u8g2 font name
 * @return Buffer containing the actual font data
 */
uint8_t *U8g2hal_GetFontByName(const std::string &font_name) {
    if (u8g2_font_map.empty())
        return nullptr;
    auto it = u8g2_font_map.find(font_name);
    if (it != u8g2_font_map.end()) {
        return const_cast<uint8_t *>(it->second);
    }
    return nullptr;
}

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)

/**
 * 4-wire SPI Hardware Callback Routine (ARM)
 */
uint8_t cb_byte_spi_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {

    const std::shared_ptr<UcgSpiProvider>& spi = info->provider->getSpiProvider();

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

/**
 * I2C Hardware Callback Routine (ARM)
 */
uint8_t cb_byte_i2c_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    uint8_t *data;
    const std::shared_ptr<UcgI2CProvider> &i2c = info->provider->getI2CProvider();

    switch (msg) {
        case U8X8_MSG_BYTE_INIT: {
            i2c->open(info);
            break;
        }
        case U8X8_MSG_BYTE_SEND: {
            data = (uint8_t *) arg_ptr;
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
    std::shared_ptr<UcgGpioProvider> gpio = info->provider->getGpioProvider();

    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: {
            initializeGpio(info, gpio);
            break;
        }
        case U8X8_MSG_DELAY_NANO: {                     // delay arg_int * 1 nano second
            usleep(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_100NANO: {                  // delay arg_int * 100 nano seconds
            usleep(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_10MICRO: {                  // delay arg_int * 10 micro seconds
            usleep(arg_int * 10);
            break;
        }
        case U8X8_MSG_DELAY_MILLI: {                    // delay arg_int * 1 milli second
            usleep(arg_int * 1000);
            break;
        }
        case U8X8_MSG_DELAY_I2C: {                      // arg_int is the I2C speed in 100KHz, e.g. 4 = 400 KHz
            usleep(arg_int);                            // arg_int=1: delay by 5us, arg_int = 4: delay by 1.25us
            break;
        }
        case U8X8_MSG_GPIO_D0: {                        // D0 or SPI clock pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_CLOCK)
            gpio->write(info->pin_map.d0, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D1: {                        // D1 or SPI data pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_DATA)
            gpio->write(info->pin_map.d1, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D2: {                        // D2 pin: Output level in arg_int
            gpio->write(info->pin_map.d2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D3: {                        // D3 pin: Output level in arg_int
            gpio->write(info->pin_map.d3, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D4: {                        // D4 pin: Output level in arg_int
            gpio->write(info->pin_map.d4, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D5: {                        // D5 pin: Output level in arg_int
            gpio->write(info->pin_map.d5, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D6: {                        // D6 pin: Output level in arg_int
            gpio->write(info->pin_map.d6, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D7: {                        // D7 pin: Output level in arg_int
            gpio->write(info->pin_map.d7, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_E: {                         // E/WR pin: Output level in arg_int
            gpio->write(info->pin_map.en, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS: {                        // CS (chip select) pin: Output level in arg_int
            gpio->write(info->pin_map.cs, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_DC: {                        // DC (data/cmd, A0, register select) pin: Output level in arg_int
            gpio->write(info->pin_map.dc, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_RESET: {                     // Reset pin: Output level in arg_int
            gpio->write(info->pin_map.reset, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS1: {                       // CS1 (chip select) pin: Output level in arg_int
            gpio->write(info->pin_map.cs1, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS2: {                       // CS2 (chip select) pin: Output level in arg_int
            gpio->write(info->pin_map.cs2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_I2C_CLOCK: {                 // arg_int=0: Output low at I2C clock pin
            gpio->write(info->pin_map.scl, arg_int);
            break;                                      // arg_int=1: Input dir with pullup high for I2C clock pin
        }
        case U8X8_MSG_GPIO_I2C_DATA: {                  // arg_int=0: Output low at I2C data pin
            gpio->write(info->pin_map.sda, arg_int);
            break;                                      // arg_int=1: Input dir with pullup high for I2C data pin
        }
        default: {
            u8x8_SetGPIOResult(u8x8, 1);            // default return value
            break;
        }
    }
    return 1;
}

/**
 * Perform special initialization procedures for supported SoC devices
 * @param info The ucgdisplay descriptor
 * @param gpio The default gpio provider
 */
void initializeGpio(const std::shared_ptr<ucgd_t> &info, const std::shared_ptr<UcgGpioProvider> &gpio) {
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
            std::shared_ptr<UcgIOProvider> pigpioProvider;

            //If the user selected pigpio as the default provider, it should already be initialized
            if (gpio->getProvider()->getName() == PROVIDER_PIGPIO || gpio->getProvider()->getName() == PROVIDER_PIGPIOD) {
                log.debug("hal_init_gpio() : Using existing pigpio provider for configuring pin modes");
                pigpioProvider = pMan->getProvider(gpio->getProvider()->getName());
            } else {
                //Attempt #1: Try Pigpio Daemon mode
                log.debug("hal_init_gpio() : Attempting to retrieve and connect to the Pigpio daemon...");

                if (!tryGetAndInitProvider(info, PROVIDER_PIGPIOD, pigpioProvider)) {
                    //Attempt #2: Try Pigpio Standalone mode
                    log.debug("hal_init_gpio() : Looks like PIGPIO Daemon is unavailable, trying Pigpio standalone...");

                    if (!tryGetAndInitProvider(info, PROVIDER_PIGPIO, pigpioProvider)) {
                        //Attempt no fucks given: Do nothing
                        log.warn("hal_init_gpio() : Failed to retrieve/initialize both pigpio daemon and standalone. Falling back to default routine.");
                        return;
                    }
                }
            }

            std::shared_ptr<UcgGpioProvider> pigpioGpio = pigpioProvider->getGpioProvider();

            //Check which hardware peripheral device we need to configure
            if (comm_int == COMINT_3WSPI || comm_int == COMINT_4WSPI || comm_int == COMINT_ST7920SPI) {
                int spi_bus_number = info->getOptionInt(OPT_SPI_BUS);
                if (spi_bus_number == SPI_PERIPHERAL_MAIN) {
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_MISO, UcgGpioProvider::GpioMode::MODE_ALT0); //MISO
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_MOSI, UcgGpioProvider::GpioMode::MODE_ALT0); //MOSI
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_SCLK, UcgGpioProvider::GpioMode::MODE_ALT0); //SCLK
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_CE1, UcgGpioProvider::GpioMode::MODE_ALT0); //CE1
                    pigpioGpio->init(info, SPI_RPI_PIN_MAIN_CE0, UcgGpioProvider::GpioMode::MODE_ALT0); //CE0
                    log.debug("hal_init_gpio() : Pins initialized for MAIN SPI Hardware Peripheral");
                } else if (spi_bus_number == SPI_PERIPHERAL_AUX) {
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_MISO, UcgGpioProvider::GpioMode::MODE_ALT4); //MISO
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_MOSI, UcgGpioProvider::GpioMode::MODE_ALT4); //MOSI
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_SCLK, UcgGpioProvider::GpioMode::MODE_ALT4); //SCLK
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_CE0, UcgGpioProvider::GpioMode::MODE_ALT4); //CE0
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_CE1, UcgGpioProvider::GpioMode::MODE_ALT4); //CE1
                    pigpioGpio->init(info, SPI_RPI_PIN_AUX_CE2, UcgGpioProvider::GpioMode::MODE_ALT4); //CE2
                    log.debug("hal_init_gpio() : Pins initialized for AUXILLARY SPI Hardware Peripheral");
                } else {
                    throw std::runtime_error("hal_init_gpio() : SPI bus number not supported");
                }
            } else if (comm_int == COMINT_I2C) {
                pigpioGpio->init(info, I2C_RPI_PIN_SDA, UcgGpioProvider::GpioMode::MODE_ALT0); //Data / SDA
                pigpioGpio->init(info, I2C_RPI_PIN_SCL, UcgGpioProvider::GpioMode::MODE_ALT0); //Clock / SCL
            } else if (comm_int == COMINT_UART) {
                pigpioGpio->init(info, UART_RPI_PIN_TXD, UcgGpioProvider::GpioMode::MODE_ALT0); //Transmit / TXD
                pigpioGpio->init(info, UART_RPI_PIN_RXD, UcgGpioProvider::GpioMode::MODE_ALT0); //Receive / RXD
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
void initializeGpioAllOut(const std::shared_ptr<ucgd_t> &info, const std::shared_ptr<UcgGpioProvider> &gpio) {
    Log &log = ServiceLocator::getInstance().getLogger();
    log.debug("Initializing all gpio lines to OUTPUT for Comm Int = {}, Comm Type = {}, Provider: {} ", info->comm_int, info->comm_type, gpio->getProvider()->getName());

    // called once during init phase of u8g2/u8x8, can be used to setup pins
    gpio->init(info, info->pin_map.d0, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d1, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.sda, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.scl, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d2, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d3, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d4, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d5, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d6, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.d7, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.dc, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.cs, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.cs1, UcgGpioProvider::GpioMode::MODE_OUTPUT);
    gpio->init(info, info->pin_map.cs2, UcgGpioProvider::GpioMode::MODE_OUTPUT);
}

bool tryGetAndInitProvider(const std::shared_ptr<ucgd_t> &context, const std::string& providerName, std::shared_ptr<UcgIOProvider>& provider) {
    Log log = ServiceLocator::getInstance().getLogger();
    std::unique_ptr<ProviderManager> &pMan = ServiceLocator::getInstance().getProviderManager();

    //Try and retrieve provider for configuring special pin modes specific to the Raspberry Pi
    std::shared_ptr<UcgIOProvider>& tmpProvider = pMan->getProvider(providerName);

    //Check if installed
    if (!tmpProvider->isAvailable()) {
        log.debug("tryGetAndInitProvider() : Provider '{}' not installed on the system", providerName);
        return false;
    }

    //Try to initialize
    if (!tmpProvider->isInitialized()) {
        try {
            tmpProvider->initialize(context);
        } catch (std::runtime_error& e) {
            log.warn("tryGetAndInitProvider() : Provider '{}' failed to start (Reason: {})", providerName, std::string(e.what()));
            return false;
        }
    }

    provider = tmpProvider;

    return true;
}

#else

/**
 * SPI Callback Routine (virtual mode)
 */
uint8_t cb_byte_spi_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return cb_byte_4wire_sw_spi(info, u8x8, msg, arg_int, arg_ptr);
}

/**
 * HW I2C Wrapper. Uses software bit-bang implementation (virtual mode)
 */
uint8_t cb_byte_i2c_hw(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    return cb_byte_sw_i2c(info, u8x8, msg, arg_int, arg_ptr);
}

/**
 * GPIO and Delay Routine (virtual mode)
 */
uint8_t cb_gpio_delay(const std::shared_ptr<ucgd_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: { // called once during init phase of u8g2/u8x8, can be used to setup pins
            //cout << "U8X8_MSG_GPIO_AND_DELAY_INIT" << endl;
            break;
        }
        case U8X8_MSG_DELAY_NANO: { // delay arg_int * 1 nano second
            //cout << "U8X8_MSG_DELAY_NANO" << endl;
            break;
        }
        case U8X8_MSG_DELAY_100NANO: {       // delay arg_int * 100 nano seconds
            //cout << "U8X8_MSG_DELAY_100NANO" << endl;
            break;
        }
        case U8X8_MSG_DELAY_10MICRO: {       // delay arg_int * 10 micro seconds
            //cout << "U8X8_MSG_DELAY_10MICRO" << endl;
            break;
        }
        case U8X8_MSG_DELAY_MILLI: {           // delay arg_int * 1 milli second
            //cout << "U8X8_MSG_DELAY_MILLI" << endl;
            break;
        }
        case U8X8_MSG_DELAY_I2C: {                                  // arg_int is the I2C speed in 100KHz, e.g. 4 = 400 KHz
            //cout << "U8X8_MSG_DELAY_I2C" << endl;          // arg_int=1: delay by 5us, arg_int = 4: delay by 1.25us
            break;
        }
        case U8X8_MSG_GPIO_D0: {                // D0 or SPI clock pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_CLOCK)
            //cout << "U8X8_MSG_GPIO_D0" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D1: {                // D1 or SPI data pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_DATA)
            //cout << "\t- [Bit]: " << to_string(arg_int) << flush << endl;
            break;
        }
        case U8X8_MSG_GPIO_D2: {                // D2 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D2" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D3: {                // D3 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D3" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D4: {                // D4 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D4" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D5: {                // D5 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D5" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D6: {                // D6 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D6" << endl;
            break;
        }
        case U8X8_MSG_GPIO_D7: {                // D7 pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_D7" << endl;
            break;
        }
        case U8X8_MSG_GPIO_E: {               // E/WR pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_E" << endl;
            break;
        }
        case U8X8_MSG_GPIO_CS: {                // CS (chip select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_CS" << endl;
            break;
        }
        case U8X8_MSG_GPIO_DC: {                // DC (data/cmd, A0, register select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_DC" << endl;
            break;
        }
        case U8X8_MSG_GPIO_RESET: {            // Reset pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_RESET" << endl;
            break;
        }
        case U8X8_MSG_GPIO_CS1: {                // CS1 (chip select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_CS1" << endl;
            break;
        }
        case U8X8_MSG_GPIO_CS2: {                // CS2 (chip select) pin: Output level in arg_int
            //cout << "U8X8_MSG_GPIO_CS2" << endl;
            break;
        }
        case U8X8_MSG_GPIO_I2C_CLOCK: {        // arg_int=0: Output low at I2C clock pin
            //cout << "U8X8_MSG_GPIO_I2C_CLOCK" << endl;
            break;                            // arg_int=1: Input dir with pullup high for I2C clock pin
        }
        case U8X8_MSG_GPIO_I2C_DATA: {           // arg_int=0: Output low at I2C data pin
            //cout << "U8X8_MSG_GPIO_I2C_DATA" << endl;
            break;                            // arg_int=1: Input dir with pullup high for I2C data pin
        }
        default: {
            //u8x8_SetGPIOResult(u8x8, 1);            // default return value
            break;
        }
    }

    return 1;
}

#endif

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
