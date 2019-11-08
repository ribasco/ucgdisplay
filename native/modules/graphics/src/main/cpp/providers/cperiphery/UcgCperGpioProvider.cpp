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
#include <UcgCperGpioProvider.h>
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

UcgCperGpioProvider::UcgCperGpioProvider(UcgIOProvider *provider) : UcgGpioProvider(provider) {

}

UcgCperGpioProvider::~UcgCperGpioProvider() {
    _close();
};

void UcgCperGpioProvider::init(const std::shared_ptr<ucgd_t> &context, int pin, UcgGpioProvider::GpioMode mode) {
    if (pin < 0)
        return;
    UcgGpioProvider::init(context, pin, mode);

    //TODO: device path removed, need to do automatic translation the gpio number to a path
    //std::string devicePath = getOptionValueString(OPT_DEVICE_GPIO_PATH, DEFAULT_GPIO_DEVICE_PATH);

    std::string devicePath = UcgGpioProvider::buildGpioDevicePath(context);

    //TODO: Validate path

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

void UcgCperGpioProvider::write(int pin, uint8_t value) {
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

void UcgCperGpioProvider::close() {
    _close();
}

int UcgCperGpioProvider::_close() {
    for (auto &it : m_GpioLineCache) {
        std::shared_ptr<gpio_t> ptr = it.second;
        int retval = cp_gpio_close(ptr.get());
        if (retval == 0) {
            cp_gpio_free(ptr.get());
        }
    }
    return 0;
}

UcgCperipheryProvider *UcgCperGpioProvider::getProvider() {
    return dynamic_cast<UcgCperipheryProvider *>(UcgProviderBase::getProvider());
}

bool UcgCperGpioProvider::isModeSupported(const UcgGpioProvider::GpioMode &mode) {
    return true;
}

std::shared_ptr<gpio_t> UcgCperGpioProvider::findOrCreateGpioLine(int pin) {
    if (pin < 0)
        throw GpioException(std::string("findOrCreateGpioLine() : Invalid pin number: ") + std::to_string(pin));
    auto it = m_GpioLineCache.find(pin);
    if (it != m_GpioLineCache.end()) {
        //found instance, return
        return it->second;
    }
    //found nothing, create new
    auto res = m_GpioLineCache.insert(std::make_pair(pin, std::shared_ptr<gpio_t>(cp_gpio_new())));
    return res.first->second;
}
