#!/usr/bin/env bash

echo "   _____ ____  __  __ _____ _____ _      ______            _      _      "
echo "  / ____/ __ \|  \/  |  __ \_   _| |    |  ____|     /\   | |    | |     "
echo " | |   | |  | | \  / | |__) || | | |    | |__       /  \  | |    | |     "
echo " | |   | |  | | |\/| |  ___/ | | | |    |  __|     / /\ \ | |    | |     "
echo " | |___| |__| | |  | | |    _| |_| |____| |____   / ____ \| |____| |____ "
echo "  \_____\____/|_|  |_|_|   |_____|______|______| /_/    \_\______|______|"
echo "                                                                         "


cd /home/raffy/projects/ucgdisplay

mvn clean package -P'cross-compile,!build-linux-x86_64' -DskipTests=true -Dgpg.skip
