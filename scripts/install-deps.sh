#!/usr/bin/env bash

set -e

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

# From: https://github.com/tpoechtrager/cctools-port/blob/master/.travis.yml
# mkdir -p tmp && TMP=$PWD/tmp && git clone https://github.com/tpoechtrager/apple-libtapi.git && cd apple-libtapi && INSTALLPREFIX=$TMP ./build.sh && ./install.sh && cd .. && cd cctools && ./configure --with-libtapi=$TMP && make