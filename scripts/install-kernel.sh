#!/usr/bin/env bash
set -e

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    P1=https://kernel.ubuntu.com/~kernel-ppa/mainline/v4.19.75/linux-image-unsigned-4.19.75-041975-generic_4.19.75-041975.201909210733_amd64.deb
    P2=https://kernel.ubuntu.com/~kernel-ppa/mainline/v4.19.75/linux-headers-4.19.75-041975_4.19.75-041975.201909210733_all.deb
    P3=https://kernel.ubuntu.com/~kernel-ppa/mainline/v4.19.75/linux-headers-4.19.75-041975-generic_4.19.75-041975.201909210733_amd64.deb
    #P1=http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.15.18/linux-image-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    #P2=http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.15.18/linux-headers-4.15.18-041518_4.15.18-041518.201804190330_all.deb
    #P3=http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.15.18/linux-headers-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb

    wget ${P1}
    wget ${P2}
    wget ${P3}
    sudo dpkg -i linux-image-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    sudo dpkg -i linux-headers-4.15.18-041518_4.15.18-041518.201804190330_all.deb
    sudo dpkg -i linux-headers-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    #sudo dpkg -i linux-image-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    #sudo dpkg -i linux-headers-4.15.18-041518_4.15.18-041518.201804190330_all.deb
    #sudo dpkg -i linux-headers-4.15.18-041518-generic_4.15.18-041518.201804190330_amd64.deb
    uname -sr
fi