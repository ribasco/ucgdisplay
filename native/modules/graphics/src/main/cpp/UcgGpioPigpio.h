//
// Created by raffy on 22/10/2019.
//

#ifndef UCGD_MOD_GRAPHICS_UCGGPIOPIGPIO_H
#define UCGD_MOD_GRAPHICS_UCGGPIOPIGPIO_H

#include "UcgGpio.h"

class UcgGpioPigpio : public UcgGpio {
public:
    void gpioLineInit(int pin, GpioDirection direction) override;

    void digitalWrite(int pin, uint8_t value) override;
};


#endif //UCGD_MOD_GRAPHICS_UCGGPIOPIGPIO_H
