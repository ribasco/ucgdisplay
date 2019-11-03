#ifndef UCGD_MOD_GRAPHICS_COMMON_H
#define UCGD_MOD_GRAPHICS_COMMON_H

#include <string>
#include <memory>
#include <Log.h>

std::string exec(const char *cmd);

std::string get_soc_model();

std::string get_soc_hardware();

std::string get_soc_description();

std::string get_soc_revision();

bool is_soc_raspberrypi();

#endif //UCGD_MOD_GRAPHICS_COMMON_H
