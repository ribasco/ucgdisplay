set(PROJ_NAME c-periphery)
set(PROJ_PATH ${LIB_DIR}/${PROJ_NAME})

DOWNLOAD_GITPROJ("https://github.com/vsergeev/c-periphery" ${PROJ_NAME} "master")

file(GLOB PROJ_SRC_FILES "${LIB_DIR}/${PROJ_NAME}/src/*.c")
file(GLOB PROJ_HEADER_FILES "${LIB_DIR}/${PROJ_NAME}/src/*.h")

add_library(cperiphery STATIC ${PROJ_HEADER_FILES} ${PROJ_SRC_FILES})

set_target_properties(cperiphery
        PROPERTIES
        ARCHIVE_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/install/${PROJ_NAME}/lib"
        LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/install/${PROJ_NAME}/lib"
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/install/${PROJ_NAME}/lib"
        )

set(CPERIPHERY_INCLUDE "${PROJ_PATH}/src")
set(CPERIPHERY_LIB ${PROJECT_BINARY_DIR}/install/${PROJ_NAME}/lib/${CMAKE_STATIC_LIBRARY_PREFIX}cperiphery${CMAKE_STATIC_LIBRARY_SUFFIX})

message(STATUS "[C-PERIPHERY] CPERIPHERY_INCLUDE = ${CPERIPHERY_INCLUDE}")
message(STATUS "[C-PERIPHERY] CPERIPHERY_LIB = ${CPERIPHERY_LIB}")

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(CPeriphery FOUND_VAR CPeriphery_FOUND REQUIRED_VARS CPERIPHERY_INCLUDE PROJ_SRC_FILES PROJ_HEADER_FILES FAIL_MESSAGE "Could not find cperiphery package")

mark_as_advanced(
        CPERIPHERY_LIB
        CPERIPHERY_INCLUDE
)