//
// Created by raffy on 7/4/18.
//

#include "U8g2Hal.h"

#include "Global.h"
#include "CommSpi.h"
#include <wiringPi.h>
#include <iostream>
#include <cstring>
#include <algorithm>
#include <utility>

using namespace std;

#define U8G2_HAL_SPI_SPEED 500000
#define U8G2_HAL_SPI_MODE SPI_DEV_MODE_0
#define U8G2_HAL_SPI_CHANNEL 0

static bool initialized = false;

static u8g2_setup_func_map_t u8g2_setup_functions; //NOLINT

static u8g2_lookup_font_map_t u8g2_font_map; //NOLINT

void u8g2hal_init() {
    cout << "Initializing U8G2 HAL" << endl;
    u8g2hal_init_setupfunctions(u8g2_setup_functions);
    u8g2hal_init_fonts(u8g2_font_map);
}

u8g2_setup_func_t u8g2hal_get_setupproc(const std::string &function_name) {
    if (u8g2_setup_functions.empty())
        return nullptr;
    auto it = u8g2_setup_functions.find(function_name);
    if (it != u8g2_setup_functions.end()) {
        return it->second;
    }
    return nullptr;
}

uint8_t *u8g2hal_get_fontbyname(const std::string &font_name) {
    if (u8g2_font_map.empty())
        return nullptr;
    auto it = u8g2_font_map.find(font_name);
    if (it != u8g2_font_map.end()) {
        return const_cast<uint8_t *>(it->second);
    }
    return nullptr;
}

#if defined(__arm__)

/**
 * SPI Hardware Callback Routine (ARM)
 */
uint8_t cb_rpi_byte_spi_hw(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            if (spi_write(U8G2_HAL_SPI_CHANNEL, (uint8_t *) arg_ptr, arg_int) < 1)
                cerr << "Unable to send data" << endl;
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            cout << "[SPI]: Initialize" << endl;

            //disable chip-select
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);

            //initialize spi device
            int fd = spi_setup(U8G2_HAL_SPI_CHANNEL, U8G2_HAL_SPI_SPEED, U8G2_HAL_SPI_MODE);
            if (fd < 0) {
                cerr << "Problem initializing SPI on RPI" << endl;
                return 0;
            }

            //IMPORTANT: Make sure we reset the pin modes to activate the SPI hardware features!!!!
            pinModeAlt(info->pin_map.d1_spi_data, 0b100);
            pinModeAlt(info->pin_map.d0_spi_clock, 0b100);
            pinModeAlt(info->pin_map.cs, 0b100);
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
        default:
            return 0;
    }
    return 1;
}

/**
 * I2C Hardware Callback Routine for Raspberry Pi
 */
uint8_t cb_rpi_byte_i2c_hw(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    static uint8_t buffer[32];        /* u8g2/u8x8 will never send more than 32 bytes between START_TRANSFER and END_TRANSFER */
    static uint8_t buf_idx;
    uint8_t *data;

    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            data = (uint8_t *) arg_ptr;
            while (arg_int > 0) {
                buffer[buf_idx++] = *data;
                data++;
                arg_int--;
            }
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            //disable chip-select
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);

            //initialize i2c device here

            //IMPORTANT: Make sure we reset the pin modes to activate the SPI hardware features!!!!
            /*pinModeAlt(u8g2_rpi_hal.sda, 0b100);
            pinModeAlt(u8g2_rpi_hal.scl, 0b100);*/
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
        default:
            return 0;
    }
    return 1;
}

