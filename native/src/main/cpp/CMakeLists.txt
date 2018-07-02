cmake_minimum_required(VERSION 3.11)
project(pidisp)

option(USE_DEV "Use UDev for Device Status monitoring instead of INotify" OFF)

message(STATUS "[MAIN] CROSS COMPILE  = ${CMAKE_CROSSCOMPILING}")
message(STATUS "[MAIN] SYSTEM_NAME    = ${CMAKE_SYSTEM_NAME}")
message(STATUS "[MAIN] PROCESSOR      = ${CMAKE_SYSTEM_PROCESSOR}")
message(STATUS "[MAIN] JAVA_HOME      = $ENV{JAVA_HOME}")

if (WIN32)
    message(STATUS "[MAIN] You are running on WINDOWS with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (UNIX)
    message(STATUS "[MAIN] You are running on unix/linux with processor = ${CMAKE_SYSTEM_PROCESSOR}")
elseif (MSVC)
    message(STATUS "[MAIN] You are running on MSVC with processor = ${CMAKE_SYSTEM_PROCESSOR}")
endif ()

set(CMAKE_CXX_STANDARD 17)
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

add_library(pidisp SHARED InputDevHelper.h InputDevHelper.cpp InputDeviceManager.h InputDeviceManager.cpp InputEventManager.h InputEventManager.cpp) #  ${U8G2_HEADER_FILES} ${U8G2_SRC_FILES}
add_executable(pidisp-test InputDeviceTest.cpp InputDevHelper.h InputDevHelper.cpp InputDeviceManager.h InputDeviceManager.cpp InputEventManager.h InputEventManager.cpp)

# Add libevent dependency (called usually if the library was built from the repository)
if (LibEvent_FOUND AND TARGET libevent)
    message(STATUS "[LIBEVENT] Found 'libevent' target. Adding as dependency")
    add_dependencies(pidisp libevent)
    add_dependencies(pidisp-test libevent)
endif ()

# Add u8g2 dependency
#[[if (U8G2_FOUND AND TARGET u8g2)
    add_dependencies(pidisp u8g2)
endif ()]]

add_custom_command(TARGET pidisp POST_BUILD COMMAND ${CMAKE_COMMAND} -E copy "$<TARGET_FILE:pidisp>" "${CMAKE_SOURCE_DIR}/../resources/")

include_directories(${LIBEVENT_INCLUDE_DIRS} ${JNI_INCLUDE_DIRS} ${U8G2_INCLUDE_DIR})
target_link_libraries(pidisp ${LIBEVENT_LIBRARIES} ${JNI_LIBRARIES}) # ${U8G2_LIBRARIES}
target_link_libraries(pidisp-test ${LIBEVENT_LIBRARIES}) #${U8G2_LIBRARIES}