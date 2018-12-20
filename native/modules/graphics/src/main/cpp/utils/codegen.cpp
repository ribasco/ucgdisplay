#include "codegen.h"
#include <iterator>
#include <utility>
#include <regex>

using namespace std;

static map<string, string> paths;

#define BASE_PROJECT_PATH_KEY "base_project_path"
#define BASE_PROJECT_PATH_NATIVE_KEY "base_project_native_path"
#define BASE_PROJECT_PATH_DRIVERS_KEY "base_project_drivers_path"
#define BASE_PROJECT_PATH_NATIVE_CPP_KEY "base_project_native_cpp_path"
#define U8G2_PROJECT_PATH_KEY "u8g2_project_path"
#define U8G2_CODEBUILD_PATH_KEY "u8g2_codebuild_path"
#define U8G2_CODEBUILD_SRCFILE_KEY "u8g2_codebuild_srcfile"
#define U8G2_FONT_PATH_KEY "u8g2_font_path"

#define BASE_PROJECT_PATH paths[BASE_PROJECT_PATH_KEY]
#define BASE_PROJECT_PATH_NATIVE paths[BASE_PROJECT_PATH_NATIVE_KEY]
#define BASE_PROJECT_PATH_DRIVERS paths[BASE_PROJECT_PATH_DRIVERS_KEY]
#define BASE_PROJECT_PATH_NATIVE_CPP paths[BASE_PROJECT_PATH_NATIVE_CPP_KEY]
#define U8G2_PROJECT_PATH paths[U8G2_PROJECT_PATH_KEY]
#define U8G2_CODEBUILD_PATH paths[U8G2_CODEBUILD_PATH_KEY]
#define U8G2_CODEBUILD_SRCFILE paths[U8G2_CODEBUILD_SRCFILE_KEY]
#define U8G2_FONT_PATH paths[U8G2_FONT_PATH_KEY]

#define FILE_CONTROLLERS "controllers.h"
#define FILE_LOOKUP_SETUP "U8g2LookupSetup.cpp"
#define FILE_LOOKUP_FONTS "U8g2LookupFonts.cpp"
#define FILE_JAVA_GLCDSIZE "GlcdSize.java"
#define FILE_JAVA_GLCD "Glcd.java"
#define FILE_JAVA_GLCDCONTROLLERTYPE "GlcdControllerType.java"
#define FILE_JAVA_GLCDFONT "GlcdFont.java"

static vector<string> excludeFonts = {"u8g2_font_siji_t"};
static vector<string> win32_excluded_fonts = {"u8g2_font_unifont_t_chinese3",
                                              "u8g2_font_unifont_t_japanese1",
                                              "u8g2_font_unifont_t_japanese2",
                                              "u8g2_font_unifont_t_japanese3",
                                              "u8g2_font_unifont_t_korean2",
                                              "u8g2_font_wqy12_t_gb2312",
                                              "u8g2_font_wqy12_t_gb2312a",
                                              "u8g2_font_wqy12_t_gb2312b",
                                              "u8g2_font_wqy15_t_gb2312",
                                              "u8g2_font_wqy15_t_gb2312a",
                                              "u8g2_font_wqy15_t_gb2312b",
                                              "u8g2_font_wqy16_t_gb2312",
                                              "u8g2_font_wqy16_t_gb2312a",
                                              "u8g2_font_wqy16_t_gb2312b",
                                              "u8g2_font_wqy14_t_gb2312",
                                              "u8g2_font_wqy14_t_gb2312a",
                                              "u8g2_font_wqy14_t_gb2312b",
                                              "u8g2_font_wqy13_t_gb2312",
                                              "u8g2_font_wqy13_t_gb2312a",
                                              "u8g2_font_wqy13_t_gb2312b",
                                              "u8g2_font_t0_11_t_all",
                                              "u8g2_font_f10_b_t_japanese1",
                                              "u8g2_font_f10_b_t_japanese2",
                                              "u8g2_font_f10_t_japanese1",
                                              "u8g2_font_f10_t_japanese2",
                                              "u8g2_font_f12_b_t_japanese1",
                                              "u8g2_font_f12_b_t_japanese2",
                                              "u8g2_font_f12_t_japanese1",
                                              "u8g2_font_f12_t_japanese2",
                                              "u8g2_font_f16_b_t_japanese1",
                                              "u8g2_font_f16_b_t_japanese2",
                                              "u8g2_font_f16_t_japanese1",
                                              "u8g2_font_f16_t_japanese2",
                                              "u8g2_font_b10_b_t_japanese1",
                                              "u8g2_font_b10_b_t_japanese2",
                                              "u8g2_font_b10_t_japanese1",
                                              "u8g2_font_b10_t_japanese2",
                                              "u8g2_font_b12_b_t_japanese1",
                                              "u8g2_font_b12_b_t_japanese2",
                                              "u8g2_font_b12_b_t_japanese3",
                                              "u8g2_font_b12_t_japanese1",
                                              "u8g2_font_b12_t_japanese2",
                                              "u8g2_font_b12_t_japanese3",
                                              "u8g2_font_b16_b_t_japanese1",
                                              "u8g2_font_b16_b_t_japanese2",
                                              "u8g2_font_b16_b_t_japanese3",
                                              "u8g2_font_b16_t_japanese1",
                                              "u8g2_font_b16_t_japanese2",
                                              "u8g2_font_b16_t_japanese3"};