/**
 * GPIO and Delay Procedure Routine for Raspberry Pi
*/
uint8_t cb_rpi_gpio_delay(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: { // called once during init phase of u8g2/u8x8, can be used to setup pins
            if (!initialized) {
                //Initialize WiringPi from here
                int retval = wiringPiSetup();
                if (retval < 0) {
                    cerr << "Unable to initialize Wiring Pi: " << strerror(errno) << endl;
                    exit(-1);
                }
                initialized = true;
            }

            cout << "[GPIO]: Initialize" << endl;
            //Configure pin modes
            pinMode(info->pin_map.d1_spi_data, OUTPUT);
            pinMode(info->pin_map.d0_spi_clock, OUTPUT);
            pinMode(info->pin_map.cs, OUTPUT);

            cout << "[GPIO] Init Success (MOSI: " << to_string(info->pin_map.d1_spi_data) << ", CLOCK: " << to_string(info->pin_map.d1_spi_data) << ", CS: " << to_string(info->pin_map.cs) << ")" << endl;
            break;
        }
        case U8X8_MSG_DELAY_NANO: { // delay arg_int * 1 nano second
            //this is important. Removing this will cause garbage data to be displayed.
            delayMicroseconds(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_100NANO: {       // delay arg_int * 100 nano seconds
            delayMicroseconds(arg_int == 0 ? 0 : 1);
            break;
        }
        case U8X8_MSG_DELAY_10MICRO: {       // delay arg_int * 10 micro seconds
            delayMicroseconds(arg_int);
            break;
        }
        case U8X8_MSG_DELAY_MILLI: {           // delay arg_int * 1 milli second
            delay(arg_int);
            break;
        }
        case U8X8_MSG_DELAY_I2C: {               // arg_int is the I2C speed in 100KHz, e.g. 4 = 400 KHz
            delayMicroseconds(arg_int);          // arg_int=1: delay by 5us, arg_int = 4: delay by 1.25us
            break;
        }
        case U8X8_MSG_GPIO_D0: {                // D0 or SPI clock pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_CLOCK)
            digitalWrite(info->pin_map.d0_spi_clock, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D1: {                // D1 or SPI data pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_DATA)
            digitalWrite(info->pin_map.d1_spi_data, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D2: {                // D2 pin: Output level in arg_int
            digitalWrite(info->pin_map.d2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D3: {                // D3 pin: Output level in arg_int
            digitalWrite(info->pin_map.d3, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D4: {                // D4 pin: Output level in arg_int
            digitalWrite(info->pin_map.d4, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D5: {                // D5 pin: Output level in arg_int
            digitalWrite(info->pin_map.d5, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D6: {                // D6 pin: Output level in arg_int
            digitalWrite(info->pin_map.d6, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_D7: {                // D7 pin: Output level in arg_int
            digitalWrite(info->pin_map.d7, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_E: {               // E/WR pin: Output level in arg_int
            digitalWrite(info->pin_map.en, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS: {                // CS (chip select) pin: Output level in arg_int
            digitalWrite(info->pin_map.cs, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_DC: {                // DC (data/cmd, A0, register select) pin: Output level in arg_int
            digitalWrite(info->pin_map.dc, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_RESET: {            // Reset pin: Output level in arg_int
            digitalWrite(info->pin_map.reset, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS1: {                // CS1 (chip select) pin: Output level in arg_int
            digitalWrite(info->pin_map.cs1, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_CS2: {                // CS2 (chip select) pin: Output level in arg_int
            digitalWrite(info->pin_map.cs2, arg_int);
            break;
        }
        case U8X8_MSG_GPIO_I2C_CLOCK: {        // arg_int=0: Output low at I2C clock pin
            digitalWrite(info->pin_map.scl, arg_int);
            break;                            // arg_int=1: Input dir with pullup high for I2C clock pin
        }
        case U8X8_MSG_GPIO_I2C_DATA: {           // arg_int=0: Output low at I2C data pin
            digitalWrite(info->pin_map.sda, arg_int);
            break;                            // arg_int=1: Input dir with pullup high for I2C data pin
        }
        default: {
            u8x8_SetGPIOResult(u8x8, 1);            // default return value
            break;
        }
    }
    return 1;
}

#elif defined(__linux__) && (defined(__x86_64__) || defined(i386))

/**
 * SPI Callback Routine (x86 simulation)
 */
uint8_t cb_rpi_byte_spi_hw(u8g2_info_t* info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            cout << "[SPI] U8X8_MSG_BYTE_SEND" << endl;
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            //disable chip-select
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);
            cout << "[SPI] U8X8_MSG_BYTE_INIT" << endl;
            break;
        }
        case U8X8_MSG_BYTE_START_TRANSFER: {
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_enable_level);
            u8x8->gpio_and_delay_cb(u8x8, U8X8_MSG_DELAY_NANO, u8x8->display_info->post_chip_enable_wait_ns, nullptr);
            cout << "[SPI] U8X8_MSG_BYTE_START_TRANSFER" << endl;
            break;
        }
        case U8X8_MSG_BYTE_END_TRANSFER: {
            u8x8->gpio_and_delay_cb(u8x8, U8X8_MSG_DELAY_NANO, u8x8->display_info->pre_chip_disable_wait_ns, nullptr);
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);
            cout << "[SPI] U8X8_MSG_BYTE_END_TRANSFER" << endl;
            break;
        }
        default:
            return 0;
    }
    return 1;
}

/**
 * I2C Callback Routine (x86 simulation)
 */
uint8_t cb_rpi_byte_i2c_hw(u8g2_info_t* info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            cout << "U8X8_MSG_BYTE_SEND" << endl;
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            //disable chip-select
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);
            cout << "U8X8_MSG_BYTE_INIT" << endl;
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
        default:
            return 0;
    }
    return 1;
}

/**
 * GPIO and Delay Routine (x86 simulation)
 */
uint8_t cb_rpi_gpio_delay(u8g2_info_t* info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {

    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: {
            cout << "U8X8_MSG_GPIO_AND_DELAY_INIT" << endl;
            break;
        }
        case U8X8_MSG_DELAY_MILLI: {
            cout << "U8X8_MSG_DELAY_MILLI" << endl;
            break;
        }
        case U8X8_MSG_DELAY_NANO: {
            //this is important. Removing this will cause garbage data to be displayed.
            cout << "U8X8_MSG_DELAY_NANO" << endl;
            break;
        }
        case U8X8_MSG_GPIO_SPI_CLOCK: {
            cout << "U8X8_MSG_GPIO_SPI_CLOCK" << endl;
            break;
        }
        case U8X8_MSG_GPIO_SPI_DATA: {
            cout << "U8X8_MSG_GPIO_SPI_DATA" << endl;
            break;
        }
        case U8X8_MSG_GPIO_CS: {
            cout << "U8X8_MSG_GPIO_CS" << endl;
            break;
        }
        default: {
            return 0;
        }
    }
    return 1;
}
#endif