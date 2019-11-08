#ifndef UCGD_MOD_GRAPHICS_UCGPIGPIOPROVIDERBASE_H
#define UCGD_MOD_GRAPHICS_UCGPIGPIOPROVIDERBASE_H

#include <UcgIOProvider.h>

class UcgPigpioProviderBase : public UcgIOProvider {
public:
    enum PigpioType : int {
        TYPE_STANDALONE,
        TYPE_DAEMON
    };

    UcgPigpioProviderBase(const std::string &name, PigpioType mType);

    PigpioType getType();

private:
    PigpioType m_Type;
};


#endif //UCGD_MOD_GRAPHICS_UCGPIGPIOPROVIDERBASE_H