string sanitizeDisplayName(display_mode_info &display) {
    //"([0-9]{1,})X([0-9]{1,})|(NONAME?[0-9]*)|([_-]*)"
    regex words_regex("([0-9]{1,})X([0-9]{1,})|([_-]*)");
    string ds = to_string(display.tileWidth * 8).append("x").append(to_string(display.tileHeight * 8));
    string tmp = display.name;

    tmp = std::regex_replace(tmp, words_regex, "");

    string newStr = CODE_DISPLAY_PREFIX + string("_").append(ds);
    if (!tmp.empty())
        newStr.append("_").append(tmp);

    return newStr;
}

string getSetupFunctionName(int controller_idx, int display_idx, const string &buffer_code) {
    //printf("(u8g2_t *u8g2, const u8g2_cb_t *rotation, u8x8_msg_cb byte_cb, u8x8_msg_cb gpio_and_delay_cb);\n");
    string setupFuncName;
    setupFuncName += "u8g2_Setup_";
    setupFuncName += string(strlowercase(controller_list[controller_idx].name));
    setupFuncName += "_";
    if (controller_list[controller_idx].cad_shortname[0] != '\0') {
        setupFuncName += string(strlowercase(controller_list[controller_idx].cad_shortname));
        setupFuncName += "_";
    }
    setupFuncName += string(strlowercase(controller_list[controller_idx].display_list[display_idx].name));
    setupFuncName += "_";
    setupFuncName += buffer_code;
    return setupFuncName;
}

set<string> getControllers() {
    set<string> controllers;
    for (controller &i : controller_list) {
        string name = string(i.name);
        transform(name.begin(), name.end(), name.begin(), ::toupper);
        controllers.insert(name);
    }
    return controllers;
}

vector<string> getSupportedModes(controller &controller) {
    vector<string> modes;

    if (controller.com & COM_4WSPI) {
        modes.emplace_back("4WSPI");
    }
    if (controller.com & COM_3WSPI) {
        modes.emplace_back("3WSPI");
    }
    if (controller.com & COM_I2C) {
        modes.emplace_back("I2C");
    }
    if (controller.com & COM_UART) {
        modes.emplace_back("UART");
    }
    if (controller.com & COM_6800) {
        modes.emplace_back("6800");
    }
    if (controller.com & COM_8080) {
        modes.emplace_back("8080");
    }
    if (controller.com & COM_KS0108) {
        modes.emplace_back("KS0108");
    }
    if (controller.com & COM_ST7920SPI) {
        modes.emplace_back("ST7920SPI");
    }
    if (controller.com & COM_SED1520) {
        modes.emplace_back("SED1520");
    }
    return modes;
}

/**
 * Returns the display size of the controller in string format
 */
