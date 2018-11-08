#!/usr/bin/env bash
set -e

echo "  #####   #######  #     #  ######   ###  #        #######"
echo " #     #  #     #  ##   ##  #     #   #   #        #      "
echo " #        #     #  # # # #  #     #   #   #        #      "
echo " #        #     #  #  #  #  ######    #   #        #####  "
echo " #        #     #  #     #  #         #   #        #      "
echo " #     #  #     #  #     #  #         #   #        #      "
echo "  #####   #######  #     #  #        ###  #######  #######"

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    mvn clean compile -P'cross-compile,!build-linux-amd64'
else
    mvn clean compile
fi