# Introduction

A universal character/graphics display library for ARM embedded devices based on Java. Provides drivers for character based lcd devices (Hitachi HD44780) and over 40+ graphic monochrome lcd devices (Powered by U8g2). 

# Features
   
* Display drivers
    * Character LCD driver features
        * Pure java implementation for Hitachi HD44780 driver powered by Pi4j (Will probably add JNI/native support if performance is an issue)
        * Designed with flexibility in mind allowing different for configuration setups for interfacing with your ARM device (e.g. Gpio expanders/I2C/SPI/Shift Registers)
        * No mandatory pin mapping configuration! You have the freedom to choose whatever device pins you want to use for your LCD device.
    * Graphic LCD driver features
        * Over 46+ display controllers supported. Refer to the table below for the supported display controllers.
        * Powered by U8g2. Basically, the graphics lcd driver is just a wrapper for the u8g2 library, so the drawing operations found in this library should be equivalent to the ones available in U8g2. 

* Event-driven UI framework (Currently under development) 

# Supported display controllers

* Character LCD

    Hitachi HD44780
* Graphic/Dot-Matrix LCD

    A2PRINTER, HX1230, IL3820, IST3020, KS0108, LC7981, LD7032, LS013B7DH03, LS027B7DH01, MAX7219, NT7534, PCD8544, PCF8812, RA8835, SBN1661, SED1330, SED1520, SH1106, SH1107, SH1108, SH1122, SSD1305, SSD1306, SSD1309, SSD1317, SSD1322, SSD1325, SSD1326, SSD1327, SSD1329, SSD1606, SSD1607, ST75256, ST7565, ST7567, ST7586S, ST7588, ST7920, T6963, UC1601, UC1604, UC1608, UC1610, UC1611, UC1638, UC1701
 
# Pre-requisites

* Wiring Pi library
 
# Installation

* Note: Make sure you have Wiring Pi library installed on your ARM device

# Usage examples

## Character LCD Example (HD44780)

## Graphic LCD Example (ST7920)

# Limitations

* This library is guaranteed to work on the Raspberry Pi, but I cannot guarantee that it would work on other ARM based devices (e.g. Tinker Board) as I only have Raspberry Pi in my possession (Donations welcome :D)
* As of this writing, only the display drivers are available. The UI framework is still under development.
* Only fullbuffer mode is supported on the graphics display driver.
 
# Known Issues/Troubleshooting

* Click [here](https://docs.google.com/spreadsheets/d/1WDh6J3zFE3j332CEIOvFzXhryOhoF7VAGc9Pf5vEo0s/edit?usp=sharing "Google spreadsheets") to check the current status of the display controller

# Contributing

* Build instructions

# Other projects you might be interested on

* GLCD Emulator/Simulator