string getDisplaySize(controller &controller) {
    return to_string(controller.tile_width * 8) + "x" + to_string(controller.tile_height * 8);
}

/**
 * Returns a set of available display sizes supported by u8g2
 */
set<display_size_t> getDisplaySizes() {
    set<display_size_t> sizes;
    for (auto &controller : controller_list) {
        display_size_t size = {controller.tile_width, controller.tile_height, getDisplaySize(controller)};
        sizes.insert(size);
    }
    return sizes;
}

void getSetupFunctions(set<string> &functions, const string &bufferCode) {
    for (int i = 0; i < sizeof(controller_list) / sizeof(*controller_list); i++) {
        controller &controller = controller_list[i];
        int id = 0;
        while (controller.display_list[id].name != nullptr) {
            functions.insert(getSetupFunctionName(i, id, bufferCode));
            id++;
        }
    }
}

void printAvailableDisplays() {
    cout << "Controller\t" << "Display Name\t" << "Cad Name\t" << "Cad Short\t" << "Supported Modes\t"
         << "Display Size\t" << "Total Size\t" << "Setup Procedure" << endl;
    for (int i = 0; i < sizeof(controller_list) / sizeof(*controller_list); i++) {
        controller &controller = controller_list[i];
        int id = 0;
        while (controller.display_list[id].name != nullptr) {
            cout << controller.name << "\t"
                 << controller.display_list[id].name << "\t"
                 << controller.cad << "\t"
                 << controller.cad_shortname << "\t"
                 << joinList(getSupportedModes(controller)) << "\t"
                 << getDisplaySize(controller) << "\t"
                 << to_string((controller.tile_width * 8) * (controller.tile_height * 8)) << "\t"
                 << getSetupFunctionName(i, id, BUFFER_CODE_FULL) << endl;
            id++;
        }
    }
}

void printGlcdDisplayNames() {
    for (auto &controller : controller_list) {
        int id = 0;
        while (controller.display_list[id].name != nullptr) {
            string controllerName = string(controller.name);
            string displayName = string(controller.display_list[id].name);
            string cadName = string(controller.cad);
            string cadNameShort = string(controller.cad_shortname);
            string supportedModes = joinList(getSupportedModes(controller));

            string enumName = controllerName.append("_").append(displayName);
            if (!cadNameShort.empty())
                enumName.append("_").append(cadNameShort);
            transform(enumName.begin(), enumName.end(), enumName.begin(), ::toupper);
            cout << enumName << "," << endl;

            id++;
        }
    }
}

void populateDisplayModes(int controller_id, int display_id, map<string, string> &displayModes) {
    vector<string> modes = getSupportedModes(controller_list[controller_id]);
    for (auto mode : modes) {
        string setupFunction = getSetupFunctionName(controller_id, display_id, BUFFER_CODE_FULL);

        auto result = find_if(displayModes.begin(), displayModes.end(),
                              [&](std::pair<string, string> a) -> bool {
                                  return (a.first == mode) && (a.second == setupFunction);
                              });

        //Insert to map if not yet existing (setupFunction)
        if (result == displayModes.end())
            displayModes.insert(make_pair(mode, setupFunction));
    }
}

/**
 * Re-organize the u8g2 struct and store them inside a map
 *
 * @param controllers The map to be used for data population
 */
