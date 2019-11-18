/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGDPIGPIOPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGDPIGPIOPROVIDER_H

#include <utility>
#include <iostream>
#include <UcgdProvider.h>
#include <UcgdPigpioProviderBase.h>

class PigpioProviderException : public std::runtime_error {
public:
    explicit PigpioProviderException(const std::string &arg) : runtime_error(arg) {}

    explicit PigpioProviderException(const char *string) : runtime_error(string) {}

    explicit PigpioProviderException(const runtime_error &error) : runtime_error(error) {}
};

class PigpioInitException : public PigpioProviderException {
public:
    explicit PigpioInitException(const std::string &msg) : PigpioProviderException(msg) {}
};

class UcgdPigpioProvider : public UcgdPigpioProviderBase, public std::enable_shared_from_this<UcgdPigpioProvider> {
public:
    UcgdPigpioProvider();

    ~UcgdPigpioProvider() override;

    void open(const std::shared_ptr<ucgd_t> &context) override;

    std::string getLibraryName() override;

    [[nodiscard]] bool supportsGpio() const override;

    [[nodiscard]] bool supportsSPI() const override;

    [[nodiscard]] bool supportsI2C() const override;

    std::shared_ptr<UcgdPigpioProvider> getPointer();
protected:
    std::shared_ptr<UcgdGpioPeripheral> createGpioPeripheral() override;

    std::shared_ptr<UcgdI2CPeripheral> createI2CPeripheral() override;

    std::shared_ptr<UcgdSpiPeripheral> createSpiPeripheral() override;

private:
    static int checkPigpioDaemonStatus();
};


#endif //UCGD_MOD_GRAPHICS_UCGDPIGPIOPROVIDER_H
