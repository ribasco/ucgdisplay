//
// Created by raffy on 7/5/18.
//

#ifndef UCGDISP_GENERATE_H
#define UCGDISP_GENERATE_H

#include <iostream>
#include <cstring>
#include <vector>
#include <set>
#include <memory>
#include <algorithm>
#include <cstdio>
#include <map>
#include <string>
#include <sys/stat.h>
#include <sys/types.h>
#include <fstream>
#include <sstream>
#include <cstring>
#include <unistd.h>
#include <ctime>
#include <iomanip>
#include <dirent.h>

using namespace std;

#define COM_4WSPI        0x0001
#define COM_3WSPI        0x0002
#define COM_6800        0x0004
#define COM_8080        0x0008
#define COM_I2C        0x0010
#define COM_ST7920SPI    0x0020            /* mostly identical to COM_4WSPI, but does not use DC */
#define COM_UART        0x0040
#define COM_KS0108    0x0080            /* mostly identical to 6800 mode, but has more chip select lines */
#define COM_SED1520    0x0100

#define CODE_DISPLAY_PREFIX "D"

#define BUFFER_CODE_FULL "f"
#define BUFFER_CODE_1 "1"
#define BUFFER_CODE_2 "2"

#define AUTOGEN_MSG "/*\n * THIS IS AN AUTO-GENERATED CODE!! PLEASE DO NOT MODIFY (Last updated: " __DATE__ " " __TIME__ ") \n */\n"

struct display {
    /* this name must match the display part of the device procedure */
    /* u8x8_d_<controller>_<display> */
    const char *name;
};

struct display_size_t {
    int tile_width;
    int tile_height;
    string desc;
};

bool operator==(const display_size_t& lhs, const display_size_t &rhs) {
    return (lhs.tile_width == rhs.tile_width) && (lhs.tile_height == rhs.tile_height);
}

bool operator<(const display_size_t& lhs, const display_size_t& rhs) {
    return lhs.desc < rhs.desc;//(lhs.tile_width < rhs.tile_width) || (lhs.tile_height < rhs.tile_height);
}

bool operator!=(const display_size_t& lhs, const display_size_t &rhs) {
    return !(rhs == lhs);
}

struct display_mode_info {
    string controller; //controller name
    string name; //display name
    string name_proper; //sanitized version
    int tileWidth;
    int tileHeight;
    map<string, string> modes; //mode, setup function
};

typedef map<string, vector<display_mode_info>> glcd_info_tree_t;

struct controller {
    /* the name must match the controller part of the device procedure */
    /* u8x8_d_<controller>_<display> */
    const char *name;

    int tile_width;
    int tile_height;
    const char *ll_hvline;
    const char *cad;
    const char *cad_shortname;
    unsigned com;
    char *note;
    unsigned is_generate_u8g2_class;    /* currently not used, instead conrolled by COM_UART */
    struct display display_list[10];    /* usually not used completly, but space does not matter much here */
};

bool fileExists(const string &file) {
    struct stat info{};
    return stat(file.c_str(), &info) == 0 && !(info.st_mode & S_IFDIR);
}

bool dirExists(const string &dir) {
    struct stat info{};
    return stat(dir.c_str(), &info) == 0 && (info.st_mode & S_IFDIR);
}

bool initOutputDir(const string &outputDir) {
    if (!dirExists(outputDir)) {
#ifdef _WIN32
        const int dir_err = mkdir(outputDir.c_str());
#else
        const int dir_err = mkdir(outputDir.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);
#endif
        if (-1 == dir_err)
            return false;
    }
    return true;
}

void appendAutogenMsg(stringstream &str) {
    str << AUTOGEN_MSG << endl;
}

string joinList(vector<string> list) {
    std::string result;
    for (size_t i = 0, i_end = list.size(); i < i_end; ++i) {
        result += (i ? "," : "");
        result += list[i];
    }
    return result;
}

string GetCurrentWorkingDir() {
    char buff[FILENAME_MAX];
    getcwd(buff, FILENAME_MAX);
    std::string current_working_dir(buff);
    return current_working_dir;
}

bool saveFileToPath(const string &filePath, const string &data) {
    ofstream file;
    file.open(filePath);
    file << data;
    file.close();
    return fileExists(filePath);
}

bool saveFileToOutputDir(const string &fileName, const string &data) {
    //Commit to file
    string outputDir = GetCurrentWorkingDir() + "/output";
    if (!initOutputDir(outputDir)) {
        cerr << "Could not create output directory" << endl;
        exit(1);
    }
    saveFileToPath(outputDir + "/" + fileName, data);
    return true;
}

char *strlowercase(const char *s) {
    int i, len = static_cast<int>(strlen(s));
    static char buf[1024];
    for (i = 0; i <= len; i++)
        buf[i] = static_cast<char>(tolower(s[i]));
    return buf;
}

vector<string> getAvailableFonts(const string &dirPath) {
    DIR *dir;
    struct dirent *ent;
    vector<string> files;
    if ((dir = opendir(dirPath.c_str())) != nullptr) {
        /* print all the files and directories within directory */
        while ((ent = readdir(dir)) != nullptr) {
            string fileName = string(ent->d_name);
            std::size_t pos = fileName.find_last_of(".c");
            if (pos > 0) {
                string tmp = fileName.substr(0, pos - 1);
                if (!tmp.empty()) {
                    files.emplace_back(tmp);
                }
            }
        }
        closedir(dir);
    } else {
        /* could not open directory */
        cerr << "Could not open directory: " << dirPath << endl;
    }
    return files;
}

vector<string> tokenizeString(const string &str, char delim) {
    std::istringstream ss(str);
    std::string token;
    vector<string> tmp;
    while (std::getline(ss, token, delim)) {
        tmp.emplace_back(token);
    }
    return tmp;
}

#include "controllers.h"

#endif //UCGDISP_GENERATE_H
