/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
//
// THIS IS AN AUTO-GENERATED CODE!! DO NOT MODIFY (Last updated: Mon, 27 Jan 2020 07:31:21 +0800)
//
package com.ibasco.ucgdisplay.drivers.glcd;

import static com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics.*;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBufferType;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdControllerType;
import java.lang.SuppressWarnings;

@SuppressWarnings("unused")
public interface Glcd {
  /**
   * Display Controller: A2PRINTER
   */
  interface A2PRINTER {
    /**
     * <p>
     * Display Name:
     *     A2PRINTER :: 384X240
     * </p>
     * <p>
     * Display Width:
     *     384 pixels
     * </p>
     * <p>
     * Display height:
     *     240 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Serial/UART protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_384x240 = new GlcdDisplay(
        GlcdControllerType.A2PRINTER,
        "D_384x240",
        48,
        30,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_a2printer_384x240_f", COM_UART)
    );
  }

  /**
   * Display Controller: HX1230
   */
  interface HX1230 {
    /**
     * <p>
     * Display Name:
     *     HX1230 :: 96X68
     * </p>
     * <p>
     * Display Width:
     *     96 pixels
     * </p>
     * <p>
     * Display height:
     *     72 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     NoHWflip
     * </p>
     */
    GlcdDisplay D_96x72_96X68 = new GlcdDisplay(
        GlcdControllerType.HX1230,
        "D_96x72_96X68",
        12,
        9,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_hx1230_96x68_f", COM_4WSPI | COM_3WSPI)
    );
  }

  /**
   * Display Controller: IL3820
   */
  interface IL3820 {
    /**
     * <p>
     * Display Name:
     *     IL3820 :: 296X128
     * </p>
     * <p>
     * Display Width:
     *     296 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     PartlysupportedbyU8x8,noHWflip,nocontrastsetting,V2produceslesserscreen-flicker
     * </p>
     */
    GlcdDisplay D_296x128 = new GlcdDisplay(
        GlcdControllerType.IL3820,
        "D_296x128",
        37,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_il3820_296x128_f", COM_4WSPI | COM_3WSPI)
    );

    /**
     * <p>
     * Display Name:
     *     IL3820 :: V2_296X128
     * </p>
     * <p>
     * Display Width:
     *     296 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     PartlysupportedbyU8x8,noHWflip,nocontrastsetting,V2produceslesserscreen-flicker
     * </p>
     */
    GlcdDisplay D_296x128_V2296X128 = new GlcdDisplay(
        GlcdControllerType.IL3820,
        "D_296x128_V2296X128",
        37,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_il3820_v2_296x128_f", COM_4WSPI | COM_3WSPI)
    );
  }

  /**
   * Display Controller: IST3020
   */
  interface IST3020 {
    /**
     * <p>
     * Display Name:
     *     IST3020 :: ERC19264
     * </p>
     * <p>
     * Display Width:
     *     192 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_192x64_ERC19264 = new GlcdDisplay(
        GlcdControllerType.IST3020,
        "D_192x64_ERC19264",
        24,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ist3020_erc19264_f", COM_4WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: IST7920
   */
  interface IST7920 {
    /**
     * <p>
     * Display Name:
     *     IST7920 :: 128X128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128 = new GlcdDisplay(
        GlcdControllerType.IST7920,
        "D_128x128",
        16,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ist7920_128x128_f", COM_4WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: KS0108
   */
  interface KS0108 {
    /**
     * <p>
     * Display Name:
     *     KS0108 :: ERM19264
     * </p>
     * <p>
     * Display Width:
     *     192 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 6800 protocol for KS0108 (more chip-select lines)</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_192x64_ERM19264 = new GlcdDisplay(
        GlcdControllerType.KS0108,
        "D_192x64_ERM19264",
        24,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ks0108_erm19264_f", COM_KS0108)
    );

    /**
     * <p>
     * Display Name:
     *     KS0108 :: 128X64
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 6800 protocol for KS0108 (more chip-select lines)</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64 = new GlcdDisplay(
        GlcdControllerType.KS0108,
        "D_128x64",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ks0108_128x64_f", COM_KS0108)
    );
  }

  /**
   * Display Controller: LC7981
   */
  interface LC7981 {
    /**
     * <p>
     * Display Name:
     *     LC7981 :: 240X64
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 6800 protocol</li></ul>
     * <p>
     * Notes from author:
     *     U8x8notsupported,nopowerdown,noHWflip,noconstrast
     * </p>
     */
    GlcdDisplay D_240x64 = new GlcdDisplay(
        GlcdControllerType.LC7981,
        "D_240x64",
        30,
        8,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_lc7981_240x64_f", COM_6800)
    );

    /**
     * <p>
     * Display Name:
     *     LC7981 :: 160X80
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     80 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 6800 protocol</li></ul>
     * <p>
     * Notes from author:
     *     U8x8notsupported,nopowerdown,noHWflip,noconstrast
     * </p>
     */
    GlcdDisplay D_160x80 = new GlcdDisplay(
        GlcdControllerType.LC7981,
        "D_160x80",
        20,
        10,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_lc7981_160x80_f", COM_6800)
    );

