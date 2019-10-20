# Sample toolchain file for building for Windows from an Ubuntu Linux system.
#
# Typical usage:
#    *) install cross compiler: `sudo apt-get install mingw-w64 g++-mingw-w64`
#    *) cd build
#    *) cmake -DCMAKE_TOOLCHAIN_FILE=~/MingWToolchain.cmake.cmake ..

message(STATUS "MingW Toolchain Loaded")

set(CMAKE_SYSTEM_NAME Windows)
set(CMAKE_SYSTEM_PROCESSOR x86_64)
set(TOOLCHAIN_PREFIX x86_64-w64-mingw32)
set(TOOLCHAIN_DESC "MingW-w64 Toolchain 64bit (${CMAKE_SYSTEM_NAME} - ${CMAKE_SYSTEM_PROCESSOR})")

# cross compilers to use for C and C++
set(CMAKE_C_COMPILER ${TOOLCHAIN_PREFIX}-gcc)
set(CMAKE_CXX_COMPILER ${TOOLCHAIN_PREFIX}-g++)
set(CMAKE_RC_COMPILER ${TOOLCHAIN_PREFIX}-windres)
set(CMAKE_C_LINK_EXECUTABLE ${TOOLCHAIN_PREFIX}-ld)

message(STATUS "FIND ROOT PATH:: /usr/${TOOLCHAIN_PREFIX}")
set(CMAKE_FIND_ROOT_PATH "/usr/x86_64-w64-mingw32/")

# target environment on the build host system
#   set 1st to dir with the cross compiler's C/C++ headers/libs

# modify default behavior of FIND_XXX() commands to
# search for headers/libs in the target environment and
# search for programs in the build host environment
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)