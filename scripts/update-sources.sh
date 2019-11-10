#!/usr/bin/env bash
set -e

# ==================================================
# Steps to update
# ==================================================
# 1) Compile and run UpdateControllerHeader
# 2) Compile and run codegen
# 3) Commit and push changes to master (if any)

echo '=============================================================='
echo ''
echo '_________              .___          ________                 '
echo '\_   ___ \   ____    __| _/  ____   /  _____/   ____    ____  '
echo '/    \  \/  /  _ \  / __ | _/ __ \ /   \  ___ _/ __ \  /    \ '
echo '\     \____(  <_> )/ /_/ | \  ___/ \    \_\  \\  ___/ |   |  \'
echo ' \______  / \____/ \____ |  \___  > \______  / \___  >|___|  /'
echo '        \/              \/      \/         \/      \/      \/ '
echo '                     code generator script                    '
echo '=============================================================='

SCRIPT_DIR=$(pwd)
BASE_DIR=$(realpath ${SCRIPT_DIR}/..)
PROJECT_DIR=$(realpath ${SCRIPT_DIR}/../native/modules/graphics)
UTILS_DIR=${PROJECT_DIR}/src/main/cpp/utils
ARCH=$(uname -m)

echo "BASE_DIR=${BASE_DIR}"
echo "SCRIPT_DIR=${SCRIPT_DIR}"
echo "PROJECT_DIR=${PROJECT_DIR}"
echo "UTILS_DIR=${UTILS_DIR}"

# Switch working directory
cd ${PROJECT_DIR}

echo ===========================================
echo "Updating controllers.h definitions"
echo ===========================================

# Compile and run UpdateControllerHeader program
mvn compiler:compile
mvn exec:java -Dexec.mainClass=com.ibasco.ucgdisplay.utils.codegen.UpdateControllerHeader

# Switch working directory
cd ${UTILS_DIR}

GENERATOR=""

# Build the code
echo "Reloading CMAKE cache"
cmake --target ucgd-code -G 'CodeBlocks - Unix Makefiles' -H. -Bbuild

echo "Building ucgd-code"
cmake --build ${UTILS_DIR}/build --target ucgd-code -- -j 4

cd ${UTILS_DIR}/build/bin

echo ===========================================
echo "Running code-generator (Base Dir=${BASE_DIR})"
echo ===========================================

./ucgd-code ${BASE_DIR} -a

if [[ -z "${TRAVIS_JOB_NUMBER}" ]]
then
      echo "You are not on Travis Build System. Skipping auto-commit"
      exit 0
fi

GITHUB_USER="ribasco"
GITHUB_AUTHOR_NAME="Source Updater"
GITHUB_AUTHOR_EMAIL="ribasco@gmail.com"

cd ${BASE_DIR}

echo ===========================================
echo "Configuring commit author details"
echo ===========================================

echo "Using GitHub Details = ${GITHUB_AUTHOR_EMAIL} and ${GITHUB_AUTHOR_NAME}"

git config --global user.email "${GITHUB_AUTHOR_EMAIL}"
git config --global user.name "${GITHUB_AUTHOR_NAME}"
git config --global push.default simple

git commit -m "Source code update"

echo ===========================================
echo "Pushing updates to remote repository"
echo ===========================================

# Push to the remote repository
git push --quiet https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/ribasco/ucgdisplay

if [ $? -eq 0 ]; then
    echo "Source updated successfully"
else
    echo "Failed to commit source to remote repository"
fi