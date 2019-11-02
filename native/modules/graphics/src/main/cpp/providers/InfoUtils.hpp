#ifndef UCGD_MOD_GRAPHICS_INFOUTILS_HPP
#define UCGD_MOD_GRAPHICS_INFOUTILS_HPP

#include <UcgdTypes.h>
#include <sstream>

class KeyNotFoundException : public std::runtime_error {
public:
    explicit KeyNotFoundException(const std::string &arg) : runtime_error(arg) {};

    explicit KeyNotFoundException(const char *string) : runtime_error(string) {}

    explicit KeyNotFoundException(const runtime_error &error) : runtime_error(error) {}
};

class ProviderNotFoundException : public std::runtime_error {
public:
    explicit ProviderNotFoundException(const std::string &arg) : runtime_error(arg) {};

    explicit ProviderNotFoundException(const char *string) : runtime_error(string) {}

    explicit ProviderNotFoundException(const runtime_error &error) : runtime_error(error) {}
};

class InfoUtils {

private:
    InfoUtils() = default;

public:
    static std::any &getOptionValue(std::shared_ptr<u8g2_info_t> info, const std::string &key) {
        auto it = info->options.find(key);
        if (it != info->options.end()) {
            return it->second;
        }
        throw KeyNotFoundException(std::string("Key '") + key + std::string("' not found in options"));
    }

    static std::string getOptionValueString(std::shared_ptr<u8g2_info_t> info, const std::string &key, std::string defaultVal = std::string()) {
        try {
            std::any value = getOptionValue(info, key);
            return std::any_cast<std::string>(value);
        } catch (KeyNotFoundException &e) {
            return defaultVal;
        }
    }

    static int getOptionValueInt(std::shared_ptr<u8g2_info_t> info, const std::string &key, int defaultVal = 0) {
        try {
            std::any value = getOptionValue(info, key);
            return std::any_cast<int>(value);
        } catch (KeyNotFoundException &e) {
            return defaultVal;
        }
    }

    static std::shared_ptr<UcgIOProvider> findProvider(std::shared_ptr<u8g2_info_t> info, const std::string &providerName) {
        auto it = info->providers.find(providerName);
        if (it != info->providers.end()) {
            return it->second;
        }
        throw ProviderNotFoundException(std::string("Provider not found: ") + providerName);
    }

    static bool supportsSPI(std::shared_ptr<u8g2_info_t> info) {
        try {
            std::string gpioProviderName = getOptionValueString(info, OPT_PROVIDER_SPI);
            return findProvider(info, gpioProviderName)->supportsSPI();
        } catch (ProviderNotFoundException& e) {
            return false;
        }
    }

    static bool supportsGPIO(std::shared_ptr<u8g2_info_t> info) {
        std::string gpioProviderName = getOptionValueString(info, OPT_PROVIDER_GPIO);
        try {
            std::shared_ptr<UcgIOProvider> provider = findProvider(info, gpioProviderName);
            return provider->supportsGpio();
        } catch (ProviderNotFoundException& e) {
            return false;
        }
    }

    static bool supportsI2C(std::shared_ptr<u8g2_info_t> info) {
        try {
            std::string gpioProviderName = getOptionValueString(info, OPT_PROVIDER_I2C);
            return findProvider(info, gpioProviderName)->supportsI2C();
        } catch (ProviderNotFoundException& e) {
            return false;
        }
    }

    static std::shared_ptr<UcgGpioProvider> findGPIOProvider(const std::shared_ptr<u8g2_info_t> &info) {
        std::string gpioProviderName = getOptionValueString(info, OPT_PROVIDER_GPIO);
        return findProvider(info, gpioProviderName)->getGpioProvider();
    };

    static std::shared_ptr<UcgSpiProvider> findSPIProvider(const std::shared_ptr<u8g2_info_t> &info) {
        std::string gpioProviderName = getOptionValueString(info, OPT_PROVIDER_SPI);
        return findProvider(info, gpioProviderName)->getSpiProvider();
    };

    static std::shared_ptr<UcgI2CProvider> findI2CProvider(const std::shared_ptr<u8g2_info_t> &info) {
        std::string gpioProviderName = getOptionValueString(info, OPT_PROVIDER_I2C);
        return findProvider(info, gpioProviderName)->getI2CProvider();
    };
};

#endif //UCGD_MOD_GRAPHICS_INFOUTILS_HPP
