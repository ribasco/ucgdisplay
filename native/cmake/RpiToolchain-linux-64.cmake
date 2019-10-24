include(${CMAKE_CURRENT_LIST_DIR}/../include/CMakeFunctions.txt)

if (NOT UNIX)
    message(FATAL_ERROR "[RPI-TOOLCHAIN] Unsupported platform for this RPI Toolchain")
endif ()

# Define our host system
set(CMAKE_SYSTEM_NAME Linux)
SET(CMAKE_SYSTEM_PROCESSOR arm)
set(CMAKE_SYSTEM_VERSION 1)

set(TOOLCHAIN_DESC "Raspberry Pi Toolchain 64bit (${CMAKE_SYSTEM_NAME} - ${CMAKE_SYSTEM_PROCESSOR})")
set(TOOLS_DIR_PATH ${CMAKE_CURRENT_LIST_DIR}/../tools)
#unset(RPI_TOOLCHAIN_PATH CACHE)
set(RPI_TOOLCHAIN_PATH "${TOOLS_DIR_PATH}/rpi-tools" CACHE PATH "The path of the Raspberry Pi Toolchain")
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/../../../../../cmake") # Set module path
set(CMAKE_STAGING_PREFIX ${RPI_TOOLCHAIN_PATH})

# Convert to real path
get_filename_component(CMAKE_MODULE_PATH "" REALPATH BASE_DIR "${CMAKE_MODULE_PATH}")
get_filename_component(TOOLS_DIR_PATH "" REALPATH BASE_DIR "${TOOLS_DIR_PATH}")
get_filename_component(RPI_TOOLCHAIN_PATH "" REALPATH BASE_DIR "${RPI_TOOLCHAIN_PATH}")

set(TC_PREFIX "gcc7-linaro-arm-linux-gnueabi-raspbian-x64")

# Cross-compiler variables
set(CMAKE_C_COMPILER ${RPI_TOOLCHAIN_PATH}/${TC_PREFIX}/bin/aarch64-rpi-linux-gnu-gcc)
set(CMAKE_CXX_COMPILER ${RPI_TOOLCHAIN_PATH}/${TC_PREFIX}/bin/aarch64-rpi-linux-gnu-g++)
set(CMAKE_SYSROOT ${RPI_TOOLCHAIN_PATH}/${TC_PREFIX}/aarch64-rpi-linux-gnu/sysroot)

# =================================================================
# Configure Toolchain
# =================================================================

# Define the sysroot path for the RaspberryPi distribution in our tools folder
set(CMAKE_FIND_ROOT_PATH ${CMAKE_SYSROOT})

# Use our definitions for compiler tools
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)

# Search for libraries and headers in the target directories only
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)