void buildDisplayTree(glcd_info_tree_t &controllers) {
    for (int i = 0; i < sizeof(controller_list) / sizeof(*controller_list); i++) {
        controller &controller = controller_list[i];
        string controllerName = string(controller.name);
        transform(controllerName.begin(), controllerName.end(), controllerName.begin(), ::toupper);

        int id = 0;
        while (controller.display_list[id].name != nullptr) {
            string displayName = string(controller.display_list[id].name);
            transform(displayName.begin(), displayName.end(), displayName.begin(), ::toupper);

            auto controller_it = controllers.find(controllerName);

            if (controller_it == controllers.end()) { //If controller not yet in the map
                //Create display_info and pre-populate
                display_mode_info info = {controllerName, displayName, "", controller.tile_width,
                                          controller.tile_height, controller.ll_hvline};
                info.name_proper = sanitizeDisplayName(info);
                populateDisplayModes(i, id, info.modes);
                vector<display_mode_info> displayList;
                displayList.emplace_back(info);
                controllers.insert(make_pair(controllerName, move(displayList)));
            } else { //controller exists in map
                vector<display_mode_info> &displayList = controller_it->second;
                auto it = std::find_if(displayList.begin(), displayList.end(),
                                       [&displayName](const display_mode_info &i) -> bool {
                                           return i.name == displayName;
                                       });

                //Check if display name exists in the list
                if (it == displayList.end()) {
                    display_mode_info newInfo = {controllerName, displayName, "", controller.tile_width, controller.tile_height, controller.ll_hvline};
                    newInfo.name_proper = sanitizeDisplayName(newInfo);
                    populateDisplayModes(i, id, newInfo.modes);
                    controller_it->second.emplace_back(newInfo);
                } else {
                    //if display name is already in-list, retrieve existing and update the modes map
                    populateDisplayModes(i, id, it->modes);
                }
            }
            id++;
        }
    }
}

/**
 * Builds the GlcdSetupInfo() line for Glcd.java
 */
vector<string> buildCode_GetGlcdSetupInfo(display_mode_info &dInfo) {
    vector<string> setupInfoCodes;

    //Ex1: new GlcdSetupInfo("u8g2_Setup_ssd1306_96x16_er_f", COM_3WSPI | COM_8080 | COM_KS0108),
    //Ex2: new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_96x16_er_f", COM_I2C)

    //Merge the protocol codes
    map<string, vector<string>> tmpSetup;
    for (const auto &modes : dInfo.modes) {
        string modeName = "COM_" + modes.first;
        string setupFunc = modes.second;

        //check if key exists
        auto it = tmpSetup.find(setupFunc);
        if (it != tmpSetup.end()) {
            it->second.emplace_back(modeName);
        } else {
            vector<string> m;
            m.emplace_back(modeName);
            tmpSetup.insert(make_pair(setupFunc, move(m)));
        }
    }

    for (const auto &it : tmpSetup) {
        string setupFunc = it.first;
        vector<string> setupModes = it.second;

        stringstream modes;
        std::copy(setupModes.begin(), setupModes.end() - 1, std::ostream_iterator<string>(modes, " | "));
        modes << setupModes.back();

        stringstream tmp;
        tmp << "new GlcdSetupInfo(";
        tmp << "\"" << setupFunc << "\", ";
        tmp << modes.str();
        tmp << ")";
        setupInfoCodes.emplace_back(tmp.str());
    }

    return setupInfoCodes;
}

/**
 * Build the primary lookup table Glcd.java
 */
void buildCode_Glcd() {
    string filePath = FILE_JAVA_GLCD;

    stringstream code;

    map<string, vector<display_mode_info>> controllers;
    buildDisplayTree(controllers);

    appendAutogenMsg(code);

    code << "package com.ibasco.ucgdisplay.drivers.glcd;\n\n";

    code << "import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBufferType;\n";
    code << "import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdControllerType;\n";
    code << "import static com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics.*;\n\n";

    code << "@SuppressWarnings(\"unused\")\n";
    code << "public interface Glcd {\n";

    for (const auto &ctrl : controllers) {
        string controllerName = ctrl.first;
        code << "\t/** Controller Name: " << controllerName << " **/" << endl;

        code << "\tinterface " << controllerName << " {" << endl;
        for (auto disp : ctrl.second) {
            string dName = disp.name_proper; //use sanitized version
            code << "\t\t" << "/** U8G2 Name: " << ctrl.first << " " << disp.name << " **/" << endl;
            code << "\t\t" << "GlcdDisplay " << dName << " = new GlcdDisplay(" << endl;
            code << "\t\t\t" << "GlcdControllerType." << controllerName << "," << endl;
            code << "\t\t\t" << "\"" << dName << "\"," << endl;
            code << "\t\t\t" << to_string(disp.tileWidth) << "," << endl;
            code << "\t\t\t" << to_string(disp.tileHeight) << "," << endl;
            if (disp.buffer_type == "u8g2_ll_hvline_horizontal_right_lsb") {
                code << "\t\t\t" << "GlcdBufferType.HORIZONTAL," << endl;
            } else if (disp.buffer_type == "u8g2_ll_hvline_vertical_top_lsb") {
                code << "\t\t\t" << "GlcdBufferType.VERTICAL," << endl;
            } else {
                code << "\t\t\t" << "GlcdBufferType.UNKNOWN," << endl;
            }
            //code << "\t\t\t" << "\"" << disp.buffer_type << "\"," << endl;
            //generate setup info code
            vector<string> setupInfoCodes = buildCode_GetGlcdSetupInfo(disp);

            for (int i = 0; i < setupInfoCodes.size(); i++) {
                code << "\t\t\t" << setupInfoCodes[i];
                if (i < setupInfoCodes.size() - 1)
                    code << ",\n";
            }

            code << "\n\t\t);" << endl;
        }
        code << "\t}\n" << endl;
    }

    code << "}\n";

    saveFileToOutputDir(FILE_JAVA_GLCD, code.str());
}

