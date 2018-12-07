#!/usr/bin/env bash
set -e

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then

    wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.8.17/linux-headers-4.8.17-040817_4.8.17-040817.201701090438_all.deb
    wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.8.17/linux-headers-4.8.17-040817-generic_4.8.17-040817.201701090438_amd64.deb
    wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.8.17/linux-image-4.8.17-040817-generic_4.8.17-040817.201701090438_amd64.deb
    sudo dpkg -i linux-image-4.8.17-040817-generic_4.8.17-040817.201701090438_amd64.deb
    sudo dpkg -i linux-headers-4.8.17-040817-generic_4.8.17-040817.201701090438_amd64.deb
    sudo dpkg -i linux-headers-4.8.17-040817_4.8.17-040817.201701090438_all.deb
    uname -sr
fi