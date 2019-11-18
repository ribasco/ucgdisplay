#ifndef UCGD_MOD_GRAPHICS_UCGDPIGPIOPROVIDERBASE_H
#define UCGD_MOD_GRAPHICS_UCGDPIGPIOPROVIDERBASE_H

#include <UcgdProvider.h>

class UcgdPigpioProviderBase : public UcgdProvider {
public:
    enum PigpioType : int {
        TYPE_STANDALONE,
        TYPE_DAEMON
    };

    UcgdPigpioProviderBase(const std::string &name, PigpioType mType);

    PigpioType getType();

private:
    PigpioType m_Type;
};


#endif //UCGD_MOD_GRAPHICS_UCGDPIGPIOPROVIDERBASE_H
