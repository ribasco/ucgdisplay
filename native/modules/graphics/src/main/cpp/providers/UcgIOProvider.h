/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgIOProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGIOPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGIOPROVIDER_H

#include <memory>
#include <utility>
#include <UcgdTypes.h>

//Forward declarations
class UcgSpiProvider;

class UcgI2CProvider;

class UcgGpioProvider;

//Class exceptions
class UcgProviderException : public std::runtime_error {
public:
    explicit UcgProviderException(const std::string &arg) : runtime_error(arg) {}

    explicit UcgProviderException(const char *string) : runtime_error(string) {}

    explicit UcgProviderException(const runtime_error &error) : runtime_error(error) {}
};

//Class exceptions
class UcgProviderInitException : public UcgProviderException {
public:
    explicit UcgProviderInitException(const std::string &arg) : UcgProviderException(arg) {}

    explicit UcgProviderInitException(const char *string) : UcgProviderException(string) {}

    explicit UcgProviderInitException(const runtime_error &error) : UcgProviderException(error) {}
};

class UcgIOProvider {
public:
    explicit UcgIOProvider(std::shared_ptr<u8g2_info_t> info, const std::string& name) : m_Info(std::move(info)), m_Name(name) {}

    virtual ~UcgIOProvider() = default;

    const std::shared_ptr<u8g2_info_t> &getInfo() {
        return m_Info;
    }

    virtual const std::shared_ptr<UcgSpiProvider> &getSpiProvider() {
        return m_spiProvider;
    }

    virtual const std::shared_ptr<UcgI2CProvider> &getI2CProvider() {
        return m_i2cProvider;
    }

    virtual const std::shared_ptr<UcgGpioProvider> &getGpioProvider() {
        return m_gpioProvider;
    }

    bool supportsGpio() const {
        return m_gpioProvider != nullptr;
    }

    bool supportsSPI() const {
        return m_spiProvider != nullptr;
    }

    bool supportsI2C() const {
        return m_i2cProvider != nullptr;
    }

    std::string& getName() {
        return m_Name;
    }
protected:
    void setSPIProvider(const std::shared_ptr<UcgSpiProvider> &spiProvider) {
        m_spiProvider = spiProvider;
    }

    void setI2CProvider(const std::shared_ptr<UcgI2CProvider> &i2cProvider) {
        m_i2cProvider = i2cProvider;
    }

    void setGPIOProvider(const std::shared_ptr<UcgGpioProvider> &gpioProvider) {
        m_gpioProvider = gpioProvider;
    }

private:
    std::shared_ptr<UcgSpiProvider> m_spiProvider;
    std::shared_ptr<UcgI2CProvider> m_i2cProvider;
    std::shared_ptr<UcgGpioProvider> m_gpioProvider;
    std::shared_ptr<u8g2_info_t> m_Info;
    std::string m_Name;
};

#endif //UCGD_MOD_GRAPHICS_UCGIOPROVIDER_H
