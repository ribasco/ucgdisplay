#include "U8g2TestHal.h"

#include <iostream>
#include <spi.h>
#include <i2c.h>
#include <gpiod.hpp>
#include <unistd.h>
#include <iomanip>
#include <utility>

#define DEFAULT_SPI_SPEED 1000000

static std::shared_ptr<u8g2_info_t> u8g2_rpi_hal;

using namespace std;

void u8g2_rpi_hal_init(std::shared_ptr<u8g2_info_t> param) {
    u8g2_rpi_hal = std::move(param);
}

gpiod::line* get_gpio_line_ex(const std::shared_ptr<u8g2_info_t>& info, int pin) {
    auto res = info->gpio_tmp.find(pin);
    gpiod::line* gpio_line = nullptr;
    if (res != info->gpio_tmp.end()) {
        gpio_line = &res->second;
    }
    return gpio_line;
}

void gpio_line_init(const std::shared_ptr<u8g2_info_t> &info, int pin, int direction) {
    try {
        //Do not process unassinged
        if (pin <= -1)
            return;

        if (info->gpio_chip == nullptr) {
            std::cerr << "> GPIO chip has not yet been initialized";
            return;
        }

        gpiod::line* line = get_gpio_line_ex(info, pin);

        if (line == nullptr) {
            auto it = info->gpio_tmp.insert(std::make_pair(pin, info->gpio_chip->get_line(pin)));
            line = &it.first->second;
        }

        //Release if used
        if (line->is_used()) {
            std::cout << "> GPIO Line " << std::to_string(pin) << " currently in-use. Releasing." << std::endl;
            line->release();
            std::cout << "> GPIO Line " << std::to_string(pin) << " released." << std::endl;
        }

        line->request({"ucgtest", direction, 0});
        std::cout << "> Aquired GPIO line: " << std::to_string(pin) << ", Direction: " << std::to_string(direction) << ", Name: " << line->name() << ", Address: " << std::to_string((uintptr_t )line) << std::endl;
    } catch (const std::system_error &e) {
        std::cerr << "> GPIO line initialization failed (Chip: " << info->gpio_device << ", Line: " << std::to_string(pin) << ", Code: " << e.code() << ", Reason: " << e.what() << ")" << std::endl;
        throw e;
    }
}

void digital_write(const std::shared_ptr<u8g2_info_t> &info, int pin, uint8_t value, const std::string& tag = "") {
    try {
        gpiod::line* line = get_gpio_line_ex(info, pin);
        if (line == nullptr) {
            std::cerr << "GPIO Line not initialized: " << std::to_string(pin) << "(" << tag << ")" << std::endl;
            return;
        }
        line->set_value(value);
    } catch (const std::system_error &e) {
        std::cerr << "Unable to write value to gpio line (Line: " << std::to_string(pin) << ", Code: " << e.code() << ", Reason: " << e.what() << ")" << std::endl;
        throw e;
    }
}

uint8_t cb_byte_spi_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            auto *buf = (uint8_t *) arg_ptr;
            if (spi_transfer(info->spi.get(), buf, buf, arg_int) < 0) {
                fprintf(stderr, "spi_transfer(): %s\n", spi_errmsg(info->spi.get()));
                return 0;
            }
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            std::cout << "Initializing SPI device" << std::endl;
            //disable chip-select
            u8x8_gpio_SetCS(u8x8, u8x8->display_info->chip_disable_level);
            int speed = info->device_speed <= -1 ? DEFAULT_SPI_SPEED : info->device_speed;
            std::cout << "Opening SPI device: " << info->transport_device << std::endl;
            if (spi_open(info->spi.get(), info->transport_device.c_str(), 0, speed) < 0) {
                fprintf(stderr, "spi_open(): %s\n", spi_errmsg(info->spi.get()));
                return 0;
            }
            std::cout << "Successfully initialized SPI device" << std::endl;
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

void pigpio_gpio_init(const std::shared_ptr<u8g2_info_t> &info) {

}

void libgpiod_gpio_init(const std::shared_ptr<u8g2_info_t> &info) {
    gpio_line_init(info, info->pin_map.d0, gpiod::line_request::DIRECTION_AS_IS);
    gpio_line_init(info, info->pin_map.d1, gpiod::line_request::DIRECTION_AS_IS);
    gpio_line_init(info, info->pin_map.sda, gpiod::line_request::DIRECTION_AS_IS);
    gpio_line_init(info, info->pin_map.scl, gpiod::line_request::DIRECTION_AS_IS);
    gpio_line_init(info, info->pin_map.d2, gpiod::line_request::DIRECTION_OUTPUT);
    gpio_line_init(info, info->pin_map.d3, gpiod::line_request::DIRECTION_OUTPUT);
    gpio_line_init(info, info->pin_map.d4, gpiod::line_request::DIRECTION_OUTPUT);
    gpio_line_init(info, info->pin_map.d5, gpiod::line_request::DIRECTION_OUTPUT);
    gpio_line_init(info, info->pin_map.d6, gpiod::line_request::DIRECTION_OUTPUT);
    gpio_line_init(info, info->pin_map.d7, gpiod::line_request::DIRECTION_OUTPUT);
    gpio_line_init(info, info->pin_map.dc, gpiod::line_request::DIRECTION_OUTPUT);

    gpio_line_init(info, info->pin_map.cs, gpiod::line_request::DIRECTION_AS_IS);
    gpio_line_init(info, info->pin_map.cs1, gpiod::line_request::DIRECTION_AS_IS);
    gpio_line_init(info, info->pin_map.cs2, gpiod::line_request::DIRECTION_AS_IS);
}

