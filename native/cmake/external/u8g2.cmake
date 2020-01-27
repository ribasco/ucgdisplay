#
# THIS IS AN AUTO-GENERATED CODE!! DO NOT MODIFY (Last updated: Mon, 27 Jan 2020 07:31:22 +0800)
#

include(ExternalProject)

set(PROJ_PREFIX "u8g2")

#1: https://github.com/olikraus/u8g2.git
#2: /home/raffy/projects/u8g2.git
#3: https://github.com/ribasco/u8g2.git

ExternalProject_Add(
        project_u8g2
        GIT_REPOSITORY "https://github.com/ribasco/u8g2.git"
        GIT_TAG "master"
        PREFIX ${PROJ_PREFIX}
        INSTALL_COMMAND ""
        CONFIGURE_COMMAND ""
        BUILD_COMMAND ""
)

ExternalProject_Get_Property(project_u8g2 SOURCE_DIR INSTALL_DIR)

# Hack (See: https://stackoverflow.com/questions/45516209/cmake-how-to-use-interface-include-directories-with-externalproject)
file(MAKE_DIRECTORY ${SOURCE_DIR}/csrc)

# Define all the sources
list(APPEND U8G2_SRC
    "${SOURCE_DIR}/csrc/u8g2.h"
    "${SOURCE_DIR}/csrc/u8g2_bitmap.c"
    "${SOURCE_DIR}/csrc/u8g2_box.c"
    "${SOURCE_DIR}/csrc/u8g2_buffer.c"
    "${SOURCE_DIR}/csrc/u8g2_circle.c"
    "${SOURCE_DIR}/csrc/u8g2_cleardisplay.c"
    "${SOURCE_DIR}/csrc/u8g2_d_memory.c"
    "${SOURCE_DIR}/csrc/u8g2_d_setup.c"
    "${SOURCE_DIR}/csrc/u8g2_font.c"
    "${SOURCE_DIR}/csrc/u8g2_fonts.c"
    "${SOURCE_DIR}/csrc/u8g2_hvline.c"
    "${SOURCE_DIR}/csrc/u8g2_input_value.c"
    "${SOURCE_DIR}/csrc/u8g2_intersection.c"
    "${SOURCE_DIR}/csrc/u8g2_kerning.c"
    "${SOURCE_DIR}/csrc/u8g2_line.c"
    "${SOURCE_DIR}/csrc/u8g2_ll_hvline.c"
    "${SOURCE_DIR}/csrc/u8g2_message.c"
    "${SOURCE_DIR}/csrc/u8g2_polygon.c"
    "${SOURCE_DIR}/csrc/u8g2_selection_list.c"
    "${SOURCE_DIR}/csrc/u8g2_setup.c"
    "${SOURCE_DIR}/csrc/u8log.c"
    "${SOURCE_DIR}/csrc/u8log_u8g2.c"
    "${SOURCE_DIR}/csrc/u8log_u8x8.c"
    "${SOURCE_DIR}/csrc/u8x8.h"
    "${SOURCE_DIR}/csrc/u8x8_8x8.c"
    "${SOURCE_DIR}/csrc/u8x8_byte.c"
    "${SOURCE_DIR}/csrc/u8x8_cad.c"
    "${SOURCE_DIR}/csrc/u8x8_capture.c"
    "${SOURCE_DIR}/csrc/u8x8_d_a2printer.c"
    "${SOURCE_DIR}/csrc/u8x8_d_il3820_296x128.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ist3020.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ist7920.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ks0108.c"
    "${SOURCE_DIR}/csrc/u8x8_d_lc7981.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ld7032_60x32.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ls013b7dh03.c"
    "${SOURCE_DIR}/csrc/u8x8_d_max7219.c"
    "${SOURCE_DIR}/csrc/u8x8_d_pcd8544_84x48.c"
    "${SOURCE_DIR}/csrc/u8x8_d_pcf8812.c"
    "${SOURCE_DIR}/csrc/u8x8_d_pcf8814_hx1230.c"
    "${SOURCE_DIR}/csrc/u8x8_d_sbn1661.c"
    "${SOURCE_DIR}/csrc/u8x8_d_sed1330.c"
    "${SOURCE_DIR}/csrc/u8x8_d_sh1106_64x32.c"
    "${SOURCE_DIR}/csrc/u8x8_d_sh1106_72x40.c"
    "${SOURCE_DIR}/csrc/u8x8_d_sh1107.c"
    "${SOURCE_DIR}/csrc/u8x8_d_sh1108.c"
    "${SOURCE_DIR}/csrc/u8x8_d_sh1122.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1305.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1306_128x32.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1306_128x64_noname.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1306_48x64.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1306_64x32.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1306_64x48.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1306_72x40.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1306_96x16.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1309.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1316.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1317.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1318.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1322.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1325.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1326.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1327.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1329.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1606_172x72.c"
    "${SOURCE_DIR}/csrc/u8x8_d_ssd1607_200x200.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7511.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st75256.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7528.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st75320.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7565.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7567.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7586s_erc240160.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7586s_s028hn118a.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7588.c"
    "${SOURCE_DIR}/csrc/u8x8_d_st7920.c"
    "${SOURCE_DIR}/csrc/u8x8_d_stdio.c"
    "${SOURCE_DIR}/csrc/u8x8_d_t6963.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1601.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1604.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1608.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1610.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1611.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1617.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1638.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1701_dogs102.c"
    "${SOURCE_DIR}/csrc/u8x8_d_uc1701_mini12864.c"
    "${SOURCE_DIR}/csrc/u8x8_debounce.c"
    "${SOURCE_DIR}/csrc/u8x8_display.c"
    "${SOURCE_DIR}/csrc/u8x8_fonts.c"
    "${SOURCE_DIR}/csrc/u8x8_gpio.c"
    "${SOURCE_DIR}/csrc/u8x8_input_value.c"
    "${SOURCE_DIR}/csrc/u8x8_message.c"
    "${SOURCE_DIR}/csrc/u8x8_selection_list.c"
    "${SOURCE_DIR}/csrc/u8x8_setup.c"
    "${SOURCE_DIR}/csrc/u8x8_string.c"
    "${SOURCE_DIR}/csrc/u8x8_u16toa.c"
    "${SOURCE_DIR}/csrc/u8x8_u8toa.c"
    )

add_library(u8g2 STATIC ${U8G2_SRC})
add_dependencies(u8g2 project_u8g2)
target_include_directories(u8g2 PUBLIC "${SOURCE_DIR}/csrc")

# Mark these source files as generated or cmake will throw an error during reload
# - Ref 1: https://cmake.org/cmake/help/v3.12/prop_sf/GENERATED.html
# - Ref 2: https://stackoverflow.com/questions/47812230/cmake-make-add-library-depend-on-externalproject-add
set_source_files_properties(${U8G2_SRC} PROPERTIES GENERATED TRUE)
