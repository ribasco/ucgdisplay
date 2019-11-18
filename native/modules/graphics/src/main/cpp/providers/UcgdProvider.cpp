#include <UcgdProvider.h>

#include <UcgdSpiPeripheral.h>
#include <UcgdI2CPeripheral.h>
#include <UcgdGpioPeripheral.h>

#include <utility>

UcgdProvider::UcgdProvider(std::string name) : m_Name(std::move(name)),
                                                        m_Initialized(false),
                                                        log(ServiceLocator::getInstance().getLogger()),
                                                        m_spiProvider(nullptr),
                                                        m_gpioProvider(nullptr),
                                                        m_i2cProvider(nullptr),
                                                        m_Available(false) {

}

void UcgdProvider::setInitialized(bool initialized) {
    m_Initialized = initialized;
}

void UcgdProvider::markAvailable() {
    m_Available = true;
}

void UcgdProvider::markUnavailable() {
    m_Available = false;
}

bool UcgdProvider::isProvided() {
    return false;
}

bool UcgdProvider::isInitialized() const {
    return m_Initialized;
}

std::string &UcgdProvider::getName() {
    return m_Name;
}

bool UcgdProvider::isAvailable() {
    return m_Available;
}

std::shared_ptr<UcgdSpiPeripheral> &UcgdProvider::getSpiProvider() {
    if (m_spiProvider == nullptr) {
        log.debug(std::string("get_spi_provider() : Creating a new instance of spi peripheral for provider: ") + getName());
        m_spiProvider = createSpiPeripheral();
    }
    return m_spiProvider;
}

std::shared_ptr<UcgdI2CPeripheral> &UcgdProvider::getI2CProvider() {
    if (m_i2cProvider == nullptr) {
        log.debug(std::string("get_i2C_provider() : Creating a new instance of i2c peripheral for provider: ") + getName());
        m_i2cProvider = createI2CPeripheral();
    }
    return m_i2cProvider;
}

std::shared_ptr<UcgdGpioPeripheral> &UcgdProvider::getGpioProvider() {
    if (m_gpioProvider == nullptr) {
        log.debug(std::string("get_gpio_provider() : Creating a new instance of gpio peripheral for provider: ") + getName());
        m_gpioProvider = createGpioPeripheral();
    }
    return m_gpioProvider;
}

bool UcgdProvider::supportsGpio() const {
    return m_gpioProvider != nullptr;
}

bool UcgdProvider::supportsSPI() const {
    return m_spiProvider != nullptr;
}

bool UcgdProvider::supportsI2C() const {
    return m_i2cProvider != nullptr;
}

/*
void UcgdProvider::close() {
    debug("close() : Using default provider close implementation");
}*/
