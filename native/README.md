# Guide to cross-compiling from an x86_64 (64bit) Linux OS

### Prerequisites

Refer to the links under References for more information about these packages

- Debian x86_64 (64bit)  Linux OS. This is the host OS where the compilation will take place.
- GCC v7.4 above
- CMake 3.10+
- [OSXCROSS Toolchain](https://github.com/tpoechtrager/osxcross) (For cross compiling mac osx binaries from a Linux x86_64 machine). Provided by the project.
- [Raspberry Pi Toolchain](https://github.com/ribasco/rpi-tools)  (For cross compiling arm binaries frpom a Linux x86_64 machine). Provided by the project.
- [MingW-w64 Toolchain](http://mingw-w64.org/doku.php) (For cross compiling windows binaries from a Linux x86_64 machine).
- **Required Linux Packages:**
	- gcc-multilib
	- g++-multilib
	- mingw-w64
	- g++-7
	- gcc-7

> Note: multilib packages are required to compile 32-bit binaries from a 64bit host machine

### Package installation

```
sudo apt updatee
```

```
sudo apt install gcc-multilib g++-multilib mingw-w64, g++-7 gcc-7
```

### Method #1: Apache ANT (Easy Method)

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

**Legend:**

- **project root dir** = The root project directory (e.g. /home/user/projects/ucgdisplay)

**CMake build process:**

1. Run cmake build to generate the necessary build files
2. Run make to proceed with the compilation

#### Linux - ARM 32bit

CMake arguments:
```
cmake -DCMAKE_BUILD_TYPE=Release 
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/RpiToolchain-linux-32.cmake 
      -G "CodeBlocks - Unix Makefiles" 
       <project root dir>/native/modules/graphics/src/main/cpp

```
#### Linux - ARM 64bit

CMake arguments:
```
cmake -DCMAKE_BUILD_TYPE=Release 
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/RpiToolchain-linux-64.cmake 
      -G "CodeBlocks - Unix Makefiles" 
       <project root dir>/native/modules/graphics/src/main/cpp
```

#### Windows - 32bit
CMake arguments:
```
-DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/MingWToolchain-32.cmake
```

#### Windows - 64bit
CMake arguments:

```
-DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/MingWToolchain-64.cmake
```

#### Mac OSX - 64bit
```
cmake -DCMAKE_BUILD_TYPE=Release 
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/OSXToolchain-32.cmake -DOSXCROSS_HOST=x86_64-apple-darwin15 
      -DOSXCROSS_TARGET_DIR=<project root dir>/native/tools/osxcross 
      -DOSXCROSS_SDK=<project root dir>/native/tools/osxcross/SDK/MacOSX10.11.sdk 
      -DOSXCROSS_TARGET=darwin15 
      -DCMAKE_SHARED_LINKER_FLAGS=-v 
      -G "CodeBlocks - Unix Makefiles" <project root dir>/native/modules/graphics/src/main/cpp
```

## Configuring Development Environment


**CLion IDE**

- Environment Settings (File -> Settings)
	- Coming soon 


**Eclipse CDT (Coming soon)**

- Environment Settings
	- Coming soon 

### Building your own cross-compiler toolchain (using crosstools-ng)

- Coming soon

# References
- [Ubuntu Kernel headers/images (all platforms)](https://kernel.ubuntu.com/~kernel-ppa/mainline/)
- [Linaro Toolchain Downloads](https://www.linaro.org/downloads/)
- [Crosstool-NG Documentation](https://crosstool-ng.github.io/docs/)
- [Installing RPI Packages from Ubuntu with chroot](https://raspberrypi.stackexchange.com/questions/23675/install-raspbian-packages-directly-from-ubuntu-with-chroot-to-raspbian-file-syst)
- [Debian "buster" for Raspberry Pi 3 on QEMU](https://github.com/wimvanderbauwhede/limited-systems/wiki/Debian-%22buster%22-for-Raspberry-Pi-3-on-QEMU)
- [MingW-w64](http://mingw-w64.org)
- [Mac OS X cross toolchain for Linux and FreeBSD (OSXCROSS)](https://github.com/tpoechtrager/osxcross)
- [Prebuilt Raspberry Pi Toolchain (Linaro 32bit/64bit)](https://github.com/ribasco/rpi-tools)