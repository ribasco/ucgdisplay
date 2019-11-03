#!/usr/bin/env bash
set -e

echo "  _____        _____ _  __          _____ ______ "
echo " |  __ \ /\   / ____| |/ /    /\   / ____|  ____|"
echo " | |__) /  \ | |    | ' /    /  \ | |  __| |__   "
echo " |  ___/ /\ \| |    |  <    / /\ \| | |_ |  __|  "
echo " | |  / ____ \ |____| . \  / ____ \ |__| | |____ "
echo " |_| /_/    \_\_____|_|\_\/_/    \_\_____|______|"
echo "                                                 "

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    mvn package -P'cross-compile,!build-linux-x86_64' -Dlicense.skipUpdateLicense=true -DskipTests=true -Dgpg.skip
else
    mvn package -DskipTests=true -Dgpg.skip
fi