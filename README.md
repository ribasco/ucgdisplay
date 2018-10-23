Universal Character and Graphics Display Library for Java
==========================================================

[![Build Status](https://travis-ci.org/ribasco/ucgdisplay.svg?branch=master)](https://travis-ci.org/ribasco/ucgdisplay) [![Maven Central](https://img.shields.io/maven-central/v/com.ibasco.ucgdisplay/ucg-display.svg?label=Maven%20Central)](https://search.maven.org/search?q=com.ibasco.ucgdisplay) [![Join the chat at https://gitter.im/ucgdisplay/Lobby](https://badges.gitter.im/ucgdisplay/Lobby.svg)](https://gitter.im/ucgdisplay/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4209cfcd33eb4f98a6e1d16414804d45)](https://app.codacy.com/app/ribasco/ucgdisplay?utm_source=github.com&utm_medium=referral&utm_content=ribasco/ucgdisplay&utm_campaign=Badge_Grade_Dashboard)

### Features

---

##### General features

* Supports both Character and Graphics/Dot-Matrix display devices

##### Display drivers

######  Character LCD driver features
* Pure java implementation for Hitachi HD44780 driver powered by Pi4j (Will probably add JNI/native support in the future if performance is an issue)
* Designed with flexibility in mind allowing for different configuration styles of interfacing with your SBC device (e.g. GPIO expanders/I2C/SPI)
* No fixed/mandatory pin mapping. You have the freedom to choose whatever device pins you want to use for your LCD device.
* Available LCD adapters
    * MCP23017
    * Shift Register (e.g. 74HC595)
    * GPIO
    * MCP23008 (Coming soon)
    * PCF8574 (Coming soon)
###### Graphic LCD driver features
* Over 46+ controllers are supported. Refer to the table below for the list of supported display controllers.
* The graphics display module wraps around the popular c/c++ [U8g2](https://github.com/olikraus/u8g2) library by Oliver. All drawing operations present in the library should be similar to the ones found in U8g2 (Refer to the official [U8g2 reference manual](https://github.com/olikraus/u8g2/wiki/u8g2reference) for more information). 

#####  Event-driven UI framework (Coming soon) 

### Supported display controllers

---
###### Character LCD

* Hitachi HD44780
    
###### Graphic/Dot-Matrix LCD

* A2PRINTER, HX1230, IL3820, IST3020, KS0108, LC7981, LD7032, LS013B7DH03, LS027B7DH01, MAX7219, NT7534, PCD8544, PCF8812, RA8835, SBN1661, SED1330, SED1520, SH1106, SH1107, SH1108, SH1122, SSD1305, SSD1306, SSD1309, SSD1317, SSD1322, SSD1325, SSD1326, SSD1327, SSD1329, SSD1606, SSD1607, ST75256, ST7565, ST7567, ST7586S, ST7588, ST7920, T6963, UC1601, UC1604, UC1608, UC1610, UC1611, UC1638, UC1701

### Project Resources

---
* [Snapshot Builds](https://oss.sonatype.org/content/repositories/snapshots/com/ibasco/ucgdisplay/)
* [Release Builds](https://oss.sonatype.org/content/repositories/releases/com/ibasco/ucgdisplay)

### Pre-requisites

---
* Java JDK 1.8 or higher
* [Wiring Pi](http://wiringpi.com/) library (Only required for ARM based devices)
 
### Installation

---


1. From maven repository

    ```xml
    <dependencies>
        <!-- Character display driver (HD44780) -->
         <dependency>
             <groupId>com.ibasco.ucgdisplay</groupId>
             <artifactId>ucgd-drivers-clcd</artifactId>
             <version>0.2-alpha</version>
         </dependency>
      
         <!-- Graphics display driver -->
         <dependency>
             <groupId>com.ibasco.ucgdisplay</groupId>
             <artifactId>ucgd-drivers-glcd</artifactId>
             <version>0.2-alpha</version>
         </dependency>
    </dependencies>
    ```
    
2. From source

    Clone from github

    ```bash
    git clone https://github.com/ribasco/ucgdisplay.git
    ```        

    Switch to the project directory

    ```bash
    cd ucgdisplay
    ```

    Install to your local maven repository

    ```bash
    mvn install
    ```

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
            charDriver.write("Hello World".getBytes());
        } catch (Exception e) {
            log.error("Error occured", e);
        }
    }
}

```

######  Graphic LCD Example (ST7920)

Simple hello world example for ST7920 controller

```java
package com.ibasco.ucgdisplay.examples;

import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;

public class GlcdST7920Example {
    public static void main(String[] args) {
        GlcdConfig config = GlcdConfigBuilder.create()
                .rotation(GlcdRotation.ROTATION_NONE)
                .busInterface(GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                .display(Glcd.ST7920.D_128x64)
                .pinMap(new GlcdPinMapConfig()
                        .map(GlcdPin.SPI_CLOCK, 0)
                        .map(GlcdPin.SPI_MOSI, 0)
                        .map(GlcdPin.CS, 0)
                )
                .build();

        GlcdDriver driver = new GlcdDriver(config);


        driver.setCursor(0, 10);
        driver.setFont(GlcdFont.FONT_6X12_MR);
        driver.clearBuffer();
        driver.drawString("Hello World");
        driver.sendBuffer();
    }
}

```

### Limitations

---
* Only works for Raspberry Pi at the moment. I will add support for other SBCs as soon as I am able to obtain them.  
* The adapters for character lcd driver Mcp23008LcdAdapter and Pcf8574TLcdAdapter are not yet implemented. 

### Contribution guidelines

---
* Build instructions (Coming soon)

### Related projects

---
* GLCD Emulator/Simulator (Coming soon)
