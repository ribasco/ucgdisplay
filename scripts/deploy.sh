#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# Do not deploy on pull-requests or commits on other branches
if [ "${TRAVIS_PULL_REQUEST}" != "false" -o "${TRAVIS_BRANCH}" != "master" ]; then
    echo "Skipping deploy (Reason: Pull-Request or Non-master branch detected)"
    exit 0
fi

if [[ $TRAVIS_OS_NAME == 'linux' ]] && [[ ${TRAVIS_PULL_REQUEST} == 'false' ]] && [[ ${TRAVIS_TAG} == '' ]]; then
    echo "  _____    ______   _____    _         ____   __     __"
    echo " |  __ \  |  ____| |  __ \  | |       / __ \  \ \   / /"
    echo " | |  | | | |__    | |__) | | |      | |  | |  \ \_/ / "
    echo " | |  | | |  __|   |  ___/  | |      | |  | |   \   /  "
    echo " | |__| | | |____  | |      | |____  | |__| |    | |   "
    echo " |_____/  |______| |_|      |______|  \____/     |_|   "
    echo "                                                       "

    mvn deploy -DskipTests --settings scripts/settings.xml
fi