# Define our host system
set(CMAKE_SYSTEM_NAME Linux)
SET(CMAKE_SYSTEM_PROCESSOR arm)
set(CMAKE_SYSTEM_VERSION 1)

message(STATUS "[TOOLCHAIN] Raspberry Pi Toolchain loaded")

# Set module path
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/../../../../../cmake")

message(STATUS "[TOOLCHAIN] Using module path = ${CMAKE_MODULE_PATH}")

set(CMAKE_STAGING_PREFIX ${RPI_TOOLCHAIN_DIR})
set(CMAKE_SYSROOT "${CMAKE_STAGING_PREFIX}/arm-linux-gnueabihf/sysroot")

message(STATUS "STAGING PREFIX - ${CMAKE_STAGING_PREFIX}")

# Define the cross compiler locations
set(CMAKE_C_COMPILER ${CMAKE_STAGING_PREFIX}/bin/arm-linux-gnueabihf-gcc.exe)
set(CMAKE_CXX_COMPILER ${CMAKE_STAGING_PREFIX}/bin/arm-linux-gnueabihf-g++.exe)
set(CMAKE_MAKE_PROGRAM ${CMAKE_STAGING_PREFIX}/bin/make.exe)
set(CMAKE_C_LINK_EXECUTABLE ${CMAKE_STAGING_PREFIX}/bin/arm-linux-gnueabihf-ld.exe)
#set(CMAKE_CXX_LINK_EXECUTABLE ${CMAKE_STAGING_PREFIX}/bin/arm-linux-gnueabihf-ld.exe)

message(STATUS "[TOOLCHAIN] STAGING PREFIX  = ${CMAKE_STAGING_PREFIX}")
message(STATUS "[TOOLCHAIN] MAKE            = ${CMAKE_MAKE_PROGRAM}")
message(STATUS "[TOOLCHAIN] C COMPILER      = ${CMAKE_C_COMPILER}")
message(STATUS "[TOOLCHAIN] C++ COMPILER    = ${CMAKE_CXX_COMPILER}\n")
message(STATUS "[TOOLCHAIN] SYSROOT         = ${CMAKE_SYSROOT}")

# Define the sysroot path for the RaspberryPi distribution in our tools folder
set(CMAKE_FIND_ROOT_PATH ${CMAKE_SYSROOT})

# Use our definitions for compiler tools
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
# Search for libraries and headers in the target directories only
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)