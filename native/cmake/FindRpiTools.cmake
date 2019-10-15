set(TOOLS_DIR_PATH ${CMAKE_CURRENT_LIST_DIR}/../tools)
set(RPI_TOOLCHAIN_PATH "${TOOLS_DIR_PATH}/rpi" CACHE PATH "The path of the Raspberry Pi Toolchain")

message(STATUS "[FIND-TOOLCHAIN] Search Path = ${RPI_TOOLCHAIN_PATH}")

function(VERIFY_TOOLCHAIN path)
    message(STATUS "[VERIFY_TOOLCHAIN] Verifying toolchain path = ${path}")
    if (NOT EXISTS ${path})
        message(STATUS "[VERIFY_TOOLCHAIN] Toolchain path ${path} does not exist")
        set(TOOLCHAIN_VALID false PARENT_SCOPE)
    else ()
        set(C_PATH ${RPI_TOOLCHAIN_PATH}${CMAKE_C_COMPILER})
        set(CXX_PATH ${RPI_TOOLCHAIN_PATH}${CMAKE_CXX_COMPILER})

        message(STATUS "[VERIFY_TOOLCHAIN] Checking if c and c++ compiler paths are valid")
        message(STATUS "[VERIFY_TOOLCHAIN] C_COMPILER = ${C_PATH}")
        message(STATUS "[VERIFY_TOOLCHAIN] CXX_COMPILER = ${CXX_PATH}")

        if (EXISTS ${C_PATH} AND EXISTS ${CXX_PATH})
            set(TOOLCHAIN_VALID true PARENT_SCOPE)
        else ()
            set(TOOLCHAIN_VALID false PARENT_SCOPE)
        endif ()
    endif ()
endfunction()

if (NOT UNIX)
    message(FATAL_ERROR "Unsupported platform for this RPI Toolchain")
endif ()

# TODO: Use the built-in file manipulation provided by CMAKE

if ((EXISTS ${RPI_TOOLCHAIN_PATH}) AND (NOT EXISTS "${RPI_TOOLCHAIN_PATH}/arm-bcm2708"))
    message(STATUS "[FIND-TOOLCHAIN] RPi tools directory exists but missing required files...Removing")
    file(REMOVE_RECURSE ${RPI_TOOLCHAIN_PATH})
endif ()

# Verify toolchain path
VERIFY_TOOLCHAIN(${RPI_TOOLCHAIN_PATH})

message(STATUS "[VERIFY_TOOLCHAIN] IS VALID = ${TOOLCHAIN_VALID}")

# Check if the path exists, if it doesn't, download a copy to the source directory
if (NOT ${TOOLCHAIN_VALID})
    if (EXISTS ${RPI_TOOLCHAIN_PATH})
        message(STATUS "[FIND-TOOLCHAIN] Cleaning tools directory")
        file(REMOVE_RECURSE ${RPI_TOOLCHAIN_PATH})
    endif ()

    if (UNIX)
        message(STATUS "[FIND-TOOLCHAIN] Downloading toolchain to ${TOOLS_DIR_PATH}/toolchain.tar.gz")
        file(DOWNLOAD https://github.com/raspberrypi/tools/archive/master.tar.gz ${TOOLS_DIR_PATH}/toolchain.tar.gz SHOW_PROGRESS)

        message(STATUS "[FIND-TOOLCHAIN] Unzipping '${TOOLS_DIR_PATH}/toolchain.tar.gz' to '${TOOLS_DIR_PATH}'")
        execute_process(COMMAND tar xvzf ${TOOLS_DIR_PATH}/toolchain.tar.gz WORKING_DIRECTORY ${TOOLS_DIR_PATH} ERROR_VARIABLE tc_unzip OUTPUT_QUIET)
    else ()
        message(STATUS "[FIND-TOOLCHAIN] Downloading toolchain to ${TOOLS_DIR_PATH}/toolchain.zip")
        file(DOWNLOAD https://github.com/raspberrypi/tools/archive/master.zip ${TOOLS_DIR_PATH}/toolchain.zip SHOW_PROGRESS)

        message(STATUS "[FIND-TOOLCHAIN] Unzipping '${TOOLS_DIR_PATH}/toolchain.zip' to '${TOOLS_DIR_PATH}'")
        execute_process(COMMAND unzip ${TOOLS_DIR_PATH}/toolchain.zip WORKING_DIRECTORY ${TOOLS_DIR_PATH} ERROR_VARIABLE tc_unzip)
    endif ()

    if (tc_unzip)
        message(FATAL_ERROR "[FIND-TOOLCHAIN] Could not unzip contents of the downloaded toolchain (${tc_unzip})")
    endif ()

    message(STATUS "[FIND-TOOLCHAIN] Moving '${TOOLS_DIR_PATH}/tools-master/' to '${RPI_TOOLCHAIN_PATH}'")
    execute_process(COMMAND mv ${TOOLS_DIR_PATH}/tools-master/ ${RPI_TOOLCHAIN_PATH} ERROR_VARIABLE tc_move)

    if (tc_move)
        message(FATAL_ERROR "[FIND-TOOLCHAIN] Could not perform move operation (${tc_move})")
    endif ()

    message(STATUS "[FIND-TOOLCHAIN] Removing toolchain.zip from ${TOOLS_DIR_PATH}" ERROR_VARIABLE tc_remove)
    file(REMOVE ${TOOLS_DIR_PATH}/toolchain.tar.gz)

    if (tc_remove)
        message(FATAL_ERROR "[FIND-TOOLCHAIN] Could not perform delete operation")
    endif ()

    # Verify once again
    message(STATUS "[FIND-TOOLCHAIN] Verifying downloaded toolchain")
    VERIFY_TOOLCHAIN(${RPI_TOOLCHAIN_PATH})

    if (NOT ${TOOLCHAIN_VALID})
        message(FATAL_ERROR "[FIND-TOOLCHAIN] Downloaded toolchain invalid or corrupted (${TOOLS_DIR_PATH})")
    else ()
        message(STATUS "[FIND-TOOLCHAIN] Downloaded toolchain is valid")
    endif ()
else ()
    message(STATUS "[FIND-TOOLCHAIN] Toolchain path already exists. Skipping download (Valid = ${TOOLCHAIN_VALID})")
endif ()

if (EXISTS "${RPI_TOOLCHAIN_PATH}/arm-bcm2708")
    message(STATUS "[FIND-TOOLCHAIN] Found toolchain = ${RPI_TOOLCHAIN_PATH}")
    set(RPI_TOOLCHAIN_DIR ${RPI_TOOLCHAIN_PATH})
    set(RPI_TOOLCHAIN_FOUND true)
else ()
    set(RPI_TOOLCHAIN_FOUND false)
endif ()

mark_as_advanced(
        RPI_TOOLCHAIN_DIR
)