include(ExternalProject)

# package autoconf-archive is required

set(LIBNAME "gpiod")
set(CMAKE_INSTALL_PREFIX ${PROJECT_BINARY_DIR}/install/libgpiod)
set(PROJ_INCLUDE_DIR "${LIB_DIR}/include")
set(PROJ_TAG "[LIBGPIOD]")

if (NOT $ENV{TRAVIS_OS_NAME} STREQUAL "")
    set(KERNEL_VERSION "4.8.17-040817")
    message(STATUS "Detected travis environment. Using fixed kernel version = ${KERNEL_VERSION}")
else ()
    execute_process(COMMAND uname -r OUTPUT_VARIABLE KERNEL_VERSION OUTPUT_STRIP_TRAILING_WHITESPACE)
endif ()

message(STATUS "${PROJ_TAG} Using kernel version = ${KERNEL_VERSION} (Travis Os Name = $ENV{TRAVIS_OS_NAME})")

if (EXISTS /usr/src/linux-headers-${KERNEL_VERSION}/include/linux/compiler_types.h AND (NOT EXISTS ${PROJ_INCLUDE_DIR}/linux/compiler_types.h))
    file(COPY /usr/src/linux-headers-${KERNEL_VERSION}/include/linux/compiler_types.h DESTINATION ${PROJ_INCLUDE_DIR}/linux)
else ()
    message(STATUS "Skipping copy of compiler_types.h")
endif ()

if (EXISTS /usr/src/linux-headers-${KERNEL_VERSION}/include/uapi/linux/gpio.h)
    message(STATUS "${PROJ_TAG} Checking required header file 'gpio.h' = yes")
else ()
    message(STATUS "${PROJ_TAG} Checking required header file 'gpio.h' = no")
    execute_process(COMMAND ls -l /usr/src/linux-headers-${KERNEL_VERSION}/include/uapi)
endif ()

set(CFLAGS "-I/usr/src/linux-headers-${KERNEL_VERSION}/include/uapi -I${PROJ_INCLUDE_DIR} -fPIC")

message(STATUS "${PROJ_TAG} CFLAGS = ${CFLAGS}, LIB_DIR=${LIB_DIR}")

# ac_cv_func_realloc_0_nonnull=yes

ExternalProject_Add(
        libgpiod
        GIT_REPOSITORY "https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git"
        GIT_TAG "v1.2"
        STAMP_DIR ${PROJECT_BINARY_DIR}/stamp
        UPDATE_COMMAND ""
        CONFIGURE_COMMAND CC=${CMAKE_C_COMPILER} CXX=${CMAKE_CXX_COMPILER} ./autogen.sh --enable-tools=yes --enable-bindings-cxx --prefix=${CMAKE_INSTALL_PREFIX} --host=arm-linux-gnueabihf CFLAGS=${CFLAGS} ac_cv_func_malloc_0_nonnull=yes
        SOURCE_DIR "${CMAKE_SOURCE_DIR}/lib/libgpiod"
        BINARY_DIR "${CMAKE_SOURCE_DIR}/lib/libgpiod"
        INSTALL_COMMAND make install
        INSTALL_DIR ${CMAKE_INSTALL_PREFIX}
        UPDATE_COMMAND ""
        BUILD_COMMAND make
        CMAKE_ARGS -DCMAKE_INSTALL_PREFIX:PATH=<INSTALL_DIR>
)

set(LIBGPIOD_INCLUDE_DIR ${CMAKE_INSTALL_PREFIX}/include)
set(LIBGPIOD_LIB_C ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiod${CMAKE_SHARED_LIBRARY_SUFFIX}.2.1.0)
set(LIBGPIOD_LIB_CXX ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiodcxx${CMAKE_SHARED_LIBRARY_SUFFIX}.1.0.0)

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(LibGpiod FOUND_VAR LibGpiod_FOUND REQUIRED_VARS LIBGPIOD_INCLUDE_DIR LIBGPIOD_LIB_C LIBGPIOD_LIB_CXX FAIL_MESSAGE "Could not find libgpiod package")

message(STATUS "${PROJ_TAG} LIBGPIOD_INCLUDE_DIR = ${LIBGPIOD_INCLUDE_DIR}")
message(STATUS "${PROJ_TAG} LIBGPIOD_LIB_C = ${LIBGPIOD_LIB_C}")
message(STATUS "${PROJ_TAG} LIBGPIOD_LIB_CXX = ${LIBGPIOD_LIB_CXX}")

mark_as_advanced(
        LIBGPIOD_INCLUDE_DIR
        LIBGPIOD_LIB_C
        LIBGPIOD_LIB_CXX
)