/**
 * Build GlcdController.java enum
 */
void buildCode_GlcdControllerType() {
    set<string> controllers = getControllers();

    if (controllers.empty())
        return;

    stringstream code;
    appendAutogenMsg(code);
    code << "package com.ibasco.ucgdisplay.drivers.glcd.enums;\n\n";

    code << "@SuppressWarnings(\"unused\")\n";
    code << "public enum GlcdControllerType {" << endl;
    for (const auto &controller : controllers) {
        static int i = 1;
        code << "\t" << controller;
        code << (i < controllers.size() ? ",\n" : "");
        i++;
    }
    code << "\n}";

    saveFileToOutputDir(FILE_JAVA_GLCDCONTROLLERTYPE, code.str());
}

void buildCode_GlcdSize() {
    set<display_size_t> sizes = getDisplaySizes();

    if (sizes.empty())
        return;

    stringstream code;
    code << AUTOGEN_MSG << endl;
    code << "package com.ibasco.ucgdisplay.drivers.glcd.enums;\n\n";

    code << "import java.util.Arrays;\n\n";

    code << "@SuppressWarnings(\"unused\")\n";
    code << "public enum GlcdSize {" << endl;

    cout << "There are a total of " << to_string(sizes.size()) << "display sizes" << endl;
    for (auto size : sizes) {
        //cout << "SIZE: " << string(size.desc) << " - " << to_string(size.tile_width) << " x " << to_string(size.tile_height) << endl;
        static int i = 1;
        code << "\tSIZE_" << size.desc << "(" << to_string(size.tile_width) << ", "
             << to_string(size.tile_height) << ")";
        code << (i < sizes.size() ? ",\n" : "");
        i++;
    }

    //Private members
    code << ";\n\n";
    code << "\tprivate int tileWidth;\n";
    code << "\tprivate int tileHeight;\n\n";

    //Constructor
    code << "\tGlcdSize(int tileWidth, int tileHeight) {\n";
    code << "\t\tthis.tileWidth = tileWidth;\n";
    code << "\t\tthis.tileHeight = tileHeight;\n";
    code << "\t}\n\n";

    //Getter/Setters
    code << "\tpublic int getDisplayWidth() {\n";
    code << "\t\treturn tileWidth * 8;\n";
    code << "\t}\n\n";

    code << "\tpublic int getDisplayHeight() {\n";
    code << "\t\treturn tileHeight * 8;\n";
    code << "\t}\n\n";

    code << "\tpublic int getTileWidth() {\n";
    code << "\t\treturn tileWidth;\n";
    code << "\t}\n\n";

    code << "\tpublic int getTileHeight() {\n";
    code << "\t\treturn tileHeight;\n";
    code << "\t}\n";

    code << "\n\tpublic static GlcdSize get(int tileWidth, int tileHeight) {\n";
    code << "\t\treturn Arrays.stream(GlcdSize.values())\n";
    code << "\t\t\t.filter(p -> (p.getTileWidth() == tileWidth) && (p.getTileHeight() == tileHeight))\n";
    code << "\t\t\t.findFirst().orElse(null);\n";
    code << "\t}\n";

    code << "}";

    saveFileToOutputDir(FILE_JAVA_GLCDSIZE, code.str());
}

