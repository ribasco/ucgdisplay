//
// Created by raffy on 22/10/2019.
//

#ifndef UCGD_MOD_GRAPHICS_UCGGPIO_H
#define UCGD_MOD_GRAPHICS_UCGGPIO_H

#include <utility>
#include "U8g2Hal.h"

class GpioException: public std::exception
{
    virtual const char* what() const noexcept
    {
        return "GPIO line init exception";
    }
};

struct u8g2_info_t;

class UcgGpio {

public:
    enum GpioDirection : int {
        DIR_INPUT = 0,
        DIR_OUTPUT = 1,
        DIR_ASIS = 2
    };

    UcgGpio(std::shared_ptr<u8g2_info_t> info) : info(std::move(info)) { }
    virtual void gpioLineInit(int pin, GpioDirection direction) = 0;
    virtual void digitalWrite(int pin, uint8_t value) = 0;

protected:
    std::shared_ptr<u8g2_info_t> info;
};

#endif //UCGD_MOD_GRAPHICS_UCGGPIO_H
