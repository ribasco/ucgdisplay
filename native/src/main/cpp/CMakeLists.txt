cmake_minimum_required(VERSION 3.10)

project(pidisp VERSION 1.0.0 DESCRIPTION "Native library for processing input events to be used by pidisplay")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_C_FLAGS_DEBUG "-D_DEBUG")
#set(CMAKE_BUILD_TYPE Debug)

# Include Boost
#set(BOOST_ROOT /home/raffy/projects/boost)
#set(Boost_USE_STATIC_LIBS   ON)
#set(Boost_USE_MULTITHREADED ON)
#set(Boost_USE_STATIC_RUNTIME OFF)

#find_package(Boost 1.67.0 COMPONENTS system REQUIRED)
#if (Boost_FOUND)
#    include_directories(${Boost_INCLUDE_DIR})
#endif ()

#
# Where to look for modules
#
set(CMAKE_MODULE_PATH ${CMAKE_SOURCE_DIR}/cmake/modules)

#find_package(Libevdev REQUIRED)
find_package(JNI REQUIRED)
find_package(Libudev REQUIRED)
find_package(LibEvent REQUIRED)

#if(Libevent_FOUND)
#    message("Found libevent library")
#    add_executable(libevtest LibEventTest.cpp)
#    target_link_libraries(libevtest LINK_PUBLIC pthread event event_pthreads ${LIBEVENT_LIBRARIES})
#endif()

include_directories(${JNI_INCLUDE_DIRS})

# Set the output folder where your program will be created
set(BUILD_DIRECTORY ${CMAKE_BUILD_TYPE})
string(TOLOWER ${CMAKE_BUILD_TYPE} BUILD_BIN_DIR)

set(CMAKE_BINARY_DIR ${CMAKE_SOURCE_DIR}/${BUILD_BIN_DIR}/bin)
set(EXECUTABLE_OUTPUT_PATH ${CMAKE_BINARY_DIR})
set(LIBRARY_OUTPUT_PATH ${CMAKE_BINARY_DIR})

message("Using binary output path: ${CMAKE_BINARY_DIR}")

set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -Wall")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall")

add_library(pidisp SHARED InputDevHelper.h InputDevHelper.cpp InputDeviceManager.h InputDeviceManager.cpp InputEventManager.h InputEventManager.cpp)
add_executable(pidisp-test InputDeviceTest.cpp InputDevHelper.h InputDevHelper.cpp InputDeviceManager.h InputDeviceManager.cpp InputEventManager.h InputEventManager.cpp)

set_target_properties(pidisp PROPERTIES VERSION ${PROJECT_VERSION})
set_target_properties(pidisp PROPERTIES PUBLIC_HEADER include/InputDeviceManager.h)

include(GNUInstallDirs)
include_directories(/usr/include/libevdev-1.0)

install(TARGETS pidisp
        ARCHIVE DESTINATION lib
        LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
        PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})

message("Library Destination: ${CMAKE_INSTALL_LIBDIR}, Header Destination: ${CMAKE_INSTALL_INCLUDEDIR}")

target_link_libraries(pidisp PUBLIC pthread event event_pthreads udev ${LIBEVENT_LIBRARIES})
target_link_libraries(pidisp-test PUBLIC pthread event event_pthreads udev ${LIBEVENT_LIBRARIES})