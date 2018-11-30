#!/usr/bin/env bash

if [[ -z ${1} ]]
then
      echo "Build target not specified (e.g. native-build-cc-linux-arm)"
      exit -1
fi

ant -Droot.dir=.. -buildfile build-graphics.xml ${1}