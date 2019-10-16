include(ExternalProject)

# package autoconf-archive is required

set(LIBNAME "gpiod")
set(CMAKE_INSTALL_PREFIX ${PROJECT_BINARY_DIR}/install/libgpiod)
set(PROJ_INCLUDE_DIR "${LIB_DIR}/include")
set(PROJ_TAG "[LIBGPIOD]")
set(LIBGPIOD_VERSION "v1.4.1")

#[[if (NOT $ENV{TRAVIS_OS_NAME} STREQUAL "")
    set(KERNEL_VERSION "4.15.18-041518")
    message(STATUS "Detected travis environment. Using fixed kernel version = ${KERNEL_VERSION}")
else ()
    execute_process(COMMAND uname -r OUTPUT_VARIABLE KERNEL_VERSION OUTPUT_STRIP_TRAILING_WHITESPACE)
endif ()]]

message(STATUS "${PROJ_TAG} Using kernel version = ${KERNEL_VERSION} (Travis Os Name = $ENV{TRAVIS_OS_NAME})")
message(STATUS "${PROJ_TAG} Project Include Dir = ${PROJ_INCLUDE_DIR}")
message(STATUS "${PROJ_TAG} SYSROOT = ${CMAKE_SYSROOT}")

#set(CFLAGS "-I/usr/src/linux-headers-${KERNEL_VERSION}/include/uapi -I/usr/src/linux-headers-${KERNEL_VERSION}/include -I${PROJ_INCLUDE_DIR}")
set(CFLAGS "-I${CMAKE_SYSROOT}/usr/include")

# -fPIC
# ac_cv_func_realloc_0_nonnull=yes

ExternalProject_Add(
        project_libgpiod
        GIT_REPOSITORY "https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git"
        GIT_TAG ${LIBGPIOD_VERSION}
        STAMP_DIR ${PROJECT_BINARY_DIR}/stamp
        UPDATE_COMMAND ""
        CONFIGURE_COMMAND CC=${CMAKE_C_COMPILER} CXX=${CMAKE_CXX_COMPILER} ./autogen.sh --enable-tools=no --enable-bindings-cxx --prefix=${CMAKE_INSTALL_PREFIX} --host=arm-linux-gnueabihf CFLAGS=-I${CMAKE_SYSROOT}/usr/include LDFLAGS=-L${SYSROOT}/usr/lib ac_cv_func_malloc_0_nonnull=yes
        SOURCE_DIR "${CMAKE_SOURCE_DIR}/lib/libgpiod"
        BUILD_IN_SOURCE 1
        INSTALL_COMMAND make install
        INSTALL_DIR ${CMAKE_INSTALL_PREFIX}
        UPDATE_COMMAND ""
        BUILD_COMMAND make
        CMAKE_ARGS -DCMAKE_INSTALL_PREFIX:PATH=<INSTALL_DIR>
)

ExternalProject_Get_Property(project_libgpiod INSTALL_DIR)

add_library(libgpiod SHARED IMPORTED)

set_property(TARGET libgpiod PROPERTY IMPORTED_LOCATION ${INSTALL_DIR}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiodcxx${CMAKE_SHARED_LIBRARY_SUFFIX})
set_property(TARGET libgpiod PROPERTY POSITION_INDEPENDENT_CODE ON)

add_dependencies(libgpiod project_libgpiod)

set(LIBGPIOD_INCLUDE_DIR ${CMAKE_INSTALL_PREFIX}/include)
set(LIBGPIOD_LIB_C ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiod${CMAKE_SHARED_LIBRARY_SUFFIX})
set(LIBGPIOD_LIB_CXX ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiodcxx${CMAKE_SHARED_LIBRARY_SUFFIX})

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(LibGpiod FOUND_VAR LibGpiod_FOUND REQUIRED_VARS LIBGPIOD_INCLUDE_DIR LIBGPIOD_LIB_C LIBGPIOD_LIB_CXX FAIL_MESSAGE "Could not find libgpiod package")

include_directories(${INSTALL_DIR}/include)

message(STATUS "${PROJ_TAG} CFLAGS = ${CFLAGS}")
message(STATUS "${PROJ_TAG} INSTALL_DIR = ${INSTALL_DIR}")
message(STATUS "${PROJ_TAG} LIBGPIOD_INCLUDE_DIR = ${LIBGPIOD_INCLUDE_DIR}")
message(STATUS "${PROJ_TAG} LIBGPIOD_LIB_C = ${LIBGPIOD_LIB_C}")
message(STATUS "${PROJ_TAG} LIBGPIOD_LIB_CXX = ${LIBGPIOD_LIB_CXX}")

mark_as_advanced(
        LIBGPIOD_INCLUDE_DIR
        LIBGPIOD_LIB_C
        LIBGPIOD_LIB_CXX
)