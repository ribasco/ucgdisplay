set(CMAKE_INSTALL_PREFIX ${PROJECT_BINARY_DIR}/install/libevent)

macro(GetLibFromSys)
    set(LIBEVENT_ROOT CACHE PATH "" "Root directory of libevent installation")
    set(LibEvent_EXTRA_PREFIXES /usr/local /opt/local /usr)

    if (${CMAKE_CROSSCOMPILING})
        list(APPEND LibEvent_EXTRA_PREFIXES ${CMAKE_INSTALL_PREFIX})
    endif ()

    foreach (prefix ${LibEvent_EXTRA_PREFIXES})
        message(STATUS "[LIBEVENT] + Searching in: ${prefix}")
        list(APPEND LibEvent_INCLUDE_PATHS "${prefix}/include")
        list(APPEND LibEvent_LIBRARIES_PATHS "${prefix}/lib")
    endforeach ()

    find_path(LIBEVENT_INCLUDE_DIRS evhttp.h event.h PATHS ${LibEvent_INCLUDE_PATHS} NO_CMAKE_FIND_ROOT_PATH)

    find_library(LIBEVENT_PTHREADS_LIB NAMES event_pthreads PATHS ${LibEvent_LIBRARIES_PATHS} NO_CMAKE_FIND_ROOT_PATH) #NO_DEFAULT_PATH NO_CMAKE_FIND_ROOT_PATH
    find_library(LIBEVENT_LIBRARIES NAMES event event_core event_extra libevent event_pthread PATHS ${LibEvent_LIBRARIES_PATHS} NO_CMAKE_FIND_ROOT_PATH) #NO_DEFAULT_PATH NO_CMAKE_FIND_ROOT_PATH

    message(STATUS "======================================================================")
    message(STATUS "[LIBEVENT] LibEvent_EXTRA_PREFIXES = ${LibEvent_EXTRA_PREFIXES}")
    message(STATUS "[LIBEVENT] LibEvent_INCLUDE_PATHS = ${LibEvent_INCLUDE_PATHS}")
    message(STATUS "[LIBEVENT] LibEvent_LIBRARIES_PATHS = ${LibEvent_LIBRARIES_PATHS}")
    message(STATUS "[LIBEVENT] LIBEVENT_LIBRARIES = ${LIBEVENT_LIBRARIES}")
    message(STATUS "[LIBEVENT] LIBEVENT_INCLUDE_DIRS = ${LIBEVENT_INCLUDE_DIRS}")
    message(STATUS "======================================================================")

    if (LIBEVENT_LIBRARIES AND LIBEVENT_INCLUDE_DIRS)
        message(STATUS "[LIBEVENT] GetLibFromSys() = FOUND")
        set(LibEvent_FOUND TRUE)
    else ()
        message(STATUS "[LIBEVENT] GetLibFromSys() = NOT FOUND")
        set(LibEvent_FOUND FALSE)
    endif ()
endmacro()

macro(GetLibFromRepo)
    include(ExternalProject)

    set(LIBNAME "event")

    # TODO: Check if we have already downloaded libevent

    ExternalProject_Add(
            libevent
            GIT_REPOSITORY "https://github.com/libevent/libevent.git"
            GIT_TAG release-2.1.8-stable
            STAMP_DIR ${PROJECT_BINARY_DIR}/stamp
            SOURCE_DIR ${CMAKE_SOURCE_DIR}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}${LIBNAME}
            INSTALL_DIR ${CMAKE_INSTALL_PREFIX}
            INSTALL_COMMAND make install
            #CONFIGURE_COMMAND ""
            UPDATE_COMMAND ""
            BUILD_COMMAND make all
            CMAKE_ARGS -DCMAKE_INSTALL_PREFIX:PATH=<INSTALL_DIR> -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER} -DCMAKE_CXX_COMPILER=${CMAKE_CXX_COMPILER} -DCMAKE_SYSROOT=${CMAKE_SYSROOT} -DEVENT__DISABLE_OPENSSL=ON -DEVENT__BUILD_SHARED_LIBRARIES=ON -DEVENT__DISABLE_THREAD_SUPPORT=OFF
    )

    set(LIBEVENT_INCLUDE_DIRS "${CMAKE_INSTALL_PREFIX}/include")
    set(LIBEVENT_LIBRARIES "${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}${LIBNAME}${CMAKE_SHARED_LIBRARY_SUFFIX}")

    list(APPEND LIBEVENT_LIBRARIES "pthread")

    if (LIBEVENT_INCLUDE_DIRS AND LIBEVENT_LIBRARIES)
        message(STATUS "[LIBEVENT] GetLibFromRepo() = FOUND")
        set(LibEvent_FOUND TRUE)
    else ()
        message(STATUS "[LIBEVENT] GetLibFromRepo() = NOT FOUND")
        set(LibEvent_FOUND FALSE)
    endif ()
endmacro()

if (NOT CMAKE_CROSSCOMPILING)
    GetLibFromSys()
endif ()

# Always retrieve the library from repository if we are cross-compiling
if (CMAKE_CROSSCOMPILING OR (NOT CMAKE_CROSSCOMPILING AND NOT LibEvent_FOUND))
    message(STATUS "[LIBEVENT] Retrieving 'libevent' from external repository")
    GetLibFromRepo()
endif ()

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(LibEvent FOUND_VAR LibEvent_FOUND REQUIRED_VARS LIBEVENT_INCLUDE_DIRS LIBEVENT_LIBRARIES FAIL_MESSAGE "Could not find libevent package")

message(STATUS "[LIBEVENT] INCLUDE = ${LIBEVENT_INCLUDE_DIRS}")
message(STATUS "[LIBEVENT] LIBRARIES = ${LIBEVENT_LIBRARIES}")

mark_as_advanced(
        LIBEVENT_LIBRARIES
        LIBEVENT_INCLUDE_DIRS
)
