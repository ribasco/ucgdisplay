#!/usr/bin/env bash
set -e

# ==================================================
# Steps to update
# ==================================================
# 1) Compile and run UpdateControllerHeader
# 2) Compile and run codegen
# 3) Commit and push changes to master (if any)

SCRIPT_DIR=$(pwd)
PROJECT_DIR=${SCRIPT_DIR}/../native/modules/graphics
UTILS_DIR=${PROJECT_DIR}/src/main/cpp/utils
ARCH=$(uname -m)

# Switch working directory
cd ${SCRIPT_DIR}

# Compile and run UpdateControllerHeader program
mvn compiler:compile
mvn exec:java -Dexec.mainClass=com.ibasco.ucgdisplay.utils.codegen.UpdateControllerHeader

# Switch working directory
cd ${UTILS_DIR}

GENERATOR=""

if [ "$(uname)" == "Darwin" ]; then
    # Do something under Mac OS X platform
    GENERATOR="CodeBlocks - Unix Makefiles"
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    # Do something under GNU/Linux platform
    GENERATOR="CodeBlocks - Unix Makefiles"
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
    # Do something under 32 bits Windows NT platform
    GENERATOR="CodeBlocks - MinGW Makefiles"
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
    # Do something under 64 bits Windows NT platform
    GENERATOR="CodeBlocks - MinGW Makefiles"
fi

# Build the code
cmake --target ucgd-code -G "${GENERATOR}'" -H. -Bbuild

