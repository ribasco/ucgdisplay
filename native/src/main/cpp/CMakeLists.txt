cmake_minimum_required(VERSION 3.10)

project(pidisp VERSION 1.0.0 DESCRIPTION "Native library for processing input events to be used by pidisplay")

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_BUILD_TYPE Release)

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

# Set the output folder where your program will be created
set(BUILD_DIRECTORY ${CMAKE_BUILD_TYPE})
string(TOLOWER ${CMAKE_BUILD_TYPE} BUILD_BIN_DIR)

set(CMAKE_BINARY_DIR ${CMAKE_SOURCE_DIR}/${BUILD_BIN_DIR}/bin)
set(EXECUTABLE_OUTPUT_PATH ${CMAKE_BINARY_DIR})
set(LIBRARY_OUTPUT_PATH ${CMAKE_BINARY_DIR})

#set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Wall")
#set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall")

add_library(pidisp SHARED InputDeviceManager.cpp InputDevHelper.cpp)

#set_target_properties(pidisp PROPERTIES VERSION ${PROJECT_VERSION})
#set_target_properties(pidisp PROPERTIES PUBLIC_HEADER include/InputDeviceManager.h)

include(GNUInstallDirs)

install(TARGETS pidisp
        ARCHIVE DESTINATION lib
        LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
        PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})

TARGET_LINK_LIBRARIES(pidisp pthread)