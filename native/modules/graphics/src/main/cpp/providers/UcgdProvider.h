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
#ifndef UCGD_MOD_GRAPHICS_UCGDPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGDPROVIDER_H

#include <memory>
#include <utility>
#include <UcgdTypes.h>
#include <ServiceLocator.h>
#include <Global.h>

//Forward declarations
class UcgdSpiPeripheral;

class UcgdI2CPeripheral;

class UcgdGpioPeripheral;

//Class exceptions
class UcgProviderException : public std::runtime_error {
public:
    explicit UcgProviderException(const std::string &arg, UcgdProvider *provider) : runtime_error(arg), m_Provider(provider) {}

    explicit UcgProviderException(const char *string, UcgdProvider *provider) : runtime_error(string), m_Provider(provider) {}

    explicit UcgProviderException(const runtime_error &error, UcgdProvider *provider) : runtime_error(error), m_Provider(provider) {}

    UcgdProvider *getProvider() {
        return this->m_Provider;
    }

private:
    UcgdProvider *m_Provider;
};

//Class exceptions
class UcgProviderInitException : public UcgProviderException {
public:
    explicit UcgProviderInitException(const std::string &arg, UcgdProvider *provider) : UcgProviderException(arg, provider) {}

    explicit UcgProviderInitException(const char *string, UcgdProvider *provider) : UcgProviderException(string, provider) {}

    explicit UcgProviderInitException(const runtime_error &error, UcgdProvider *provider) : UcgProviderException(error, provider) {}

    explicit UcgProviderInitException(const std::exception &error, UcgdProvider *provider) : UcgProviderException(std::string(error.what()), provider) {}
};

class UcgdProvider {
    friend class UcgProviderBase;

public:
    explicit UcgdProvider(std::string name);

    virtual ~UcgdProvider() {
        ::debug("UcgdProvider: destructor");
    };

    /**
     * Perform initialization
     */
    virtual void open(const std::shared_ptr<ucgd_t> &context) = 0;

    /**
     * Checks if the current provider is available/installed on the system
     *
     * @return true if the provider is available
     */
    bool isAvailable();

    /**
     * Get the library name of this provider
     *
     * @return The library (*.so) name associated with this provider
     */
    virtual std::string getLibraryName() = 0;

    std::shared_ptr<UcgdSpiPeripheral> &getSpiProvider();

    std::shared_ptr<UcgdI2CPeripheral> &getI2CProvider();

    std::shared_ptr<UcgdGpioPeripheral> &getGpioProvider();

    virtual bool supportsGpio() const;

    virtual bool supportsSPI() const;

    virtual bool supportsI2C() const;

    //virtual void close();

    virtual bool isProvided();

    std::string &getName();

    bool isInitialized() const;

    void markAvailable();

    void markUnavailable();

    void setInitialized(bool initialized);

protected:
    Log &log;

    virtual std::shared_ptr<UcgdGpioPeripheral> createGpioPeripheral() = 0;

    virtual std::shared_ptr<UcgdI2CPeripheral> createI2CPeripheral() = 0;

    virtual std::shared_ptr<UcgdSpiPeripheral> createSpiPeripheral() = 0;

private:
    std::string m_Name;
    bool m_Initialized;
    bool m_Available;

    std::shared_ptr<UcgdSpiPeripheral> m_spiProvider;
    std::shared_ptr<UcgdI2CPeripheral> m_i2cProvider;
    std::shared_ptr<UcgdGpioPeripheral> m_gpioProvider;
};

#endif //UCGD_MOD_GRAPHICS_UCGDPROVIDER_H
