cmake_minimum_required(VERSION 3.9)
project(ucgd-mod-graphics)

include(../../../../../include/CMakeCommon.txt)

# Include default packages
find_package(U8G2 REQUIRED)

list(APPEND UCGDISP_HDR "Global.h" "U8g2Utils.h" "U8g2Graphics.h" "U8g2Hal.h")
list(APPEND UCGDISP_SRC "Global.cpp" "U8g2Utils.cpp" "U8g2Graphics.cpp" "U8g2Hal.cpp" "U8g2LookupSetup.cpp" "U8g2LookupFonts.cpp")

add_library(ucgdisp SHARED ${UCGDISP_HDR} ${UCGDISP_SRC})
add_executable(ucgdisp-gen "utils/codegen.h" "utils/codegen.cpp")

target_compile_options(ucgdisp PRIVATE -Wno-write-strings)
target_compile_options(ucgdisp-gen PRIVATE -Wno-write-strings)

# Add Wiring Pi Package (ARM only)
if (UNIX AND (${CMAKE_SYSTEM_PROCESSOR} MATCHES "^arm"))
    find_package(WiringPi REQUIRED)
    message(STATUS "[WIRING-PI] WIRINGPI_LIBRARIES = ${WIRINGPI_LIBRARIES}")
    message(STATUS "[WIRING-PI] WIRINGPI_INCLUDE_DIR = ${WIRINGPI_INCLUDE_DIR}")

    message(STATUS "[WIRING-PI] Adding wiringPi Target")
    add_dependencies(ucgdisp wiringPi)
    list(APPEND INC_DIRS ${WIRINGPI_INCLUDE_DIR})
    list(APPEND LINK_LIBS ${WIRINGPI_LIBRARIES})
endif ()

# Add u8g2 dependency
if (TARGET u8g2)
    add_dependencies(ucgdisp u8g2)
    list(APPEND INC_DIRS ${U8G2_INCLUDE_DIR})
    list(APPEND LINK_LIBS ${U8G2_LIBRARIES})
else ()
    message(FATAL_ERROR "U8g2 target not found")
endif ()

include_directories(${INC_DIRS})

if (WIN32)
    message(STATUS "[target_link_libraries] Setting MINGW dependencies static")
    target_link_libraries(ucgdisp ${LINK_LIBS} -static-libgcc -static-libstdc++ -static)
else ()
    target_link_libraries(ucgdisp ${LINK_LIBS})
endif ()