set(PROJ_ROOT ${CMAKE_BINARY_DIR}/thirdparty/cperiphery)
set(LIBNAME periphery)

include(ExternalProject)

# c-periphery
ExternalProject_Add(
        cperiphery
        GIT_REPOSITORY "https://github.com/vsergeev/c-periphery.git"
        GIT_TAG "master"
        #BUILD_IN_SOURCE true
        UPDATE_COMMAND ""
        PATCH_COMMAND ""
        CONFIGURE_COMMAND ""
        BINARY_DIR "${PROJ_ROOT}/src"
        SOURCE_DIR "${PROJ_ROOT}/src"
        INSTALL_COMMAND ""
        UPDATE_COMMAND ""
        BUILD_COMMAND make all
)

ExternalProject_Get_property(cperiphery BINARY_DIR)

message("[C-PERIPHERY] Binary directory = ${BINARY_DIR}")

#file(GLOB CPERIPHERY_INCLUDE ${BINARY_DIR}/src/*.h)
set(CPERIPHERY_INCLUDE "${BINARY_DIR}/src")
set(CPERIPHERY_LIB "${BINARY_DIR}/${LIBNAME}${CMAKE_STATIC_LIBRARY_SUFFIX}")

message(STATUS "[C-PERIPHERY] CPERIPHERY_INCLUDE = ${CPERIPHERY_INCLUDE}")
message(STATUS "[C-PERIPHERY] CPERIPHERY_LIB = ${CPERIPHERY_LIB}")

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(CPeriphery FOUND_VAR CPeriphery_FOUND REQUIRED_VARS CPERIPHERY_INCLUDE CPERIPHERY_LIB FAIL_MESSAGE "Could not find cperiphery package")

mark_as_advanced(
        CPERIPHERY_LIB
        CPERIPHERY_INCLUDE
)