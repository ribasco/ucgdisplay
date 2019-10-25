# Guide to cross-compiling from an x86_64 (64bit) Linux OS

### 1. Prerequisites

Refer to the links under References for more information about these packages

- Debian x86_64 (64bit)  Linux OS. This is the host OS where the compilation will take place.
- GCC v7.4 above
- CMake 3.10+
- CLang (LLVM Based)
- GCC/G++ Multilib
- Autoconf/Automake
- Latest Apache Maven version
- Latest Apache Ant version
- [OSXCROSS Toolchain](https://github.com/tpoechtrager/osxcross) (For cross compiling mac osx binaries from a Linux x86_64 machine). Provided by the project.
- [Raspberry Pi Toolchain](https://github.com/ribasco/rpi-tools)  (For cross compiling arm binaries frpom a Linux x86_64 machine). Provided by the project.
- [MingW-w64 Toolchain](http://mingw-w64.org/doku.php) (For cross compiling windows binaries from a Linux x86_64 machine).

### 2. Package installation

##### Required Linux Packages (Debian)
    - autoconf
    - autoconf-archive
    - automake
    - cmake (v3.10+)
	- gcc-multilib
	- g++-multilib
	- mingw-w64
	- clang
	- build-essential

Install the required packages for cross-compilation 

```
sudo apt update
```

```
sudo apt install autoconf autoconf-archive automake cmake gcc-multilib g++-multilib mingw-w64 clang build-essential
```

### 3. Environment Settings

- Make sure JAVA_HOME is set to your current JDK

### 4. Clone the source from the repository
Pick a working directory for your build and clone the project source including it's required submodules (e.g. rpi toolchain).

```
git clone https://github.com/ribasco/ucgdisplay.git --recurse-submodules --remote-submodules
```

### 5. Building the project 

#### Cross-compiling via Maven (All-in-one)


If you compile with maven, you will compile all java sources including the c++ libraries for all supported platforms. Take note that we added `build-linux-x86_64` as an exclusion. This is needed to avoid building for the same target twice.  

If no profiles are provided, no cross-compilation will take place. Only the libraries for the Host system will be built.

```
mvn clean install -P'cross-compile,!build-linux-x86_64' -DskipTests=true -Dgpg.skip
``` 

#### Cross-compining via Apache ANT

With ant, you can select a specific c++ module to cross-compile. 

> Note: Build files are located under root 'scripts' directory 

Building only the C++ graphics library for all supported platforms

```
cd scripts
ant -file build-graphics.xml -Droot.dir=<project root directory> native-build-cc-all
```

Building only the C++ input library for all supported platforms
```
cd scripts
ant -file build-input.xml -Droot.dir=<project root directory> native-build-cc-all
```

The output files are located at `<proj root>/native/modules/graphics/target/classes/natives/` directory

#### Cross-compiling via CMake

**Properties:**

- **project root dir** = The root project directory (e.g. /home/user/projects/ucgdisplay)

**CMake build process:**

> Note: Set working directory to <project root>/native/modules/graphics/src/main/cpp

1. Refresh/generate the necessary build files
2. Make build process

##### Linux - ARM 32bit

```
cmake --target ucgdisp
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/RpiToolchain-linux-32.cmake
      -DCMAKE_BUILD_TYPE=Release
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/arm_32
```

```
cmake --build <arm32 build dir path>/linux/arm_32 --target ucgdisp -- -j 4
```

##### Linux - ARM 64bit

```
cmake --target ucgdisp
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/RpiToolchain-linux-64.cmake
      -DCMAKE_BUILD_TYPE=Release
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/arm_64
```

```
cmake --build <arm64 build dir path>/linux/arm_64 --target ucgdisp -- -j 4
```

##### Linux - x86_32 (32 bit / Virtual Mode)

```
cmake --target ucgdisp
      -DCMAKE_BUILD_TYPE=Release 
      -DCMAKE_C_FLAGS=-m32 
      -DCMAKE_CXX_FLAGS=-m32 
      -DCMAKE_SHARED_LINKER_FLAGS=-m32
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/x86_32
```

```
cmake --build <x86_32 build dir path>/linux/x86_32 --target ucgdisp -- -j 4
```

##### Linux - x86_64 (64 bit / Virtual Mode)

```
cmake --target ucgdisp
      -DCMAKE_BUILD_TYPE=Release
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/x86_64
```

```
cmake --build <x86_64 build dir path>/linux/x86_64 --target ucgdisp -- -j 4
```

##### Windows - 32bit (Virtual Mode)

```
cmake --target ucgdisp 
       -DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/MingWToolchain-32.cmake
       -DCMAKE_BUILD_TYPE=Release
       -G 'CodeBlocks - Unix Makefiles'
       -H.
       -Bbuild/windows/x86_32
```

```
cmake --build <win32 build dir path>/windows/x86_32 --target ucgdisp -- -j 4
```

##### Windows - 64bit (Virtual Mode)

```
cmake --target ucgdisp 
       -DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/MingWToolchain-64.cmake
       -DCMAKE_BUILD_TYPE=Release
       -G 'CodeBlocks - Unix Makefiles'
       -H.
       -Bbuild/windows/x86_64
```

```
cmake --build <win32 build dir path>/windows/x86_64 --target ucgdisp -- -j 4
```

##### Mac OSX - 32bit (Virtual Mode)
```
cmake --target ucgdisp
      -DCMAKE_BUILD_TYPE=Release 
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/OSXToolchain-32.cmake 
      -DOSXCROSS_HOST=x86_64-apple-darwin15 
      -DOSXCROSS_TARGET_DIR=<project root dir>/native/tools/osxcross 
      -DOSXCROSS_SDK=<project root dir>/native/tools/osxcross/SDK/MacOSX10.11.sdk 
      -DOSXCROSS_TARGET=darwin15 
      -DCMAKE_SHARED_LINKER_FLAGS=-v 
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/osx/x86_32
```

```
cmake --build <osx32 build dir path>/osx/x86_32 --target ucgdisp -- -j 4
```

##### Mac OSX - 64bit (Virtual Mode)
```
cmake --target ucgdisp
      -DCMAKE_BUILD_TYPE=Release 
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/OSXToolchain-64.cmake 
      -DOSXCROSS_HOST=x86_64-apple-darwin15 
      -DOSXCROSS_TARGET_DIR=<project root dir>/native/tools/osxcross 
      -DOSXCROSS_SDK=<project root dir>/native/tools/osxcross/SDK/MacOSX10.11.sdk 
      -DOSXCROSS_TARGET=darwin15 
      -DCMAKE_SHARED_LINKER_FLAGS=-v 
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/osx/x86_64
```

```
cmake --build <osx64 build dir path>/osx/x86_64 --target ucgdisp -- -j 4
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

# Credits

- olikarus - Project maintainer of [u8g2](https://github.com/olikraus/u8g2)
- vsergeev - Project maintainer of [c-periphery](https://github.com/vsergeev/c-periphery)
- joan2937 - Project maintainer of [pigpio](https://github.com/joan2937/pigpio)

# References
- [Ubuntu Kernel headers/images (all platforms)](https://kernel.ubuntu.com/~kernel-ppa/mainline/)
- [Linaro Toolchain Downloads](https://www.linaro.org/downloads/)
- [Crosstool-NG Documentation](https://crosstool-ng.github.io/docs/)
- [Installing RPI Packages from Ubuntu with chroot](https://raspberrypi.stackexchange.com/questions/23675/install-raspbian-packages-directly-from-ubuntu-with-chroot-to-raspbian-file-syst)
- [Debian "buster" for Raspberry Pi 3 on QEMU](https://github.com/wimvanderbauwhede/limited-systems/wiki/Debian-%22buster%22-for-Raspberry-Pi-3-on-QEMU)
- [MingW-w64](http://mingw-w64.org)
- [Mac OS X cross toolchain for Linux and FreeBSD (OSXCROSS)](https://github.com/tpoechtrager/osxcross)
- [Prebuilt Raspberry Pi Toolchain (Linaro 32bit/64bit)](https://github.com/ribasco/rpi-tools)