#!/usr/bin/env bash

ls -l native/tools/thirdparty

cd native/tools/thirdparty

mkdir libtapi

LIBTAPI_PATH=$(pwd)/libtapi

tar -C ./libtapi -xvf libtapi.tar.gz

ls -l ${LIBTAPI_PATH}

echo "Creating a symlink to /usr/local/lib/libtapi.so"
sudo ln -s ${LIBTAPI_PATH}/lib/libtapi.so.6.0.1 /usr/local/lib/libtapi.so

ls -l /usr/local/lib/libtapi.so

mkdir libxar

LIBXAR_PATH=$(pwd)/libxar

tar -C ./libxar -xvf libxar.tar.gz

ls -l ${LIBXAR_PATH}

echo "Creating a symlink to /usr/local/lib/libxar.so"
sudo ln -s ${LIBXAR_PATH}/lib/libxar.so.1 /usr/local/lib/libxar.so
sudo ln -s ${LIBXAR_PATH}/lib/libxar.a /usr/local/lib/libxar.a
sudo ln -s ${LIBXAR_PATH}/lib/libxar.la /usr/local/lib/libxar.la

echo "Confirming paths"
ls -l /usr/local/lib/libxar.so
ls -l /usr/local/lib/libxar.a
ls -l /usr/local/lib/libxar.la