void stripFontPrefixes(string &fontName) {
    //pre-process: strip u8g2_font_ prefix
    size_t pos = fontName.find("u8g2_font_");
    if (pos != string::npos) {
        fontName.erase(0, pos + 10);
    }
    //pre-process: strip u8x8_font_ prefix
    pos = fontName.find("u8x8_font_");
    if (pos != string::npos) {
        fontName.erase(0, pos + 10);
    }
    //strip: u8glib_
    pos = fontName.find("u8glib_");
    if (pos != string::npos) {
        fontName.erase(0, pos + 7);
    }
}

//TODO: Automatically generate size + font name in the comment
string buildCode_GlcdFont_Comment(const string &fontName) {
    vector<string> tokens = tokenizeString(fontName, '_');
    cout << fontName << endl;
    for (const auto &token : tokens) {
        cout << "\t" << token << endl;
    }
    return "";
}

void buildCode_GlcdFont() {
    vector<string> fonts = getAvailableFonts(U8G2_FONT_PATH);

    if (fonts.empty())
        return;

    sort(fonts.begin(), fonts.end());

    stringstream code;
    code << AUTOGEN_MSG << endl;
    code << "package com.ibasco.ucgdisplay.drivers.glcd.enums;\n\n";

    //code << "import com.ibasco.ucgdisplay.core.ui.Font;\n\n";

    code << "@SuppressWarnings(\"unused\")\n";
    code << "public enum GlcdFont {" << endl;

    for (const auto &font : fonts) {
        static int i = 1;

        if (std::find(excludeFonts.begin(), excludeFonts.end(), font) != excludeFonts.end()) {
            cout << "Note: Excluding font name: " << font << endl;
            i++;
            continue;
        }

        string fontUpper = font;
        stripFontPrefixes(fontUpper);
        //buildCode_GlcdFont_Comment(fontUpper);

        transform(fontUpper.begin(), fontUpper.end(), fontUpper.begin(), ::toupper);
        fontUpper.insert(0, "FONT_");

        code << "\t" << fontUpper << "(\"" << font << "\")";
        code << (i < fonts.size() ? ",\n" : "");
        i++;
    }
    code << ";";
    code << "\n\n";
    code << "\tprivate String fontKey;\n\n";
    code << "\tGlcdFont(String fontKey) {\n";
    code << "\t\tthis.fontKey = fontKey;\n";
    code << "\t}\n\n";

    //code << "\t@Override\n";
    code << "\tpublic String getKey() {\n";
    code << "\t\treturn fontKey;\n";
    code << "\t}\n";
    code << "}";

    saveFileToOutputDir(FILE_JAVA_GLCDFONT, code.str());
}

/**
 * Build map of setup functions to be used by native library
 */
void buildCode_updateCppLookupSetupFunctions() {
    set<string> setupfunctions;
    getSetupFunctions(setupfunctions, BUFFER_CODE_FULL);

    stringstream code;
    appendAutogenMsg(code);

    code << "#include \"U8g2Hal.h\"\n";
    code << "#include <iostream>\n\n";

    code << "void u8g2hal_InitSetupFunctions(u8g2_setup_func_map_t &setup_map) {\n";
    code << "\tsetup_map.clear();\n";

    for (const auto &setup : setupfunctions) {
        code << "\tsetup_map[\"" << setup << "\"] = " << setup << ";" << endl;
    }

    code << "}";

    string data = code.str();
    saveFileToOutputDir(FILE_LOOKUP_SETUP, data);
}