    /**
     * <p>
     * Display Name:
     *     LC7981 :: 160X160
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 6800 protocol</li></ul>
     * <p>
     * Notes from author:
     *     U8x8notsupported,nopowerdown,noHWflip,noconstrast
     * </p>
     */
    GlcdDisplay D_160x160 = new GlcdDisplay(
        GlcdControllerType.LC7981,
        "D_160x160",
        20,
        20,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_lc7981_160x160_f", COM_6800)
    );

    /**
     * <p>
     * Display Name:
     *     LC7981 :: 240X128
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 6800 protocol</li></ul>
     * <p>
     * Notes from author:
     *     U8x8notsupported,nopowerdown,noHWflip,noconstrast
     * </p>
     */
    GlcdDisplay D_240x128 = new GlcdDisplay(
        GlcdControllerType.LC7981,
        "D_240x128",
        30,
        16,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_lc7981_240x128_f", COM_6800)
    );
  }

  /**
   * Display Controller: LD7032
   */
  interface LD7032 {
    /**
     * <p>
     * Display Name:
     *     LD7032 :: 60X32
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_64x32_60X32 = new GlcdDisplay(
        GlcdControllerType.LD7032,
        "D_64x32_60X32",
        8,
        4,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_ld7032_60x32_f", COM_4WSPI),
        new GlcdSetupInfo("u8g2_Setup_ld7032_i2c_60x32_f", COM_I2C)
    );
  }

  /**
   * Display Controller: LS013B7DH03
   */
  interface LS013B7DH03 {
    /**
     * <p>
     * Display Name:
     *     LS013B7DH03 :: 128X128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128 = new GlcdDisplay(
        GlcdControllerType.LS013B7DH03,
        "D_128x128",
        16,
        16,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_ls013b7dh03_128x128_f", COM_4WSPI)
    );
  }

  /**
   * Display Controller: LS013B7DH05
   */
  interface LS013B7DH05 {
    /**
     * <p>
     * Display Name:
     *     LS013B7DH05 :: 144X168
     * </p>
     * <p>
     * Display Width:
     *     144 pixels
     * </p>
     * <p>
     * Display height:
     *     168 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_144x168 = new GlcdDisplay(
        GlcdControllerType.LS013B7DH05,
        "D_144x168",
        18,
        21,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_ls013b7dh05_144x168_f", COM_4WSPI)
    );
  }

  /**
   * Display Controller: LS027B7DH01
   */
  interface LS027B7DH01 {
    /**
     * <p>
     * Display Name:
     *     LS027B7DH01 :: 400X240
     * </p>
     * <p>
     * Display Width:
     *     400 pixels
     * </p>
     * <p>
     * Display height:
     *     240 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_400x240 = new GlcdDisplay(
        GlcdControllerType.LS027B7DH01,
        "D_400x240",
        50,
        30,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_ls027b7dh01_400x240_f", COM_4WSPI)
    );
  }

  /**
   * Display Controller: MAX7219
   */
  interface MAX7219 {
    /**
     * <p>
     * Display Name:
     *     MAX7219 :: 64X8
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     8 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_64x8 = new GlcdDisplay(
        GlcdControllerType.MAX7219,
        "D_64x8",
        8,
        1,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_max7219_64x8_f", COM_4WSPI)
    );

    /**
     * <p>
     * Display Name:
     *     MAX7219 :: 8X8
     * </p>
     * <p>
     * Display Width:
     *     8 pixels
     * </p>
     * <p>
     * Display height:
     *     8 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_8x8 = new GlcdDisplay(
        GlcdControllerType.MAX7219,
        "D_8x8",
        1,
        1,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_max7219_8x8_f", COM_4WSPI)
    );

    /**
     * <p>
     * Display Name:
     *     MAX7219 :: 32X8
     * </p>
     * <p>
     * Display Width:
     *     32 pixels
     * </p>
     * <p>
     * Display height:
     *     8 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_32x8 = new GlcdDisplay(
        GlcdControllerType.MAX7219,
        "D_32x8",
        4,
        1,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_max7219_32x8_f", COM_4WSPI)
    );
  }

  /**
   * Display Controller: NT7534
   */
  interface NT7534 {
    /**
     * <p>
     * Display Name:
     *     NT7534 :: TG12864R
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_TG12864R = new GlcdDisplay(
        GlcdControllerType.NT7534,
        "D_128x64_TG12864R",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_nt7534_tg12864r_f", COM_4WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: PCD8544
   */
  interface PCD8544 {
    /**
     * <p>
     * Display Name:
     *     PCD8544 :: 84X48
     * </p>
     * <p>
     * Display Width:
     *     88 pixels
     * </p>
     * <p>
     * Display height:
     *     48 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     NoHWflip
     * </p>
     */
    GlcdDisplay D_88x48_84X48 = new GlcdDisplay(
        GlcdControllerType.PCD8544,
        "D_88x48_84X48",
        11,
        6,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_pcd8544_84x48_f", COM_4WSPI | COM_3WSPI)
    );
  }

  /**
   * Display Controller: PCF8812
   */
  interface PCF8812 {
    /**
     * <p>
     * Display Name:
     *     PCF8812 :: 96X65
     * </p>
     * <p>
     * Display Width:
     *     96 pixels
     * </p>
     * <p>
     * Display height:
     *     72 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     NoHWflip
     * </p>
     */
    GlcdDisplay D_96x72_96X65 = new GlcdDisplay(
        GlcdControllerType.PCF8812,
        "D_96x72_96X65",
        12,
        9,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_pcf8812_96x65_f", COM_4WSPI | COM_3WSPI)
    );
  }

