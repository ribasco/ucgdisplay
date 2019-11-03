#!/usr/bin/env bash
set -e

echo "   _____ ____  __  __ _____ _____ _      ______ "
echo "  / ____/ __ \|  \/  |  __ \_   _| |    |  ____|"
echo " | |   | |  | | \  / | |__) || | | |    | |__   "
echo " | |   | |  | | |\/| |  ___/ | | | |    |  __|  "
echo " | |___| |__| | |  | | |    _| |_| |____| |____ "
echo "  \_____\____/|_|  |_|_|   |_____|______|______|"


if [[ $TRAVIS_OS_NAME == 'linux' || ($(uname -s) == 'Linux' && $(uname -m) == 'x86_64') ]]; then
    mvn clean compile -P'cross-compile,!build-linux-x86_64'
else
    mvn clean compile
fi