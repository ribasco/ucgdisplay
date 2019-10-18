# Cross-compiling

### Prerequisites

Refer to the links under References for more information about these packages

- Debian Linux OS (Ubuntu preferred)
- CMake 3.10+
- OSXCROSS Toolchain (For cross compiling mac osx binaries)
- Mingw 64 (For cross compiling linux binaries)
- Raspberry Pi Toolchain (For cross compiling arm binaries for the Pi)
- Packages: gcc-multilib, g++-multilib, mingw-w64, g++-7, gcc-7

### Method #1: Apache ANT

> Note: Build files are located under root 'scripts' directory 

Build Graphics Module

```
cd scripts
ant -file build-graphics.xml -Droot.dir=<project root directory> native-build-cc-all
```

Build Input Module
```
cd scripts
ant -file build-input.xml -Droot.dir=<project root directory> native-build-cc-all
```

### Method #2: CMake (v3.10 above)

CMake v3.10+ is required

#### Linux - ARM 32bit

CMake arguments:
```
-DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/RpiToolchain.cmake
```

#### Windows - 64bit
CMake arguments:
```
-DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/MingWToolchain.cmake
```

#### Mac OSX - 64bit
CMake arguments:
```
-DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/OSXToolchain.cmake
-DCMAKE_BUILD_TYPE=Release
-DOSXCROSS_HOST=x86_64-apple-darwin15
-DOSXCROSS_TARGET_DIR=<tools dir path>/osxcross
-DOSXCROSS_SDK=<tools dir path>/osxcross/SDK/MacOSX10.11.sdk
-DOSXCROSS_TARGET=darwin15
```

### Building cross-compiler with crosstool-ng

Coming soon

# References
- [Ubuntu Kernel headers/images (all platforms)](https://kernel.ubuntu.com/~kernel-ppa/mainline/)
- [Linaro Toolchain Downloads](https://www.linaro.org/downloads/)
- [Crosstool-NG Documentation](https://crosstool-ng.github.io/docs/)
- [Installing RPI Packages from Ubuntu with chroot](https://raspberrypi.stackexchange.com/questions/23675/install-raspbian-packages-directly-from-ubuntu-with-chroot-to-raspbian-file-syst)
- [Debian "buster" for Raspberry Pi 3 on QEMU](https://github.com/wimvanderbauwhede/limited-systems/wiki/Debian-%22buster%22-for-Raspberry-Pi-3-on-QEMU)
- [MingW-w64](http://mingw-w64.org)
- [Mac OS X cross toolchain for Linux and FreeBSD (OSXCROSS)](https://github.com/tpoechtrager/osxcross)
- [Prebuilt Raspberry Pi Toolchain (Linaro 32bit/64bit)](https://github.com/ribasco/rpi-tools)