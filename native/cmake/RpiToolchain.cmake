function(VERIFY_TOOLCHAIN path)
    message(STATUS "[VERIFY-TOOLCHAIN] Verifying toolchain path = ${path}")
    if (NOT EXISTS ${path})
        message(STATUS "[VERIFY-TOOLCHAIN] Toolchain path '${path}' NOT FOUND")
        set(TOOLCHAIN_VALID false PARENT_SCOPE)
    else ()
        set(C_PATH ${CMAKE_C_COMPILER})
        set(CXX_PATH ${CMAKE_CXX_COMPILER})

        message(STATUS "[VERIFY-TOOLCHAIN] Checking if c and c++ compiler paths are valid")
        message(STATUS "[VERIFY-TOOLCHAIN] C_COMPILER = ${C_PATH}")
        message(STATUS "[VERIFY-TOOLCHAIN] CXX_COMPILER = ${CXX_PATH}")

        if (EXISTS ${C_PATH} AND EXISTS ${CXX_PATH})
            set(TOOLCHAIN_VALID true PARENT_SCOPE)
        else ()
            set(TOOLCHAIN_VALID false PARENT_SCOPE)
        endif ()
    endif ()
endfunction()

if (NOT UNIX)
    message(FATAL_ERROR "[RPI-TOOLCHAIN] Unsupported platform for this RPI Toolchain")
endif ()

# Define our host system
set(CMAKE_SYSTEM_NAME Linux)
SET(CMAKE_SYSTEM_PROCESSOR arm)
set(CMAKE_SYSTEM_VERSION 1)

set(TOOLS_DIR_PATH ${CMAKE_CURRENT_LIST_DIR}/../tools)
set(RPI_TOOLCHAIN_PATH "${TOOLS_DIR_PATH}/rpi" CACHE PATH "The path of the Raspberry Pi Toolchain")
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/../../../../../cmake") # Set module path
set(CMAKE_SYSROOT ${RPI_TOOLCHAIN_PATH}/arm-bcm2708/arm-linux-gnueabihf/arm-linux-gnueabihf/sysroot)

# Convert to real path
get_filename_component(CMAKE_MODULE_PATH "" REALPATH BASE_DIR "${CMAKE_MODULE_PATH}")
get_filename_component(TOOLS_DIR_PATH "" REALPATH BASE_DIR "${TOOLS_DIR_PATH}")
get_filename_component(RPI_TOOLCHAIN_PATH "" REALPATH BASE_DIR "${RPI_TOOLCHAIN_PATH}")

# Cross-compiler variables
set(CMAKE_C_COMPILER ${RPI_TOOLCHAIN_PATH}/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian/bin/arm-linux-gnueabihf-gcc)
set(CMAKE_CXX_COMPILER ${RPI_TOOLCHAIN_PATH}/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian/bin/arm-linux-gnueabihf-g++)
set(CMAKE_SYSROOT ${RPI_TOOLCHAIN_PATH}/arm-bcm2708/arm-linux-gnueabihf/arm-linux-gnueabihf/sysroot)
#set(CMAKE_C_COMPILER ${RPI_TOOLCHAIN_PATH}/arm-bcm2708/arm-linux-gnueabihf/bin/arm-linux-gnueabihf-gcc)
#set(CMAKE_CXX_COMPILER ${RPI_TOOLCHAIN_PATH}/arm-bcm2708/arm-linux-gnueabihf/bin/arm-linux-gnueabihf-g++)

message(STATUS "[RPI-TOOLCHAIN] -------------------------------------------------------------------------------------------------")
message(STATUS "[RPI-TOOLCHAIN] Raspberry Pi Toolchain loaded (${CMAKE_SYSTEM_NAME} - ${CMAKE_SYSTEM_PROCESSOR})")
message(STATUS "[RPI-TOOLCHAIN] -------------------------------------------------------------------------------------------------")
message(STATUS "[RPI-TOOLCHAIN] CMAKE_CURRENT_LIST_DIR = ${CMAKE_CURRENT_LIST_DIR}")
message(STATUS "[RPI-TOOLCHAIN] CMake module path = ${CMAKE_MODULE_PATH}")
message(STATUS "[RPI-TOOLCHAIN] Tools dir path = ${TOOLS_DIR_PATH}")
message(STATUS "[RPI-TOOLCHAIN] RPI Toolchain Path = ${RPI_TOOLCHAIN_PATH}")
message(STATUS "[RPI-TOOLCHAIN] -------------------------------------------------------------------------------------------------")
message(STATUS "[RPI-TOOLCHAIN] MAKE            = ${CMAKE_MAKE_PROGRAM}")
message(STATUS "[RPI-TOOLCHAIN] C COMPILER      = ${CMAKE_C_COMPILER}")
message(STATUS "[RPI-TOOLCHAIN] C++ COMPILER    = ${CMAKE_CXX_COMPILER}")
message(STATUS "[RPI-TOOLCHAIN] SYSROOT         = ${CMAKE_SYSROOT}")
message(STATUS "[RPI-TOOLCHAIN] -------------------------------------------------------------------------------------------------")

# =================================================================
# Download Toolchain from Github
# =================================================================

