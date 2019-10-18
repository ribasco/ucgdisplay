#!/usr/bin/env bash
set -e

echo " ######      #      #####   #    #     #      #####   #######"
echo " #     #    # #    #     #  #   #     # #    #     #  #      "
echo " #     #   #   #   #        #  #     #   #   #        #      "
echo " ######   #     #  #        ###     #     #  #  ####  #####  "
echo " #        #######  #        #  #    #######  #     #  #      "
echo " #        #     #  #     #  #   #   #     #  #     #  #      "
echo " #        #     #   #####   #    #  #     #   #####   #######"

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    mvn package -P'cross-compile,!build-linux-x86_64' -DskipTests=true -Dgpg.skip
else
    mvn package -DskipTests=true -Dgpg.skip
fi