# Define our host system
set(CMAKE_SYSTEM_NAME Linux)
SET(CMAKE_SYSTEM_PROCESSOR arm)
set(CMAKE_SYSTEM_VERSION 1)

message(STATUS "[RPI-TOOLCHAIN] Raspberry Pi Toolchain loaded (${CMAKE_SYSTEM_NAME} - ${CMAKE_SYSTEM_PROCESSOR})")

# Set module path
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/../../../../../cmake")
get_filename_component(CMAKE_MODULE_PATH "" REALPATH BASE_DIR "${CMAKE_MODULE_PATH}")

message(STATUS "[RPI-TOOLCHAIN] Using module path = ${CMAKE_MODULE_PATH}")

# Load Raspberry Pi Toolchain
find_package(RpiTools REQUIRED)

set(CMAKE_STAGING_PREFIX ${RPI_TOOLCHAIN_DIR})
set(CMAKE_SYSROOT ${CMAKE_STAGING_PREFIX}/arm-bcm2708/arm-linux-gnueabihf/arm-linux-gnueabihf/sysroot)

# Define the cross compiler locations
set(CMAKE_C_COMPILER ${CMAKE_STAGING_PREFIX}/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian/bin/arm-linux-gnueabihf-gcc)
set(CMAKE_CXX_COMPILER ${CMAKE_STAGING_PREFIX}/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian/bin/arm-linux-gnueabihf-g++)
#set(CMAKE_C_COMPILER ${CMAKE_STAGING_PREFIX}/arm-bcm2708/arm-linux-gnueabihf/bin/arm-linux-gnueabihf-gcc)
#set(CMAKE_CXX_COMPILER ${CMAKE_STAGING_PREFIX}/arm-bcm2708/arm-linux-gnueabihf/bin/arm-linux-gnueabihf-g++)

if (NOT RPI_TOOLCHAIN_FOUND)
    message(FATAL_ERROR "Please provide the path of your raspberry pi toolchain")
endif ()

# https://github.com/raspberrypi/tools.git

message(STATUS "[RPI-TOOLCHAIN] Using toolchain path = ${RPI_TOOLCHAIN_DIR}")
message(STATUS "[RPI-TOOLCHAIN] STAGING PREFIX  = ${CMAKE_STAGING_PREFIX}")
message(STATUS "[RPI-TOOLCHAIN] MAKE            = ${CMAKE_MAKE_PROGRAM}")
message(STATUS "[RPI-TOOLCHAIN] C COMPILER      = ${CMAKE_C_COMPILER}")
message(STATUS "[RPI-TOOLCHAIN] C++ COMPILER    = ${CMAKE_CXX_COMPILER}")
message(STATUS "[RPI-TOOLCHAIN] SYSROOT         = ${CMAKE_SYSROOT}")

# Define the sysroot path for the RaspberryPi distribution in our tools folder
set(CMAKE_FIND_ROOT_PATH ${CMAKE_SYSROOT})

# Use our definitions for compiler tools
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
# Search for libraries and headers in the target directories only
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)