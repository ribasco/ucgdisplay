include(ExternalProject)

# package autoconf-archive is required

set(LIBNAME "gpiod")
set(CMAKE_INSTALL_PREFIX ${PROJECT_BINARY_DIR}/install/libgpiod)
set(PROJ_INCLUDE_DIR "${LIB_DIR}/include")
set(PROJ_TAG "[LIBGPIOD]")

execute_process(COMMAND uname -r OUTPUT_VARIABLE KERNEL_VERSION OUTPUT_STRIP_TRAILING_WHITESPACE)

if (NOT EXISTS ${PROJ_INCLUDE_DIR}/linux/compiler_types.h)
    file(COPY /usr/src/linux-headers-${KERNEL_VERSION}/include/linux/compiler_types.h DESTINATION ${PROJ_INCLUDE_DIR}/linux)
endif ()

set(CFLAGS "-I/usr/src/linux-headers-${KERNEL_VERSION}/include/uapi -I${PROJ_INCLUDE_DIR}")

message(STATUS "${PROJ_TAG} CFLAGS = ${CFLAGS}, LIB_DIR=${LIB_DIR}")

ExternalProject_Add(
        libgpiod
        GIT_REPOSITORY "https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git"
        GIT_TAG "master"
        STAMP_DIR ${PROJECT_BINARY_DIR}/stamp
        UPDATE_COMMAND ""
        CONFIGURE_COMMAND ./autogen.sh --enable-tools=no --enable-bindings-cxx --prefix=${CMAKE_INSTALL_PREFIX} --host=arm-linux-gnueabihf CFLAGS=${CFLAGS} CC=${CMAKE_C_COMPILER} CXX=${CMAKE_CXX_COMPILER}
        SOURCE_DIR "${CMAKE_SOURCE_DIR}/lib/libgpiod"
        BINARY_DIR "${CMAKE_SOURCE_DIR}/lib/libgpiod"
        INSTALL_COMMAND make install
        INSTALL_DIR ${CMAKE_INSTALL_PREFIX}
        UPDATE_COMMAND ""
        BUILD_COMMAND make
        CMAKE_ARGS -DCMAKE_INSTALL_PREFIX:PATH=<INSTALL_DIR> -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER} -DCMAKE_CXX_COMPILER=${CMAKE_CXX_COMPILER} -DCMAKE_SYSROOT=${CMAKE_SYSROOT}
)

set(LIBGPIOD_INCLUDE_DIR ${CMAKE_INSTALL_PREFIX}/include)
set(LIBGPIOD_LIB_C ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiod${CMAKE_SHARED_LIBRARY_SUFFIX})
set(LIBGPIOD_LIB_CXX ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiodcxx${CMAKE_SHARED_LIBRARY_SUFFIX})

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