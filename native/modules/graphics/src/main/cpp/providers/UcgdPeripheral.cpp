#include <UcgdPeripheral.h>

#include <UcgdSpiPeripheral.h>
#include <UcgdI2CPeripheral.h>
#include <UcgdGpioPeripheral.h>

UcgdPeripheral::UcgdPeripheral(const std::shared_ptr<UcgdProvider> &provider) :
        m_Provider(provider),
        log(ServiceLocator::getInstance().getLogger()) {
}

UcgdPeripheral::~UcgdPeripheral() {
    debug("UcgdPeripheral: destructor");
    m_OpenDevices.clear();
}

auto UcgdPeripheral::registerDevice(const std::shared_ptr<ucgd_t> &context) -> void {
    m_OpenDevices.push_back(context);
    log.debug("Registered device: {}", context->address());
}

auto UcgdPeripheral::getDevices() -> const std::vector<std::shared_ptr<ucgd_t>> & {
    return m_OpenDevices;
}

std::shared_ptr<UcgdProvider> UcgdPeripheral::getProvider() {
    auto provider = m_Provider.lock();
    if (!provider)
        throw std::runtime_error("getProvider() : provider instance already out of scope");
    return provider;
}

void UcgdPeripheral::open(const std::shared_ptr<ucgd_t> &context) {

}


