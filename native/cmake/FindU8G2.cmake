
set(U8G2_DIR ${LIB_DIR}/u8g2)
set(PROJ_NAME "u8g2")

DOWNLOAD_GITPROJ("https://github.com/ribasco/u8g2" ${PROJ_NAME} "master")

if (NOT EXISTS ${U8G2_DIR}/csrc)
    message(FATAL_ERROR "[U8G2] Could not locate csrc directory")
endif ()

#"${U8G2_DIR}/tools/font/build/single_font_files/*.c"
file(GLOB U8G2_SRC_FILES "${U8G2_DIR}/csrc/*.c")
file(GLOB U8G2_HEADER_FILES "${U8G2_DIR}/csrc/*.h")
set(U8G2_INCLUDE_DIR ${U8G2_DIR}/csrc)

#file(COPY ${U8G2_HEADER_FILES} DESTINATION ${PROJECT_BINARY_DIR}/install/u8g2/include)
add_library(u8g2 STATIC ${U8G2_HEADER_FILES} ${U8G2_SRC_FILES})
set_target_properties(u8g2
        PROPERTIES
        ARCHIVE_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/install/u8g2/lib"
        LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/install/u8g2/lib"
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/install/u8g2/lib"
        )

#set(U8G2_INCLUDE_DIR ${PROJECT_BINARY_DIR}/install/u8g2/include)
set(U8G2_LIBRARIES ${PROJECT_BINARY_DIR}/install/u8g2/lib/${CMAKE_STATIC_LIBRARY_PREFIX}u8g2${CMAKE_STATIC_LIBRARY_SUFFIX})

message(STATUS "[U8G2] U8G2_INCLUDE_DIR = ${U8G2_INCLUDE_DIR}")
message(STATUS "[U8G2] U8G2_LIBRARIES = ${U8G2_LIBRARIES}")

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(u8g2 FOUND_VAR U8G2_FOUND REQUIRED_VARS U8G2_SRC_FILES U8G2_HEADER_FILES U8G2_INCLUDE_DIR FAIL_MESSAGE "Could not find u8g2 package")

mark_as_advanced(U8G2_INCLUDE_DIR U8G2_LIBRARIES U8G2_SRC_FILES U8G2_HEADER_FILES)