include(ExternalProject)

set(PROJ_NAME c-periphery)
set(PROJ_PATH ${LIB_DIR}/${PROJ_NAME})
set(PROJ_PREFIX "cperiphery")

ExternalProject_Add(
        project_cperiphery
        GIT_REPOSITORY "https://github.com/vsergeev/c-periphery.git"
        GIT_TAG "v1.1.3"
        PREFIX ${PROJ_PREFIX}
        BUILD_IN_SOURCE 1
        INSTALL_COMMAND ""
        CONFIGURE_COMMAND ""
        BUILD_COMMAND make
)

ExternalProject_Get_Property(project_cperiphery INSTALL_DIR SOURCE_DIR)

# Hack (See: https://stackoverflow.com/questions/45516209/cmake-how-to-use-interface-include-directories-with-externalproject)
file(MAKE_DIRECTORY ${SOURCE_DIR}/src)

add_library(cperiphery STATIC IMPORTED GLOBAL)
set_target_properties(cperiphery PROPERTIES INTERFACE_INCLUDE_DIRECTORIES ${SOURCE_DIR}/src)
set_target_properties(cperiphery PROPERTIES IMPORTED_LOCATION ${SOURCE_DIR}/periphery${CMAKE_STATIC_LIBRARY_SUFFIX})

add_dependencies(cperiphery project_cperiphery)