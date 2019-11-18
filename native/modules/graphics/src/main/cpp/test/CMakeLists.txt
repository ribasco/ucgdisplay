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
        "${PROVIDER_DIR_PATH}/UcgdPeripheral.h"
        "${PROVIDER_DIR_PATH}/UcgdPeripheral.cpp"
        "${PROVIDER_DIR_PATH}/UcgdProvider.h"
        "${PROVIDER_DIR_PATH}/UcgdProvider.cpp"
        "${PROVIDER_DIR_PATH}/UcgdSpiPeripheral.h"
        "${PROVIDER_DIR_PATH}/UcgdGpioPeripheral.h"
        "${PROVIDER_DIR_PATH}/UcgdI2CPeripheral.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperipheryProvider.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperipheryProvider.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperGpioPeripheral.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperGpioPeripheral.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperI2CPeripheral.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperI2CPeripheral.h"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperSpiPeripheral.cpp"
        "${PROVIDER_CPERIPHERY_DIR_PATH}/UcgdCperSpiPeripheral.h"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodGpioPeripheral.cpp"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodGpioPeripheral.h"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodProvider.cpp"
        "${PROVIDER_LIBGPIOD_DIR_PATH}/UcgdLibgpiodProvider.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodGpioPeripheral.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodGpioPeripheral.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodI2CPeripheral.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodI2CPeripheral.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodProvider.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodProvider.h"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodSpiPeripheral.cpp"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}/UcgdPigpiodSpiPeripheral.h"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioGpioPeripheral.cpp"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioGpioPeripheral.h"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioI2CPeripheral.cpp"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioI2CPeripheral.h"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioProvider.cpp"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioProvider.h"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioSpiPeripheral.cpp"
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}/UcgdPigpioSpiPeripheral.h"
        "${ucgd-mod-graphics_SOURCE_DIR}/ServiceLocator.h"
        "${ucgd-mod-graphics_SOURCE_DIR}/DeviceManager.h"
        "${ucgd-mod-graphics_SOURCE_DIR}/ProviderManager.h"
        "${ucgd-mod-graphics_SOURCE_DIR}/ProviderManager.cpp"
        "${ucgd-mod-graphics_SOURCE_DIR}/ServiceLocator.cpp"
        "${ucgd-mod-graphics_SOURCE_DIR}/DeviceManager.cpp"
        "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioCommon.h"
        "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioProviderBase.h"
        "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioCommon.cpp"
        "${PROVIDER_PIGPIO_DIR_PATH}/UcgdPigpioProviderBase.cpp"
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
        "${PROVIDER_PIGPIO_STANDALN_DIR_PATH}"
        "${PROVIDER_PIGPIOD_DAEMON_DIR_PATH}"
        "${PROVIDER_LIBGPIOD_DIR_PATH}")

target_sources(ucgd-test PUBLIC
        "../${GLOBAL_INC_DIR}/Global.h"
        "../${GLOBAL_INC_DIR}/Global.cpp"
        "../${GLOBAL_INC_DIR}/Common.h"
        "../${GLOBAL_INC_DIR}/Common.cpp"
        "../${GLOBAL_INC_DIR}/Utils.h"
        "../${GLOBAL_INC_DIR}/Utils.cpp"
        PRIVATE
        ${TEST_SOURCES})


target_link_libraries(ucgd-test -ldl libgpiod pigpio pigpiod_if2 u8g2 cperiphery)
