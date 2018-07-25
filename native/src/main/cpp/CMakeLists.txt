cmake_minimum_required(VERSION 3.11)
project(pidisp)

if (WIN32)
    message(STATUS "[MAIN] You are running on WINDOWS with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (UNIX)
    message(STATUS "[MAIN] You are running on unix/linux with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (MSVC)
    message(STATUS "[MAIN] You are running on MSVC with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (APPLE)
    message(STATUS "[MAIN] You are running on MAC OS with processor = ${CMAKE_SYSTEM_PROCESSOR}")
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
find_package(JNILib REQUIRED)
find_package(Threads REQUIRED)
set(THREADS_PREFER_PTHREAD_FLAG ON)

if (UNIX)
    find_package(LibEvent REQUIRED)
    find_package(WiringPi REQUIRED)
    message(STATUS "[WIRING-PI] WIRINGPI_LIBRARIES = ${WIRINGPI_LIBRARIES}")
    message(STATUS "[WIRING-PI] WIRINGPI_INCLUDE_DIR = ${WIRINGPI_INCLUDE_DIR}")

    list(APPEND PIDISP_HDR "InputDevHelper.h" "InputDeviceManager.h" "InputEventManager.h" "CommSpi.h")
    list(APPEND PIDISP_SRC "InputDevHelper.cpp" "InputDeviceManager.cpp" "InputEventManager.cpp" "CommSpi.cpp")
endif ()

list(APPEND PIDISP_HDR "Global.h" "U8g2Utils.h" "U8g2Interface.h" "U8g2Hal.h" "GpioEventService.h")
list(APPEND PIDISP_SRC "Global.cpp" "U8g2Utils.cpp" "U8g2Interface.cpp" "U8g2Hal.cpp" "GpioEventService.cpp" "U8g2LookupSetup.cpp" "U8g2LookupFonts.cpp")
message(STATUS "PIDISP SOURCE = ${U8G2_SRC_FILES}")

add_library(pidisp SHARED ${PIDISP_HDR} ${PIDISP_SRC})
add_executable(pidisp-gen "utils/codegen.h" "utils/codegen.cpp")
if (UNIX)
    add_executable(pidisp-test "InputDevHelper.h" "InputEventManager.h" "InputDevHelper.cpp" "InputEventManager.cpp" "InputDeviceTest.cpp")
endif()

if (WIN32)
    set(CMAKE_SHARED_LIBRARY_PREFIX "")
endif ()

target_compile_options(pidisp PRIVATE -Wno-write-strings)
target_compile_options(pidisp-gen PRIVATE -Wno-write-strings)

# Add Wiring Pi dependency
if (UNIX AND (TARGET wiringPi))
    message(STATUS "[WIRING-PI] Adding wiringPi Target")
    add_dependencies(pidisp wiringPi)
endif ()

list(APPEND INC_DIRS ${JNI_INCLUDE_DIRS} ${U8G2_INCLUDE_DIR} ${WIRINGPI_INCLUDE_DIR} ${U8G2_INCLUDE_DIR})
list(APPEND LINK_LIBS ${JNI_LIBRARIES} ${U8G2_LIBRARIES} ${WIRINGPI_LIBRARIES} Threads::Threads)

# Add libevent dependency (called usually if the library was built from the repository)
if (UNIX AND (LibEvent_FOUND AND TARGET libevent))
    message(STATUS "[LIBEVENT] Found 'libevent' target. Adding as dependency")
    list(APPEND INC_DIRS ${LIBEVENT_INCLUDE_DIRS})
    list(APPEND LINK_LIBS ${LIBEVENT_LIBRARIES})
    add_dependencies(pidisp libevent)
    add_dependencies(pidisp-test libevent)
    target_link_libraries(pidisp-test ${LINK_LIBS})
endif ()

# Add u8g2 dependency
if (TARGET u8g2)
    add_dependencies(pidisp u8g2)
endif ()

include_directories(${INC_DIRS})
target_link_libraries(pidisp ${LINK_LIBS})