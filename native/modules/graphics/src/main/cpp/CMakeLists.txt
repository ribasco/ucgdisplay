cmake_minimum_required(VERSION 3.10)
project(ucgd-mod-graphics)

# set(CMAKE_DEBUG_TARGET_PROPERTIES INCLUDE_DIRECTORIES)
set(GLOBAL_INC_DIR "../../../../../include")

include(${GLOBAL_INC_DIR}/CMakeCommon.txt)

list(APPEND UCGDISP_HDR "${GLOBAL_INC_DIR}/Global.h" "U8g2Utils.h" "U8g2Graphics.h" "U8g2Hal.h")
list(APPEND UCGDISP_SRC "${GLOBAL_INC_DIR}/Global.cpp" "U8g2Utils.cpp" "U8g2Graphics.cpp" "U8g2Hal.cpp" "U8g2LookupSetup.cpp" "U8g2LookupFonts.cpp")

add_library(ucgdisp SHARED ${UCGDISP_HDR} ${UCGDISP_SRC})
target_sources(ucgdisp PRIVATE ${UCGDISP_HDR} ${UCGDISP_SRC})
target_include_directories(ucgdisp PRIVATE "${GLOBAL_INC_DIR}" "${JNI_INCLUDE_DIRS}")
target_compile_options(ucgdisp PRIVATE -Wno-write-strings)

add_subdirectory(utils)

# Add C-periphery and Libgpiod packages (ARM only)
if (UNIX AND (${CMAKE_SYSTEM_PROCESSOR} MATCHES "^arm"))
    include(external/libgpiod)
    include(external/c-periphery)

    # Add c-periphery
    if (TARGET cperiphery)
        message(STATUS "Found c-periphery")
        target_link_libraries(ucgdisp cperiphery)
    endif ()

    # Add libgpiod
    if (TARGET libgpiod)
        target_link_libraries(ucgdisp libgpiod)
    endif ()
endif ()

# Add the U8g2 library
include(external/u8g2)
if (TARGET u8g2)
    target_link_libraries(ucgdisp u8g2)
endif ()

message(STATUS "[INCL-LIBRARIES] ${INC_DIRS}")
message(STATUS "[LINK-LIBRARIES] ${LINK_LIBS}")

if (WIN32)
    message(STATUS "[target_link_libraries] Setting MINGW dependencies static")
    target_link_libraries(ucgdisp ${LINK_LIBS} -static-libgcc -static-libstdc++ -static)
else ()
    target_link_libraries(ucgdisp ${LINK_LIBS})
endif ()