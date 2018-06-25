macro(GetLibFromSys)
    set(LIBEVENT_ROOT CACHE PATH "Root directory of libevent installation")
    set(LibEvent_EXTRA_PREFIXES /usr/local /opt/local "$ENV{HOME}" ${LIBEVENT_ROOT})

    foreach (prefix ${LibEvent_EXTRA_PREFIXES})
        message(STATUS "[LIBEVENT] Search Prefix: ${prefix}")
        list(APPEND LibEvent_INCLUDE_PATHS "${prefix}/include")
        list(APPEND LibEvent_LIBRARIES_PATHS "${prefix}/lib")
    endforeach ()

    # Looking for "event.h" will find the Platform SDK include dir on windows
    # so we also look for a peer header like evhttp.h to get the right path
    find_path(LIBEVENT_INCLUDE_DIRS evhttp.h event.h PATHS ${LibEvent_INCLUDE_PATHS})

    # "lib" prefix is needed on Windows in some cases
    # newer versions of libevent use three libraries
    find_library(LIBEVENT_PTHREADS_LIB NAMES event_pthreads PATHS ${LibEvent_LIBRARIES_PATHS}) #NO_DEFAULT_PATH NO_CMAKE_FIND_ROOT_PATH
    find_library(LIBEVENT_LIBRARIES NAMES event event_core event_extra libevent event_pthread PATHS ${LibEvent_LIBRARIES_PATHS}) #NO_DEFAULT_PATH NO_CMAKE_FIND_ROOT_PATH

    if (LIBEVENT_LIBRARIES AND LIBEVENT_INCLUDE_DIRS)
        set(Libevent_FOUND TRUE)
        set(LIBEVENT_LIBRARIES ${LIBEVENT_LIBRARIES})
    else ()
        set(Libevent_FOUND FALSE)
    endif ()
    set(Libevent_METHOD "SYS")
endmacro()

macro(GetLibFromRepo)
    include(ExternalProject)

    set(LIBNAME "event")
    set(CMAKE_INSTALL_PREFIX ${PROJECT_BINARY_DIR}/install/libevent)

    message(STATUS "[LIBEVENT] C-COMPILER: ${CMAKE_C_COMPILER}")
    message(STATUS "[LIBEVENT] C++COMPILER: ${CMAKE_CXX_COMPILER}")
    message(STATUS "[LIBEVENT] SYSROOT: ${CMAKE_SYSROOT}")

    ExternalProject_Add(
            libevent
            GIT_REPOSITORY "https://github.com/libevent/libevent.git"
            GIT_TAG release-2.1.8-stable
            STAMP_DIR ${PROJECT_BINARY_DIR}/stamp
            SOURCE_DIR ${CMAKE_SOURCE_DIR}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}${LIBNAME}
            INSTALL_DIR ${CMAKE_INSTALL_PREFIX}
            INSTALL_COMMAND make install
            #CONFIGURE_COMMAND ""
            #UPDATE_COMMAND ""
            BUILD_COMMAND make all
            CMAKE_ARGS -DCMAKE_INSTALL_PREFIX:PATH=<INSTALL_DIR> -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER} -DCMAKE_CXX_COMPILER=${CMAKE_CXX_COMPILER} -DCMAKE_SYSROOT=${CMAKE_SYSROOT} -DEVENT__DISABLE_OPENSSL=ON -DEVENT__BUILD_SHARED_LIBRARIES=ON -DEVENT__DISABLE_THREAD_SUPPORT=OFF
    )

    set(LIBEVENT_INCLUDE_DIRS "${CMAKE_INSTALL_PREFIX}/include")
    set(LIBEVENT_LIBRARIES "${CMAKE_INSTALL_PREFIX}/lib/${CMAKE_SHARED_LIBRARY_PREFIX}${LIBNAME}${CMAKE_SHARED_LIBRARY_SUFFIX}")

    list(APPEND LIBEVENT_LIBRARIES "pthread")

    if (LIBEVENT_INCLUDE_DIRS AND LIBEVENT_LIBRARIES)
        set(LibEvent_FOUND TRUE)
    else ()
        set(LibEvent_FOUND FALSE)
    endif ()

    set(LibEvent_METHOD "REPO")
endmacro()

GetLibFromSys()

if (CMAKE_CROSSCOMPILING OR (NOT LibEvent_FOUND))
    message(STATUS "[LIBEVENT] No libevent found in the current system. Retrieving library from REPOSITORY")
    GetLibFromRepo()
else ()
    if (LibEvent_FOUND)
        if (NOT LibEvent_FIND_QUIETLY)
            message(STATUS "Found libevent: ${LIBEVENT_LIBRARIES}")
        endif ()
    else ()
        if (LibEvent_FIND_REQUIRED)
            message(FATAL_ERROR "Could NOT find libevent.")
        endif ()
        message(STATUS "libevent NOT found.")
    endif ()
endif ()

message(STATUS "[LIBEVENT] INCLUDE = ${LIBEVENT_INCLUDE_DIRS}")
message(STATUS "[LIBEVENT] LIBRARIES = ${LIBEVENT_LIBRARIES}")

mark_as_advanced(
        LIBEVENT_LIBRARIES
        LIBEVENT_INCLUDE_DIRS
)
