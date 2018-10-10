/*
 * THIS IS AN AUTO-GENERATED CODE!! PLEASE DO NOT MODIFY (Last updated: Oct 10 2018 01:02:14)
 */

package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdControllerType;

import static com.ibasco.pidisplay.core.u8g2.U8g2Graphics.*;

@SuppressWarnings("unused")
public interface Glcd {
	/** Controller Name: A2PRINTER **/
	interface A2PRINTER {
		/** U8G2 Name: A2PRINTER 384X240 **/
		GlcdDisplay D_384x240 = new GlcdDisplay(
			GlcdControllerType.A2PRINTER,
			"D_384x240",
			48,
			30,
			new GlcdSetupInfo("u8g2_Setup_a2printer_384x240_f", COM_UART)
		);
	}

	/** Controller Name: HX1230 **/
	interface HX1230 {
		/** U8G2 Name: HX1230 96X68 **/
		GlcdDisplay D_96x72 = new GlcdDisplay(
			GlcdControllerType.HX1230,
			"D_96x72",
			12,
			9,
			new GlcdSetupInfo("u8g2_Setup_hx1230_96x68_f", COM_3WSPI | COM_4WSPI)
		);
	}

	/** Controller Name: IL3820 **/
	interface IL3820 {
		/** U8G2 Name: IL3820 296X128 **/
		GlcdDisplay D_296x128 = new GlcdDisplay(
			GlcdControllerType.IL3820,
			"D_296x128",
			37,
			16,
			new GlcdSetupInfo("u8g2_Setup_il3820_296x128_f", COM_3WSPI | COM_4WSPI)
		);
		/** U8G2 Name: IL3820 V2_296X128 **/
		GlcdDisplay D_296x128_V2 = new GlcdDisplay(
			GlcdControllerType.IL3820,
			"D_296x128_V2",
			37,
			16,
			new GlcdSetupInfo("u8g2_Setup_il3820_v2_296x128_f", COM_3WSPI | COM_4WSPI)
		);
	}