/**
 * GPIO and Delay Procedure Routine (ARM)
*/
uint8_t cb_gpio_delay(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr) {
    switch (msg) {
        case U8X8_MSG_GPIO_AND_DELAY_INIT: { // called once during init phase of u8g2/u8x8, can be used to setup pins
            std::cout << "> Initializing GPIO lines" << std::endl;
            //Configure pin modes
            libgpiod_gpio_init(info);
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
            digital_write(info, info->pin_map.d0, arg_int, "D0");
            break;
        }
        case U8X8_MSG_GPIO_D1: {                // D1 or SPI data pin: Output level in arg_int (U8X8_MSG_GPIO_SPI_DATA)
            digital_write(info, info->pin_map.d1, arg_int, "D1");
            break;
        }
        case U8X8_MSG_GPIO_D2: {                // D2 pin: Output level in arg_int
            digital_write(info, info->pin_map.d2, arg_int, "D2");
            break;
        }
        case U8X8_MSG_GPIO_D3: {                // D3 pin: Output level in arg_int
            digital_write(info, info->pin_map.d3, arg_int, "D3");
            break;
        }
        case U8X8_MSG_GPIO_D4: {                // D4 pin: Output level in arg_int
            digital_write(info, info->pin_map.d4, arg_int, "D4");
            break;
        }
        case U8X8_MSG_GPIO_D5: {                // D5 pin: Output level in arg_int
            digital_write(info, info->pin_map.d5, arg_int, "D5");
            break;
        }
        case U8X8_MSG_GPIO_D6: {                // D6 pin: Output level in arg_int
            digital_write(info, info->pin_map.d6, arg_int, "D6");
            break;
        }
        case U8X8_MSG_GPIO_D7: {                // D7 pin: Output level in arg_int
            digital_write(info, info->pin_map.d7, arg_int, "D7");
            break;
        }
        case U8X8_MSG_GPIO_E: {               // E/WR pin: Output level in arg_int
            digital_write(info, info->pin_map.en, arg_int, "E/WR");
            break;
        }
        case U8X8_MSG_GPIO_CS: {                // CS (chip select) pin: Output level in arg_int
            digital_write(info, info->pin_map.cs, arg_int, "CS");
            break;
        }
        case U8X8_MSG_GPIO_DC: {                // DC (data/cmd, A0, register select) pin: Output level in arg_int
            digital_write(info, info->pin_map.dc, arg_int, "DC");
            break;
        }
        case U8X8_MSG_GPIO_RESET: {            // Reset pin: Output level in arg_int
            digital_write(info, info->pin_map.reset, arg_int, "RST");
            break;
        }
        case U8X8_MSG_GPIO_CS1: {                // CS1 (chip select) pin: Output level in arg_int
            digital_write(info, info->pin_map.cs1, arg_int, "CS1");
            break;
        }
        case U8X8_MSG_GPIO_CS2: {                // CS2 (chip select) pin: Output level in arg_int
            digital_write(info, info->pin_map.cs2, arg_int, "CS2");
            break;
        }
        case U8X8_MSG_GPIO_I2C_CLOCK: {        // arg_int=0: Output low at I2C clock pin
            digital_write(info, info->pin_map.scl, arg_int, "I2C_CLK");
            break;                            // arg_int=1: Input dir with pullup high for I2C clock pin
        }
        case U8X8_MSG_GPIO_I2C_DATA: {           // arg_int=0: Output low at I2C data pin
            digital_write(info, info->pin_map.sda, arg_int, "I2C_DAT");
            break;                            // arg_int=1: Input dir with pullup high for I2C data pin
        }
        default: {
            u8x8_SetGPIOResult(u8x8, 1);            // default return value
            break;
        }
    }
    return 1;
}

uint8_t cb_byte_i2c_hw(const std::shared_ptr<u8g2_info_t> &info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr) {
    uint8_t *data;

    switch (msg) {
        case U8X8_MSG_BYTE_SEND: {
            data = (uint8_t *) arg_ptr;
            __u16 addr = u8x8_GetI2CAddress(u8x8);
            struct i2c_msg i2cMsg = {.addr = addr, .flags = 0, .len = arg_int, .buf = data};
            if (i2c_transfer(info->i2c.get(), &i2cMsg, 1) < 0) {
                fprintf(stderr, "i2c_transfer(): %s\n", i2c_errmsg(info->i2c.get()));
                return 0;
            }
            break;
        }
        case U8X8_MSG_BYTE_INIT: {
            if (i2c_open(info->i2c.get(), info->transport_device.c_str()) < 0) {
                fprintf(stderr, "i2c_open(): %s\n", i2c_errmsg(info->i2c.get()));
                return 0;
            }
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