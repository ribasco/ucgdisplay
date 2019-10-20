cmake_minimum_required(VERSION 3.10)
project(ucgd-mod-graphics)

include(CheckIncludeFile)

set(GLOBAL_INC_DIR "../../../../../include")

include(${GLOBAL_INC_DIR}/CMakeCommon.txt)

# The version number
set (UCGD_VERSION_MAJOR 1)
set (UCGD_VERSION_MINOR 4)

# configure a header file to pass some of the CMake settings
# to the source code
configure_file (
        "${PROJECT_SOURCE_DIR}/UcgdConfig.h.in"
        "${PROJECT_BINARY_DIR}/UcgdConfig.h"
)

# Project Specific Options
option(USE_PIGPIO "Use pigpio library for gpio, spi and i2c peripherals" OFF)

list(APPEND UCGDISP_HDR "${GLOBAL_INC_DIR}/Global.h" "U8g2Utils.h" "U8g2Graphics.h" "U8g2Hal.h")
list(APPEND UCGDISP_SRC "${GLOBAL_INC_DIR}/Global.cpp" "U8g2Utils.cpp" "U8g2Graphics.cpp" "U8g2Hal.cpp" "U8g2LookupSetup.cpp" "U8g2LookupFonts.cpp")

add_library(ucgdisp SHARED ${UCGDISP_HDR} ${UCGDISP_SRC})
target_sources(ucgdisp PRIVATE ${UCGDISP_HDR} ${UCGDISP_SRC})
target_include_directories(ucgdisp PRIVATE "${GLOBAL_INC_DIR}" "${JNI_INCLUDE_DIRS}")
target_compile_options(ucgdisp PRIVATE -Wno-write-strings)

add_subdirectory(utils)

# Add C-periphery and Libgpiod packages (ARM only)
if (UNIX AND (${CMAKE_SYSTEM_PROCESSOR} MATCHES "^arm"))
    # Add c-periphery
    include(external/c-periphery)
    if (TARGET cperiphery)
        message(STATUS "Found c-periphery")
        target_link_libraries(ucgdisp cperiphery)
    endif ()

    # Add libgpiod
    include(external/libgpiod)
    if (TARGET libgpiod)
        target_link_libraries(ucgdisp libgpiod)
    endif ()

    # PIGPIO (Optional)
    include(external/pigpio)
    if (TARGET pigpio)
        target_link_libraries(ucgdisp pigpio)
    endif ()
endif ()

# Add the U8g2 library (all platforms)
include(external/u8g2)
if (TARGET u8g2)
    target_link_libraries(ucgdisp u8g2)
endif ()

if (WIN32)
    message(STATUS "[target_link_libraries] Setting MINGW dependencies static")
    target_link_libraries(ucgdisp ${LINK_LIBS} -static-libgcc -static-libstdc++ -static)
else ()
    target_link_libraries(ucgdisp ${LINK_LIBS})
endif ()