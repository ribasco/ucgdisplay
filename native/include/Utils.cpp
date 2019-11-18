#include "Utils.h"

#include <string>
#include <memory>
#include <map>
#include <array>
#include <algorithm>
#include <cctype>

const std::map<std::string, std::string> revisions = { // NOLINT(cert-err58-cpp)
        {"900021", "Raspberry Pi A+ v1.1 512MB"},
        {"900032", "Raspberry Pi B+ v1.2 512MB"},
        {"900092", "Raspberry Pi Zero v1.2 512MB"},
        {"900093", "Raspberry Pi Zero v1.3 512MB"},
        {"9000c1", "Raspberry Pi Zero W v1.1 512MB"},
        {"9020e0", "Raspberry Pi 3A+ v1.0 512MB"},
        {"920092", "Raspberry Pi Zero v1.2 512MB"},
        {"920093", "Raspberry Pi Zero v1.3 512MB"},
        {"900061", "Raspberry Pi CM v1.1 512MB"},
        {"a01040", "Raspberry Pi 2B v1.0 1GB"},
        {"a01041", "Raspberry Pi 2B v1.1 1GB"},
        {"a02082", "Raspberry Pi 3B v1.2 1GB"},
        {"a020a0", "Raspberry Pi CM3 v1.0 1GB"},
        {"a020d3", "Raspberry Pi 3B+ v1.3 1GB"},
        {"a02042", "Raspberry Pi 2B (with BCM2837) v1.2 1GB"},
        {"a21041", "Raspberry Pi 2B v1.1 1GB"},
        {"a22042", "Raspberry Pi 2B (with BCM2837) v1.2 1GB"},
        {"a22082", "Raspberry Pi 3B v1.2 1GB"},
        {"a220a0", "Raspberry Pi CM3 v1.0 1GB"},
        {"a32082", "Raspberry Pi 3B v1.2 1GB"},
        {"a52082", "Raspberry Pi 3B v1.2 1GB"},
        {"a22083", "Raspberry Pi 3B v1.3 1GB"},
        {"a02100", "Raspberry Pi CM3+ v1.0 1GB"},
        {"a03111", "Raspberry Pi 4B v1.1 1GB"},
        {"b03111", "Raspberry Pi 4B v1.1 2GB"},
        {"c03111", "Raspberry Pi 4B v1.1 4GB"}
};

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
#include <dlfcn.h>

bool Utils::isLibraryLoaded(const std::string &libPath) {
    if (libPath.empty())
        return false;
    void *h = dlopen(libPath.c_str(), RTLD_NOW | RTLD_NOLOAD);
    if (h != nullptr) {
        dlclose(h);
        return true;
    }
    return false;
}
#endif

std::string Utils::exec(const char *cmd) {
    std::array<char, 128> buffer{};
    std::string result;
    std::unique_ptr<FILE, decltype(&pclose)> pipe(popen(cmd, "r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgets(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result += buffer.data();
    }
    return result;
}

std::string Utils::get_soc_model() {
    return exec(R"(cat /proc/cpuinfo | grep Model | cut -d : -f 2 | awk '{$1=$1};1' | tr -d '\040\011\012\015')");
}

std::string Utils::get_soc_hardware() {
    return exec(R"(cat /proc/cpuinfo | grep Hardware | cut -d : -f 2 | awk '{$1=$1};1 | tr -d '\040\011\012\015'')");
}

std::string Utils::get_soc_revision() {
    std::string rev = exec(R"(cat /proc/cpuinfo | grep Revision | cut -d : -f 2 | awk '{$1=$1};1' | tr -d '\040\011\012\015')");
    return rev;
}

std::string Utils::get_soc_description() {
    if (!is_soc_raspberrypi())
        return std::string();
    auto it = revisions.find(get_soc_revision());
    if (it != revisions.end()) {
        return it->second;
    }
    return std::string();
}

/**
 * -----------------------------------------------------------------------------------------------------------------------------
 * Note: From the raspberry pi website: https://www.raspberrypi.org/documentation/hardware/raspberrypi/revision-codes/README.md
 * ------------------------------------------------------------------------------------------------------------------------------
 * Note: As of the 4.9 kernel, all Pis report BCM2835, even those with BCM2836, BCM2837 and BCM2711 processors.
 * You should not use this string to detect the processor.
 * Decode the revision code using the information below, or cat /sys/firmware/devicetree/base/model
 */
bool Utils::is_soc_raspberrypi() {
    //std::transform(revision.begin(), revision.end(), revision.begin(), ::tolower);
    return revisions.find(get_soc_revision()) != revisions.end();
}