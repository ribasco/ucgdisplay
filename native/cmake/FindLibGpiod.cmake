include(ExternalProject)

# package autoconf-archive is required

set(LIBNAME "gpiod")
set(PROJ_PREFIX "libgpiod")
set(CMAKE_INSTALL_PREFIX ${PROJECT_BINARY_DIR}/${PROJ_PREFIX}/libgpiod)
set(PROJ_INCLUDE_DIR "${LIB_DIR}/include")
set(PROJ_TAG "[LIBGPIOD]")
set(LIBGPIOD_VERSION "v1.4.1")

message(STATUS "${PROJ_TAG} Using kernel version = ${KERNEL_VERSION} (Travis Os Name = $ENV{TRAVIS_OS_NAME})")
message(STATUS "${PROJ_TAG} Project Include Dir = ${PROJ_INCLUDE_DIR}")
message(STATUS "${PROJ_TAG} SYSROOT = ${CMAKE_SYSROOT}")

set(CFLAGS "-I${CMAKE_SYSROOT}/usr/include")


ExternalProject_Add(
        project_libgpiod
        GIT_REPOSITORY "https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git"
        GIT_TAG ${LIBGPIOD_VERSION}
        PREFIX ${PROJ_PREFIX}
        INSTALL_DIR ${PROJ_PREFIX}
        DOWNLOAD_DIR ${PROJ_PREFIX}/src
        SOURCE_DIR ${PROJ_PREFIX}/src/libgpiod
        BINARY_DIR ${PROJ_PREFIX}/src/libgpiod
        TMP_DIR ${PROJ_PREFIX}/tmp
        STAMP_DIR ${PROJ_PREFIX}/stamp
        CONFIGURE_COMMAND CC=${CMAKE_C_COMPILER} CXX=${CMAKE_CXX_COMPILER} ./autogen.sh --enable-tools=no --enable-bindings-cxx --prefix=<INSTALL_DIR> --host=arm-linux-gnueabihf CFLAGS=-I${CMAKE_SYSROOT}/usr/include LDFLAGS=-L${SYSROOT}/usr/lib ac_cv_func_malloc_0_nonnull=yes
        UPDATE_COMMAND ""
        INSTALL_COMMAND make install
        BUILD_COMMAND make
        CMAKE_ARGS -DCMAKE_INSTALL_PREFIX:PATH=<INSTALL_DIR>
)

ExternalProject_Get_Property(project_libgpiod INSTALL_DIR)

# Hack (See: https://stackoverflow.com/questions/45516209/cmake-how-to-use-interface-include-directories-with-externalproject)
file(MAKE_DIRECTORY ${SOURCE_DIR}/src)

add_library(libgpiod SHARED IMPORTED)

set_target_properties(libgpiod PROPERTIES INTERFACE_INCLUDE_DIRECTORIES ${CMAKE_CURRENT_SOURCE_DIR})
set_target_properties(libgpiod PROPERTIES IMPORTED_LOCATION ${INSTALL_DIR}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiodcxx${CMAKE_SHARED_LIBRARY_SUFFIX})
set_target_properties(libgpiod PROPERTIES POSITION_INDEPENDENT_CODE ON)

add_dependencies(libgpiod project_libgpiod)

set(LIBGPIOD_INCLUDE_DIR ${CMAKE_INSTALL_PREFIX}/include)
set(LIBGPIOD_LIB_C ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiod${CMAKE_SHARED_LIBRARY_SUFFIX})
set(LIBGPIOD_LIB_CXX ${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiodcxx${CMAKE_SHARED_LIBRARY_SUFFIX})

#[[
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
)]]
