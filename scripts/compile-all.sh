#!/usr/bin/env bash

cd /home/raffy/projects/ucgdisplay

mvn clean package -P'cross-compile,!build-linux-x86_64' -DskipTests=true -Dgpg.skip
