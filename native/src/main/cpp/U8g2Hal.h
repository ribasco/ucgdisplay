//
// Created by raffy on 7/4/18.
//

#ifndef PIDISP_U8G2CALLBACKS_H
#define PIDISP_U8G2CALLBACKS_H

#include <u8g2.h>
#include <memory>
#include <map>
#include <functional>

using namespace std;

typedef std::function<uint8_t(u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr)> u8g2_msg_func_t;

typedef std::function<void(u8g2_t *u8g2, const u8g2_cb_t *rotation, u8x8_msg_cb byte_cb, u8x8_msg_cb gpio_and_delay_cb)> u8g2_setup_func_t;

typedef std::map<std::string, u8g2_setup_func_t> u8g2_setup_func_map_t;

typedef std::map<std::string, const uint8_t *> u8g2_lookup_font_map_t;


/*uint8_t clk;    //lcd ENABLE pin
uint8_t mosi;   //lcd RW pin
uint8_t cs;     //lcd RS pin
uint8_t reset;  //optional (not yet supported)
uint8_t dc;
uint8_t sda;
uint8_t scl;
uint8_t d0;
uint8_t d1;
uint8_t d2;
uint8_t d3;
uint8_t d4;
uint8_t d5;
uint8_t d6;
uint8_t d7;*/

typedef struct {
    uint8_t d0_spi_clock; //spi-clock
    uint8_t d1_spi_data; //spi-data
    uint8_t d2;
    uint8_t d3;
    uint8_t d4;
    uint8_t d5;
    uint8_t d6;
    uint8_t d7;
    uint8_t en;
    uint8_t cs;
    uint8_t dc;
    uint8_t reset;
    uint8_t scl;
    uint8_t sda;
    uint8_t cs1;
    uint8_t cs2;
} u8g2_pin_map_t;

typedef struct {
    u8g2_pin_map_t pin_map;
    shared_ptr<u8g2_t> u8g2;
    u8g2_setup_func_t setup_cb;
    u8g2_msg_func_t byte_cb;
    u8g2_msg_func_t gpio_cb;
    u8g2_cb_t *rotation;
} u8g2_info_t;

/**
 * Initialize the lookup t ables. This shuld be called prior to calling the other methods found in this file
 */
void u8g2hal_init();

/**
 * Initialize u8g2 setup lookup table
 */
void u8g2hal_init_setupfunctions(u8g2_setup_func_map_t &setup_map);

/**
 * Initialize u8g2 font lookup table
 */
void u8g2hal_init_fonts(u8g2_lookup_font_map_t &font_map);

/**
 * Byte communication callback for I2C on the Raspberry Pi
 */
uint8_t cb_rpi_byte_i2c_hw(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * Byte communications callback for SPI on the Raspberry Pi
 */
uint8_t cb_rpi_byte_spi_hw(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, void *arg_ptr);

/**
 * GPIO and Delay callback for Raspberry Pi
 */
uint8_t cb_rpi_gpio_delay(u8g2_info_t *info, u8x8_t *u8x8, uint8_t msg, uint8_t arg_int, U8X8_UNUSED void *arg_ptr);

/**
 * Helper utility function to obtain the u8g2 setup callback by name
 *
 * @param function_name The u8g2 setup procedure name
 * @return The function callback if found, otherwise null if not found
 */
u8g2_setup_func_t u8g2hal_get_setupproc(const std::string &function_name);

uint8_t* u8g2hal_get_fontbyname(const std::string &font_name);


#endif //PIDISP_U8G2CALLBACKS_H
