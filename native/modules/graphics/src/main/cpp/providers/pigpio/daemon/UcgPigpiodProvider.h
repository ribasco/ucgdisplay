/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpiodProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGPIGPIODPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGPIGPIODPROVIDER_H

#include <utility>
#include <iostream>
#include <UcgPigpioProviderBase.h>

class PigpiodProviderException : public std::runtime_error {
public:
    explicit PigpiodProviderException(const std::string &arg) : runtime_error(arg) {}

    explicit PigpiodProviderException(const char *string) : runtime_error(string) {}

    explicit PigpiodProviderException(const runtime_error &error) : runtime_error(error) {}
};

class PigpiodInitException : public PigpiodProviderException {
public:
    explicit PigpiodInitException(const std::string &msg) : PigpiodProviderException(msg) {}
};

class UcgPigpiodProvider : public UcgPigpioProviderBase {
public:
    explicit UcgPigpiodProvider();

    explicit UcgPigpiodProvider(const std::string& address, const std::string& port);

    ~UcgPigpiodProvider() override;

    [[nodiscard]] int getHandle() const;

    void initialize(const std::shared_ptr<ucgd_t>& context) override;

    std::string getLibraryName() override;

    const std::shared_ptr<UcgSpiProvider> &getSpiProvider() override;

    const std::shared_ptr<UcgI2CProvider> &getI2CProvider() override;

    const std::shared_ptr<UcgGpioProvider> &getGpioProvider() override;

    [[nodiscard]] bool supportsGpio() const override;

    [[nodiscard]] bool supportsSPI() const override;

    [[nodiscard]] bool supportsI2C() const override;

private:
    int m_Handle;
    std::string address;
    std::string port;
};


#endif //UCGD_MOD_GRAPHICS_UCGPIGPIODPROVIDER_H
