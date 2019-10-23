cmake_minimum_required(VERSION 3.10)
project(ucgd-utils-codegen)

add_executable(ucgd-code controllers.h codegen.h codegen.cpp)
target_compile_options(ucgd-code PRIVATE -Wno-write-strings)