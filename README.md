>  **WARNING:** This project is currently on ALPHA stage. Features may be added or removed at any point in time. Backwards compatibility between development versions are not guaranteed.

### Universal Character/Graphics LCD Library for Java

[![Build Status](https://travis-ci.org/ribasco/ucgdisplay.svg?branch=master)](https://travis-ci.org/ribasco/ucgdisplay) [![Maven Central](https://img.shields.io/maven-central/v/com.ibasco.ucgdisplay/ucg-display.svg?label=Maven%20Central)](https://search.maven.org/search?q=com.ibasco.ucgdisplay) [![Join the chat at https://gitter.im/ucgdisplay/Lobby](https://badges.gitter.im/ucgdisplay/Lobby.svg)](https://gitter.im/ucgdisplay/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4209cfcd33eb4f98a6e1d16414804d45)](https://app.codacy.com/app/ribasco/ucgdisplay?utm_source=github.com&utm_medium=referral&utm_content=ribasco/ucgdisplay&utm_campaign=Badge_Grade_Dashboard) [![Javadocs](https://www.javadoc.io/badge/com.ibasco.ucgdisplay/ucg-display.svg)](https://www.javadoc.io/doc/com.ibasco.ucgdisplay/ucg-display)

### Change Log

**1.5.0-alpha**

- Migrated to JDK 11
- Fixed 'symbol lookup error' (Issue: #22)
- Added support for multiple I/O peripheral providers ([libgpiod](https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git/), [pigpiod](https://github.com/joan2937/pigpio), [c-periphery](https://github.com/vsergeev/c-periphery)) (Issue #24)
- 32 bit and 64 bit architectures are now fully supported for ARM and x86. Note that 32 bit support for OSX platform may be removed in the future versions.
- Added native logging capability for improved traceability (native routes to SLF4J)
- On Raspberry Pi platforms, GPIO pins are now automatically configured internally by the native library.
- Added more options for different I/O peripheral providers
- Introduced API changes for [GlcdConfigBuilder](https://github.com/ribasco/ucgdisplay/blob/master/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/drivers/glcd/GlcdConfigBuilder.java). 

### Features

---

##### General features

* Easy to use API
* Supports both Character and Graphics/Dot-Matrix display devices
* Support for multiple I/O peripheral providers
  * [Libgpiod](https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git/)
  * [Pigpio](https://github.com/joan2937/pigpio) (Daemon or Standalone)
  * [C-Periphery](https://github.com/vsergeev/c-periphery) (Based on the linux kernel)
* JDK 11 Compatible (Available for both 32 and 64 bit architectures)
* Provides native bindings via JNI to the popular U8g2 library for graphics displays.
* Introspection capability

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
* [Display Controller Status](https://goo.gl/5GNQmy) - A spreadsheet containing the status of all supported GLCD.

### Pre-requisites

---
* Java JDK 11 (See [AdoptOpenJDK](https://adoptopenjdk.net/))
* (Optional) [Libgpiod](https://github.com/brgl/libgpiod) (v1.4.1 or higher) - C library and tools for interacting with the linux GPIO character device (gpiod stands for GPIO device).
* (Optional) [Pigpio](https://github.com/joan2937/pigpio) (v71 above) - pigpio is a C library for the Raspberry which allows control of the General Purpose Input Outputs (GPIO). 

> **IMPORTANT:** For libgpiod, make sure the c++ bindings are included when you install from source (--enable-bindings-cxx)

### Installation

---


1. From maven repository

    ```xml
    <dependencies>
        <!-- Character display driver (HD44780) -->
         <dependency>
             <groupId>com.ibasco.ucgdisplay</groupId>
             <artifactId>ucgd-drivers-clcd</artifactId>
             <version>1.5.0-alpha</version>
         </dependency>
      
         <!-- Graphics display driver -->
         <dependency>
             <groupId>com.ibasco.ucgdisplay</groupId>
             <artifactId>ucgd-drivers-glcd</artifactId>
             <version>1.5.0-alpha</version>
         </dependency>
    </dependencies>
    ```
    
2. From source

    Clone from Github (Sub-modules included)

    ```bash
    git clone --recurse-submodules -j4 https://github.com/ribasco/ucgdisplay.git
    ```

    Switch to the project directory

    ```bash
    cd ucgdisplay
    ```

    Install to your local maven repository

    ```bash
    mvn install
    ```
    
    > **Note:** To cross compile native library please check out the project [cross-compilation guide](https://github.com/ribasco/ucgdisplay/tree/master/native)

### Usage examples

---
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

######  Graphic LCD Example (ST7920)

Simple hello world example for `ST7920` display controller using RPi's SPI Hardware capability.

>  **Note:** For I2C/SPI hardware capability, there is no need to map the pins. Doing so might result into 'operation is not permitted' errors.  



See [ST7920 Example Code](https://github.com/ribasco/ucgdisplay/blob/master/examples/drivers/glcd/src/main/java/com/ibasco/ucgdisplay/examples/glcd/GlcdST7920Example.java)


```java
package com.ibasco.ucgdisplay.examples;

import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

public class GlcdST7920Example {
    public static void main(String[] args) throws Exception {
        new GlcdST7920Example().run();
    }
    
    private void drawU8G2Logo(int offset, GlcdDriver driver) {
        driver.setFontMode(1);

        driver.setFontDirection(0);
        driver.setFont(GlcdFont.FONT_INB16_MF);
        driver.drawString(offset, 22, "U");

        driver.setFontDirection(1);
        driver.setFont(GlcdFont.FONT_INB19_MN);
        driver.drawString(offset + 14, 8, "8");

        driver.setFontDirection(0);
        driver.setFont(GlcdFont.FONT_INB16_MF);
        driver.drawString(offset + 36, 22, "g");
        driver.drawString(offset + 48, 22, "2");

        driver.drawHLine(offset + 2, 25, 34);
        driver.drawHLine(offset + 3, 26, 34);
        driver.drawVLine(offset + 32, 22, 12);
        driver.drawVLine(offset + 33, 23, 12);
    }
    
    private void run() throws Exception {
        //SPI HW 4-Wire config for ST7920
        
        //NOTE: On Raspberry Pi systems, you do not need 
        //to explicitly map the pins when hardware capability is 
        //activated for SPI, I2C or UART peripherals. 
        //Pins are automatically configured internally for these interfaces.

        //Pinout for Main SPI Peripheral (Raspberry Pi / J8 Header)
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
            	//Set to 800,000 bits per second
                .option(GlcdOption.DEVICE_SPEED, 800000)
            	//Use CE1 or Chip Select 1 on Main SPI peripheral
                .option(GlcdOption.SPI_CHANNEL, SpiChannel.CHANNEL_1)
                .build();       
        
        GlcdDriver driver = new GlcdDriver(config);

        //Set the Font (This is required for drawing strings)
        driver.setFont(GlcdFont.FONT_6X12_MR);

        //Get the maximum character height
        int maxHeight = driver.getMaxCharHeight();

        long startMillis = System.currentTimeMillis();

        log.debug("Starting display loop");

        XBMData xbmData = XBMUtils.decodeXbmFile(getClass().getResourceAsStream("/ironman.xbm"));

        int offset = 50;
        
        for (int i = 1000; i >= 0; i--) {
            //Clear the GLCD buffer
            driver.clearBuffer();

            if (offset >= 128) {
                offset = 0;
            }

            drawU8G2Logo(offset++, driver);

            driver.drawXBM(0, 0, 45, 64, Objects.requireNonNull(xbmData).getData());

            //Write Operations to the GLCD buffer
            driver.setFont(GlcdFont.FONT_6X12_MR);
            driver.drawString(55, maxHeight * 3, "ucgdisplay");
            driver.drawString(55, maxHeight * 4, "1.5.0-alpha");
            driver.drawString(100, maxHeight * 5, String.valueOf(i));

            //Send all buffered data to the display
            driver.sendBuffer();

            Thread.sleep(1);
        }

        //Clear the display
        driver.clearDisplay();
        long endTime = System.currentTimeMillis() - startMillis;

        log.info("Done in {} seconds", Duration.ofMillis(endTime).toSeconds());        
    }
}

```

**Manually Setting pin mode with wiring pi**

This is no longer needed in version 1.5.x  or above.

```bash
gpio mode 12 alt0 && gpio mode 13 alt0 && gpio mode 14 alt0 && gpio mode 10 alt0 && gpio readall
```

**Verify**

use `gpio readall` to verify that we have set the correct mode (`ALT0`) for `MOSI`, `SCLK` and `CE1` (Chip-Select)

```bash
 +-----+-----+---------+------+---+---Pi 2---+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 |     |     |    3.3v |      |   |  1 || 2  |   |      | 5v      |     |     |
 |   2 |   8 |   SDA.1 | ALT0 | 1 |  3 || 4  |   |      | 5v      |     |     |
 |   3 |   9 |   SCL.1 | ALT0 | 1 |  5 || 6  |   |      | 0v      |     |     |
 |   4 |   7 | GPIO. 7 |   IN | 1 |  7 || 8  | 1 | ALT0 | TxD     | 15  | 14  |
 |     |     |      0v |      |   |  9 || 10 | 1 | ALT0 | RxD     | 16  | 15  |
 |  17 |   0 | GPIO. 0 |   IN | 0 | 11 || 12 | 0 | IN   | GPIO. 1 | 1   | 18  |
 |  27 |   2 | GPIO. 2 |   IN | 0 | 13 || 14 |   |      | 0v      |     |     |
 |  22 |   3 | GPIO. 3 |   IN | 0 | 15 || 16 | 0 | IN   | GPIO. 4 | 4   | 23  |
 |     |     |    3.3v |      |   | 17 || 18 | 1 | IN   | GPIO. 5 | 5   | 24  |
 |  10 |  12 |    MOSI | ALT0 | 0 | 19 || 20 |   |      | 0v      |     |     |
 |   9 |  13 |    MISO | ALT0 | 0 | 21 || 22 | 1 | OUT  | GPIO. 6 | 6   | 25  |
 |  11 |  14 |    SCLK | ALT0 | 0 | 23 || 24 | 1 | OUT  | CE0     | 10  | 8   |
 |     |     |      0v |      |   | 25 || 26 | 1 | OUT  | CE1     | 11  | 7   |
 |   0 |  30 |   SDA.0 |   IN | 1 | 27 || 28 | 1 | IN   | SCL.0   | 31  | 1   |
 |   5 |  21 | GPIO.21 |   IN | 1 | 29 || 30 |   |      | 0v      |     |     |
 |   6 |  22 | GPIO.22 |   IN | 1 | 31 || 32 | 0 | IN   | GPIO.26 | 26  | 12  |
 |  13 |  23 | GPIO.23 |   IN | 0 | 33 || 34 |   |      | 0v      |     |     |
 |  19 |  24 | GPIO.24 |   IN | 0 | 35 || 36 | 0 | IN   | GPIO.27 | 27  | 16  |
 |  26 |  25 | GPIO.25 |   IN | 0 | 37 || 38 | 0 | OUT  | GPIO.28 | 28  | 20  |
 |     |     |      0v |      |   | 39 || 40 | 0 | IN   | GPIO.29 | 29  | 21  |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+---Pi 2---+---+------+---------+-----+-----+
```

### Limitations

---
* Works only on linux kernel v4.8 or higher
* The adapters for character lcd driver `Mcp23008LcdAdapter` and `Pcf8574TLcdAdapter` are not yet implemented. 
* Due to lack of hardware availability, I am currently unable to test the `SPI`, `I2C` and bit-bang interfaces for most of the display controllers. The code implementation for these interfaces are complete, so In theory, they should work but it is not confirmed whether they are working or not. I am going to rely on your feedback for this and I will do my best to support any issues you may encounter.
* As of JDK 11, javadocs won't be available for the meantime due to issues encountered during the build process. Still working into resolving this issue, but for now it remains disabled.

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