	/** Controller Name: IST3020 **/
	interface IST3020 {
		/** U8G2 Name: IST3020 ERC19264 **/
		GlcdDisplay D_192x64_ERC19264 = new GlcdDisplay(
			GlcdControllerType.IST3020,
			"D_192x64_ERC19264",
			24,
			8,
			new GlcdSetupInfo("u8g2_Setup_ist3020_erc19264_f", COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: KS0108 **/
	interface KS0108 {
		/** U8G2 Name: KS0108 128X64 **/
		GlcdDisplay D_128x64 = new GlcdDisplay(
			GlcdControllerType.KS0108,
			"D_128x64",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ks0108_128x64_f", COM_KS0108)
		);
		/** U8G2 Name: KS0108 ERM19264 **/
		GlcdDisplay D_192x64_ERM19264 = new GlcdDisplay(
			GlcdControllerType.KS0108,
			"D_192x64_ERM19264",
			24,
			8,
			new GlcdSetupInfo("u8g2_Setup_ks0108_erm19264_f", COM_KS0108)
		);
	}

	/** Controller Name: LC7981 **/
	interface LC7981 {
		/** U8G2 Name: LC7981 160X80 **/
		GlcdDisplay D_160x80 = new GlcdDisplay(
			GlcdControllerType.LC7981,
			"D_160x80",
			20,
			10,
			new GlcdSetupInfo("u8g2_Setup_lc7981_160x80_f", COM_6800)
		);
		/** U8G2 Name: LC7981 160X160 **/
		GlcdDisplay D_160x160 = new GlcdDisplay(
			GlcdControllerType.LC7981,
			"D_160x160",
			20,
			20,
			new GlcdSetupInfo("u8g2_Setup_lc7981_160x160_f", COM_6800)
		);
		/** U8G2 Name: LC7981 240X128 **/
		GlcdDisplay D_240x128 = new GlcdDisplay(
			GlcdControllerType.LC7981,
			"D_240x128",
			30,
			16,
			new GlcdSetupInfo("u8g2_Setup_lc7981_240x128_f", COM_6800)
		);
		/** U8G2 Name: LC7981 240X64 **/
		GlcdDisplay D_240x64 = new GlcdDisplay(
			GlcdControllerType.LC7981,
			"D_240x64",
			30,
			8,
			new GlcdSetupInfo("u8g2_Setup_lc7981_240x64_f", COM_6800)
		);
	}

	/** Controller Name: LD7032 **/
	interface LD7032 {
		/** U8G2 Name: LD7032 60X32 **/
		GlcdDisplay D_64x32 = new GlcdDisplay(
			GlcdControllerType.LD7032,
			"D_64x32",
			8,
			4,
			new GlcdSetupInfo("u8g2_Setup_ld7032_60x32_f", COM_4WSPI),
			new GlcdSetupInfo("u8g2_Setup_ld7032_i2c_60x32_f", COM_I2C)
		);
	}

	/** Controller Name: LS013B7DH03 **/
	interface LS013B7DH03 {
		/** U8G2 Name: LS013B7DH03 128X128 **/
		GlcdDisplay D_128x128 = new GlcdDisplay(
			GlcdControllerType.LS013B7DH03,
			"D_128x128",
			16,
			16,
			new GlcdSetupInfo("u8g2_Setup_ls013b7dh03_128x128_f", COM_4WSPI)
		);
	}

	/** Controller Name: LS027B7DH01 **/
	interface LS027B7DH01 {
		/** U8G2 Name: LS027B7DH01 400X240 **/
		GlcdDisplay D_400x240 = new GlcdDisplay(
                GlcdControllerType.LS027B7DH01,
                "D_400x240",
                50,
                30,
                new GlcdSetupInfo("u8g2_Setup_ls027b7dh01_400x240_f", COM_4WSPI)
		);
	}

	/** Controller Name: MAX7219 **/
	interface MAX7219 {
        /** U8G2 Name: MAX7219 64X8 **/
        GlcdDisplay D_64x8 = new GlcdDisplay(
                GlcdControllerType.MAX7219,
                "D_64x8",
                8,
                1,
                new GlcdSetupInfo("u8g2_Setup_max7219_64x8_f", COM_4WSPI)
        );
		/** U8G2 Name: MAX7219 32X8 **/
		GlcdDisplay D_32x8 = new GlcdDisplay(
			GlcdControllerType.MAX7219,
			"D_32x8",
			4,
			1,
			new GlcdSetupInfo("u8g2_Setup_max7219_32x8_f", COM_4WSPI)
		);
		/** U8G2 Name: MAX7219 8X8 **/
		GlcdDisplay D_8x8 = new GlcdDisplay(
			GlcdControllerType.MAX7219,
			"D_8x8",
			1,
			1,
			new GlcdSetupInfo("u8g2_Setup_max7219_8x8_f", COM_4WSPI)
		);
	}

	/** Controller Name: NT7534 **/
	interface NT7534 {
		/** U8G2 Name: NT7534 TG12864R **/
		GlcdDisplay D_128x64_TG12864R = new GlcdDisplay(
			GlcdControllerType.NT7534,
			"D_128x64_TG12864R",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_nt7534_tg12864r_f", COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: PCD8544 **/
	interface PCD8544 {
		/** U8G2 Name: PCD8544 84X48 **/
		GlcdDisplay D_88x48 = new GlcdDisplay(
			GlcdControllerType.PCD8544,
			"D_88x48",
			11,
			6,
			new GlcdSetupInfo("u8g2_Setup_pcd8544_84x48_f", COM_3WSPI | COM_4WSPI)
		);
	}

	/** Controller Name: PCF8812 **/
	interface PCF8812 {
		/** U8G2 Name: PCF8812 96X65 **/
		GlcdDisplay D_96x72 = new GlcdDisplay(
			GlcdControllerType.PCF8812,
			"D_96x72",
			12,
			9,
			new GlcdSetupInfo("u8g2_Setup_pcf8812_96x65_f", COM_3WSPI | COM_4WSPI)
		);
	}

	/** Controller Name: RA8835 **/
	interface RA8835 {
		/** U8G2 Name: RA8835 NHD_240X128 **/
		GlcdDisplay D_240x128_NHD = new GlcdDisplay(
			GlcdControllerType.RA8835,
			"D_240x128_NHD",
			30,
			16,
			new GlcdSetupInfo("u8g2_Setup_ra8835_nhd_240x128_f", COM_6800 | COM_8080)
		);
		/** U8G2 Name: RA8835 320X240 **/
		GlcdDisplay D_320x240 = new GlcdDisplay(
			GlcdControllerType.RA8835,
			"D_320x240",
			40,
			30,
			new GlcdSetupInfo("u8g2_Setup_ra8835_320x240_f", COM_6800 | COM_8080)
		);
	}

	/** Controller Name: SBN1661 **/
	interface SBN1661 {
		/** U8G2 Name: SBN1661 122X32 **/
		GlcdDisplay D_128x32 = new GlcdDisplay(
			GlcdControllerType.SBN1661,
			"D_128x32",
			16,
			4,
			new GlcdSetupInfo("u8g2_Setup_sbn1661_122x32_f", COM_SED1520)
		);
	}

	/** Controller Name: SED1330 **/
	interface SED1330 {
		/** U8G2 Name: SED1330 240X128 **/
		GlcdDisplay D_240x128 = new GlcdDisplay(
			GlcdControllerType.SED1330,
			"D_240x128",
			30,
			16,
			new GlcdSetupInfo("u8g2_Setup_sed1330_240x128_f", COM_6800 | COM_8080)
		);
	}

	/** Controller Name: SED1520 **/
	interface SED1520 {
		/** U8G2 Name: SED1520 122X32 **/
		GlcdDisplay D_128x32 = new GlcdDisplay(
			GlcdControllerType.SED1520,
			"D_128x32",
			16,
			4,
			new GlcdSetupInfo("u8g2_Setup_sed1520_122x32_f", COM_SED1520)
		);
	}

	/** Controller Name: SH1106 **/
	interface SH1106 {
		/** U8G2 Name: SH1106 128X64_NONAME **/
		GlcdDisplay D_128x64_NONAME = new GlcdDisplay(
			GlcdControllerType.SH1106,
			"D_128x64_NONAME",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_sh1106_128x64_noname_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_128x64_noname_f", COM_I2C)
		);
		/** U8G2 Name: SH1106 128X64_VCOMH0 **/
		GlcdDisplay D_128x64_VCOMH0 = new GlcdDisplay(
			GlcdControllerType.SH1106,
			"D_128x64_VCOMH0",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_sh1106_128x64_vcomh0_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_128x64_vcomh0_f", COM_I2C)
		);
		/** U8G2 Name: SH1106 128X64_WINSTAR **/
		GlcdDisplay D_128x64_WINSTAR = new GlcdDisplay(
			GlcdControllerType.SH1106,
			"D_128x64_WINSTAR",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_sh1106_128x64_winstar_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_128x64_winstar_f", COM_I2C)
		);
		/** U8G2 Name: SH1106 72X40_WISE **/
		GlcdDisplay D_72x40_WISE = new GlcdDisplay(
			GlcdControllerType.SH1106,
			"D_72x40_WISE",
			9,
			5,
			new GlcdSetupInfo("u8g2_Setup_sh1106_72x40_wise_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_72x40_wise_f", COM_I2C)
		);
		/** U8G2 Name: SH1106 64X32 **/
		GlcdDisplay D_64x32 = new GlcdDisplay(
			GlcdControllerType.SH1106,
			"D_64x32",
			8,
			4,
			new GlcdSetupInfo("u8g2_Setup_sh1106_64x32_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1106_i2c_64x32_f", COM_I2C)
		);
	}

	/** Controller Name: SH1107 **/
	interface SH1107 {
		/** U8G2 Name: SH1107 64X128 **/
		GlcdDisplay D_64x128 = new GlcdDisplay(
			GlcdControllerType.SH1107,
			"D_64x128",
			8,
			16,
			new GlcdSetupInfo("u8g2_Setup_sh1107_64x128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_64x128_f", COM_I2C)
		);
		/** U8G2 Name: SH1107 SEEED_96X96 **/
		GlcdDisplay D_96x96_SEEED = new GlcdDisplay(
			GlcdControllerType.SH1107,
			"D_96x96_SEEED",
			12,
			12,
			new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_seeed_96x96_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_sh1107_seeed_96x96_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: SH1107 128X128 **/
		GlcdDisplay D_128x128 = new GlcdDisplay(
			GlcdControllerType.SH1107,
			"D_128x128",
			16,
			16,
			new GlcdSetupInfo("u8g2_Setup_sh1107_128x128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1107_i2c_128x128_f", COM_I2C)
		);
	}

	/** Controller Name: SH1108 **/
	interface SH1108 {
		/** U8G2 Name: SH1108 160X160 **/
		GlcdDisplay D_160x160 = new GlcdDisplay(
			GlcdControllerType.SH1108,
			"D_160x160",
			20,
			20,
			new GlcdSetupInfo("u8g2_Setup_sh1108_160x160_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1108_i2c_160x160_f", COM_I2C)
		);
	}

	/** Controller Name: SH1122 **/
	interface SH1122 {
		/** U8G2 Name: SH1122 256X64 **/
		GlcdDisplay D_256x64 = new GlcdDisplay(
			GlcdControllerType.SH1122,
			"D_256x64",
			32,
			8,
			new GlcdSetupInfo("u8g2_Setup_sh1122_256x64_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_sh1122_i2c_256x64_f", COM_I2C)
		);
	}

    /** Controller Name: SSD0323 **/
    interface SSD0323 {
        /** U8G2 Name: SSD0323 OS128064 **/
        GlcdDisplay D_128x64_OS128064 = new GlcdDisplay(
                GlcdControllerType.SSD0323,
                "D_128x64_OS128064",
                16,
                8,
                new GlcdSetupInfo("u8g2_Setup_ssd0323_i2c_os128064_f", COM_I2C),
                new GlcdSetupInfo("u8g2_Setup_ssd0323_os128064_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
        );
    }

	/** Controller Name: SSD1305 **/
	interface SSD1305 {
		/** U8G2 Name: SSD1305 128X32_NONAME **/
		GlcdDisplay D_128x32_NONAME = new GlcdDisplay(
			GlcdControllerType.SSD1305,
			"D_128x32_NONAME",
			16,
			4,
			new GlcdSetupInfo("u8g2_Setup_ssd1305_128x32_noname_f", COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1305_i2c_128x32_noname_f", COM_I2C)
		);
		/** U8G2 Name: SSD1305 128X64_ADAFRUIT **/
		GlcdDisplay D_128x64_ADAFRUIT = new GlcdDisplay(
			GlcdControllerType.SSD1305,
			"D_128x64_ADAFRUIT",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1305_128x64_adafruit_f", COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1305_i2c_128x64_adafruit_f", COM_I2C)
		);
	}

	/** Controller Name: SSD1306 **/
	interface SSD1306 {
		/** U8G2 Name: SSD1306 128X64_NONAME **/
		GlcdDisplay D_128x64_NONAME = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_128x64_NONAME",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_128x64_noname_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x64_noname_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 128X64_VCOMH0 **/
		GlcdDisplay D_128x64_VCOMH0 = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_128x64_VCOMH0",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_128x64_vcomh0_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x64_vcomh0_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 128X64_ALT0 **/
		GlcdDisplay D_128x64_ALT0 = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_128x64_ALT0",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_128x64_alt0_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x64_alt0_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 128X32_UNIVISION **/
		GlcdDisplay D_128x32_UNIVISION = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_128x32_UNIVISION",
			16,
			4,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_128x32_univision_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_128x32_univision_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 64X48_ER **/
		GlcdDisplay D_64x48_ER = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_64x48_ER",
			8,
			6,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_64x48_er_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_64x48_er_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 48X64_WINSTAR **/
		GlcdDisplay D_48x64_WINSTAR = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_48x64_WINSTAR",
			6,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_48x64_winstar_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_48x64_winstar_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 64X32_NONAME **/
		GlcdDisplay D_64x32_NONAME = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_64x32_NONAME",
			8,
			4,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_64x32_noname_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_64x32_noname_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 64X32_1F **/
		GlcdDisplay D_64x32_1F = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_64x32_1F",
			8,
			4,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_64x32_1f_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_64x32_1f_f", COM_I2C)
		);
		/** U8G2 Name: SSD1306 96X16_ER **/
		GlcdDisplay D_96x16_ER = new GlcdDisplay(
			GlcdControllerType.SSD1306,
			"D_96x16_ER",
			12,
			2,
			new GlcdSetupInfo("u8g2_Setup_ssd1306_96x16_er_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1306_i2c_96x16_er_f", COM_I2C)
		);
	}

	/** Controller Name: SSD1309 **/
	interface SSD1309 {
		/** U8G2 Name: SSD1309 128X64_NONAME2 **/
		GlcdDisplay D_128x64_NONAME2 = new GlcdDisplay(
			GlcdControllerType.SSD1309,
			"D_128x64_NONAME2",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1309_128x64_noname2_f", COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1309_i2c_128x64_noname2_f", COM_I2C)
		);
		/** U8G2 Name: SSD1309 128X64_NONAME0 **/
		GlcdDisplay D_128x64_NONAME0 = new GlcdDisplay(
			GlcdControllerType.SSD1309,
			"D_128x64_NONAME0",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1309_128x64_noname0_f", COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1309_i2c_128x64_noname0_f", COM_I2C)
		);
	}

	/** Controller Name: SSD1317 **/
	interface SSD1317 {
		/** U8G2 Name: SSD1317 96X96 **/
		GlcdDisplay D_96x96 = new GlcdDisplay(
                GlcdControllerType.SSD1317,
                "D_96x96",
                12,
                12,
                new GlcdSetupInfo("u8g2_Setup_ssd1317_96x96_f", COM_4WSPI | COM_6800 | COM_8080),
                new GlcdSetupInfo("u8g2_Setup_ssd1317_i2c_96x96_f", COM_I2C)
		);
	}

	/** Controller Name: SSD1322 **/
	interface SSD1322 {
		/** U8G2 Name: SSD1322 NHD_256X64 **/
		GlcdDisplay D_256x64_NHD = new GlcdDisplay(
			GlcdControllerType.SSD1322,
			"D_256x64_NHD",
			32,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1322_nhd_256x64_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: SSD1322 NHD_128X64 **/
		GlcdDisplay D_128x64_NHD = new GlcdDisplay(
			GlcdControllerType.SSD1322,
			"D_128x64_NHD",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1322_nhd_128x64_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: SSD1325 **/
	interface SSD1325 {
		/** U8G2 Name: SSD1325 NHD_128X64 **/
		GlcdDisplay D_128x64_NHD = new GlcdDisplay(
			GlcdControllerType.SSD1325,
			"D_128x64_NHD",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_ssd1325_i2c_nhd_128x64_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_ssd1325_nhd_128x64_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: SSD1326 **/
	interface SSD1326 {
		/** U8G2 Name: SSD1326 ER_256X32 **/
		GlcdDisplay D_256x32_ER = new GlcdDisplay(
			GlcdControllerType.SSD1326,
			"D_256x32_ER",
			32,
			4,
			new GlcdSetupInfo("u8g2_Setup_ssd1326_er_256x32_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1326_i2c_er_256x32_f", COM_I2C)
		);
	}

	/** Controller Name: SSD1327 **/
	interface SSD1327 {
		/** U8G2 Name: SSD1327 SEEED_96X96 **/
		GlcdDisplay D_96x96_SEEED = new GlcdDisplay(
			GlcdControllerType.SSD1327,
			"D_96x96_SEEED",
			12,
			12,
			new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_seeed_96x96_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_ssd1327_seeed_96x96_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: SSD1327 EA_W128128 **/
		GlcdDisplay D_128x128_EAW128128 = new GlcdDisplay(
			GlcdControllerType.SSD1327,
			"D_128x128_EAW128128",
			16,
			16,
			new GlcdSetupInfo("u8g2_Setup_ssd1327_ea_w128128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_ea_w128128_f", COM_I2C)
		);
		/** U8G2 Name: SSD1327 MIDAS_128X128 **/
		GlcdDisplay D_128x128_MIDAS = new GlcdDisplay(
			GlcdControllerType.SSD1327,
			"D_128x128_MIDAS",
			16,
			16,
			new GlcdSetupInfo("u8g2_Setup_ssd1327_i2c_midas_128x128_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_ssd1327_midas_128x128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: SSD1329 **/
	interface SSD1329 {
		/** U8G2 Name: SSD1329 128X96_NONAME **/
		GlcdDisplay D_128x96_NONAME = new GlcdDisplay(
			GlcdControllerType.SSD1329,
			"D_128x96_NONAME",
			16,
			12,
			new GlcdSetupInfo("u8g2_Setup_ssd1329_128x96_noname_f", COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: SSD1606 **/
	interface SSD1606 {
		/** U8G2 Name: SSD1606 172X72 **/
		GlcdDisplay D_176x72 = new GlcdDisplay(
			GlcdControllerType.SSD1606,
			"D_176x72",
			22,
			9,
			new GlcdSetupInfo("u8g2_Setup_ssd1606_172x72_f", COM_3WSPI | COM_4WSPI)
		);
	}

	/** Controller Name: SSD1607 **/
	interface SSD1607 {
		/** U8G2 Name: SSD1607 200X200 **/
		GlcdDisplay D_200x200 = new GlcdDisplay(
			GlcdControllerType.SSD1607,
			"D_200x200",
			25,
			25,
			new GlcdSetupInfo("u8g2_Setup_ssd1607_200x200_f", COM_3WSPI | COM_4WSPI)
		);
		/** U8G2 Name: SSD1607 GD_200X200 **/
		GlcdDisplay D_200x200_GD = new GlcdDisplay(
			GlcdControllerType.SSD1607,
			"D_200x200_GD",
			25,
			25,
			new GlcdSetupInfo("u8g2_Setup_ssd1607_gd_200x200_f", COM_3WSPI | COM_4WSPI)
		);
	}

	/** Controller Name: ST75256 **/
	interface ST75256 {
		/** U8G2 Name: ST75256 JLX256128 **/
		GlcdDisplay D_256x128_JLX256128 = new GlcdDisplay(
			GlcdControllerType.ST75256,
			"D_256x128_JLX256128",
			32,
			16,
			new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx256128_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_st75256_jlx256128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST75256 JLX256160 **/
		GlcdDisplay D_256x160_JLX256160 = new GlcdDisplay(
			GlcdControllerType.ST75256,
			"D_256x160_JLX256160",
			32,
			20,
			new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx256160_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_st75256_jlx256160_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST75256 JLX240160 **/
		GlcdDisplay D_240x160_JLX240160 = new GlcdDisplay(
			GlcdControllerType.ST75256,
			"D_240x160_JLX240160",
			30,
			20,
			new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx240160_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_st75256_jlx240160_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST75256 JLX25664 **/
		GlcdDisplay D_256x64_JLX25664 = new GlcdDisplay(
			GlcdControllerType.ST75256,
			"D_256x64_JLX25664",
			32,
			8,
			new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx25664_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_st75256_jlx25664_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST75256 JLX172104 **/
		GlcdDisplay D_176x104_JLX172104 = new GlcdDisplay(
			GlcdControllerType.ST75256,
			"D_176x104_JLX172104",
			22,
			13,
			new GlcdSetupInfo("u8g2_Setup_st75256_i2c_jlx172104_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_st75256_jlx172104_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: ST7565 **/
	interface ST7565 {
		/** U8G2 Name: ST7565 EA_DOGM128 **/
		GlcdDisplay D_128x64_EADOGM128 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_EADOGM128",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_ea_dogm128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 64128N **/
		GlcdDisplay D_128x64_64128N = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_64128N",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_64128n_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 ZOLEN_128X64 **/
		GlcdDisplay D_128x64_ZOLEN = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_ZOLEN",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_zolen_128x64_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 LM6059 **/
		GlcdDisplay D_128x64_LM6059 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_LM6059",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_lm6059_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 LX12864 **/
		GlcdDisplay D_128x64_LX12864 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_LX12864",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_lx12864_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 ERC12864 **/
		GlcdDisplay D_128x64_ERC12864 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_ERC12864",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_erc12864_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 NHD_C12864 **/
		GlcdDisplay D_128x64_NHDC12864 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_NHDC12864",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_nhd_c12864_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 JLX12864 **/
		GlcdDisplay D_128x64_JLX12864 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x64_JLX12864",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7565_jlx12864_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 NHD_C12832 **/
		GlcdDisplay D_128x32_NHDC12832 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_128x32_NHDC12832",
			16,
			4,
			new GlcdSetupInfo("u8g2_Setup_st7565_nhd_c12832_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7565 EA_DOGM132 **/
		GlcdDisplay D_136x32_EADOGM132 = new GlcdDisplay(
			GlcdControllerType.ST7565,
			"D_136x32_EADOGM132",
			17,
			4,
			new GlcdSetupInfo("u8g2_Setup_st7565_ea_dogm132_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: ST7567 **/
	interface ST7567 {
		/** U8G2 Name: ST7567 PI_132X64 **/
		GlcdDisplay D_136x64_PI = new GlcdDisplay(
			GlcdControllerType.ST7567,
			"D_136x64_PI",
			17,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7567_pi_132x64_f", COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7567 JLX12864 **/
		GlcdDisplay D_128x64_JLX12864 = new GlcdDisplay(
			GlcdControllerType.ST7567,
			"D_128x64_JLX12864",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7567_jlx12864_f", COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7567 ENH_DG128064 **/
		GlcdDisplay D_128x64_ENHDG128064 = new GlcdDisplay(
			GlcdControllerType.ST7567,
			"D_128x64_ENHDG128064",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7567_enh_dg128064_f", COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7567 ENH_DG128064I **/
		GlcdDisplay D_128x64_ENHDG128064I = new GlcdDisplay(
			GlcdControllerType.ST7567,
			"D_128x64_ENHDG128064I",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7567_enh_dg128064i_f", COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: ST7567 64X32 **/
		GlcdDisplay D_64x32 = new GlcdDisplay(
                GlcdControllerType.ST7567,
                "D_64x32",
                8,
                4,
                new GlcdSetupInfo("u8g2_Setup_st7567_64x32_f", COM_4WSPI | COM_6800 | COM_8080),
                new GlcdSetupInfo("u8g2_Setup_st7567_i2c_64x32_f", COM_I2C)
		);
	}

	/** Controller Name: ST7586S **/
	interface ST7586S {
		/** U8G2 Name: ST7586S S028HN118A **/
		GlcdDisplay D_384x136_S028HN118A = new GlcdDisplay(
			GlcdControllerType.ST7586S,
			"D_384x136_S028HN118A",
			48,
			17,
			new GlcdSetupInfo("u8g2_Setup_st7586s_s028hn118a_f", COM_4WSPI)
		);
		/** U8G2 Name: ST7586S ERC240160 **/
		GlcdDisplay D_240x160_ERC240160 = new GlcdDisplay(
			GlcdControllerType.ST7586S,
			"D_240x160_ERC240160",
			30,
			20,
			new GlcdSetupInfo("u8g2_Setup_st7586s_erc240160_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: ST7588 **/
	interface ST7588 {
		/** U8G2 Name: ST7588 JLX12864 **/
		GlcdDisplay D_128x64_JLX12864 = new GlcdDisplay(
			GlcdControllerType.ST7588,
			"D_128x64_JLX12864",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7588_i2c_jlx12864_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_st7588_jlx12864_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: ST7920 **/
	interface ST7920 {
		/** U8G2 Name: ST7920 192X32 **/
		GlcdDisplay D_192x32 = new GlcdDisplay(
			GlcdControllerType.ST7920,
			"D_192x32",
			24,
			4,
			new GlcdSetupInfo("u8g2_Setup_st7920_192x32_f", COM_6800),
			new GlcdSetupInfo("u8g2_Setup_st7920_p_192x32_f", COM_8080),
			new GlcdSetupInfo("u8g2_Setup_st7920_s_192x32_f", COM_ST7920SPI)
		);
		/** U8G2 Name: ST7920 128X64 **/
		GlcdDisplay D_128x64 = new GlcdDisplay(
			GlcdControllerType.ST7920,
			"D_128x64",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_st7920_128x64_f", COM_6800),
			new GlcdSetupInfo("u8g2_Setup_st7920_p_128x64_f", COM_8080),
			new GlcdSetupInfo("u8g2_Setup_st7920_s_128x64_f", COM_ST7920SPI)
		);
	}

	/** Controller Name: T6963 **/
	interface T6963 {
		/** U8G2 Name: T6963 240X128 **/
		GlcdDisplay D_240x128 = new GlcdDisplay(
			GlcdControllerType.T6963,
			"D_240x128",
			30,
			16,
			new GlcdSetupInfo("u8g2_Setup_t6963_240x128_f", COM_8080)
		);
		/** U8G2 Name: T6963 240X64 **/
		GlcdDisplay D_240x64 = new GlcdDisplay(
			GlcdControllerType.T6963,
			"D_240x64",
			30,
			8,
			new GlcdSetupInfo("u8g2_Setup_t6963_240x64_f", COM_8080)
		);
		/** U8G2 Name: T6963 256X64 **/
		GlcdDisplay D_256x64 = new GlcdDisplay(
			GlcdControllerType.T6963,
			"D_256x64",
			32,
			8,
			new GlcdSetupInfo("u8g2_Setup_t6963_256x64_f", COM_8080)
		);
		/** U8G2 Name: T6963 128X64 **/
		GlcdDisplay D_128x64 = new GlcdDisplay(
			GlcdControllerType.T6963,
			"D_128x64",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_t6963_128x64_f", COM_8080)
		);
		/** U8G2 Name: T6963 160X80 **/
		GlcdDisplay D_160x80 = new GlcdDisplay(
			GlcdControllerType.T6963,
			"D_160x80",
			20,
			10,
			new GlcdSetupInfo("u8g2_Setup_t6963_160x80_f", COM_8080)
		);
	}

	/** Controller Name: UC1601 **/
	interface UC1601 {
		/** U8G2 Name: UC1601 128X32 **/
		GlcdDisplay D_128x32 = new GlcdDisplay(
			GlcdControllerType.UC1601,
			"D_128x32",
			16,
			4,
			new GlcdSetupInfo("u8g2_Setup_uc1601_128x32_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1601_i2c_128x32_f", COM_I2C)
		);
	}

	/** Controller Name: UC1604 **/
	interface UC1604 {
		/** U8G2 Name: UC1604 JLX19264 **/
		GlcdDisplay D_192x64_JLX19264 = new GlcdDisplay(
			GlcdControllerType.UC1604,
			"D_192x64_JLX19264",
			24,
			8,
			new GlcdSetupInfo("u8g2_Setup_uc1604_i2c_jlx19264_f", COM_I2C),
			new GlcdSetupInfo("u8g2_Setup_uc1604_jlx19264_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: UC1608 **/
	interface UC1608 {
		/** U8G2 Name: UC1608 ERC24064 **/
		GlcdDisplay D_240x64_ERC24064 = new GlcdDisplay(
			GlcdControllerType.UC1608,
			"D_240x64_ERC24064",
			30,
			8,
			new GlcdSetupInfo("u8g2_Setup_uc1608_erc24064_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1608_i2c_erc24064_f", COM_I2C)
		);
		/** U8G2 Name: UC1608 ERC240120 **/
		GlcdDisplay D_240x120_ERC240120 = new GlcdDisplay(
			GlcdControllerType.UC1608,
			"D_240x120_ERC240120",
			30,
			15,
			new GlcdSetupInfo("u8g2_Setup_uc1608_erc240120_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1608_i2c_erc240120_f", COM_I2C)
		);
		/** U8G2 Name: UC1608 240X128 **/
		GlcdDisplay D_240x128 = new GlcdDisplay(
			GlcdControllerType.UC1608,
			"D_240x128",
			30,
			16,
			new GlcdSetupInfo("u8g2_Setup_uc1608_240x128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1608_i2c_240x128_f", COM_I2C)
		);
	}

	/** Controller Name: UC1610 **/
	interface UC1610 {
		/** U8G2 Name: UC1610 EA_DOGXL160 **/
		GlcdDisplay D_160x104_EADOGXL160 = new GlcdDisplay(
			GlcdControllerType.UC1610,
			"D_160x104_EADOGXL160",
			20,
			13,
			new GlcdSetupInfo("u8g2_Setup_uc1610_ea_dogxl160_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1610_i2c_ea_dogxl160_f", COM_I2C)
		);
	}

	/** Controller Name: UC1611 **/
	interface UC1611 {
		/** U8G2 Name: UC1611 EA_DOGM240 **/
		GlcdDisplay D_240x64_EADOGM240 = new GlcdDisplay(
			GlcdControllerType.UC1611,
			"D_240x64_EADOGM240",
			30,
			8,
			new GlcdSetupInfo("u8g2_Setup_uc1611_ea_dogm240_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1611_i2c_ea_dogm240_f", COM_I2C)
		);
		/** U8G2 Name: UC1611 EA_DOGXL240 **/
		GlcdDisplay D_240x128_EADOGXL240 = new GlcdDisplay(
			GlcdControllerType.UC1611,
			"D_240x128_EADOGXL240",
			30,
			16,
			new GlcdSetupInfo("u8g2_Setup_uc1611_ea_dogxl240_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1611_i2c_ea_dogxl240_f", COM_I2C)
		);
		/** U8G2 Name: UC1611 EW50850 **/
		GlcdDisplay D_240x160_EW50850 = new GlcdDisplay(
			GlcdControllerType.UC1611,
			"D_240x160_EW50850",
			30,
			20,
			new GlcdSetupInfo("u8g2_Setup_uc1611_ew50850_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080),
			new GlcdSetupInfo("u8g2_Setup_uc1611_i2c_ew50850_f", COM_I2C)
		);
	}

	/** Controller Name: UC1638 **/
	interface UC1638 {
		/** U8G2 Name: UC1638 160X128 **/
		GlcdDisplay D_160x128 = new GlcdDisplay(
			GlcdControllerType.UC1638,
			"D_160x128",
			20,
			16,
			new GlcdSetupInfo("u8g2_Setup_uc1638_160x128_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

	/** Controller Name: UC1701 **/
	interface UC1701 {
		/** U8G2 Name: UC1701 EA_DOGS102 **/
		GlcdDisplay D_104x64_EADOGS102 = new GlcdDisplay(
			GlcdControllerType.UC1701,
			"D_104x64_EADOGS102",
			13,
			8,
			new GlcdSetupInfo("u8g2_Setup_uc1701_ea_dogs102_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
		/** U8G2 Name: UC1701 MINI12864 **/
		GlcdDisplay D_128x64_MINI12864 = new GlcdDisplay(
			GlcdControllerType.UC1701,
			"D_128x64_MINI12864",
			16,
			8,
			new GlcdSetupInfo("u8g2_Setup_uc1701_mini12864_f", COM_3WSPI | COM_4WSPI | COM_6800 | COM_8080)
		);
	}

}
