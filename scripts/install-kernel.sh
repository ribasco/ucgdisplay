#!/usr/bin/env bash
set -e

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    P1=http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.15.18/linux-image-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    P2=http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.15.18/linux-headers-4.15.18-041518_4.15.18-041518.201804190330_all.deb
    P3=http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.15.18/linux-headers-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb

    wget ${P1}
    wget ${P2}
    wget ${P3}
    sudo dpkg -i linux-image-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    sudo dpkg -i linux-headers-4.15.18-041518_4.15.18-041518.201804190330_all.deb
    sudo dpkg -i linux-headers-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    uname -sr
fi