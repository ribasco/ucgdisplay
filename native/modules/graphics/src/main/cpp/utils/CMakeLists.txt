cmake_minimum_required(VERSION 3.9)
project(ucgd-utils-codegen)

set(CMAKE_VERBOSE_MAKEFILE OFF)
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Libraries")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all Executables")
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin CACHE PATH "Single Directory for all static libraries")
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

add_executable(ucgd-code controllers.h codegen.h codegen.cpp)
target_compile_options(ucgd-code PRIVATE -Wno-write-strings)