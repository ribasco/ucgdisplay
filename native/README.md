# Guide to cross-compiling the native project

### 1. Prerequisites

Refer to the links under References for more information about these packages

- Debian x86_64 (64bit)  Linux OS. This is the host OS where the compilation will take place. (Ubuntu 18.04 recommended)
- GCC v7.4 above
- CMake 3.10+
- CLang v9 (LLVM Based)
- GCC/G++ Multilib
- Autoconf/Automake
- Latest Apache Maven version
- Latest Apache Ant version
- [OSXCROSS Toolchain](https://github.com/tpoechtrager/osxcross) (For cross compiling mac osx binaries from a Linux x86_64 machine). Provided by the project.
- [Raspberry Pi Toolchain](https://github.com/ribasco/rpi-tools)  (For cross compiling arm binaries frpom a Linux x86_64 machine). Provided by the project.
- [MingW-w64 Toolchain](http://mingw-w64.org/doku.php) (For cross compiling windows binaries from a Linux x86_64 machine).

### 2. Package installation

##### Package sources

<u>Ubuntu Toolchain</u>

```
sudo add-apt-repository ppa:ubuntu-toolchain-r/test
```

<u>LLVM/Clang 9</u>

Add gpg key.

```bash
wget -O - https://apt.llvm.org/llvm-snapshot.gpg.key|sudo apt-key add -
```

Add  sources. Create a file at `/etc/apt/sources.list.d/llvm.list`and paste the line below. 

>  Note: This is for Ubuntu 18.04, if you have a different version please check out [LLVM.org](https://apt.llvm.org/)

```bash
deb http://apt.llvm.org/bionic/ llvm-toolchain-bionic-9 main
```

##### Required Linux Packages (Debian)

    - build-essential
    - autoconf
    - autoconf-archive
    - automake
    - cmake (v3.10+)
    - gcc-multilib
    - g++-multilib
    - mingw-w64
    - llvm-9-dev
    - clang-9
    - libssl1.0-dev
    - libtool
    - python-setuptools
    - python3-setuptools
    - pkg-config

Install the required packages for cross-compilation 

```bash
sudo apt update
```

```bash
sudo apt install build-essential autoconf autoconf-archive automake cmake gcc-multilib g++-multilib mingw-w64 llvm-9-dev clang-9 libssl1.0-dev libtool python-setuptools python3-setuptools pkg-config
```

### 3. Environment Settings

- Make sure `JAVA_HOME` is set to your current JDK

- Export`LD_LIBRARY_PATH="/usr/lib/llvm-9/lib"`

  >  **Note**: This is only needed if you encounter issues cross-compiling for the OSX build targets. See Troubleshooting section below for more details.

### 4. Clone the source from the repository
Pick a working directory for your build and clone the project source using the following commands below

**Clone the latest from master branch (Submodules included)**

```bash
git clone --recurse-submodules -j4 https://github.com/ribasco/ucgdisplay.git
```

**Clone from a specific branch (Submodules included)**

```bash
git clone --recurse-submodules --branch 1.5.0-alpha-dev -j4 https://github.com/ribasco/ucgdisplay.git
```

### 5. Building the project 

#### Cross-compiling with Maven (Java + Native) [RECOMMENDED]

If you compile with maven, you will compile all Java sources including the native libraries of all supported platforms. For the `buildType` parameter, the possible values are `Debug`, `Release`, `RelWithDebInfo` and `MinSizeRel`.

<u>Compile Everything</u>

```bash
mvn clean install -P'cross-compile' -DbuildType=Debug -DskipTests=true -Dgpg.skip
```

<u>Compiling a specific target architecture</u>

>  **Note**: See 'List of available ANT Build Targets' below for the list of targets

```bash
mvn clean compile -P'cross-compile' -DbuildType=<buildType> -Dgraphics-build-target=<ant build target>
```

#### Cross-compiling with Apache ANT (Native Only)

With ant, you can select a specific c++ module to cross-compile. 

> Note: Build files are located under root 'scripts' directory 

Building only the C++ graphics library for all supported platforms

```bash
cd scripts
ant -file build-graphics.xml -Droot.dir=<project root directory> -Dcpp.build.type=Debug native-build-cc-all
```

Building only the C++ input library for all supported platforms
```bash
cd scripts
ant -file build-input.xml -Droot.dir=<project root directory> -Dcpp.build.type=Debug native-build-cc-all
```

Building for a specific platform/architecture

```bash
cd scripts
ant -file build-graphics.xml -Droot.dir=<project root directory> -Dcpp.build.type=Debug native-build-linux-arm32
```

All generated binaries are located at `<proj root>/native/modules/graphics/target/classes/natives/` directory

##### List of available ANT Build Targets

All targets with the **-cc** suffix needs to be executed from a 64 bit Linux operating system.

| Ant Target                     | Module   | Description                                                 |
| ------------------------------ | -------- | ----------------------------------------------------------- |
| native-build-cc-all            | Graphics | Cross-compile everything                                    |
| native-build-cc-linux-arm32    | Graphics | Cross-compile for Linux ARM 32 bit                          |
| native-build-cc-linux-arm64    | Graphics | Cross-compile for Linux ARM 64 bit                          |
| native-build-cc-osx-x86_32     | Graphics | Cross-compile for OSX x86 32 bit                            |
| native-build-cc-osx-x86_64     | Graphics | Cross-compile for OSX x86 64 bit                            |
| native-build-cc-windows-x86_32 | Graphics | Cross-compile for Windows x86 32 bit                        |
| native-build-cc-windows-x86_64 | Graphics | Cross-compile for Windows x86 64 bit                        |
| native-build-linux-arm32       | Graphics | Compile for ARM 32 (Needs an ARM 32/64 bit host)            |
| native-build-linux-x86_32      | Graphics | Compile for Linux 32 bit (Needs a Linux x86 32/64 bit host) |
| native-build-linux-x86_64      | Graphics | Compile for Linux 64 bit (Needs a Linux x86 64 bit host)    |
| native-build-macosx-x86_64     | Graphics | Compile for OSX 64 bit (Needs an OSX 64 bit host)           |
| native-build-windows-x86_32    | Graphics | Compile for Windows 32 bit (Needs a Windows 32/64 bit host) |
| native-build-windows-x86_64    | Graphics | Compile for Windows 64 bit (Needs a Windows 64 bit host)    |

#### Cross-compiling with CMake (Native Only)

CMake is the tool of choice for building the native c++ library.  Minimum required version is 3.10. 

**Properties**

| Property                       | Description                | Example                        |
| ------------------------------ | -------------------------- | ------------------------------ |
| project root dir               | The root project directory | /home/user/projects/ucgdisplay |

**CMake build process:**

> Note: Set working directory to <project root>/native/modules/graphics/src/main/cpp

1. Refresh/generate the necessary build files
2. Start make build process

##### Linux - ARM 32 bit

```bash
cmake --target ucgdisp
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/RpiToolchain-linux-32.cmake
      -DCMAKE_BUILD_TYPE=Release
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/arm_32
```

```bash
cmake --build <arm32 build dir path>/linux/arm_32 --target ucgdisp -- -j 4
```

##### Linux - ARM 64 bit

```bash
cmake --target ucgdisp
      -DCMAKE_TOOLCHAIN_FILE=<project root dir>/native/cmake/RpiToolchain-linux-64.cmake
      -DCMAKE_BUILD_TYPE=Release
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/arm_64
```

```bash
cmake --build <arm64 build dir path>/linux/arm_64 --target ucgdisp -- -j 4
```

##### Linux - x86_32 (32 bit / Virtual Mode)

```bash
cmake --target ucgdisp
      -DCMAKE_BUILD_TYPE=Release 
      -DCMAKE_C_FLAGS=-m32 
      -DCMAKE_CXX_FLAGS=-m32 
      -DCMAKE_SHARED_LINKER_FLAGS=-m32
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/x86_32
```

```bash
cmake --build <x86_32 build dir path>/linux/x86_32 --target ucgdisp -- -j 4
```

##### Linux - x86_64 (64 bit / Virtual Mode)

```bash
cmake --target ucgdisp
      -DCMAKE_BUILD_TYPE=Release
      -G 'CodeBlocks - Unix Makefiles'
      -H.
      -Bbuild/linux/x86_64
```

```bash
cmake --build <x86_64 build dir path>/linux/x86_64 --target ucgdisp -- -j 4
```

##### Windows - x86_32 (32 bit / Virtual Mode)

```bash
cmake --target ucgdisp 
       -DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/MingWToolchain-32.cmake
       -DCMAKE_BUILD_TYPE=Release
       -G 'CodeBlocks - Unix Makefiles'
       -H.
       -Bbuild/windows/x86_32
```

```bash
cmake --build <win32 build dir path>/windows/x86_32 --target ucgdisp -- -j 4
```

##### Windows - x86_64 (64 bit / Virtual Mode)

```bash
cmake --target ucgdisp 
       -DCMAKE_TOOLCHAIN_FILE=<native directory path>/cmake/MingWToolchain-64.cmake
       -DCMAKE_BUILD_TYPE=Release
       -G 'CodeBlocks - Unix Makefiles'
       -H.
       -Bbuild/windows/x86_64
```

```bash
cmake --build <win64 build dir path>/windows/x86_64 --target ucgdisp -- -j 4
```

##### Mac OS X - x86_32 (32 bit / Virtual Mode)
```bash
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

```bash
cmake --build <osx32 build dir path>/osx/x86_32 --target ucgdisp -- -j 4
```

##### Mac OS X x86_64 (64 bit / Virtual Mode)
```bash
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

```bash
cmake --build <osx64 build dir path>/osx/x86_64 --target ucgdisp -- -j 4
```

### Troubleshooting

<u>OSX: libLTO.so not found</u>

If you get an error such as:

```
error while loading shared libraries: libLTO.so.9: cannot open shared object file: No such file or directory
```

1. Make sure `llvm-9-dev` is installed (typically under `/usr/lib/llvm-9`)

2. Make sure the build system will be able to find all the required libraries in `/usr/lib/llvm-9/lib`. If it doesn't, then you need to explicitly export the library path to `LD_LIBRARY_PATH`.

   ```bash
   export LD_LIBRARY_PATH="/usr/lib/llvm-9/lib" 
   ```

<u>OSX: Libxar not found</u>

If you encounter this error, you need to install the `xar` package.

 - Required system dependencies: `libssl1.0-dev`

   ```bash
   git clone https://github.com/mackyle/xar
   cd xar/xar
   ./autogen.sh
   make
   sudo make install
   ```

## Configuring Development **Environment**

**CLion IDE (Coming soon)**

- Environment Settings (File -> Settings)
	- Coming soon 

**Eclipse CDT (Coming soon)**

- Environment Settings
	- Coming soon 

### Building your own cross-compiler tool-chain

- Crosstools-NG
- OSX Cross

# References
- [Ubuntu Kernel headers/images (all platforms)](https://kernel.ubuntu.com/~kernel-ppa/mainline/)
- [Linaro Toolchain Downloads](https://www.linaro.org/downloads/)
- [Crosstool-NG Documentation](https://crosstool-ng.github.io/docs/)
- [Installing RPI Packages from Ubuntu with chroot](https://raspberrypi.stackexchange.com/questions/23675/install-raspbian-packages-directly-from-ubuntu-with-chroot-to-raspbian-file-syst)
- [Debian "buster" for Raspberry Pi 3 on QEMU](https://github.com/wimvanderbauwhede/limited-systems/wiki/Debian-%22buster%22-for-Raspberry-Pi-3-on-QEMU)
- [MingW-w64](http://mingw-w64.org)
- [Mac OS X cross toolchain for Linux and FreeBSD (OSXCROSS)](https://github.com/tpoechtrager/osxcross)
- [Prebuilt Raspberry Pi Toolchain (Linaro 32bit/64bit)](https://github.com/ribasco/rpi-tools)