void buildCode_updateCppLookupFonts() {
    string fontFileDir = U8G2_PROJECT_PATH + "/tools/font/build/single_font_files";
    vector<string> fontFiles = getAvailableFonts(fontFileDir);

    if (fontFiles.empty())
        return;

    stringstream code;
    appendAutogenMsg(code);

    code << "#include \"U8g2Hal.h\"\n";
    code << "#include <iostream>\n\n";

    code << "void u8g2hal_InitFonts(u8g2_lookup_font_map_t &font_map) {\n";
    code << "\tfont_map.clear();\n";

    for (const auto &fontName : fontFiles) {
        if (std::find(excludeFonts.begin(), excludeFonts.end(), fontName) != excludeFonts.end()) {
            cout << "> Skipped: " << fontName << endl;
            continue;
        }
//#if defined(_WIN32)
        if (std::find(win32_excluded_fonts.begin(), win32_excluded_fonts.end(), fontName) !=
            win32_excluded_fonts.end()) {
            cout << "> Skipped: " << fontName << endl;
            continue;
        }
//#endif
        code << "\tfont_map[\"" << fontName << "\"] = " << fontName << ";" << endl;
    }


//#if defined(_WIN32) || defined(__APPLE__)
//    code << "#if !defined(_WIN32) && !defined(__APPLE___)\n";
//    for (const auto &fontName : win32_excluded_fonts) {
//        code << "\tfont_map[\"" << fontName << "\"] = " << fontName << ";" << endl;
//    }
//    code << "#endif\n";
//#endif

    code << "}";

    string data = code.str();
    saveFileToOutputDir(FILE_LOOKUP_FONTS, data);
}

void buildCode_updateControllerDefinitions(const string &codeBuildSrcFilePath) {
    regex start_ctrl_regex(".*display_controller_list_start.*");
    regex end_ctrl_regex(".*display_controller_list_end.*");

    ifstream inFile;
    inFile.open(codeBuildSrcFilePath);

    if (!inFile) {
        cerr << "Unable to open file datafile.txt";
        exit(1);   // call system to stop
    }

    string line;
    stringstream ctrl_code;

    appendAutogenMsg(ctrl_code);

    ctrl_code << "#ifndef UCGDISP__CONTROLLERS_H" << endl;
    ctrl_code << "#define UCGDISP__CONTROLLERS_H" << endl;

    int lineNum = 0;
    bool done = false, startCapture = false;

    do {
        getline(inFile, line);
        lineNum++;

        if (regex_match(line, start_ctrl_regex)) {
            startCapture = true;
        } else if (regex_match(line, end_ctrl_regex)) {
            startCapture = false;
            done = true;
        }

        if (startCapture)
            ctrl_code << line << endl;
    } while (!inFile.eof() && !done);

    ctrl_code << "#endif //UCGDISP__CONTROLLERS_H" << endl;

    string outputCode = ctrl_code.str();
    inFile.close();

    saveFileToOutputDir(FILE_CONTROLLERS, outputCode);
    //saveFileToPath(BASE_PROJECT_PATH + "/utils/controllers.h", outputCode);
}

void populatePathMap(const string &baseProjectPath, map<string, string> &paths) {
    paths.clear();
    paths[BASE_PROJECT_PATH_KEY] = baseProjectPath;
    paths[BASE_PROJECT_PATH_NATIVE_KEY] = BASE_PROJECT_PATH + "/native/modules/graphics";
    paths[BASE_PROJECT_PATH_NATIVE_CPP_KEY] = BASE_PROJECT_PATH_NATIVE + "/src/main/cpp";
    paths[BASE_PROJECT_PATH_DRIVERS_KEY] = BASE_PROJECT_PATH + "/drivers";
    paths[U8G2_PROJECT_PATH_KEY] = BASE_PROJECT_PATH_NATIVE_CPP + "/lib/u8g2";
    paths[U8G2_FONT_PATH_KEY] = U8G2_PROJECT_PATH + "/tools/font/build/single_font_files";
    paths[U8G2_CODEBUILD_PATH_KEY] = U8G2_PROJECT_PATH + "/tools/codebuild";
    paths[U8G2_CODEBUILD_SRCFILE_KEY] = U8G2_CODEBUILD_PATH + "/codebuild.c";
}

