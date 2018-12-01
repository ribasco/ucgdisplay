message(STATUS "Including c-periphery")
include(ExternalProject)

# c-periphery
ExternalProject_Add(
        cperiphery
        GIT_REPOSITORY "https://github.com/vsergeev/c-periphery.git"
        GIT_TAG "master"
        BUILD_IN_SOURCE true
        UPDATE_COMMAND ""
        PATCH_COMMAND ""
        CONFIGURE_COMMAND ""
        #INSTALL_DIR ${CMAKE_INSTALL_PREFIX}
        SOURCE_DIR "${CMAKE_SOURCE_DIR}/lib/c-periphery"
        #INSTALL_COMMAND ""
        #UPDATE_COMMAND ""
        BUILD_COMMAND make all
)

add_dependencies(ucgdisp cperiphery)