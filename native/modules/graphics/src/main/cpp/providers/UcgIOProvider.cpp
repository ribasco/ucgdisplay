#include <UcgIOProvider.h>

#include <UcgSpiProvider.h>
#include <UcgI2CProvider.h>
#include <UcgGpioProvider.h>

#include <utility>

UcgIOProvider::UcgIOProvider(std::string name) : m_Name(std::move(name)),
                                                        m_Initialized(false),
                                                        log(ServiceLocator::getInstance().getLogger()),
                                                        m_spiProvider(nullptr),
                                                        m_gpioProvider(nullptr),
                                                        m_i2cProvider(nullptr),
                                                        m_Available(false) {

}

void UcgIOProvider::setInitialized(bool initialized) {
    m_Initialized = initialized;
}

void UcgIOProvider::markAvailable() {
    m_Available = true;
}

void UcgIOProvider::markUnavailable() {
    m_Available = false;
}

bool UcgIOProvider::isProvided() {
    return false;
}

bool UcgIOProvider::isInitialized() const {
    return m_Initialized;
}

std::string &UcgIOProvider::getName() {
    return m_Name;
}

bool UcgIOProvider::isAvailable() {
    return m_Available;
}

const std::shared_ptr<UcgSpiProvider> &UcgIOProvider::getSpiProvider() {
    return m_spiProvider;
}

const std::shared_ptr<UcgI2CProvider> &UcgIOProvider::getI2CProvider() {
    return m_i2cProvider;
}

const std::shared_ptr<UcgGpioProvider> &UcgIOProvider::getGpioProvider() {
    return m_gpioProvider;
}

bool UcgIOProvider::supportsGpio() const {
    return m_gpioProvider != nullptr;
}

bool UcgIOProvider::supportsSPI() const {
    return m_spiProvider != nullptr;
}

bool UcgIOProvider::supportsI2C() const {
    return m_i2cProvider != nullptr;
}

void UcgIOProvider::setSPIProvider(const std::shared_ptr<UcgSpiProvider> &spiProvider) {
    m_spiProvider = spiProvider;
}

void UcgIOProvider::setI2CProvider(const std::shared_ptr<UcgI2CProvider> &i2cProvider) {
    m_i2cProvider = i2cProvider;
}

void UcgIOProvider::setGPIOProvider(const std::shared_ptr<UcgGpioProvider> &gpioProvider) {
    m_gpioProvider = gpioProvider;
}

void UcgIOProvider::close() {
    /*try {
        if (this->m_spiProvider != nullptr) {
            this->m_spiProvider->close();
            this->m_spiProvider = nullptr;
        }
        if (this->m_i2cProvider != nullptr) {
            this->m_i2cProvider->close();
            this->m_i2cProvider = nullptr;
        }
        if (this->m_gpioProvider != nullptr) {
            this->m_gpioProvider->close();
            this->m_gpioProvider = nullptr;
        }
    } catch (std::exception &e) {
        std::cerr << "_close () : Failed to close peripheral device: " << std::string(e.what());
    }*/
}