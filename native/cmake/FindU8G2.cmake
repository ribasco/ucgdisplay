
set(U8G2_DIR ${LIB_DIR}/u8g2)

# Check if U8G2 is already on the lib directory
find_path(U8G2_SOURCE_DIRS u8g2.h PATHS ${U8G2_DIR}/csrc NO_CMAKE_FIND_ROOT_PATH)

if (NOT EXISTS ${U8G2_DIR})
    set(U8G2_ARCHIVE_FILE_NAME u8g2.zip)
    set(U8G2_ARCHIVE_FILE_PATH ${LIB_DIR}/${U8G2_ARCHIVE_FILE_NAME})

    if (NOT EXISTS ${U8G2_ARCHIVE_FILE_PATH})
        message(STATUS "[U8G2] Downloading to ${U8G2_ARCHIVE_FILE_PATH}")
        message(STATUS "[U8G2] Not yet existing: ${U8G2_DIR}, Working directory: ${CMAKE_SOURCE_DIR}")
        file(DOWNLOAD https://github.com/olikraus/u8g2/archive/master.zip ${U8G2_ARCHIVE_FILE_PATH} SHOW_PROGRESS)
    endif ()

    if (NOT EXISTS ${U8G2_ARCHIVE_FILE_PATH})
        message(FATAL_ERROR "Could not find ${U8G2_ARCHIVE_FILE_PATH}")
    endif ()

    message(STATUS "[U8G2] Unzipping '${U8G2_ARCHIVE_FILE_NAME}' to '${LIB_DIR}'")
    execute_process(COMMAND unzip ${U8G2_ARCHIVE_FILE_PATH} WORKING_DIRECTORY ${LIB_DIR} ERROR_VARIABLE tc_unzip)

    if (tc_unzip)
        message(FATAL_ERROR "[U8G2] Could not unzip contents of the downloaded toolchain (${tc_unzip})")
    endif ()

    message(STATUS "[U8G2] Moving '${LIB_DIR}/u8g2-master/' to '${U8G2_DIR}'")
    file(RENAME "${LIB_DIR}/u8g2-master/" "${U8G2_DIR}")

    message(STATUS "[U8G2] Removing u8g2.zip from ${LIB_DIR}")
    file(REMOVE ${U8G2_ARCHIVE_FILE_PATH})
endif ()

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