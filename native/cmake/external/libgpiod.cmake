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

ExternalProject_Add(
        project_libgpiod
        GIT_REPOSITORY "https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git"
        GIT_TAG ${LIBGPIOD_VERSION}
        PREFIX ${PROJ_PREFIX}
        BUILD_IN_SOURCE 1
        CONFIGURE_COMMAND CC=${CMAKE_C_COMPILER} CXX=${CMAKE_CXX_COMPILER} ./autogen.sh --enable-tools=no --enable-bindings-cxx --prefix=<INSTALL_DIR> --host=arm-linux-gnueabihf CFLAGS=-I${CMAKE_SYSROOT}/usr/include LDFLAGS=-L${SYSROOT}/usr/lib ac_cv_func_malloc_0_nonnull=yes
        UPDATE_COMMAND ""
        INSTALL_COMMAND $(MAKE) install
        BUILD_COMMAND $(MAKE)
        CMAKE_ARGS -DCMAKE_INSTALL_PREFIX:PATH=<INSTALL_DIR>
)

ExternalProject_Get_Property(project_libgpiod SOURCE_DIR INSTALL_DIR)

# Hack (See: https://stackoverflow.com/questions/45516209/cmake-how-to-use-interface-include-directories-with-externalproject)
file(MAKE_DIRECTORY ${INSTALL_DIR}/include)

add_library(libgpiod SHARED IMPORTED)

set_target_properties(libgpiod PROPERTIES INTERFACE_INCLUDE_DIRECTORIES ${INSTALL_DIR}/include)
set_target_properties(libgpiod PROPERTIES IMPORTED_LOCATION ${INSTALL_DIR}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}gpiodcxx${CMAKE_SHARED_LIBRARY_SUFFIX})
set_target_properties(libgpiod PROPERTIES POSITION_INDEPENDENT_CODE ON)

add_dependencies(libgpiod project_libgpiod)