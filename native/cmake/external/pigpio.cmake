include(ExternalProject)

set(LIBNAME "pigpio")
set(PROJ_TAG "[PIGPIO]")
set(PROJ_REPO_URL "https://github.com/joan2937/pigpio.git")
set(PIGPIO_VERSION "master")

ExternalProject_Add(
        project_${LIBNAME}
        GIT_REPOSITORY "${PROJ_REPO_URL}"
        GIT_TAG ${PIGPIO_VERSION}
        PREFIX ${LIBNAME}
        CONFIGURE_COMMAND ""
        UPDATE_COMMAND ""
        INSTALL_DIR "${LIBNAME}/install"
        BUILD_IN_SOURCE 1
        INSTALL_COMMAND $(MAKE) install DESTDIR=<INSTALL_DIR>
        BUILD_COMMAND $(MAKE) CC=${CMAKE_C_COMPILER} CXX=${CMAKE_CXX_COMPILER} STRIP=${CMAKE_STRIP}
)

ExternalProject_Get_Property(project_${LIBNAME} SOURCE_DIR INSTALL_DIR)

# Hack (See: https://stackoverflow.com/questions/45516209/cmake-how-to-use-interface-include-directories-with-externalproject)
file(MAKE_DIRECTORY ${INSTALL_DIR}/usr/local/include)

add_library(pigpio SHARED IMPORTED)
add_library(pigpiod_if2 SHARED IMPORTED)

set_target_properties(pigpio PROPERTIES INTERFACE_INCLUDE_DIRECTORIES ${INSTALL_DIR}/usr/local/include)
set_target_properties(pigpiod_if2 PROPERTIES INTERFACE_INCLUDE_DIRECTORIES ${INSTALL_DIR}/usr/local/include)

set_target_properties(pigpio PROPERTIES IMPORTED_LOCATION "${INSTALL_DIR}/usr/local/lib/${CMAKE_SHARED_LIBRARY_PREFIX}${LIBNAME}${CMAKE_SHARED_LIBRARY_SUFFIX}")
set_target_properties(pigpiod_if2 PROPERTIES IMPORTED_LOCATION "${INSTALL_DIR}/usr/local/lib/${CMAKE_SHARED_LIBRARY_PREFIX}${LIBNAME}d_if2${CMAKE_SHARED_LIBRARY_SUFFIX}")

add_dependencies(pigpio project_${LIBNAME})
add_dependencies(pigpiod_if2 project_${LIBNAME})