if (CMAKE_CROSSCOMPILING)
    message(STATUS "[FIND-JNI] Cross-compilation detected. Checking for available ARM JDK")
    # Find JDK
    file(GLOB files "${TOOLS_DIR_PATH}/jdk/jdk*")
    foreach (file ${files})
        #message(STATUS "[FIND-JNI] File: ${file}")
        set(JDK_DIR ${file})
        break()
    endforeach ()

    if (NOT JDK_DIR OR (NOT EXISTS ${JDK_DIR}))
        message(FATAL_ERROR "Could not find ARM JDK. Please download the ARM JDK and put it inside <project_dir>/tools/jdk directory")
    endif ()

    message(STATUS "[FIND-JNI] JDK DIR = ${JDK_DIR}")

    set(JNI_INCLUDE_DIRS "")
    set(JNI_LIBRARIES "")

    list(APPEND JNI_INCLUDE_DIRS "${JDK_DIR}/include")
    list(APPEND JNI_INCLUDE_DIRS "${JDK_DIR}/include/linux")
    list(APPEND JNI_LIBRARIES "${JDK_DIR}/lib/arm/libjawt.so")
    list(APPEND JNI_LIBRARIES "${JDK_DIR}/jre/lib/arm/server/libjvm.so")

    set(JNI_FOUND true)
else ()
    include(FindJNI)
endif ()

message(STATUS "[FIND-JNI] JNI_LIBRARIES = ${JNI_LIBRARIES}")
message(STATUS "[FIND-JNI] JNI_LIBRARIES = ${JNI_LIBRARIES}")
message(STATUS "[FIND-JNI] JNI_INCLUDE_DIRS = ${JNI_INCLUDE_DIRS}")