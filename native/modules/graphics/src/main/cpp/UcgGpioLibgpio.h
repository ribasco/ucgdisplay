#ifndef UCGD_MOD_GRAPHICS_UCGGPIOLIBGPIO_H
#define UCGD_MOD_GRAPHICS_UCGGPIOLIBGPIO_H

#include "UcgGpio.h"

class UcgGpioLibgpio : public UcgGpio {
public:
    explicit UcgGpioLibgpio(const std::shared_ptr<u8g2_info_t> &info);

    void gpioLineInit(int pin, GpioDirection direction) override;

    void digitalWrite(int pin, uint8_t value) override;

private:
    gpiod::line* get_gpio_line(int pin);
    static int convert_dir(GpioDirection direction);
};


#endif //UCGD_MOD_GRAPHICS_UCGGPIOLIBGPIO_H
