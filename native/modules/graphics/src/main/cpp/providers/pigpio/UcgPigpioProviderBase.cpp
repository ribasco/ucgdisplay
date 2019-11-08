#include "UcgPigpioProviderBase.h"

UcgPigpioProviderBase::PigpioType UcgPigpioProviderBase::getType() {
    return m_Type;
}

UcgPigpioProviderBase::UcgPigpioProviderBase(const std::string &name, UcgPigpioProviderBase::PigpioType mType) : UcgIOProvider(name), m_Type(mType) {

}