# Verify toolchain path
VERIFY_TOOLCHAIN(${RPI_TOOLCHAIN_PATH})
message(STATUS "[RPI-TOOLCHAIN] Found valid RPI Toolchain = ${TOOLCHAIN_VALID}")

# Check if the path exists, if it doesn't, download a copy to the source directory
if (NOT ${TOOLCHAIN_VALID})
    # Perform clean-up opertations
    message(STATUS "[RPI-TOOLCHAIN] RPi tools directory exists but missing required files...Performing clean-up")
    file(REMOVE_RECURSE ${RPI_TOOLCHAIN_PATH})
    if (EXISTS "${TOOLS_DIR_PATH}/toolchain.tar.gz")
        file(REMOVE ${TOOLS_DIR_PATH}/toolchain.tar.gz)
        message(STATUS "[RPI-TOOLCHAIN] Removed existing archive 'toolchain.tar.gz'")
    elseif (EXISTS "${TOOLS_DIR_PATH}/toolchain.zip")
        file(REMOVE ${TOOLS_DIR_PATH}/toolchain.zip)
        message(STATUS "[RPI-TOOLCHAIN] Removed existing archive 'toolchain.zip'")
    endif ()

    if (UNIX)
        set(OUTPUT_FILENAME "toolchain.tar.gz")
        set(OUTPUT_FILEPATH "${TOOLS_DIR_PATH}/${OUTPUT_FILENAME}")

        message(STATUS "[RPI-TOOLCHAIN] Downloading toolchain to ${OUTPUT_FILEPATH}")
        file(DOWNLOAD https://github.com/raspberrypi/tools/archive/master.tar.gz ${OUTPUT_FILEPATH} SHOW_PROGRESS)

        message(STATUS "[RPI-TOOLCHAIN] Unzipping '${OUTPUT_FILEPATH}' to '${TOOLS_DIR_PATH}'")
        execute_process(COMMAND tar xvzf ${TOOLS_DIR_PATH}/${OUTPUT_FILENAME} WORKING_DIRECTORY ${TOOLS_DIR_PATH} ERROR_VARIABLE tc_unzip OUTPUT_QUIET)
    else ()
        set(OUTPUT_FILENAME "toolchain.zip")
        message(STATUS "[RPI-TOOLCHAIN] Downloading toolchain to ${TOOLS_DIR_PATH}/${OUTPUT_FILENAME}")
        file(DOWNLOAD https://github.com/raspberrypi/tools/archive/master.zip ${TOOLS_DIR_PATH}/${OUTPUT_FILENAME} SHOW_PROGRESS)

        message(STATUS "[RPI-TOOLCHAIN] Unzipping '${TOOLS_DIR_PATH}/${OUTPUT_FILENAME}' to '${TOOLS_DIR_PATH}'")
        execute_process(COMMAND unzip ${TOOLS_DIR_PATH}/${OUTPUT_FILENAME} WORKING_DIRECTORY ${TOOLS_DIR_PATH} ERROR_VARIABLE tc_unzip OUTPUT_QUIET)
    endif ()

    if (tc_unzip)
        message(FATAL_ERROR "[RPI-TOOLCHAIN] Could not unzip contents of the downloaded toolchain '${OUTPUT_FILENAME}' (${tc_unzip})")
    endif ()

    message(STATUS "[RPI-TOOLCHAIN] Moving '${TOOLS_DIR_PATH}/tools-master/' to '${RPI_TOOLCHAIN_PATH}'")
    file(RENAME ${TOOLS_DIR_PATH}/tools-master/ ${RPI_TOOLCHAIN_PATH})
    # execute_process(COMMAND mv ${TOOLS_DIR_PATH}/tools-master/ ${RPI_TOOLCHAIN_PATH} ERROR_VARIABLE tc_move)
    #if (tc_move)
    #    message(FATAL_ERROR "[RPI-TOOLCHAIN] Could not perform move operation (${tc_move})")
    #endif ()

    message(STATUS "[RPI-TOOLCHAIN] Removing '${OUTPUT_FILENAME}' from ${TOOLS_DIR_PATH}")
    file(REMOVE ${TOOLS_DIR_PATH}/${OUTPUT_FILENAME})
else ()
    message(STATUS "[RPI-TOOLCHAIN] Toolchain path already exists. Skipping download (Valid = ${TOOLCHAIN_VALID})")
endif ()

# Verify once again
message(STATUS "[RPI-TOOLCHAIN] Verifying downloaded toolchain")
VERIFY_TOOLCHAIN(${RPI_TOOLCHAIN_PATH})

if (NOT ${TOOLCHAIN_VALID})
    message(FATAL_ERROR "[RPI-TOOLCHAIN] Downloaded toolchain invalid or corrupted (${TOOLS_DIR_PATH})")
else ()
    message(STATUS "[RPI-TOOLCHAIN] Downloaded toolchain is valid")
endif ()

# =================================================================
# Configure Toolchain
# =================================================================

# Define the sysroot path for the RaspberryPi distribution in our tools folder
set(CMAKE_FIND_ROOT_PATH ${CMAKE_SYSROOT})

# Use our definitions for compiler tools
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
# Search for libraries and headers in the target directories only
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)