#ifndef UCGDISPLAY_UTILS_H
#define UCGDISPLAY_UTILS_H

#include <string>

class Utils {
public:
#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    static bool isLibraryLoaded(const std::string& libPath);
#endif
    static std::string get_soc_model();
    static std::string get_soc_hardware();
    static std::string get_soc_revision();
    static std::string get_soc_description();
    static bool is_soc_raspberrypi();
private:
    static std::string exec(const char *cmd);
};

#endif //UCGDISPLAY_UTILS_H