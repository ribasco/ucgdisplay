if (CMAKE_CROSSCOMPILING)
    #[[message(STATUS "[FIND-JNI] Cross-compilation detected. Checking for available JDK (Host System = ${CMAKE_HOST_SYSTEM_NAME}, Target OS = ${CMAKE_SYSTEM_NAME}, Target ARCH = ${CMAKE_SYSTEM_PROCESSOR})")

    set(JDK_SEARCH_DIR "")

    if (WIN32)
        set(JDK_SEARCH_DIR "jdk/windows/${CMAKE_SYSTEM_PROCESSOR}")
    elseif (UNIX)
        set(JDK_SEARCH_DIR "jdk/linux/${CMAKE_SYSTEM_PROCESSOR}")
    else ()
        message(FATAL_ERROR "[FIND-JNI] Target OS/ARCH is not currently supported")
    endif ()

    message(STATUS "[FIND-JNI] JDK Search Directory Path = ${TOOLS_DIR_PATH}/${JDK_SEARCH_DIR}")

    # set(JDK_SEARCH_FULL_PATH "${TOOLS_DIR_PATH}/${JDK_SEARCH_DIR}/jdk*")
    set(JDK_SEARCH_FULL_PATH "${JDK_INCLUDE_DIR}/${JDK_SEARCH_DIR}/jdk*")

    # Find JDK
    file(GLOB files "${JDK_SEARCH_FULL_PATH}")
    foreach (file ${files})
        message(STATUS "[FIND-JNI] File: ${file}")
        set(JDK_DIR ${file})
        break()
    endforeach ()

    if (NOT JDK_DIR OR (NOT EXISTS ${JDK_DIR}))
        message(FATAL_ERROR "Could not find available JDK. Please download/install the appropriate JDK for your target os/architecture and put it inside <project_dir>/tools/jdk/* directory")
    endif ()

    message(STATUS "[FIND-JNI] JDK DIR = ${JDK_DIR}")]]

    if (NOT EXISTS ${JAVA_HOME})
        message(FATAL_ERROR "Invalid JAVA_HOME = ${JAVA_HOME}")
    endif ()

    set(JNI_INCLUDE_DIRS "")
    set(JNI_LIBRARIES "")

    if (${CMAKE_SYSTEM_PROCESSOR} MATCHES "^arm")
        list(APPEND JNI_INCLUDE_DIRS "${JAVA_HOME}/include")
        list(APPEND JNI_INCLUDE_DIRS "${JAVA_HOME}/include/linux")
        list(APPEND JNI_LIBRARIES "${JAVA_HOME}/lib/arm/libjawt.so")
        list(APPEND JNI_LIBRARIES "${JAVA_HOME}/jre/lib/arm/server/libjvm.so")
    elseif (CMAKE_SYSTEM_PROCESSOR STREQUAL "x86_64")
        list(APPEND JNI_INCLUDE_DIRS "${JAVA_HOME}/include")
        list(APPEND JNI_INCLUDE_DIRS "${JAVA_HOME}/include/win32")
        list(APPEND JNI_LIBRARIES "${JAVA_HOME}/lib/jawt.lib")
        list(APPEND JNI_LIBRARIES "${JAVA_HOME}/lib/jvm.lib")
        # list(APPEND JNI_LIBRARIES "${JAVA_HOME}/jre/lib/jvm.lib")
    endif ()

    foreach (item ${JNI_INCLUDE_DIRS})
        if (NOT EXISTS ${item})
            message(FATAL_ERROR "Could not find entry ${item}")
        endif ()
    endforeach (item)

    set(JNI_FOUND true)
else ()
    # Use default find jni package if we are not cross-compiling
    message(STATUS "[FIND-JNI] Using default FindJNI package")
    include(FindJNI)
endif ()

message(STATUS "[FIND-JNI] JNI_LIBRARIES = ${JNI_LIBRARIES}")
message(STATUS "[FIND-JNI] JNI_LIBRARIES = ${JNI_LIBRARIES}")
message(STATUS "[FIND-JNI] JNI_INCLUDE_DIRS = ${JNI_INCLUDE_DIRS}")