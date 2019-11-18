/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgCperGpioProvider.cpp
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
#include <UcgdCperGpioPeripheral.h>
#include <gpio.h>
#include <iostream>

//need to redefine this otherwise an error would be thrown by the
// compiler due to invalid sizeof() operation on a forward declaration type
struct cp_gpio_handle {
    const struct cp_gpio_ops *ops;

    /* gpio-sysfs and gpio-cdev state */
    unsigned int line;
    int line_fd;

    /* gpio-cdev state */
    int chip_fd;
    cp_gpio_direction_t direction;
    cp_gpio_edge_t edge;

    /* error state */
    struct {
        int c_errno;
        char errmsg[96];
    } error;
};

UcgdCperGpioPeripheral::UcgdCperGpioPeripheral(const std::shared_ptr<UcgdProvider>& provider) : UcgdGpioPeripheral(provider) {

}

UcgdCperGpioPeripheral::~UcgdCperGpioPeripheral() {
    debug("UcgCperGpioProvider : destructor (Closing all gpio lines)");
    for (auto &it : m_GpioLineCache) {
        std::shared_ptr<gpio_t> ptr = it.second;
        int retval = cp_gpio_close(ptr.get());
        if (retval == 0) {
            debug("\t- Closing GPIO Line");
        }
    }
};

void UcgdCperGpioPeripheral::init(const std::shared_ptr<ucgd_t> &context, int pin, UcgdGpioPeripheral::GpioMode mode) {
    if (pin < 0)
        return;

    UcgdGpioPeripheral::init(context, pin, mode);
    std::string devicePath = UcgdGpioPeripheral::buildGpioDevicePath(context);
    log.debug("init() : [C-PERIPHERY] Initializing GPIO (Pin: {}, Mode: {}, Device Path: {})", pin, std::to_string(mode), devicePath);

    std::shared_ptr<gpio_t> gpio = findOrCreateGpioLine(pin);
    int retval = cp_gpio_open(gpio.get(), devicePath.c_str(), pin, GPIO_DIR_OUT);
    if (retval < 0) {
        std::stringstream ss;
        ss << "Failed to initialize gpio pin: " << std::to_string(pin) << " with mode '" << std::to_string(mode) << ", Reason: " << std::string(cp_gpio_errmsg(gpio.get()));
        throw GpioInitException(ss.str());
    }

    log.debug("init_gpio() : [C-PERIPHERY] Pin = {}, Mode = {}", pin, std::to_string(mode));
}

void UcgdCperGpioPeripheral::write(int pin, uint8_t value) {
    if (pin < 0)
        return;
    std::shared_ptr<gpio_t> gpio = findOrCreateGpioLine(pin);
    int retval = cp_gpio_write(gpio.get(), value);
    if (retval < 0) {
        std::stringstream ss;
        ss << "Failed to write to gpio pin: " << std::to_string(pin) << " with value '" << std::to_string(value) << ", Reason: " << std::string(cp_gpio_errmsg(gpio.get()));
        throw GpioWriteException(ss.str());
    }
}

bool UcgdCperGpioPeripheral::isModeSupported(const UcgdGpioPeripheral::GpioMode &mode) {
    return true;
}

const std::shared_ptr<gpio_t>& UcgdCperGpioPeripheral::findOrCreateGpioLine(int pin) {
    if (pin < 0)
        throw GpioException(std::string("findOrCreateGpioLine() : Invalid pin number: ") + std::to_string(pin));
    auto it = m_GpioLineCache.find(pin);
    if (it != m_GpioLineCache.end()) {
        //found instance, return
        return it->second;
    }
    //found nothing, create new
    return m_GpioLineCache.insert(std::make_pair(pin, std::shared_ptr<gpio_t>(cp_gpio_new()))).first->second;
}