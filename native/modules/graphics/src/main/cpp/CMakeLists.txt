cmake_minimum_required(VERSION 3.9)
project(ucgd-mod-graphics)

include(../../../../../include/CMakeCommon.txt)

# Include default packages
find_package(U8G2 REQUIRED)

list(APPEND UCGDISP_HDR "../../../../../include/Global.h" "U8g2Utils.h" "U8g2Graphics.h" "U8g2Hal.h")
list(APPEND UCGDISP_SRC "../../../../../include/Global.cpp" "U8g2Utils.cpp" "U8g2Graphics.cpp" "U8g2Hal.cpp" "U8g2LookupSetup.cpp" "U8g2LookupFonts.cpp")

if (UNIX AND (${CMAKE_SYSTEM_PROCESSOR} MATCHES "^arm"))
    list(APPEND UCGDISP_HDR "CommSpi.h")
    list(APPEND UCGDISP_SRC "CommSpi.cpp")
endif ()

add_library(ucgdisp SHARED ${UCGDISP_HDR} ${UCGDISP_SRC})

add_subdirectory(utils)

target_compile_options(ucgdisp PRIVATE -Wno-write-strings)

# Add Wiring Pi/C-periphery Package (ARM only)
if (UNIX AND (${CMAKE_SYSTEM_PROCESSOR} MATCHES "^arm"))
    find_package(CPeriphery REQUIRED)
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

# Add c-periphery
if (TARGET cperiphery AND CPeriphery_FOUND)
    message(STATUS "Adding cperiphery dependency")
    add_dependencies(ucgdisp cperiphery)
    list(APPEND INC_DIRS ${CPERIPHERY_INCLUDE})
    list(APPEND LINK_LIBS ${CPERIPHERY_LIB})
endif ()

include_directories(${INC_DIRS})

if (WIN32)
    message(STATUS "[target_link_libraries] Setting MINGW dependencies static")
    target_link_libraries(ucgdisp ${LINK_LIBS} -static-libgcc -static-libstdc++ -static)
else ()
    target_link_libraries(ucgdisp ${LINK_LIBS})
endif ()