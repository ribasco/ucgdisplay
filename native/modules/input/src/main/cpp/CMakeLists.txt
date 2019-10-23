cmake_minimum_required(VERSION 3.10)
project(ucgd-mod-input)

# The version number
set (UCGI_VERSION_MAJOR 1)
set (UCGI_VERSION_MINOR 4)

if (NOT UNIX)
    message(FATAL_ERROR "This module only supports Unix/Linux system")
endif ()

set(GLOBAL_INC_DIR "../../../../../include")
include(${GLOBAL_INC_DIR}/CMakeCommon.txt)

# configure a header file to pass some of the CMake settings
# to the source code
configure_file (
        "${PROJECT_SOURCE_DIR}/UcgiConfig.h.in"
        "${PROJECT_BINARY_DIR}/UcgiConfig.h"
)

# Ucgi Options here
option(USE_UDEV "Use UDev for Device Status monitoring instead of INotify" OFF)

# Libevent Package
find_package(Threads REQUIRED)
include(external/libevent)
set(THREADS_PREFER_PTHREAD_FLAG ON)
set(THREADS_PTHREAD_ARG "2" CACHE STRING "Forcibly set by CMakeLists.txt." FORCE)

list(APPEND UCGINPUT_HDR "${GLOBAL_INC_DIR}/Global.h" "InputDevHelper.h" "InputDeviceManager.h" "InputEventManager.h")
list(APPEND UCGINPUT_SRC "${GLOBAL_INC_DIR}/Global.cpp" "InputDevHelper.cpp" "InputDeviceManager.cpp" "InputEventManager.cpp")

# UCGInput Library
add_library(ucginput SHARED ${UCGINPUT_HDR} ${UCGINPUT_SRC})
target_sources(ucginput PRIVATE "${UCGINPUT_HDR}" "${UCGINPUT_SRC}")
target_link_libraries(ucginput PRIVATE ${LINK_LIBS} libevent)
target_include_directories(ucginput PRIVATE "${GLOBAL_INC_DIR}" "${JNI_INCLUDE_DIRS}")
target_compile_options(ucginput PRIVATE -Wno-write-strings)

# Input Test App
add_executable(ucginput-test "InputDevHelper.h" "InputEventManager.h" "InputDevHelper.cpp" "InputEventManager.cpp" "InputDeviceTest.cpp")
target_link_libraries(ucginput-test PRIVATE ${LINK_LIBS} libevent)
target_include_directories(ucginput-test PRIVATE "${GLOBAL_INC_DIR}" "${JNI_INCLUDE_DIRS}")
