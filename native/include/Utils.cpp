#include "Utils.h"

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