/*
 * THIS IS AN AUTO-GENERATED CODE!! PLEASE DO NOT MODIFY (Last updated: Oct 10 2018 01:02:14) 
 */

#ifndef PIDISP_CONTROLLERS_H
#define PIDISP_CONTROLLERS_H
/* display_controller_list_start */

struct controller controller_list[] =
{
  {
    "ssd1305", 	16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x32_noname" },
      { NULL }
    }
  },
  {
    "ssd1305", 	16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x32_noname" },
      { NULL }
    }
  },

  {
    "ssd1305", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_adafruit" },
      { NULL }
    }
  },
  {
    "ssd1305", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_adafruit" },
      { NULL }
    }
  },
  
  {
    "ssd1306", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname" },
      { "128x64_vcomh0" },
      { "128x64_alt0" },
      { NULL }
    }
  },
  {
    "ssd1306", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname" },
      { "128x64_vcomh0" },
      { "128x64_alt0" },
      { NULL }
    }
  },

   {
    "sh1106", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname" },
      { "128x64_vcomh0" },
      { "128x64_winstar" },
      { NULL }
    }
  },
  {
    "sh1106", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname" },
      { "128x64_vcomh0" },
      { "128x64_winstar" },
      { NULL }
    }
  },

   {
    "sh1106", 	9, 	5, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "72x40_wise" },
      { NULL }
    }
  },
  {
    "sh1106", 	9, 	5, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "72x40_wise" },
      { NULL }
    }
  },

   {
    "sh1106", 	8, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "64x32" },
      { NULL }
    }
  },
  {
    "sh1106", 	8, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "64x32" },
      { NULL }
    }
  },
  
   {
    "sh1107", 	 8, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "64x128" },
      { NULL }
    }
  },
  {
    "sh1107", 	 8, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "64x128" },
      { NULL }
    }
  },
  
   {
    "sh1107", 	12, 	12, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "seeed_96x96" },
      { NULL }
    }
  },
  {
    "sh1107", 	12, 	12, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "seeed_96x96" },
      { NULL }
    }
  },

   {
    "sh1107", 	 16, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "128x128" },
      { NULL }
    }
  },
  {
    "sh1107", 	 16, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "128x128" },
      { NULL }
    }
  },

   {
    "sh1108", 	 20, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "160x160" },
      { NULL }
    }
  },
  {
    "sh1108", 	 20, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "160x160" },
      { NULL }
    }
  },
  
   {
    "sh1122", 	 32, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "256x64" },
      { NULL }
    }
  },
  {
    "sh1122", 	 32, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "256x64" },
      { NULL }
    }
  },

  {
    "ssd1306", 	16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x32_univision" },
      { NULL }
    }
  },
  {
    "ssd1306", 	16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x32_univision" },
      { NULL }
    }
  },  

  {
    "ssd1306", 	8, 	6, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "64x48_er" },
      { NULL }
    }
  },
  {
    "ssd1306", 	8, 	6, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "64x48_er" },
      { NULL }
    }
  },  
  
  {
    "ssd1306", 	6, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "48x64_winstar" },
      { NULL }
    }
  },
  {
    "ssd1306", 	6, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "48x64_winstar" },
      { NULL }
    }
  },  


  {
    "ssd1306", 	8, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "64x32_noname" },
      { "64x32_1f" },
      { NULL }
    }
  },
  {
    "ssd1306", 	8, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "64x32_noname" },
      { "64x32_1f" },
      { NULL }
    }
  }, 

  {
    "ssd1306", 	12, 	2, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "96x16_er" },
      { NULL }
    }
  },
  {
    "ssd1306", 	12, 	2, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "96x16_er" },
      { NULL }
    }
  },  
  
  
  {
    "ssd1309", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname2" },
      { NULL }
    }
  },
  {
    "ssd1309", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname2" },
      { NULL }
    }
  },
  

  {
    "ssd1309", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname0" },
      { NULL }
    }
  },
  {
    "ssd1309", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64_noname0" },
      { NULL }
    }
  },

  {
    "ssd1317", 	12, 	12, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "96x96" },
      { NULL }
    }
  },
  {
    "ssd1317", 	12, 	12, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "96x96" },
      { NULL }
    }
  },
  
  {
    "ssd1325", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "nhd_128x64" },
      { NULL }
    }
  },
  {
    "ssd1325", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "nhd_128x64" },
      { NULL }
    }
  },  

  {
    "ssd0323", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "os128064" },
      { NULL }
    }
  },
  {
    "ssd0323", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "os128064" },
      { NULL }
    }
  },  


  {
    "ssd1326", 	32, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "er_256x32" },
      { NULL }
    }
  },
  {
    "ssd1326", 	32, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "er_256x32" },
      { NULL }
    }
  },  
  

  {
    "ssd1327", 	12, 	12, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "seeed_96x96" },
      { NULL }
    }
  },
  {
    "ssd1327", 	12, 	12, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "seeed_96x96" },
      { NULL }
    }
  },  

  {
    "ssd1327", 	16, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_w128128" },
      { "midas_128x128" },
      { NULL }
    }
  },
  {
    "ssd1327", 	16, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_w128128" },
      { "midas_128x128" },
      { NULL }
    }
  },  

  {
    "ssd1329", 	16, 	12, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x96_noname" },
      { NULL }
    }
  },

  
  {
    "ld7032", 	8, 	4, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_011", "", COM_4WSPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "60x32" },
      { NULL }
    }
  },
  {
    "ld7032", 	8, 	4, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_ld7032_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "60x32" },
      { NULL }
    }
  },
  {
    "st7920", 	24, 	4, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_001", "p", COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "192x32" },
      { NULL }
    }
  },
  {
    "st7920", 	24, 	4, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_001", "", COM_6800,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "192x32" },
      { NULL }
    }
  },
  {
    "st7920", 	24, 	4, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_st7920_spi", "s", COM_ST7920SPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "192x32" },
      { NULL }
    }
  },
  {
    "st7920", 	16, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_001", "p", COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64" },
      { NULL }
    }
  },
  {
    "st7920", 	16, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_001", "", COM_6800,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64" },
      { NULL }
    }
  },
  {
    "st7920", 	16, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_st7920_spi", "s", COM_ST7920SPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64" },
      { NULL }
    }
  },
  {
    "ls013b7dh03", 	16, 	16, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_001", "", COM_4WSPI,		/* cad procedure is not required (no DC for this display) so it could be a dummy procedure here */
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x128" },
      { NULL }
    }
  },
  {
    "ls027b7dh01", 	50, 	30, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_001", "", COM_4WSPI,		/* cad procedure is not required (no DC for this display) so it could be a dummy procedure here */
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "400x240" },
      { NULL }
    }
  },
  {
    "uc1701", 		13, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogs102" },
      { NULL }
    }
  },
  {
    "uc1701", 		16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "mini12864" },
      { NULL }
    }
  },
  {
    "pcd8544", 		11, 	6, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI,
    "No HW flip", /* is_generate_u8g2_class= */ 1,
    {
      { "84x48" },
      { NULL }
    }
  },
  {
    "pcf8812", 		12, 	9, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI,
    "No HW flip", /* is_generate_u8g2_class= */ 1,
    {
      { "96x65" },
      { NULL }
    }
  },
  {
    "hx1230", 		12, 	9, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI,
    "No HW flip", /* is_generate_u8g2_class= */ 1,
    {
      { "96x68" },
      { NULL }
    }
  },  
  {
    "uc1604", 	24, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx19264" },
      { NULL }
    }
  },  
  {
    "uc1604", 	24, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx19264" },
      { NULL }
    }
  },  
  {
    "uc1608", 	30, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "erc24064" },
      { NULL }
    }
  },  
  {
    "uc1608", 	30, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "erc24064" },
      { NULL }
    }
  },  
  {
    "uc1608", 	30, 	15, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "erc240120" },
      { NULL }
    }
  },  
  {
    "uc1608", 	30, 	15, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "erc240120" },
      { NULL }
    }
  },  
  {
    "uc1608", 	30, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "240x128" },
      { NULL }
    }
  },  
  {
    "uc1608", 	30, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "240x128" },
      { NULL }
    }
  },  

  {
    "uc1638", 	20, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "160x128" },
      { NULL }
    }
  },  
  //{
	/* this device requires cd=1 for arguments, not clear whether the u8x8_cad_uc16xx_i2c works */
    //"uc1638", 	20, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    //"", /* is_generate_u8g2_class= */ 1,
    //{
     // { "160x128" },
     // { NULL }
    //}
  //},  
  
  {
    "uc1610", 		20, 	13, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "3W SPI not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogxl160" },
      { NULL }
    }
  },  
  {
    "uc1610", 		20, 	13, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "3W SPI not tested, I2C not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogxl160" },
      { NULL }
    }
  },  
  {
    "uc1611", 	30, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogm240" },
      { NULL }
    }
  },  
  {
    "uc1611", 	30, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogm240" },
      { NULL }
    }
  },  
  {
    "uc1611", 	30, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogxl240" },
      { NULL }
    }
  },  
  {
    "uc1611", 	30, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogxl240" },
      { NULL }
    }
  },  
  {
    "uc1611", 	30, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "240x160, active high chip select", /* is_generate_u8g2_class= */ 1,
    {
      { "ew50850" },		/* 240x160 */
      { NULL }
    }
  },  
  {
    "uc1611", 	30, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "240x160, active high chip select", /* is_generate_u8g2_class= */ 1,
    {
      { "ew50850" },		/* 240x160 */
      { NULL }
    }
  },  
  
