#include <UcgProviderBase.h>

#include <UcgSpiProvider.h>
#include <UcgI2CProvider.h>
#include <UcgGpioProvider.h>

UcgProviderBase::~UcgProviderBase() {
    close();
}

auto UcgProviderBase::close() -> void {
    if (m_OpenDevices.empty())
        return;
    //Close all open devices for this provider
    for (auto const &device : m_OpenDevices)
        close(device);
    m_OpenDevices.clear();
}

auto UcgProviderBase::registerDevice(const std::shared_ptr<ucgd_t> &context) -> void {
    m_OpenDevices.insert(m_OpenDevices.begin(), context);
}

auto UcgProviderBase::getDevices() -> const std::vector<std::shared_ptr<ucgd_t>> & {
    return m_OpenDevices;
}

UcgIOProvider *UcgProviderBase::getProvider() {
    return m_Provider;
}
