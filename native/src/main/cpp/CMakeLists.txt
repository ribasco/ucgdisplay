cmake_minimum_required(VERSION 3.11)
project(pidisp)

#-DCMAKE_TOOLCHAIN_FILE=./cmake/RpiToolchain.cmake
#option(CROSS_COMPILE "Cross compile option" OFF)
#-DJAVA_HOME=/home/raffy/projects/sample-cmake/tools/jdk/jdk1.8.0_172

message(STATUS "[MAIN] CROSS COMPILE  = ${CMAKE_CROSSCOMPILING}")
message(STATUS "[MAIN] SYSTEM_NAME    = ${CMAKE_SYSTEM_NAME}")
message(STATUS "[MAIN] PROCESSOR      = ${CMAKE_SYSTEM_PROCESSOR}")
message(STATUS "[MAIN] JAVA_HOME      = $ENV{JAVA_HOME}")

#[[if (WIN32)
    message(STATUS "[MAIN] You are running on WINDOWS with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (UNIX)
    message(STATUS "[MAIN] You are running on unix/linux with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (MSVC)
    message(STATUS "[MAIN] You are running on MSVC with processor = ${CMAKE_SYSTEM_PROCESSOR}")
endif ()]]

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/cmake")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Libraries")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Executables")
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all static libraries")

# Adhere to GNU filesystem layout conventions
include(GNUInstallDirs)

find_package(LibEvent REQUIRED)
find_package(JNILib REQUIRED)

add_library(pidisp SHARED InputDevHelper.h InputDevHelper.cpp InputDeviceManager.h InputDeviceManager.cpp InputEventManager.h InputEventManager.cpp)
add_executable(pidisp-test InputDeviceTest.cpp InputDevHelper.h InputDevHelper.cpp InputDeviceManager.h InputDeviceManager.cpp InputEventManager.h InputEventManager.cpp)

if (LibEvent_FOUND)
    message(STATUS "[LIBEVENT] Files obtained from ${LibEvent_METHOD}")
    if (LibEvent_METHOD STREQUAL "REPO")
        message(STATUS "[LIBEVENT] Files obtained from REPOSITORY")
        add_dependencies(pidisp libevent)
        add_dependencies(pidisp-test libevent)
    endif ()
else ()
    message(FATAL_ERROR "Libevent not found. Method = ${Libevent_METHOD}")
endif ()

include_directories(${LIBEVENT_INCLUDE_DIRS} ${JNI_INCLUDE_DIRS})
target_link_libraries(pidisp ${LIBEVENT_LIBRARIES} ${JNI_LIBRARIES})
target_link_libraries(pidisp-test ${LIBEVENT_LIBRARIES})