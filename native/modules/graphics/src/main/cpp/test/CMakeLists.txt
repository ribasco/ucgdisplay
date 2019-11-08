cmake_minimum_required(VERSION 3.10)
project(ucgd-test)

if(POLICY CMP0076)
    cmake_policy(SET CMP0076 NEW)
endif()

list(APPEND TEST_SOURCES
        "U8g2Test.cpp"
        "U8g2TestHal.h"
        "U8g2TestHal.cpp"
        )

list(APPEND TEST_SOURCES
        "${ucgd-mod-graphics_SOURCE_DIR}/UcgdTypes.h"
        "${PROVIDER_DIR_PATH}/UcgProviderBase.h"
        "${PROVIDER_DIR_PATH}/UcgIOProvider.h"
        "${PROVIDER_DIR_PATH}/UcgSpiProvider.h"
        "${PROVIDER_DIR_PATH}/UcgGpioProvider.h"
        "${PROVIDER_DIR_PATH}/UcgI2CProvider.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperipheryProvider.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperipheryProvider.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperGpioProvider.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperGpioProvider.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperI2CProvider.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperI2CProvider.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperSpiProvider.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgCperSpiProvider.h"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgLibgpiodGpioProvider.cpp"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgLibgpiodGpioProvider.h"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgLibgpiodProvider.cpp"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgLibgpiodProvider.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodGpioProvider.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodGpioProvider.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodI2CProvider.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodI2CProvider.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodProvider.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodProvider.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodSpiProvider.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgPigpiodSpiProvider.h"
        )

add_executable(ucgd-test "")

target_include_directories(ucgd-test PRIVATE
        "../${GLOBAL_INC_DIR}"
        "${ucgd-mod-graphics_SOURCE_DIR}"
        "${JNI_INCLUDE_DIRS}"
        "${PROVIDER_DIR_PATH}"
        "${PROVIDER_CPERIPHERY_DIR_PATH}"
        "${PROVIDER_LIBGPIOD_DIR_PATH}"
        "${PROVIDER_PIGPIO_DIR_PATH}"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}")

target_sources(ucgd-test PUBLIC
        "../${GLOBAL_INC_DIR}/Global.h"
        "../${GLOBAL_INC_DIR}/Global.cpp"
        "../${GLOBAL_INC_DIR}/Common.h"
        "../${GLOBAL_INC_DIR}/Common.cpp"
        PRIVATE
        ${TEST_SOURCES})

target_link_libraries(ucgd-test libgpiod pigpiod_if2 u8g2 cperiphery)