  /**
   * Display Controller: RA8835
   */
  interface RA8835 {
    /**
     * <p>
     * Display Name:
     *     RA8835 :: NHD_240X128
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     TestedwithRA8835
     * </p>
     */
    GlcdDisplay D_240x128_NHD240X128 = new GlcdDisplay(
        GlcdControllerType.RA8835,
        "D_240x128_NHD240X128",
        30,
        16,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_ra8835_nhd_240x128_f", COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     RA8835 :: 320X240
     * </p>
     * <p>
     * Display Width:
     *     320 pixels
     * </p>
     * <p>
     * Display height:
     *     240 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     nottested
     * </p>
     */
    GlcdDisplay D_320x240 = new GlcdDisplay(
        GlcdControllerType.RA8835,
        "D_320x240",
        40,
        30,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_ra8835_320x240_f", COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: SBN1661
   */
  interface SBN1661 {
    /**
     * <p>
     * Display Name:
     *     SBN1661 :: 122X32
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Special protocol for SED1520</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32_122X32 = new GlcdDisplay(
        GlcdControllerType.SBN1661,
        "D_128x32_122X32",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sbn1661_122x32_f", COM_SED1520)
    );
  }

  /**
   * Display Controller: SED1330
   */
  interface SED1330 {
    /**
     * <p>
     * Display Name:
     *     SED1330 :: 240X128
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested,mightworkforRA8835andSED1335also
     * </p>
     */
    GlcdDisplay D_240x128 = new GlcdDisplay(
        GlcdControllerType.SED1330,
        "D_240x128",
        30,
        16,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_sed1330_240x128_f", COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: SED1520
   */
  interface SED1520 {
    /**
     * <p>
     * Display Name:
     *     SED1520 :: 122X32
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Special protocol for SED1520</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32_122X32 = new GlcdDisplay(
        GlcdControllerType.SED1520,
        "D_128x32_122X32",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sed1520_122x32_f", COM_SED1520)
    );
  }

  /**
   * Display Controller: SH1106
   */
  interface SH1106 {
    /**
     * <p>
     * Display Name:
     *     SH1106 :: 72X40_WISE
     * </p>
     * <p>
     * Display Width:
     *     72 pixels
     * </p>
     * <p>
     * Display height:
     *     40 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_72x40_72X40WISE = new GlcdDisplay(
        GlcdControllerType.SH1106,
        "D_72x40_72X40WISE",
        9,
        5,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1106_72x40_wise_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_72x40_wise_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1106 :: 128X64_WINSTAR
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_128x64_128X64WINSTAR = new GlcdDisplay(
        GlcdControllerType.SH1106,
        "D_128x64_128X64WINSTAR",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1106_128x64_winstar_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_128x64_winstar_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1106 :: 64X32
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_64x32 = new GlcdDisplay(
        GlcdControllerType.SH1106,
        "D_64x32",
        8,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1106_64x32_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_64x32_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1106 :: 128X64_NONAME
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_128x64_128X64NONAME = new GlcdDisplay(
        GlcdControllerType.SH1106,
        "D_128x64_128X64NONAME",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1106_128x64_noname_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_128x64_noname_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1106 :: 128X64_VCOMH0
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_128x64_128X64VCOMH0 = new GlcdDisplay(
        GlcdControllerType.SH1106,
        "D_128x64_128X64VCOMH0",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1106_128x64_vcomh0_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_128x64_vcomh0_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SH1107
   */
  interface SH1107 {
    /**
     * <p>
     * Display Name:
     *     SH1107 :: PIMORONI_128X128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128_PIMORONI128X128 = new GlcdDisplay(
        GlcdControllerType.SH1107,
        "D_128x128_PIMORONI128X128",
        16,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1107_pimoroni_128x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_pimoroni_128x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1107 :: SEEED_128X128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128_SEEED128X128 = new GlcdDisplay(
        GlcdControllerType.SH1107,
        "D_128x128_SEEED128X128",
        16,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1107_seeed_128x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_seeed_128x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1107 :: 128X128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128 = new GlcdDisplay(
        GlcdControllerType.SH1107,
        "D_128x128",
        16,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1107_128x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_128x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1107 :: 64X128
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_64x128 = new GlcdDisplay(
        GlcdControllerType.SH1107,
        "D_64x128",
        8,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1107_64x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_64x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SH1107 :: SEEED_96X96
     * </p>
     * <p>
     * Display Width:
     *     96 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_96x96_SEEED96X96 = new GlcdDisplay(
        GlcdControllerType.SH1107,
        "D_96x96_SEEED96X96",
        12,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1107_seeed_96x96_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_seeed_96x96_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SH1108
   */
  interface SH1108 {
    /**
     * <p>
     * Display Name:
     *     SH1108 :: 160X160
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_160x160 = new GlcdDisplay(
        GlcdControllerType.SH1108,
        "D_160x160",
        20,
        20,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_sh1108_160x160_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1108_i2c_160x160_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SH1122
   */
  interface SH1122 {
    /**
     * <p>
     * Display Name:
     *     SH1122 :: 256X64
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x64 = new GlcdDisplay(
        GlcdControllerType.SH1122,
        "D_256x64",
        32,
        8,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_sh1122_256x64_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_sh1122_i2c_256x64_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD0323
   */
  interface SSD0323 {
    /**
     * <p>
     * Display Name:
     *     SSD0323 :: OS128064
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_OS128064 = new GlcdDisplay(
        GlcdControllerType.SSD0323,
        "D_128x64_OS128064",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd0323_os128064_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd0323_i2c_os128064_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1305
   */
  interface SSD1305 {
    /**
     * <p>
     * Display Name:
     *     SSD1305 :: 128X32_NONAME
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32_128X32NONAME = new GlcdDisplay(
        GlcdControllerType.SSD1305,
        "D_128x32_128X32NONAME",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1305_128x32_noname_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1305_i2c_128x32_noname_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1305 :: 128X32_ADAFRUIT
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32_128X32ADAFRUIT = new GlcdDisplay(
        GlcdControllerType.SSD1305,
        "D_128x32_128X32ADAFRUIT",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1305_128x32_adafruit_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1305_i2c_128x32_adafruit_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1305 :: 128X64_ADAFRUIT
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_128X64ADAFRUIT = new GlcdDisplay(
        GlcdControllerType.SSD1305,
        "D_128x64_128X64ADAFRUIT",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1305_128x64_adafruit_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1305_i2c_128x64_adafruit_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1306
   */
  interface SSD1306 {
    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 64X32_NONAME
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_64x32_64X32NONAME = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_64x32_64X32NONAME",
        8,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_64x32_noname_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_64x32_noname_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 96X16_ER
     * </p>
     * <p>
     * Display Width:
     *     96 pixels
     * </p>
     * <p>
     * Display height:
     *     16 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_96x16_96X16ER = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_96x16_96X16ER",
        12,
        2,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_96x16_er_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_96x16_er_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 72X40_ER
     * </p>
     * <p>
     * Display Width:
     *     72 pixels
     * </p>
     * <p>
     * Display height:
     *     40 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_72x40_72X40ER = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_72x40_72X40ER",
        9,
        5,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_72x40_er_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_72x40_er_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 128X32_UNIVISION
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32_128X32UNIVISION = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_128x32_128X32UNIVISION",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_128x32_univision_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x32_univision_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 128X64_ALT0
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_128X64ALT0 = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_128x64_128X64ALT0",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_128x64_alt0_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x64_alt0_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 64X48_ER
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     48 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_64x48_64X48ER = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_64x48_64X48ER",
        8,
        6,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_64x48_er_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_64x48_er_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 128X64_NONAME
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_128X64NONAME = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_128x64_128X64NONAME",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_128x64_noname_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x64_noname_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 128X32_WINSTAR
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32_128X32WINSTAR = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_128x32_128X32WINSTAR",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_128x32_winstar_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x32_winstar_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 128X64_VCOMH0
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_128X64VCOMH0 = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_128x64_128X64VCOMH0",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_128x64_vcomh0_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x64_vcomh0_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 48X64_WINSTAR
     * </p>
     * <p>
     * Display Width:
     *     48 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_48x64_48X64WINSTAR = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_48x64_48X64WINSTAR",
        6,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_48x64_winstar_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_48x64_winstar_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1306 :: 64X32_1F
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_64x32_64X321F = new GlcdDisplay(
        GlcdControllerType.SSD1306,
        "D_64x32_64X321F",
        8,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1306_64x32_1f_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_64x32_1f_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1309
   */
  interface SSD1309 {
    /**
     * <p>
     * Display Name:
     *     SSD1309 :: 128X64_NONAME2
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_128X64NONAME2 = new GlcdDisplay(
        GlcdControllerType.SSD1309,
        "D_128x64_128X64NONAME2",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1309_128x64_noname2_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1309_i2c_128x64_noname2_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1309 :: 128X64_NONAME0
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_128X64NONAME0 = new GlcdDisplay(
        GlcdControllerType.SSD1309,
        "D_128x64_128X64NONAME0",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1309_128x64_noname0_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1309_i2c_128x64_noname0_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1316
   */
  interface SSD1316 {
    /**
     * <p>
     * Display Name:
     *     SSD1316 :: 128X32
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32 = new GlcdDisplay(
        GlcdControllerType.SSD1316,
        "D_128x32",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1316_128x32_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1316_i2c_128x32_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1317
   */
  interface SSD1317 {
    /**
     * <p>
     * Display Name:
     *     SSD1317 :: 96X96
     * </p>
     * <p>
     * Display Width:
     *     96 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_96x96 = new GlcdDisplay(
        GlcdControllerType.SSD1317,
        "D_96x96",
        12,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1317_96x96_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1317_i2c_96x96_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1318
   */
  interface SSD1318 {
    /**
     * <p>
     * Display Name:
     *     SSD1318 :: 128X96
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x96 = new GlcdDisplay(
        GlcdControllerType.SSD1318,
        "D_128x96",
        16,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1318_128x96_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1318_i2c_128x96_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1318 :: 128X96_XCP
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x96_128X96XCP = new GlcdDisplay(
        GlcdControllerType.SSD1318,
        "D_128x96_128X96XCP",
        16,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1318_128x96_xcp_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1318_i2c_128x96_xcp_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1322
   */
  interface SSD1322 {
    /**
     * <p>
     * Display Name:
     *     SSD1322 :: NHD_256X64
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     RequiresU8G2_16BIT(seeu8g2.h)
     * </p>
     */
    GlcdDisplay D_256x64_NHD256X64 = new GlcdDisplay(
        GlcdControllerType.SSD1322,
        "D_256x64_NHD256X64",
        32,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1322_nhd_256x64_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1322 :: NHD_128X64
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_NHD128X64 = new GlcdDisplay(
        GlcdControllerType.SSD1322,
        "D_128x64_NHD128X64",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1322_nhd_128x64_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: SSD1325
   */
  interface SSD1325 {
    /**
     * <p>
     * Display Name:
     *     SSD1325 :: NHD_128X64
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_NHD128X64 = new GlcdDisplay(
        GlcdControllerType.SSD1325,
        "D_128x64_NHD128X64",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1325_nhd_128x64_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1325_i2c_nhd_128x64_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1326
   */
  interface SSD1326 {
    /**
     * <p>
     * Display Name:
     *     SSD1326 :: ER_256X32
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x32_ER256X32 = new GlcdDisplay(
        GlcdControllerType.SSD1326,
        "D_256x32_ER256X32",
        32,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1326_er_256x32_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1326_i2c_er_256x32_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1327
   */
  interface SSD1327 {
    /**
     * <p>
     * Display Name:
     *     SSD1327 :: WS_96X64
     * </p>
     * <p>
     * Display Width:
     *     96 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_96x64_WS96X64 = new GlcdDisplay(
        GlcdControllerType.SSD1327,
        "D_96x64_WS96X64",
        12,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1327_ws_96x64_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_ws_96x64_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1327 :: EA_W128128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128_EAW128128 = new GlcdDisplay(
        GlcdControllerType.SSD1327,
        "D_128x128_EAW128128",
        16,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1327_ea_w128128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_ea_w128128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1327 :: VISIONOX_128X96
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x96_VISIONOX128X96 = new GlcdDisplay(
        GlcdControllerType.SSD1327,
        "D_128x96_VISIONOX128X96",
        16,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1327_visionox_128x96_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_visionox_128x96_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1327 :: WS_128X128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128_WS128X128 = new GlcdDisplay(
        GlcdControllerType.SSD1327,
        "D_128x128_WS128X128",
        16,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1327_ws_128x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_ws_128x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1327 :: MIDAS_128X128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x128_MIDAS128X128 = new GlcdDisplay(
        GlcdControllerType.SSD1327,
        "D_128x128_MIDAS128X128",
        16,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1327_midas_128x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_midas_128x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1327 :: SEEED_96X96
     * </p>
     * <p>
     * Display Width:
     *     96 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_96x96_SEEED96X96 = new GlcdDisplay(
        GlcdControllerType.SSD1327,
        "D_96x96_SEEED96X96",
        12,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1327_seeed_96x96_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_seeed_96x96_f", COM_I2C)
    );
  }

  /**
   * Display Controller: SSD1329
   */
  interface SSD1329 {
    /**
     * <p>
     * Display Name:
     *     SSD1329 :: 128X96_NONAME
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x96_128X96NONAME = new GlcdDisplay(
        GlcdControllerType.SSD1329,
        "D_128x96_128X96NONAME",
        16,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1329_128x96_noname_f", COM_4WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: SSD1606
   */
  interface SSD1606 {
    /**
     * <p>
     * Display Name:
     *     SSD1606 :: 172X72
     * </p>
     * <p>
     * Display Width:
     *     176 pixels
     * </p>
     * <p>
     * Display height:
     *     72 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     PartlysupportedbyU8x8,noHWflip,nocontrastsetting
     * </p>
     */
    GlcdDisplay D_176x72_172X72 = new GlcdDisplay(
        GlcdControllerType.SSD1606,
        "D_176x72_172X72",
        22,
        9,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1606_172x72_f", COM_4WSPI | COM_3WSPI)
    );
  }

  /**
   * Display Controller: SSD1607
   */
  interface SSD1607 {
    /**
     * <p>
     * Display Name:
     *     SSD1607 :: GD_200X200
     * </p>
     * <p>
     * Display Width:
     *     200 pixels
     * </p>
     * <p>
     * Display height:
     *     200 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     PartlysupportedbyU8x8,noHWflip,nocontrastsetting,v2includesanoptimizedLUT
     * </p>
     */
    GlcdDisplay D_200x200_GD200X200 = new GlcdDisplay(
        GlcdControllerType.SSD1607,
        "D_200x200_GD200X200",
        25,
        25,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1607_gd_200x200_f", COM_4WSPI | COM_3WSPI)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1607 :: 200X200
     * </p>
     * <p>
     * Display Width:
     *     200 pixels
     * </p>
     * <p>
     * Display height:
     *     200 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     PartlysupportedbyU8x8,noHWflip,nocontrastsetting,v2includesanoptimizedLUT
     * </p>
     */
    GlcdDisplay D_200x200 = new GlcdDisplay(
        GlcdControllerType.SSD1607,
        "D_200x200",
        25,
        25,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1607_200x200_f", COM_4WSPI | COM_3WSPI)
    );

    /**
     * <p>
     * Display Name:
     *     SSD1607 :: WS_200X200
     * </p>
     * <p>
     * Display Width:
     *     200 pixels
     * </p>
     * <p>
     * Display height:
     *     200 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     PartlysupportedbyU8x8,noHWflip,nocontrastsetting,v2includesanoptimizedLUT
     * </p>
     */
    GlcdDisplay D_200x200_WS200X200 = new GlcdDisplay(
        GlcdControllerType.SSD1607,
        "D_200x200_WS200X200",
        25,
        25,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_ssd1607_ws_200x200_f", COM_4WSPI | COM_3WSPI)
    );
  }

  /**
   * Display Controller: ST7511
   */
  interface ST7511 {
    /**
     * <p>
     * Display Name:
     *     ST7511 :: AVD_320X240
     * </p>
     * <p>
     * Display Width:
     *     320 pixels
     * </p>
     * <p>
     * Display height:
     *     240 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_320x240_AVD320X240 = new GlcdDisplay(
        GlcdControllerType.ST7511,
        "D_320x240_AVD320X240",
        40,
        30,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7511_avd_320x240_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: ST75256
   */
  interface ST75256 {
    /**
     * <p>
     * Display Name:
     *     ST75256 :: WO256X128
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x128_WO256X128 = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_256x128_WO256X128",
        32,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_wo256x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_wo256x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX256160
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x160_JLX256160 = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_256x160_JLX256160",
        32,
        20,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx256160_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx256160_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX256160_ALT
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x160_JLX256160ALT = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_256x160_JLX256160ALT",
        32,
        20,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx256160_alt_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx256160_alt_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX256128
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x128_JLX256128 = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_256x128_JLX256128",
        32,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx256128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx256128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX256160M
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x160_JLX256160M = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_256x160_JLX256160M",
        32,
        20,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx256160m_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx256160m_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX19296
     * </p>
     * <p>
     * Display Width:
     *     192 pixels
     * </p>
     * <p>
     * Display height:
     *     96 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_192x96_JLX19296 = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_192x96_JLX19296",
        24,
        12,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx19296_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx19296_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX240160
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x160_JLX240160 = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_240x160_JLX240160",
        30,
        20,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx240160_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx240160_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX25664
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_256x64_JLX25664 = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_256x64_JLX25664",
        32,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx25664_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx25664_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST75256 :: JLX172104
     * </p>
     * <p>
     * Display Width:
     *     176 pixels
     * </p>
     * <p>
     * Display height:
     *     104 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_176x104_JLX172104 = new GlcdDisplay(
        GlcdControllerType.ST75256,
        "D_176x104_JLX172104",
        22,
        13,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75256_jlx172104_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx172104_f", COM_I2C)
    );
  }

  /**
   * Display Controller: ST7528
   */
  interface ST7528 {
    /**
     * <p>
     * Display Name:
     *     ST7528 :: NHD_C160100
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     104 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_160x104_NHDC160100 = new GlcdDisplay(
        GlcdControllerType.ST7528,
        "D_160x104_NHDC160100",
        20,
        13,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7528_nhd_c160100_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st7528_i2c_nhd_c160100_f", COM_I2C)
    );
  }

  /**
   * Display Controller: ST75320
   */
  interface ST75320 {
    /**
     * <p>
     * Display Name:
     *     ST75320 :: JLX320240
     * </p>
     * <p>
     * Display Width:
     *     320 pixels
     * </p>
     * <p>
     * Display height:
     *     240 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_320x240_JLX320240 = new GlcdDisplay(
        GlcdControllerType.ST75320,
        "D_320x240_JLX320240",
        40,
        30,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st75320_jlx320240_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st75320_i2c_jlx320240_f", COM_I2C)
    );
  }

  /**
   * Display Controller: ST7565
   */
  interface ST7565 {
    /**
     * <p>
     * Display Name:
     *     ST7565 :: ERC12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_ERC12864 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_ERC12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_erc12864_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: LM6063
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_LM6063 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_LM6063",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_lm6063_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: ERC12864_ALT
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_ERC12864ALT = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_ERC12864ALT",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_erc12864_alt_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: EA_DOGM132
     * </p>
     * <p>
     * Display Width:
     *     136 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_136x32_EADOGM132 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_136x32_EADOGM132",
        17,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_ea_dogm132_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: ZOLEN_128X64
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_ZOLEN128X64 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_ZOLEN128X64",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_zolen_128x64_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: LX12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_LX12864 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_LX12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_lx12864_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: LM6059
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_LM6059 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_LM6059",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_lm6059_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: NHD_C12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_NHDC12864 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_NHDC12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_nhd_c12864_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: NHD_C12832
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32_NHDC12832 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x32_NHDC12832",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_nhd_c12832_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: JLX12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_JLX12864 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_JLX12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_jlx12864_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: EA_DOGM128
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_EADOGM128 = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_EADOGM128",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_ea_dogm128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7565 :: 64128N
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_64128N = new GlcdDisplay(
        GlcdControllerType.ST7565,
        "D_128x64_64128N",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7565_64128n_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: ST7567
   */
  interface ST7567 {
    /**
     * <p>
     * Display Name:
     *     ST7567 :: ENH_DG128064
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_ENHDG128064 = new GlcdDisplay(
        GlcdControllerType.ST7567,
        "D_128x64_ENHDG128064",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7567_enh_dg128064_f", COM_4WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7567 :: PI_132X64
     * </p>
     * <p>
     * Display Width:
     *     136 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_136x64_PI132X64 = new GlcdDisplay(
        GlcdControllerType.ST7567,
        "D_136x64_PI132X64",
        17,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7567_pi_132x64_f", COM_4WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7567 :: OS12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_OS12864 = new GlcdDisplay(
        GlcdControllerType.ST7567,
        "D_128x64_OS12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7567_os12864_f", COM_4WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7567 :: 64X32
     * </p>
     * <p>
     * Display Width:
     *     64 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_64x32 = new GlcdDisplay(
        GlcdControllerType.ST7567,
        "D_64x32",
        8,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7567_64x32_f", COM_4WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st7567_i2c_64x32_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     ST7567 :: ENH_DG128064I
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_ENHDG128064I = new GlcdDisplay(
        GlcdControllerType.ST7567,
        "D_128x64_ENHDG128064I",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7567_enh_dg128064i_f", COM_4WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     ST7567 :: JLX12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_JLX12864 = new GlcdDisplay(
        GlcdControllerType.ST7567,
        "D_128x64_JLX12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7567_jlx12864_f", COM_4WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: ST7586S
   */
  interface ST7586S {
    /**
     * <p>
     * Display Name:
     *     ST7586S :: S028HN118A
     * </p>
     * <p>
     * Display Width:
     *     384 pixels
     * </p>
     * <p>
     * Display height:
     *     136 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_384x136_S028HN118A = new GlcdDisplay(
        GlcdControllerType.ST7586S,
        "D_384x136_S028HN118A",
        48,
        17,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_st7586s_s028hn118a_f", COM_4WSPI)
    );

    /**
     * <p>
     * Display Name:
     *     ST7586S :: ERC240160
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x160_ERC240160 = new GlcdDisplay(
        GlcdControllerType.ST7586S,
        "D_240x160_ERC240160",
        30,
        20,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_st7586s_erc240160_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: ST7588
   */
  interface ST7588 {
    /**
     * <p>
     * Display Name:
     *     ST7588 :: JLX12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_JLX12864 = new GlcdDisplay(
        GlcdControllerType.ST7588,
        "D_128x64_JLX12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_st7588_jlx12864_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st7588_i2c_jlx12864_f", COM_I2C)
    );
  }

  /**
   * Display Controller: ST7920
   */
  interface ST7920 {
    /**
     * <p>
     * Display Name:
     *     ST7920 :: 192X32
     * </p>
     * <p>
     * Display Width:
     *     192 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>UNKNOWN</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_192x32 = new GlcdDisplay(
        GlcdControllerType.ST7920,
        "D_192x32",
        24,
        4,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_st7920_p_192x32_f", COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st7920_192x32_f", COM_6800),
        new GlcdSetupInfo("u8g2_Setup_st7920_s_192x32_f", COM_ST7920SPI)
    );

    /**
     * <p>
     * Display Name:
     *     ST7920 :: 128X64
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>UNKNOWN</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64 = new GlcdDisplay(
        GlcdControllerType.ST7920,
        "D_128x64",
        16,
        8,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_st7920_p_128x64_f", COM_8080),
        new GlcdSetupInfo("u8g2_Setup_st7920_128x64_f", COM_6800),
        new GlcdSetupInfo("u8g2_Setup_st7920_s_128x64_f", COM_ST7920SPI)
    );
  }

  /**
   * Display Controller: T6963
   */
  interface T6963 {
    /**
     * <p>
     * Display Name:
     *     T6963 :: 240X64
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x64 = new GlcdDisplay(
        GlcdControllerType.T6963,
        "D_240x64",
        30,
        8,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_t6963_240x64_f", COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     T6963 :: 256X64
     * </p>
     * <p>
     * Display Width:
     *     256 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_256x64 = new GlcdDisplay(
        GlcdControllerType.T6963,
        "D_256x64",
        32,
        8,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_t6963_256x64_f", COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     T6963 :: 128X64
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_128x64 = new GlcdDisplay(
        GlcdControllerType.T6963,
        "D_128x64",
        16,
        8,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_t6963_128x64_f", COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     T6963 :: 128X64_ALT
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_128x64_128X64ALT = new GlcdDisplay(
        GlcdControllerType.T6963,
        "D_128x64_128X64ALT",
        16,
        8,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_t6963_128x64_alt_f", COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     T6963 :: 160X80
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     80 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     Nottested
     * </p>
     */
    GlcdDisplay D_160x80 = new GlcdDisplay(
        GlcdControllerType.T6963,
        "D_160x80",
        20,
        10,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_t6963_160x80_f", COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     T6963 :: 240X128
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x128 = new GlcdDisplay(
        GlcdControllerType.T6963,
        "D_240x128",
        30,
        16,
        GlcdBufferType.HORIZONTAL,
        new GlcdSetupInfo("u8g2_Setup_t6963_240x128_f", COM_8080)
    );
  }

  /**
   * Display Controller: UC1601
   */
  interface UC1601 {
    /**
     * <p>
     * Display Name:
     *     UC1601 :: 128X32
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     32 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x32 = new GlcdDisplay(
        GlcdControllerType.UC1601,
        "D_128x32",
        16,
        4,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1601_128x32_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1601_i2c_128x32_f", COM_I2C)
    );
  }

  /**
   * Display Controller: UC1604
   */
  interface UC1604 {
    /**
     * <p>
     * Display Name:
     *     UC1604 :: JLX19264
     * </p>
     * <p>
     * Display Width:
     *     192 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_192x64_JLX19264 = new GlcdDisplay(
        GlcdControllerType.UC1604,
        "D_192x64_JLX19264",
        24,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1604_jlx19264_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1604_i2c_jlx19264_f", COM_I2C)
    );
  }

  /**
   * Display Controller: UC1608
   */
  interface UC1608 {
    /**
     * <p>
     * Display Name:
     *     UC1608 :: ERC24064
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x64_ERC24064 = new GlcdDisplay(
        GlcdControllerType.UC1608,
        "D_240x64_ERC24064",
        30,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1608_erc24064_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1608_i2c_erc24064_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     UC1608 :: 240X128
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x128 = new GlcdDisplay(
        GlcdControllerType.UC1608,
        "D_240x128",
        30,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1608_240x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1608_i2c_240x128_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     UC1608 :: ERC240120
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     120 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x120_ERC240120 = new GlcdDisplay(
        GlcdControllerType.UC1608,
        "D_240x120_ERC240120",
        30,
        15,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1608_erc240120_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1608_i2c_erc240120_f", COM_I2C)
    );
  }

  /**
   * Display Controller: UC1610
   */
  interface UC1610 {
    /**
     * <p>
     * Display Name:
     *     UC1610 :: EA_DOGXL160
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     104 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     3WSPInottested
     * </p>
     */
    GlcdDisplay D_160x104_EADOGXL160 = new GlcdDisplay(
        GlcdControllerType.UC1610,
        "D_160x104_EADOGXL160",
        20,
        13,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1610_ea_dogxl160_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1610_i2c_ea_dogxl160_f", COM_I2C)
    );
  }

  /**
   * Display Controller: UC1611
   */
  interface UC1611 {
    /**
     * <p>
     * Display Name:
     *     UC1611 :: EW50850
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     240x160,activehighchipselect
     * </p>
     */
    GlcdDisplay D_240x160_EW50850 = new GlcdDisplay(
        GlcdControllerType.UC1611,
        "D_240x160_EW50850",
        30,
        20,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1611_ew50850_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1611_i2c_ew50850_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     UC1611 :: CG160160
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     160 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     useCS0aschipsselect
     * </p>
     */
    GlcdDisplay D_160x160_CG160160 = new GlcdDisplay(
        GlcdControllerType.UC1611,
        "D_160x160_CG160160",
        20,
        20,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1611_cg160160_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1611_i2c_cg160160_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     UC1611 :: EA_DOGM240
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x64_EADOGM240 = new GlcdDisplay(
        GlcdControllerType.UC1611,
        "D_240x64_EADOGM240",
        30,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1611_ea_dogm240_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1611_i2c_ea_dogm240_f", COM_I2C)
    );

    /**
     * <p>
     * Display Name:
     *     UC1611 :: EA_DOGXL240
     * </p>
     * <p>
     * Display Width:
     *     240 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li>
     * <li>I2C protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_240x128_EADOGXL240 = new GlcdDisplay(
        GlcdControllerType.UC1611,
        "D_240x128_EADOGXL240",
        30,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1611_ea_dogxl240_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080),
        new GlcdSetupInfo("u8g2_Setup_uc1611_i2c_ea_dogxl240_f", COM_I2C)
    );
  }

  /**
   * Display Controller: UC1638
   */
  interface UC1638 {
    /**
     * <p>
     * Display Name:
     *     UC1638 :: 160X128
     * </p>
     * <p>
     * Display Width:
     *     160 pixels
     * </p>
     * <p>
     * Display height:
     *     128 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_160x128 = new GlcdDisplay(
        GlcdControllerType.UC1638,
        "D_160x128",
        20,
        16,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1638_160x128_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );
  }

  /**
   * Display Controller: UC1701
   */
  interface UC1701 {
    /**
     * <p>
     * Display Name:
     *     UC1701 :: MINI12864
     * </p>
     * <p>
     * Display Width:
     *     128 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_128x64_MINI12864 = new GlcdDisplay(
        GlcdControllerType.UC1701,
        "D_128x64_MINI12864",
        16,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1701_mini12864_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );

    /**
     * <p>
     * Display Name:
     *     UC1701 :: EA_DOGS102
     * </p>
     * <p>
     * Display Width:
     *     104 pixels
     * </p>
     * <p>
     * Display height:
     *     64 pixels
     * </p>
     * Supported Bus Interfaces: 
     * <ul><li>4-Wire SPI protocol</li>
     * <li>3-Wire SPI protocol</li>
     * <li>Parallel 8-bit 6800 protocol</li>
     * <li>Parallel 8-bit 8080 protocol</li></ul>
     * <p>
     * Notes from author:
     *     N/A
     * </p>
     */
    GlcdDisplay D_104x64_EADOGS102 = new GlcdDisplay(
        GlcdControllerType.UC1701,
        "D_104x64_EADOGS102",
        13,
        8,
        GlcdBufferType.VERTICAL,
        new GlcdSetupInfo("u8g2_Setup_uc1701_ea_dogs102_f", COM_4WSPI | COM_3WSPI | COM_6800 | COM_8080)
    );
  }
}