#ifdef NOTUSED
  {
    "uc1617", 	16, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx128128" },
      { NULL }
    }
  },  
  {
    "uc1617", 	16, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx128128" },
      { NULL }
    }
  },  
#endif
  
  {
    "st7565", 		16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogm128" },
      { "64128n" },       
      { "zolen_128x64" },
      { "lm6059" },
      { "lx12864" },
      { "erc12864" },
      { "nhd_c12864" },
      { "jlx12864" },
      { NULL }
    },
  },
  {
    "st7565", 		16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "nhd_c12832" },
      { NULL }
    }
  },

  {
    "uc1601", 		16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x32" },
      { NULL }
    }
  },
  {
    "uc1601", 	16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_uc16xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x32" },
      { NULL }
    }
  },  
  
  {
    "st7565", 		17, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "ea_dogm132" },
      { NULL }
    }
  },
  {
    "st7567", 		17, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "pi_132x64" },
      { NULL }
    }
  },

  {
    "st7567", 		16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx12864" },
      { "enh_dg128064" },
      { "enh_dg128064i" },
      { NULL }
    }
  },
  
  {
    "st7567", 		8, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "64x32" },
      { NULL }
    }
  },
  {
    "st7567", 		8, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "64x32" },
      { NULL }
    }
  },
  
  
  
  {
    "st7586s", 		48, 	17, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_011", "", COM_4WSPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "s028hn118a" },
      { NULL }
    },
  },
  
  {
    "st7586s", 		30, 	20, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "erc240160" },
      { NULL }
    },
  },
    
  {
    "st7588", 		16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx12864" },
      { NULL }
    },
  },
  {  /* the ST7588 has the same I2C protocol as the SSD13xx */
    "st7588", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_ssd13xx_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx12864" },
      { NULL }
    }
  },  

  {
    "st75256", 		32, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx256128" },
      { NULL }
    },
  },
  /* the ST75256 has the same I2C protocol as the SSD13xx, BUT: for arguments have the data bit set!!!! */
  /* this means, we need to implement a u8x8_cad_ssd13xx_i2c procedure with cad 011 functionality */
  /* done: u8x8_cad_st75256_i2c */
  {  
    "st75256", 	32, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_st75256_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx256128" },
      { NULL }
    }
  },  

 {
    "st75256", 		32, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx256160" },
      { NULL }
    },
  },
  /* the ST75256 has the same I2C protocol as the SSD13xx, BUT: for arguments have the data bit set!!!! */
  /* this means, we need to implement a u8x8_cad_ssd13xx_i2c procedure with cad 011 functionality */
  /* done: u8x8_cad_st75256_i2c */
  {  
    "st75256", 	32, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_st75256_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx256160" },
      { NULL }
    }
  },  

  
  {
    "st75256", 		30, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx240160" },
      { NULL }
    },
  },
  {  
    "st75256", 	30, 	20, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_st75256_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx240160" },
      { NULL }
    }
  },  

  {
    "st75256", 		32, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx25664" },
      { NULL }
    },
  },
  {  
    "st75256", 	32, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_st75256_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx25664" },
      { NULL }
    }
  },  
  
  {
    "st75256", 		22, 	13, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx172104" },
      { NULL }
    },
  },
  
  {
    "st75256", 		22, 	13, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_st75256_i2c", "i2c", COM_I2C,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "jlx172104" },
      { NULL }
    },
  },

  {
    "nt7534", 		16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "tg12864r" },
      { NULL }
    }
  },
  {
    "ist3020", 		24, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_4WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "erc19264" },
      { NULL }
    }
  },

  {
    "sbn1661", 		16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_SED1520,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "122x32" },
      { NULL }
    },
  },
  {
    "sed1520", 		16, 	4, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_SED1520,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "122x32" },
      { NULL }
    },
  },
  
  {
    "ks0108", 		16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_KS0108,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64" },
      { NULL }
    },
  },
  {
    "ks0108", 		24, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_001", "", COM_KS0108,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "erm19264" },
      { NULL }
    },
  },
  {
    "lc7981", 	20, 	10, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_6800,
    "U8x8 not supported, no powerdown, no HW flip, no constrast", /* is_generate_u8g2_class= */ 1,
    {
      { "160x80" },
      { NULL }
    }
  },
  {
    "lc7981", 	20, 	20, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_6800,
    "U8x8 not supported, no powerdown, no HW flip, no constrast", /* is_generate_u8g2_class= */ 1,
    {
      { "160x160" },
      { NULL }
    }
  },
  {
    "lc7981", 	30, 	16, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_6800,
    "U8x8 not supported, no powerdown, no HW flip, no constrast", /* is_generate_u8g2_class= */ 1,
    {
      { "240x128" },
      { NULL }
    }
  },
  {
    "lc7981", 	30, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_6800,
    "U8x8 not supported, no powerdown, no HW flip, no constrast", /* is_generate_u8g2_class= */ 1,
    {
      { "240x64" },
      { NULL }
    }
  },
  
  {
    "t6963", 	30, 	16, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "240x128" },
      { NULL }
    }
  },
  {
    "t6963", 	30, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "240x64" },
      { NULL }
    }
  },
  {
    "t6963", 	32, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "256x64" },
      { NULL }
    }
  },
  {
    "t6963", 	16, 	8, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "128x64" },
      { NULL }
    }
  },
  {
    "t6963", 	20, 	10, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_8080,
    "Not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "160x80" },
      { NULL }
    }
  },
  {
    "ssd1322", 	32, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "Requires U8G2_16BIT (see u8g2.h)", /* is_generate_u8g2_class= */ 1,
    {
      { "nhd_256x64" },
      { NULL }
    }
  },
  {
    "ssd1322", 	16, 	8, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI|COM_6800|COM_8080,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "nhd_128x64" },
      { NULL }
    }
  },
  {
    "ssd1606", 	22, 	9, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI,
    "Partly supported by U8x8, no HW flip, no contrast setting", /* is_generate_u8g2_class= */ 1,
    {
      { "172x72" },
      { NULL }
    }
  },
  {
    "ssd1607", 	25, 	25, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI,
    "Partly supported by U8x8, no HW flip, no contrast setting, v2 includes an optimized LUT", /* is_generate_u8g2_class= */ 1,
    {
      { "200x200" },
      { "gd_200x200" },	// GDEP015OC1
      //{ "v2_200x200" },
      { NULL }
    }
  },
  {
    "il3820", 	37, 	16, 	"u8g2_ll_hvline_vertical_top_lsb", "u8x8_cad_011", "", COM_4WSPI|COM_3WSPI,
    "Partly supported by U8x8, no HW flip, no contrast setting, V2 produces lesser screen-flicker", /* is_generate_u8g2_class= */ 1,
    {
      { "296x128" },
      { "v2_296x128" },
      { NULL }
    }
  },
  {
    "sed1330", 	30, 	16, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_6800|COM_8080,
    "Not tested, might work for RA8835 and SED1335 also", /* is_generate_u8g2_class= */ 1,
    {
      { "240x128" },
      { NULL }
    }
  },
  {
    "ra8835", 	30, 	16, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_6800|COM_8080,
    "Tested with RA8835", /* is_generate_u8g2_class= */ 1,
    {
      { "nhd_240x128" },
      { NULL }
    }
  },
  {
    "ra8835", 	40, 	30, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_100", "", COM_6800|COM_8080,
    "not tested", /* is_generate_u8g2_class= */ 1,
    {
      { "320x240" },
      { NULL }
    }
  },
  {
    "max7219", 	8, 	1, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_empty", "", COM_4WSPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "64x8" },
      { NULL }
    }
  },
  {
    "max7219", 	4, 	1, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_empty", "", COM_4WSPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "32x8" },
      { NULL }
    }
  },
  {
    "max7219", 	1, 	1, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_empty", "", COM_4WSPI,
    "", /* is_generate_u8g2_class= */ 1,
    {
      { "8x8" },
      { NULL }
    }
  },
  {
    "a2printer", 	48, 	30, 	"u8g2_ll_hvline_horizontal_right_lsb", "u8x8_cad_empty", "", COM_UART,
    "", /* is_generate_u8g2_class= */ 0,
    {
      { "384x240" },
      { NULL }
    }
  }
};

#endif //PIDISP_CONTROLLERS_H
