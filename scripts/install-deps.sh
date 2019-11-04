#!/usr/bin/env bash

set -e

if [[ ! $TRAVIS_OS_NAME == 'linux' ]]; then
    exit 0;
fi

echo "======================================================"
echo "Installing xar from source"
echo "======================================================"

# Install xar
mkdir -p tmp
cd tmp
git clone https://github.com/mackyle/xar.git
cd xar/xar
./autogen.sh
make
sudo make install

cd ../..

echo "======================================================"
echo "Installing TAPI from source"
echo "======================================================"

git clone https://github.com/tpoechtrager/apple-libtapi.git
cd apple-libtapi
./build.sh
sudo ./install.sh

# cd ..
#
# echo "======================================================"
# echo "Installing cctools from source"
# echo "======================================================"
#
# git clone https://github.com/tpoechtrager/cctools-port.git
# cd cctools-port/cctools
# ./configure
# make
# sudo make install