std::string promptInput(const string &msg) {
    std::string input;
    bool valid;

    do {
        cout << msg;
        std::getline(std::cin, input);       // get a line of input
        valid = true;                       // assume it's valid
        for (auto &i : input)                // check each character in the input string
        {
            if (!std::isalpha(i))            // is it an alphabetical character?
            {
                valid = false;              // if not, mark it as invalid
                //std::cout << "Invalid input.  Please input only alphabetical characters." << std::endl;
                break;              // break out of the for() loop, as we have already established the input is invalid
            }
        }
    } while (!valid);     // keep going until we get input that's valid

    return input;       // once we have valid input, return it
}

bool exportFile(const string &srcFile, const string &dstFile) {
    string outputDir = GetCurrentWorkingDir() + "/output";

    string srcFilePath = outputDir + "/" + srcFile;

    if (fileExists(srcFilePath)) {
        ifstream src(srcFilePath, std::ios::binary);
        ofstream dst(dstFile, std::ios::binary);
        dst << src.rdbuf();

        if (fileExists(dstFile)) {
            cout << "\t> Exported \"" << srcFile << "\" to \"" << dstFile << "\"" << endl;
            return true;
        }
    }
    cerr << "Unable to export file: " << srcFile << endl;
    return false;
}

int main(int argc, char *argv[]) {
    string projectPath;
    bool autoExport = false;

    if (argc < 2) {
        cerr << "Invalid number of arguments" << endl;
        return -1;
    }

    const char *basePathArg = argv[1];
    projectPath = string(basePathArg);

    if (argc >= 3) {
        const char *autoAcceptArg = argv[2];
        if (strcmp(autoAcceptArg, "-a") == 0) {
            autoExport = true;
        }
    }

    if (autoExport)
        cout << "Auto export files on completion: YES" << endl;

    if (!projectPath.empty()) {
        cout << "You have specified a project path: " << projectPath << endl;
    } else {
        projectPath = "../../../../";
        cout << "Using default project path: " << projectPath << endl;
    }

    //Update project path
    populatePathMap(projectPath, paths);

    for (auto path : paths) {
        cout << "[PATH]: " << path.first << " = " << path.second << "\t(Dir Exists: " << (dirExists(path.second) ? "YES" : "NO") << ")" << endl;
    }

    //Update controller definitions
    //buildCode_updateControllerDefinitions(U8G2_CODEBUILD_SRCFILE);

    //Update lookup map for u8g2 setup functions
    buildCode_updateCppLookupSetupFunctions();

    //Update lookup map for u8g2 fonts
    buildCode_updateCppLookupFonts();

    //Start generating java code
    buildCode_GlcdSize();
    buildCode_GlcdControllerType();
    buildCode_Glcd();
    buildCode_GlcdFont();

    //Optional: Move the generated files to their respective locations (overwrite existing)
    cout << "\nDONE!\n\n";

    string letters = autoExport ? "y" : promptInput("Export files to their respective locations? [y/n]: ");

    if (letters == "y") {
        string baseGlcdPackageDirPath =
                BASE_PROJECT_PATH_DRIVERS + "/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd";
        string enumDirPath = baseGlcdPackageDirPath + "/enums";

        //exportFile(FILE_CONTROLLERS, BASE_PROJECT_PATH_NATIVE_CPP + "/utils/" + FILE_CONTROLLERS);
        exportFile(FILE_LOOKUP_SETUP, BASE_PROJECT_PATH_NATIVE_CPP + "/" + FILE_LOOKUP_SETUP);
        exportFile(FILE_LOOKUP_FONTS, BASE_PROJECT_PATH_NATIVE_CPP + "/" + FILE_LOOKUP_FONTS);
        exportFile(FILE_JAVA_GLCDSIZE, enumDirPath + "/" + FILE_JAVA_GLCDSIZE);
        exportFile(FILE_JAVA_GLCDCONTROLLERTYPE, enumDirPath + "/" + FILE_JAVA_GLCDCONTROLLERTYPE);
        exportFile(FILE_JAVA_GLCD, baseGlcdPackageDirPath + "/" + FILE_JAVA_GLCD);
        exportFile(FILE_JAVA_GLCDFONT, enumDirPath + "/" + FILE_JAVA_GLCDFONT);
    } else {
        cout << "Skipping export" << endl;
    }

    return 0;
}