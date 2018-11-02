#!/usr/bin/env bash
set -e

if [ -z "$1" ]
then
    echo "Argument not specified"
    exit 1
fi

mvn versions:set -DnewVersion=$1