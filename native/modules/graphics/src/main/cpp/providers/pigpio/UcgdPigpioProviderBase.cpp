#include "UcgdPigpioProviderBase.h"

UcgdPigpioProviderBase::PigpioType UcgdPigpioProviderBase::getType() {
    return m_Type;
}

UcgdPigpioProviderBase::UcgdPigpioProviderBase(const std::string &name, UcgdPigpioProviderBase::PigpioType mType) : UcgdProvider(name), m_Type(mType) {

}