macro(OSXCROSS_GETCONF VAR DEFAULT_VAL)
    if (NOT ${VAR})
        set(${VAR} "$ENV{${VAR}}")
        if (${VAR})
            set(${VAR} "${${VAR}}" CACHE STRING "${VAR}")
            message(STATUS "[OSXCROSS] Using provided ${VAR}: ${${VAR}}")
        else ()
            set(${VAR} "${DEFAULT_VAL}" CACHE STRING "${VAR}")
            message(STATUS "[OSXCROSS] Using default value for ${VAR} = ${DEFAULT_VAL}")
        endif ()
    endif ()
endmacro()

OSXCROSS_GETCONF(OSXCROSS_TARGET_DIR "")
OSXCROSS_GETCONF(OSXCROSS_TARGET "darwin17")
OSXCROSS_GETCONF(OSXCROSS_HOST "i386-apple-${OSXCROSS_TARGET}")
OSXCROSS_GETCONF(OSXCROSS_SDK "${OSXCROSS_TARGET_DIR}/SDK/MacOSX10.13.sdk")

set(CMAKE_SYSTEM_NAME "Darwin")
string(REGEX REPLACE "-.*" "" CMAKE_SYSTEM_PROCESSOR "${OSXCROSS_HOST}")

set(TOOLCHAIN_DESC "OSXCoss Toolchain 32bit (${CMAKE_SYSTEM_NAME} - ${CMAKE_SYSTEM_PROCESSOR})")

set(CMAKE_C_COMPILER "${OSXCROSS_TARGET_DIR}/bin/o32-clang")
set(CMAKE_CXX_COMPILER "${OSXCROSS_TARGET_DIR}/bin/o32-clang++-libc++")

# where is the target environment
set(CMAKE_FIND_ROOT_PATH
        "${OSXCROSS_SDK}"
        "${OSXCROSS_TARGET_DIR}/macports/pkgs/opt/local")

# search for programs in the build host directories
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
# for libraries and headers in the target directories
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)

set(CMAKE_AR "${OSXCROSS_TARGET_DIR}/bin/${OSXCROSS_HOST}-ar" CACHE FILEPATH "ar")
set(CMAKE_RANLIB "${OSXCROSS_TARGET_DIR}/bin/${OSXCROSS_HOST}-ranlib" CACHE FILEPATH "ranlib")
set(CMAKE_INSTALL_NAME_TOOL "${OSXCROSS_TARGET_DIR}/bin/${OSXCROSS_HOST}-install_name_tool" CACHE FILEPATH "install_name_tool")

set(ENV{PKG_CONFIG_LIBDIR} "${OSXCROSS_TARGET_DIR}/macports/pkgs/opt/local/lib/pkgconfig")
set(ENV{PKG_CONFIG_SYSROOT_DIR} "${OSXCROSS_TARGET_DIR}/macports/pkgs")
