#!/usr/bin/env bash
set -e

echo " ###  #     #   #####   #######     #     #        #      "
echo "  #   ##    #  #     #     #       # #    #        #      "
echo "  #   # #   #  #           #      #   #   #        #      "
echo "  #   #  #  #   #####      #     #     #  #        #      "
echo "  #   #   # #        #     #     #######  #        #      "
echo "  #   #    ##  #     #     #     #     #  #        #      "
echo " ###  #     #   #####      #     #     #  #######  #######"

# Only cross-compile on linux (x86_64/amd64) environment

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    mvn --settings scripts/settings.xml install -P'cross-compile,!build-linux-amd64' -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V
else
    mvn --settings scripts/settings.xml install -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V
fi