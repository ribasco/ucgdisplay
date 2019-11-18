cmake_minimum_required(VERSION 3.10)
project(ucgd-mod-graphics)

include(CheckIncludeFile)

set(GLOBAL_INC_DIR "../../../../../include")

include(${GLOBAL_INC_DIR}/CMakeCommon.txt)

# The version number
set(UCGD_VERSION_MAJOR 1)
set(UCGD_VERSION_MINOR 5)
set(UCGD_VERSION_PATCH 0)
set(UCGD_VERSION_QUALIFIER "alpha")

# Project Specific Options
option(UCGD_DEBUG "Enable debbugging" OFF)

# configure a header file to pass some of the CMake settings
# to the source code
configure_file(
        "${PROJECT_SOURCE_DIR}/UcgdConfig.h.in"
        "${PROJECT_BINARY_DIR}/UcgdConfig.h"
)

list(APPEND UCGDISP_HDR
        "${GLOBAL_INC_DIR}/Global.h"
        "${GLOBAL_INC_DIR}/Common.h"
        "${GLOBAL_INC_DIR}/Log.h"
        "${GLOBAL_INC_DIR}/Utils.h"
        "U8g2Utils.h"
        "U8g2Graphics.h"
        "U8g2Hal.h"
        "UcgdTypes.h"
        "ServiceLocator.h"
        "DeviceManager.h"
        )
list(APPEND UCGDISP_SRC
        "${GLOBAL_INC_DIR}/Global.cpp"
        "${GLOBAL_INC_DIR}/Common.cpp"
        "${GLOBAL_INC_DIR}/Log.cpp"
        "${GLOBAL_INC_DIR}/Utils.cpp"
        "U8g2Utils.cpp"
        "U8g2Graphics.cpp"
        "U8g2Hal.cpp"
        "U8g2LookupSetup.cpp"
        "U8g2LookupFonts.cpp"
        "ServiceLocator.cpp"
        "DeviceManager.cpp"
        )

add_library(ucgdisp SHARED ${UCGDISP_HDR} ${UCGDISP_SRC})
set_target_properties(ucgdisp PROPERTIES LINK_FLAGS_RELEASE -s)

# Set provider paths
set(PROVIDER_DIR_PATH "${ucgd-mod-graphics_SOURCE_DIR}/providers")
set(PROVIDER_CPERIPHERY_DIR_PATH "${PROVIDER_DIR_PATH}/cperiphery")
set(PROVIDER_LIBGPIOD_DIR_PATH "${PROVIDER_DIR_PATH}/libgpiod")
set(PROVIDER_PIGPIO_DIR_PATH "${PROVIDER_DIR_PATH}/pigpio")
set(PROVIDER_PIGPIO_STANDALN_DIR_PATH "${PROVIDER_PIGPIO_DIR_PATH}/standalone")
set(PROVIDER_PIGPIOD_DAEMON_DIR_PATH "${PROVIDER_PIGPIO_DIR_PATH}/daemon")

if (UNIX AND (${CMAKE_SYSTEM_PROCESSOR} MATCHES "^arm"))
    list(APPEND UCGDISP_HDR
            "${PROVIDER_DIR_PATH}/UcgdPeripheral.h"
            "${PROVIDER_DIR_PATH}/UcgdProvider.h"
            "${PROVIDER_DIR_PATH}/UcgdGpioPeripheral.h"
            "${PROVIDER_DIR_PATH}/UcgdSpiPeripheral.h"
            "${PROVIDER_DIR_PATH}/UcgdI2CPeripheral.h"
            "ProviderManager.h"
            )
    list(APPEND UCGDISP_SRC
            "${PROVIDER_DIR_PATH}/UcgdPeripheral.cpp"
            "${PROVIDER_DIR_PATH}/UcgdProvider.cpp"
            "ProviderManager.cpp"
            )

    # LIBGPIOD
    include(external/libgpiod)
    if (TARGET libgpiod)
        list(APPEND UCGDISP_HDR
                "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodProvider.h"
                "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodGpioPeripheral.h")
        list(APPEND UCGDISP_SRC
                "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodProvider.cpp"
                "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodGpioPeripheral.cpp")
        target_link_libraries(ucgdisp libgpiod)
    endif ()

    # PIGPIO
    include(external/pigpio)
    if (TARGET pigpio)
        # PIGPIO - Common Files
        list(APPEND UCGDISP_HDR
                "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioCommon.h"
                "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioProviderBase.h"
                )
        list(APPEND UCGDISP_SRC
                "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioCommon.cpp"
                "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioProviderBase.cpp"
                )

        # PIGPIO - Standalone
        list(APPEND UCGDISP_HDR
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioGpioPeripheral.h"
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioI2CPeripheral.h"
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioProvider.h"
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioSpiPeripheral.h")
        list(APPEND UCGDISP_SRC
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioGpioPeripheral.cpp"
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioI2CPeripheral.cpp"
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioProvider.cpp"
                "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioSpiPeripheral.cpp")
        target_link_libraries(ucgdisp pigpio)

        # PIGPIO - Daemon
        list(APPEND UCGDISP_HDR
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodGpioPeripheral.h"
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodI2CPeripheral.h"
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodProvider.h"
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodSpiPeripheral.h")
        list(APPEND UCGDISP_SRC
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodGpioPeripheral.cpp"
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodI2CPeripheral.cpp"
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodProvider.cpp"
                "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodSpiPeripheral.cpp")
        target_link_libraries(ucgdisp pigpiod_if2)
    endif ()

    # C-PERIPHERY
    include(external/c-periphery)
    if (TARGET cperiphery)
        list(APPEND UCGDISP_HDR
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperipheryProvider.h"
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperGpioPeripheral.h"
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperSpiPeripheral.h"
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperI2CPeripheral.h")
        list(APPEND UCGDISP_SRC
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperipheryProvider.cpp"
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperGpioPeripheral.cpp"
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperSpiPeripheral.cpp"
                "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperI2CPeripheral.cpp")
        target_link_libraries(ucgdisp cperiphery)
    endif ()
endif ()

# Add the U8g2 library (all platforms)
include(external/u8g2)
if (TARGET u8g2)
    target_link_libraries(ucgdisp u8g2)
endif ()

target_sources(ucgdisp PRIVATE ${UCGDISP_HDR} ${UCGDISP_SRC})
target_include_directories(ucgdisp PRIVATE
        "${GLOBAL_INC_DIR}"
        "${JNI_INCLUDE_DIRS}"
        "${ucgd-mod-graphics_SOURCE_DIR}"
        "${PROVIDER_DIR_PATH}"
        "${PROVIDER_CPERIPHERY_DIR_PATH}"
        "${PROVIDER_PIGPIO_DIR_PATH}"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}"
        "${PROVIDER_LIBGPIOD_DIR_PATH}")
target_compile_options(ucgdisp PRIVATE -Wno-write-strings)

add_subdirectory(test)
add_subdirectory(utils)

if(EXISTS scratch)
    add_subdirectory(scratch)
endif()

# add_subdirectory(scratch)

if (WIN32)
    message(STATUS "[target_link_libraries] Setting MINGW dependencies static")
    target_link_libraries(ucgdisp ${LINK_LIBS} -static-libgcc -static-libstdc++ -static)
else ()
    target_link_libraries(ucgdisp ${LINK_LIBS})
endif ()