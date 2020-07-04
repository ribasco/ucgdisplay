>  **WARNING:** This project is currently on ALPHA stage. Features may be added or removed at any point in time. Backwards compatibility between development versions are not guaranteed.

### Universal Character/Graphics LCD Library for Java

[![Github Workflow Build Status](https://github.com/ribasco/ucgdisplay/workflows/CI%20/%20Testing%20/%20Deployment/badge.svg)](https://github.com/ribasco/ucgdisplay/actions?query=branch%3Amaster) [![Travis Build Status](https://travis-ci.org/ribasco/ucgdisplay.svg?branch=master)](https://travis-ci.org/ribasco/ucgdisplay) [![Maven Central](https://img.shields.io/maven-central/v/com.ibasco.ucgdisplay/ucg-display.svg?label=Maven%20Central)](https://search.maven.org/search?q=com.ibasco.ucgdisplay) [![Join the chat at https://gitter.im/ucgdisplay/Lobby](https://badges.gitter.im/ucgdisplay/Lobby.svg)](https://gitter.im/ucgdisplay/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4209cfcd33eb4f98a6e1d16414804d45)](https://app.codacy.com/app/ribasco/ucgdisplay?utm_source=github.com&utm_medium=referral&utm_content=ribasco/ucgdisplay&utm_campaign=Badge_Grade_Dashboard) [![Javadocs](https://www.javadoc.io/badge/com.ibasco.ucgdisplay/ucg-display.svg)](https://www.javadoc.io/doc/com.ibasco.ucgdisplay/ucg-display)

### Change Log
**2.0.0-alpha (07/04/2020)**
- Added GlcdInterfaceLookup utility class to provide additional information about a communication protocol 
- New meta data information for fonts
- Renamed GlcdBufferType to GlcdBufferLayout
- Renamed GlcdBusInterface to GlcdCommProtocol
- Renamed GlcdBusType to GlcdCommType
- Renamed GlcdControllerType to GlcdController

**1.5.2-alpha (11/23/2019)** 

- Fixed missing dependencies in the aggregate jar

**1.5.1-alpha (11/22/2019)**

- Fixed 'symbol lookup error' (Issue: [#22](https://github.com/ribasco/ucgdisplay/issues/22))
- Added support for multiple I/O peripheral providers ([libgpiod](https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git/), [pigpiod](https://github.com/joan2937/pigpio), [c-periphery](https://github.com/vsergeev/c-periphery)) (Issue [#24](https://github.com/ribasco/ucgdisplay/issues/24))
- 64 bit architectures are now fully supported for ARM and x86. Note that 32 bit support for OSX platform may be removed in the future versions.
- Added native logging capability for improved traceability (all native logging is routed to SLF4J)
- On Raspberry Pi platforms, GPIO pins are now automatically configured internally by the native library.
- API changes on [GlcdConfigBuilder](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/GlcdConfigBuilder.java). 

### Features

---

##### General features

* Supports both Character and Graphics/Dot-Matrix display devices
* Support for multiple I/O peripheral providers
  * [Libgpiod](https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git/)
  * [Pigpio](https://github.com/joan2937/pigpio) (Daemon or Standalone)
  * [C-Periphery](https://github.com/vsergeev/c-periphery) (Based on the linux kernel)
* Provides native bindings via JNI to the popular U8g2 library for graphics displays.
* Introspection capability. Allows you to examine the low-level communication data flow of the display device.

##### Display drivers

######  Character LCD driver features (Work in Progress)
* Pure java implementation for Hitachi HD44780 driver powered by Pi4j
* Flexible configuration options for interfacing with your SoC device (e.g. GPIO expanders/I2C/SPI)
* Flexible pin mapping configuration
* Supported LCD adapters
    * `MCP23017`
    * Shift Register (e.g. `74HC595`)
    * GPIO
    * `MCP23008`(Coming soon)
    * `PCF8574` (Coming soon)
###### Graphic LCD driver features (Work in Progress)
* SPI and I2C hardware capability and other software bit-banging implementations
* Over 46+ controllers are supported. Refer to the table below for the list of supported display controllers. (note: not everything has been tested yet)
* The graphics display module wraps around the popular c/c++ [U8g2](https://github.com/olikraus/u8g2) library by Oliver. All drawing operations present in the library should be similar to the ones found in U8g2 (Refer to the official [U8g2 reference manual](https://github.com/olikraus/u8g2/wiki/u8g2reference) for more information). 

### Supported display controllers

---
###### Character LCD

* Hitachi HD44780
  
###### Graphic/Dot-Matrix LCD

* A2PRINTER, HX1230, IL3820, IST3020, KS0108, LC7981, LD7032, LS013B7DH03, LS027B7DH01, MAX7219, NT7534, PCD8544, PCF8812, RA8835, SBN1661, SED1330, SED1520, SH1106, SH1107, SH1108, SH1122, SSD1305, SSD1306, SSD1309, SSD1317, SSD1322, SSD1325, SSD1326, SSD1327, SSD1329, SSD1606, SSD1607, ST75256, ST7565, ST7567, ST7586S, ST7588, ST7920, T6963, UC1601, UC1604, UC1608, UC1610, UC1611, UC1638, UC1701

### Supported bus interfaces (GLCD)

| Interface     | Type              | Description                                                                       | Hardware | Software |
|---------------|-------------------|-----------------------------------------------------------------------------------|----------|----------|
| SPI           | 4-Wire            | 4-Wire interface                                                                  | YES      | YES      |
| SPI           | 4-Wire (ST7920)   | 4-Wire interface with no DC                                                       | YES      | YES      |
| SPI           | 3-Wire            | 3-Wire interface                                                                  | NO       | YES      |
| I2C           | Two-Wire          | I2C Two-Wire interface                                                            | YES      | YES      |
| Parallel 8080 | Parallel          | Parallel 8080 (Intel)                                                             | NO       | YES      |
| Parallel 6800 | Parallel          | Parallel 6800 (Motorola)                                                          | NO       | YES      |
| Parallel 6800 | Parallel (KS0108) | Special KS0108 protocol. Mostly identical to 6800, but has more chip select lines | NO       | YES      |
| SED1520       | SED1520           | Special SED1520 protocol                                                          | NO       | YES      |


### Project Resources

---
* [Snapshot Builds](https://oss.sonatype.org/content/repositories/snapshots/com/ibasco/ucgdisplay/) - Builds from the latest development branch
* [Release Builds](https://oss.sonatype.org/content/repositories/releases/com/ibasco/ucgdisplay) - All release builds

### Pre-requisites

---
* Java JDK 1.8+ (See [AdoptOpenJDK](https://adoptopenjdk.net/))
* (Optional) [Libgpiod](https://github.com/brgl/libgpiod) (v1.4.1 or higher) - C library and tools for interacting with the linux GPIO character device (gpiod stands for GPIO device).
* (Optional) [Pigpio](https://github.com/joan2937/pigpio) (v71 above) - pigpio is a C library for the Raspberry which allows control of the General Purpose Input Outputs (GPIO). 

> **IMPORTANT:** For libgpiod, make sure the c++ bindings are included when you install from source (--enable-bindings-cxx)

### Downloading Snapshots from Sonatype

---

To be able to download snapshots from Sonatype, add the following profile entry to your `.m2/settings.xml` file and activate with  `mvn -P sonatype install <....>`.

```xml
<profile>
    <id>sonatype</id>
    <repositories>
        <repository>
            <id>ossrh</id>
            <name>Sonatype OSS Maven Repository</name>
            <url>https://oss.sonatype.org/content/groups/public</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
                <checksumPolicy>fail</checksumPolicy>
            </snapshots>
        </repository>
    </repositories>
</profile>
```

### Installation

---


1. From maven repository - All in one package

    ```xml
    <dependencies>
        <!-- Ucgdisplay Uber Jar -->
         <dependency>
             <groupId>com.ibasco.ucgdisplay</groupId>
             <artifactId>ucg-display</artifactId>
             <version>1.5.2-alpha</version>
         </dependency>
    </dependencies>
    ```
    
    
    
2. From maven repository - Specific module

    ```xml
    <dependencies>
        <!-- Character display driver (HD44780) -->
         <dependency>
             <groupId>com.ibasco.ucgdisplay</groupId>
             <artifactId>ucgd-drivers-clcd</artifactId>
             <version>1.5.2-alpha</version>
         </dependency>
      
         <!-- Graphics display driver -->
         <dependency>
             <groupId>com.ibasco.ucgdisplay</groupId>
             <artifactId>ucgd-drivers-glcd</artifactId>
             <version>1.5.2-alpha</version>
         </dependency>
    </dependencies>
    ```

3. From source

    Clone from Github (Sub-modules included)

    ```bash
    git clone --recurse-submodules -j4 https://github.com/ribasco/ucgdisplay.git
    ```

    Switch to the project directory

    ```bash
    cd ucgdisplay
    ```

    Install to your local maven repository

    * Installing from a Linux system with ARM 32/64 bit architecture
      
        ```bash
        mvn install -Dcompile.native=true
        ```

    * Installing from a 64-bit `Linux` system for `ARM` architecture 32 and 64 bit versions

        ```bash
        mvn install -Dcompile.native=true -Dgraphics.target=native-build-cc-arm-all -Dinput.target=native-build-cc-arm-all -Dbuild.type=Debug
        ```
      
    * Installing from a 64-bit `Linux` system for all supported architectures

        ```bash
        mvn install -Dcompile.native=true -Dgraphics.target=native-build-cc-all -Dinput.target=native-build-cc-all -Dbuild.type=Debug
        ```

    > **Note:** Please see the [cross-compilation guide](https://github.com/ribasco/ucgdisplay/tree/master/native) for more details

### Usage examples

---
Simple hello world examples

######  Character LCD Example (HD44780)

Simple hello world example demonstrating the use of different LCD adapters.

```java
package com.ibasco.ucgdisplay.examples;

import com.ibasco.ucgdisplay.drivers.clcd.HD44780DisplayDriver;
import com.ibasco.ucgdisplay.drivers.clcd.LcdGpioAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.LcdPinMapConfig;
import com.ibasco.ucgdisplay.drivers.clcd.LcdTemplates;
import com.ibasco.ucgdisplay.drivers.clcd.adapters.GpioLcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.adapters.Mcp23017LcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.adapters.ShiftRegisterLcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdPin;
import com.ibasco.ucgdisplay.drivers.clcd.providers.MCP23017GpioProviderExt;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HD44780Example {

    private static final Logger log = LoggerFactory.getLogger(HD44780Example.class);

    public static void main(String[] args) {
        try {
            LcdPinMapConfig config = new LcdPinMapConfig()
                    .map(LcdPin.RS, RaspiPin.GPIO_02)
                    .map(LcdPin.EN, RaspiPin.GPIO_03)
                    .map(LcdPin.DATA_4, RaspiPin.GPIO_04)
                    .map(LcdPin.DATA_5, RaspiPin.GPIO_05)
                    .map(LcdPin.DATA_6, RaspiPin.GPIO_06)
                    .map(LcdPin.DATA_7, RaspiPin.GPIO_07);
            
            //GPIO adapter
            LcdGpioAdapter adapter = new GpioLcdAdapter(config);

            //Shift register adapter
            //LcdGpioAdapter adapter = new ShiftRegisterLcdAdapter(GpioFactory.getDefaultProvider(), RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04, config);

            //MCP23017 I2C adapter (Using built-in templates)
            //MCP23017GpioProviderExt mcp23017GpioProvider = new MCP23017GpioProviderExt(I2CBus.BUS_1, 0x15);
            //LcdGpioAdapter adapter = new Mcp23017LcdAdapter(mcp23017GpioProvider, LcdTemplates.ADAFRUIT_I2C_RGBLCD_MCP23017);
            
            HD44780DisplayDriver charDriver = new HD44780DisplayDriver(adapter, 20, 4);
            charDriver.home();
            charDriver.write("Hello World");
        } catch (Exception e) {
            log.error("Error occured", e);
        }
    }
}

```

######  Graphic LCD Example (ST7920 SPI Hardware)

Example for`ST7920` display controller using RPi's SPI Hardware capability ([Other Examples](https://github.com/ribasco/ucgdisplay/blob/master/examples/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/examples/glcd/GlcdST7920HWExample.java)).

>  **Note:** For I2C/SPI hardware capability, there is no need to map the pins explicitly. 


```java
import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * ST7920 Example - Hardware SPI
 */
public class GlcdST7920HWExample {

    private static final Logger log = LoggerFactory.getLogger(GlcdST7920HWExample.class);

    public static void main(String[] args) throws Exception {
        new GlcdST7920HWExample().run();
    }

    private void run() throws Exception {
        //SPI HW 4-Wire config for ST7920

        //NOTE: On Raspberry Pi, pins can be automatically configured for hardware 		   capability (setting pin modes to ALT*).
        //Automatic pin configuration for special modes will only take place if PIGPIO is installed

        //Pinout for Main SPI Peripheral 
        // - MOSI = 10
        // - SCLK = 11
        // - CE1 = 7
        GlcdConfig config = GlcdConfigBuilder
                //Use ST7920 - 128 x 64 display, SPI 4-wire Hardware
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                //Set to 180 rotation
                .option(GlcdOption.ROTATION, GlcdRotation.ROTATION_180)
                //Using system/c-periphery provider
                .option(GlcdOption.PROVIDER, Provider.SYSTEM)
                //Set to 1,000,000 Hz/bps (1.00 MHz)
                .option(GlcdOption.BUS_SPEED, 1000000)
                //The SPI Bus (RPI as two SPI buses available, the Main and Auxillary)
                .option(GlcdOption.SPI_BUS, SpiBus.MAIN)
                //Use CE1 or Chip Select 1 on Main SPI peripheral/bus
                .option(GlcdOption.SPI_CHANNEL, SpiChannel.CHANNEL_1)
                .build();

        GlcdDriver driver = new GlcdDriver(config);

        //Clear the GLCD buffer
        driver.clearBuffer();

        //Write Operations to the GLCD buffer
        driver.setFont(GlcdFont.FONT_6X12_MR);
        driver.drawString(10, 10, "Hello World");
            
        //Send all buffered data to the display
        driver.sendBuffer();

        //Clear the display
        driver.clearDisplay();
    }
}

```

###### Graphic LCD Example (ST7920 SPI Software)

Here is an alternative version of the above example using bit-bang method.  ([Other Examples](https://github.com/ribasco/ucgdisplay/blob/master/examples/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/examples/glcd/GlcdST7920SWExample.java))

>  **Note:** This is slower than SPI Hardware

```java
import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * ST7920 Example - SPI Software Bit-Bang.
 *
 * Note: This is ALOT slower than SPI HW
 */
public class GlcdST7920SWExample {

    private static final Logger log = LoggerFactory.getLogger(GlcdST7920SWExample.class);

    public static void main(String[] args) throws Exception {
        new GlcdST7920SWExample().run();
    }

    private void run() throws Exception {
        //SPI Software config for ST7920

        //NOTE: For software/bit-bang implementation, pin modes will be automatically configured to OUTPUT by the native module

        //Pinout for Main SPI Peripheral 
        // - MOSI = 10
        // - SCLK = 11
        // - CE1 = 7
        GlcdConfig config = GlcdConfigBuilder
                //Use ST7920 - 128 x 64 display, SPI Software implementation
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_SW_4WIRE_ST7920)
                //Set to 180 rotation
                .option(GlcdOption.ROTATION, GlcdRotation.ROTATION_180)
                //Using system/c-periphery provider
                .option(GlcdOption.PROVIDER, Provider.SYSTEM)
                //Map GlcdPin to BCM pin number
                .mapPin(GlcdPin.SPI_MOSI, 10)
                .mapPin(GlcdPin.SPI_CLOCK, 11)
                .mapPin(GlcdPin.CS, 7)
                .build();

        GlcdDriver driver = new GlcdDriver(config);

        //Clear the GLCD buffer
        driver.clearBuffer();

        //Write Operations to the GLCD buffer
        driver.setFont(GlcdFont.FONT_6X12_MR);
        driver.drawString(10, 10, "Hello World");
            
        //Send all buffered data to the display
        driver.sendBuffer();
    }
}
```

###### Configuration options (All available options in GlcdOption enum)

| Option Name        | Raw Type | **Enum**                                                     | **Description**                                              | **Category** |
| ------------------ | -------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------ |
| PROVIDER           | String   | [Provider](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/Provider.java) | The default provider to use for communicating with the display. Available providers are `libgpiod`, `pigpio`, `pigpiod` and `cperiphery`. | General      |
| BUS_SPEED          | Integer  | N/A                                                          | The SPI/I2C bus speed in Hz                                  | SPI/I2C      |
| GPIO_CHIP          | Integer  | [GpioChip](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/GpioChip.java) | GPIO chip number. Will be translated to the actual GPIO device path. | GPIO         |
| SPI_CHANNEL        | Integer  | [SpiChannel](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/SpiChannel.java) | The SPI channel (Chip-Select). In raspberry pi, we have `CE0 `and `CE1` for Main SPI and `CE0`, `CE1`, `CE2` for Auxiliary. | SPI          |
| SPI_BUS            | Integer  | [SpiBus](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/SpiBus.java) | The primary bus to be used for SPI communication. In raspberry pi, there are two SPI peripherals available, the main and the auxillary. | SPI          |
| SPI_MODE           | Integer  | [SpiMode](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/SpiMode.java) | SPI mode. Mode 0, 1, 2 or 3. Check source documentation for more info. (Default: 0) | SPI          |
| SPI_BIT_ORDER      | Integer  | [SpiBitOrder](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/SpiBitOrder.java) | SPI Bit Order. Can be either 0 = `MSB First` or 1 = `LSB First`. (Default: 0) | SPI          |
| SPI_BITS_PER_WORD  | Integer  | N/A                                                          | SPI Bits per word. (Default: 8)                              | SPI          |
| SPI_FLAGS          | Integer  | N/A                                                          | Provider specific SPI flags. Only use this if you know what you are doing. Read more information on the provider's website (Default: 0). | SPI          |
| I2C_DEVICE_ADDRESS | Integer  | N/A                                                          | The address of the display device on the I2C bus.            | I2C          |
| I2C_BUS            | Integer  | [I2CBus](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/I2CBus.java) | The primary bus to be used for I2C communication.            | I2C          |
| I2C_FLAGS          | Integer  | N/A                                                          | Provider specific I2C flags.                                 | I2C          |
| ROTATION           | Integer  | [GlcdRotation](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/enums/GlcdRotation.java) | The default rotation to set for the display.                 | General      |
| PIGPIO_ADDRESS     | String   | N/A                                                          | The IP address of the pigpio daemon. (Default: 127.0.0.1)    | General      |
| PIGPIO_PORT        | Integer  | N/A                                                          | The port number of the pigpio daemon. (Default: 8888)        | General      |
| EXTRA_DEBUG_INFO   | Boolean  | N/A                                                          | Enable/Disable extra debug information to be displayed on the console (Default: false) | General      |

### Limitations

---
* Works only on linux kernel v4.8 or higher
* The adapters for character lcd driver `Mcp23008LcdAdapter` and `Pcf8574TLcdAdapter` are not yet implemented. 
* Due to lack of hardware availability, I am currently unable to test the `SPI`, `I2C` and bit-bang interfaces for most of the display controllers. The code implementation for these interfaces are complete, so In theory, they should work but it not confirmed. I am going to rely on your feedback for this and I will do my best to support any issues you may encounter.

### Contribution

---
Contributions are always welcome. For starters, checkout the guide to setting up your Dev/Build environment for more information.

* [Cross-compilation guide](https://github.com/ribasco/ucgdisplay/blob/master/native/README.md) 

### Planned Features

- For the Character LCD library, I plan to move away from Pi4J (v1.2 still using Wiring Pi which is now deprecated) and use the native layer instead for device communication. This will allow us to use different peripheral providers of our choice. 

### Credits

- [olikraus](https://github.com/olikraus) - Project maintainer of [u8g2](https://github.com/olikraus/u8g2)
- [vsergeev](https://github.com/vsergeev) - Project maintainer of [c-periphery](https://github.com/vsergeev/c-periphery)
- [joan2937](https://github.com/joan2937) - Project maintainer of [pigpio](https://github.com/joan2937/pigpio)
- [brgl](https://github.com/brgl) - Project maintainer of [libgpiod](https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git/)

### Related projects

---
* [GLCD Simulator Project](https://github.com/ribasco/glcd-emulator)
