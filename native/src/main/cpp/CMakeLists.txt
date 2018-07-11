cmake_minimum_required(VERSION 3.11)
project(pidisp)

if (WIN32)
    message(STATUS "[MAIN] You are running on WINDOWS with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (UNIX)
    message(STATUS "[MAIN] You are running on unix/linux with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (MSVC)
    message(STATUS "[MAIN] You are running on MSVC with processor = ${CMAKE_SYSTEM_PROCESSOR}")
endif ()

option(USE_DEV "Use UDev for Device Status monitoring instead of INotify" OFF)

message(STATUS "[MAIN] CROSS COMPILE  = ${CMAKE_CROSSCOMPILING}")
message(STATUS "[MAIN] SYSTEM_NAME    = ${CMAKE_SYSTEM_NAME}")
message(STATUS "[MAIN] PROCESSOR      = ${CMAKE_SYSTEM_PROCESSOR}")
message(STATUS "[MAIN] JAVA_HOME      = $ENV{JAVA_HOME}")

set(CMAKE_VERBOSE_MAKEFILE OFF)
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/cmake")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Libraries")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Executables")
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all static libraries")

# Adhere to GNU filesystem layout conventions
include(GNUInstallDirs)

set(LIB_DIR "${CMAKE_SOURCE_DIR}/lib")

#find_package(LibUdev REQUIRED)
find_package(U8G2 REQUIRED)
find_package(LibEvent REQUIRED)
find_package(JNILib REQUIRED)
find_package(Threads REQUIRED)

set(THREADS_PREFER_PTHREAD_FLAG ON)

# Obtain the required libraries, if none found in the system, cmake will try to download them for you
if (CMAKE_CROSSCOMPILING)
    include("./cmake/GetWiringPi.cmake")
else ()
    # we are not cross compiling, so we must be compiling on ARM system
    #[[ message(STATUS "System Processor = ${CMAKE_SYSTEM_PROCESSOR}")
     if (NOT ${CMAKE_SYSTEM_PROCESSOR} MATCHES "arm")
         message(FATAL_ERROR "You are only allowed to build on top of an ARM system (Raspberry Pi)")
     endif ()]]

    # Check Wiring Pi
    find_package(WiringPi)
    if (NOT WiringPi_FOUND)
        message(STATUS "Wiring Pi library not found in system. Gonna try to download...")
        include("./cmake/GetWiringPi.cmake")
    endif ()
endif ()

message(STATUS "[WIRING-PI] WIRINGPI_LIBRARIES = ${WIRINGPI_LIBRARIES}")
message(STATUS "[WIRING-PI] WIRINGPI_INCLUDE_DIR = ${WIRINGPI_INCLUDE_DIR}")

list(APPEND PIDISP_HDR "Global.h" "InputDevHelper.h" "InputDeviceManager.h" "InputEventManager.h" "U8g2Utils.h" "U8g2Interface.h" "CommSpi.h" "U8g2Hal.h" ${U8G2_HEADER_FILES})
list(APPEND PIDISP_SRC "Global.cpp" "InputDevHelper.cpp" "InputDeviceManager.cpp" "InputEventManager.cpp" "U8g2Utils.cpp" "U8g2Interface.cpp" "CommSpi.cpp" "U8g2Hal.cpp" "U8g2LookupSetup.cpp" "U8g2LookupFonts.cpp" ${U8G2_SRC_FILES})

add_library(pidisp SHARED ${PIDISP_HDR} ${PIDISP_SRC})
add_executable(pidisp-test "InputDevHelper.h" "InputEventManager.h" "InputDevHelper.cpp" "InputEventManager.cpp" "InputDeviceTest.cpp")
add_executable(pidisp-gen "utils/codegen.h" "utils/codegen.cpp")

target_compile_options(pidisp PRIVATE -Wno-write-strings)
target_compile_options(pidisp-gen PRIVATE -Wno-write-strings)
#message("Supported features = ${CMAKE_CXX_COMPILE_FEATURES}")

# Add Wiring Pi dependency
if (TARGET wiringPi)
    message(STATUS "[WIRING-PI] Adding wiringPi Target")
    add_dependencies(pidisp wiringPi)
endif ()

# Add libevent dependency (called usually if the library was built from the repository)
if (LibEvent_FOUND AND TARGET libevent)
    message(STATUS "[LIBEVENT] Found 'libevent' target. Adding as dependency")
    add_dependencies(pidisp libevent)
    add_dependencies(pidisp-test libevent)
endif ()

# Add u8g2 dependency
if (TARGET u8g2)
    add_dependencies(pidisp u8g2)
endif ()

#message(STATUS "COPY SOURCE DIR = ${CMAKE_SOURCE_DIR}")
#add_custom_command(TARGET pidisp POST_BUILD COMMAND ${CMAKE_COMMAND} -E copy "$<TARGET_FILE:pidisp>" "${CMAKE_SOURCE_DIR}/../resources/")

include_directories(${LIBEVENT_INCLUDE_DIRS} ${JNI_INCLUDE_DIRS} ${U8G2_INCLUDE_DIR} ${WIRINGPI_INCLUDE_DIR} ${U8G2_INCLUDE_DIR})
target_link_libraries(pidisp ${LIBEVENT_LIBRARIES} ${JNI_LIBRARIES} ${U8G2_LIBRARIES} ${WIRINGPI_LIBRARIES} Threads::Threads)
target_link_libraries(pidisp-test ${LIBEVENT_LIBRARIES} ${U8G2_LIBRARIES} Threads::Threads)