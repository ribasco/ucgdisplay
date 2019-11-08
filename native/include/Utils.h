#ifndef UCGDISPLAY_UTILS_H
#define UCGDISPLAY_UTILS_H

#include <string>

class Utils {
public:
#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    static bool isLibraryLoaded(const std::string& libPath);
#endif
};

#endif //UCGDISPLAY_UTILS_H