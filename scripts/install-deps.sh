#!/usr/bin/env bash

set -e

if [[ ! $TRAVIS_OS_NAME == 'linux' ]]; then
    exit 0;
fi

echo "Installing xar from source"

# Install xar

mkdir -p tmp

cd tmp

git clone https://github.com/mackyle/xar.git

cd xar/xar

./autogen.sh

./configure

make

sudo make install