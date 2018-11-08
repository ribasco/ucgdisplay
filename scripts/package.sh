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
    mvn package -P'cross-compile,!build-linux-amd64' -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true
else
    mvn package -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true
fi