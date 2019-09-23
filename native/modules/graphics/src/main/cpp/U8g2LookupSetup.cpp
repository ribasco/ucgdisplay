/*
 * THIS IS AN AUTO-GENERATED CODE!! PLEASE DO NOT MODIFY (Last updated: Sep 23 2019 16:01:36) 
 */

#include "U8g2Hal.h"
#include <iostream>

void u8g2hal_InitSetupFunctions(u8g2_setup_func_map_t &setup_map) {
	setup_map.clear();
	setup_map["u8g2_Setup_a2printer_384x240_f"] = u8g2_Setup_a2printer_384x240_f;
	setup_map["u8g2_Setup_hx1230_96x68_f"] = u8g2_Setup_hx1230_96x68_f;
	setup_map["u8g2_Setup_il3820_296x128_f"] = u8g2_Setup_il3820_296x128_f;
	setup_map["u8g2_Setup_il3820_v2_296x128_f"] = u8g2_Setup_il3820_v2_296x128_f;
	setup_map["u8g2_Setup_ist3020_erc19264_f"] = u8g2_Setup_ist3020_erc19264_f;
	setup_map["u8g2_Setup_ks0108_128x64_f"] = u8g2_Setup_ks0108_128x64_f;
	setup_map["u8g2_Setup_ks0108_erm19264_f"] = u8g2_Setup_ks0108_erm19264_f;
	setup_map["u8g2_Setup_lc7981_160x160_f"] = u8g2_Setup_lc7981_160x160_f;
	setup_map["u8g2_Setup_lc7981_160x80_f"] = u8g2_Setup_lc7981_160x80_f;
	setup_map["u8g2_Setup_lc7981_240x128_f"] = u8g2_Setup_lc7981_240x128_f;
	setup_map["u8g2_Setup_lc7981_240x64_f"] = u8g2_Setup_lc7981_240x64_f;
	setup_map["u8g2_Setup_ld7032_60x32_f"] = u8g2_Setup_ld7032_60x32_f;
	setup_map["u8g2_Setup_ld7032_i2c_60x32_f"] = u8g2_Setup_ld7032_i2c_60x32_f;
	setup_map["u8g2_Setup_ls013b7dh03_128x128_f"] = u8g2_Setup_ls013b7dh03_128x128_f;
	setup_map["u8g2_Setup_ls027b7dh01_400x240_f"] = u8g2_Setup_ls027b7dh01_400x240_f;
	setup_map["u8g2_Setup_max7219_32x8_f"] = u8g2_Setup_max7219_32x8_f;
	setup_map["u8g2_Setup_max7219_64x8_f"] = u8g2_Setup_max7219_64x8_f;
	setup_map["u8g2_Setup_max7219_8x8_f"] = u8g2_Setup_max7219_8x8_f;
	setup_map["u8g2_Setup_nt7534_tg12864r_f"] = u8g2_Setup_nt7534_tg12864r_f;
	setup_map["u8g2_Setup_pcd8544_84x48_f"] = u8g2_Setup_pcd8544_84x48_f;
	setup_map["u8g2_Setup_pcf8812_96x65_f"] = u8g2_Setup_pcf8812_96x65_f;
	setup_map["u8g2_Setup_ra8835_320x240_f"] = u8g2_Setup_ra8835_320x240_f;
	setup_map["u8g2_Setup_ra8835_nhd_240x128_f"] = u8g2_Setup_ra8835_nhd_240x128_f;
	setup_map["u8g2_Setup_sbn1661_122x32_f"] = u8g2_Setup_sbn1661_122x32_f;
	setup_map["u8g2_Setup_sed1330_240x128_f"] = u8g2_Setup_sed1330_240x128_f;
	setup_map["u8g2_Setup_sed1520_122x32_f"] = u8g2_Setup_sed1520_122x32_f;
	setup_map["u8g2_Setup_sh1106_128x64_noname_f"] = u8g2_Setup_sh1106_128x64_noname_f;
	setup_map["u8g2_Setup_sh1106_128x64_vcomh0_f"] = u8g2_Setup_sh1106_128x64_vcomh0_f;
	setup_map["u8g2_Setup_sh1106_128x64_winstar_f"] = u8g2_Setup_sh1106_128x64_winstar_f;
	setup_map["u8g2_Setup_sh1106_64x32_f"] = u8g2_Setup_sh1106_64x32_f;
	setup_map["u8g2_Setup_sh1106_72x40_wise_f"] = u8g2_Setup_sh1106_72x40_wise_f;
	setup_map["u8g2_Setup_sh1106_i2c_128x64_noname_f"] = u8g2_Setup_sh1106_i2c_128x64_noname_f;
	setup_map["u8g2_Setup_sh1106_i2c_128x64_vcomh0_f"] = u8g2_Setup_sh1106_i2c_128x64_vcomh0_f;
	setup_map["u8g2_Setup_sh1106_i2c_128x64_winstar_f"] = u8g2_Setup_sh1106_i2c_128x64_winstar_f;
	setup_map["u8g2_Setup_sh1106_i2c_64x32_f"] = u8g2_Setup_sh1106_i2c_64x32_f;
	setup_map["u8g2_Setup_sh1106_i2c_72x40_wise_f"] = u8g2_Setup_sh1106_i2c_72x40_wise_f;
	setup_map["u8g2_Setup_sh1107_128x128_f"] = u8g2_Setup_sh1107_128x128_f;
	setup_map["u8g2_Setup_sh1107_64x128_f"] = u8g2_Setup_sh1107_64x128_f;
	setup_map["u8g2_Setup_sh1107_i2c_128x128_f"] = u8g2_Setup_sh1107_i2c_128x128_f;
	setup_map["u8g2_Setup_sh1107_i2c_64x128_f"] = u8g2_Setup_sh1107_i2c_64x128_f;
	setup_map["u8g2_Setup_sh1107_i2c_seeed_96x96_f"] = u8g2_Setup_sh1107_i2c_seeed_96x96_f;
	setup_map["u8g2_Setup_sh1107_seeed_96x96_f"] = u8g2_Setup_sh1107_seeed_96x96_f;
	setup_map["u8g2_Setup_sh1108_160x160_f"] = u8g2_Setup_sh1108_160x160_f;
	setup_map["u8g2_Setup_sh1108_i2c_160x160_f"] = u8g2_Setup_sh1108_i2c_160x160_f;
	setup_map["u8g2_Setup_sh1122_256x64_f"] = u8g2_Setup_sh1122_256x64_f;
	setup_map["u8g2_Setup_sh1122_i2c_256x64_f"] = u8g2_Setup_sh1122_i2c_256x64_f;
	setup_map["u8g2_Setup_ssd0323_i2c_os128064_f"] = u8g2_Setup_ssd0323_i2c_os128064_f;
	setup_map["u8g2_Setup_ssd0323_os128064_f"] = u8g2_Setup_ssd0323_os128064_f;
	setup_map["u8g2_Setup_ssd1305_128x32_adafruit_f"] = u8g2_Setup_ssd1305_128x32_adafruit_f;
	setup_map["u8g2_Setup_ssd1305_128x32_noname_f"] = u8g2_Setup_ssd1305_128x32_noname_f;
	setup_map["u8g2_Setup_ssd1305_128x64_adafruit_f"] = u8g2_Setup_ssd1305_128x64_adafruit_f;
	setup_map["u8g2_Setup_ssd1305_i2c_128x32_adafruit_f"] = u8g2_Setup_ssd1305_i2c_128x32_adafruit_f;
	setup_map["u8g2_Setup_ssd1305_i2c_128x32_noname_f"] = u8g2_Setup_ssd1305_i2c_128x32_noname_f;
	setup_map["u8g2_Setup_ssd1305_i2c_128x64_adafruit_f"] = u8g2_Setup_ssd1305_i2c_128x64_adafruit_f;
	setup_map["u8g2_Setup_ssd1306_128x32_univision_f"] = u8g2_Setup_ssd1306_128x32_univision_f;
	setup_map["u8g2_Setup_ssd1306_128x64_alt0_f"] = u8g2_Setup_ssd1306_128x64_alt0_f;
	setup_map["u8g2_Setup_ssd1306_128x64_noname_f"] = u8g2_Setup_ssd1306_128x64_noname_f;
	setup_map["u8g2_Setup_ssd1306_128x64_vcomh0_f"] = u8g2_Setup_ssd1306_128x64_vcomh0_f;
	setup_map["u8g2_Setup_ssd1306_48x64_winstar_f"] = u8g2_Setup_ssd1306_48x64_winstar_f;
	setup_map["u8g2_Setup_ssd1306_64x32_1f_f"] = u8g2_Setup_ssd1306_64x32_1f_f;
	setup_map["u8g2_Setup_ssd1306_64x32_noname_f"] = u8g2_Setup_ssd1306_64x32_noname_f;
	setup_map["u8g2_Setup_ssd1306_64x48_er_f"] = u8g2_Setup_ssd1306_64x48_er_f;
	setup_map["u8g2_Setup_ssd1306_96x16_er_f"] = u8g2_Setup_ssd1306_96x16_er_f;
	setup_map["u8g2_Setup_ssd1306_i2c_128x32_univision_f"] = u8g2_Setup_ssd1306_i2c_128x32_univision_f;
	setup_map["u8g2_Setup_ssd1306_i2c_128x64_alt0_f"] = u8g2_Setup_ssd1306_i2c_128x64_alt0_f;
	setup_map["u8g2_Setup_ssd1306_i2c_128x64_noname_f"] = u8g2_Setup_ssd1306_i2c_128x64_noname_f;
	setup_map["u8g2_Setup_ssd1306_i2c_128x64_vcomh0_f"] = u8g2_Setup_ssd1306_i2c_128x64_vcomh0_f;
	setup_map["u8g2_Setup_ssd1306_i2c_48x64_winstar_f"] = u8g2_Setup_ssd1306_i2c_48x64_winstar_f;
	setup_map["u8g2_Setup_ssd1306_i2c_64x32_1f_f"] = u8g2_Setup_ssd1306_i2c_64x32_1f_f;
	setup_map["u8g2_Setup_ssd1306_i2c_64x32_noname_f"] = u8g2_Setup_ssd1306_i2c_64x32_noname_f;
	setup_map["u8g2_Setup_ssd1306_i2c_64x48_er_f"] = u8g2_Setup_ssd1306_i2c_64x48_er_f;
	setup_map["u8g2_Setup_ssd1306_i2c_96x16_er_f"] = u8g2_Setup_ssd1306_i2c_96x16_er_f;
	setup_map["u8g2_Setup_ssd1309_128x64_noname0_f"] = u8g2_Setup_ssd1309_128x64_noname0_f;
	setup_map["u8g2_Setup_ssd1309_128x64_noname2_f"] = u8g2_Setup_ssd1309_128x64_noname2_f;
	setup_map["u8g2_Setup_ssd1309_i2c_128x64_noname0_f"] = u8g2_Setup_ssd1309_i2c_128x64_noname0_f;
	setup_map["u8g2_Setup_ssd1309_i2c_128x64_noname2_f"] = u8g2_Setup_ssd1309_i2c_128x64_noname2_f;
	setup_map["u8g2_Setup_ssd1317_96x96_f"] = u8g2_Setup_ssd1317_96x96_f;
	setup_map["u8g2_Setup_ssd1317_i2c_96x96_f"] = u8g2_Setup_ssd1317_i2c_96x96_f;
	setup_map["u8g2_Setup_ssd1322_nhd_128x64_f"] = u8g2_Setup_ssd1322_nhd_128x64_f;
	setup_map["u8g2_Setup_ssd1322_nhd_256x64_f"] = u8g2_Setup_ssd1322_nhd_256x64_f;
	setup_map["u8g2_Setup_ssd1325_i2c_nhd_128x64_f"] = u8g2_Setup_ssd1325_i2c_nhd_128x64_f;
	setup_map["u8g2_Setup_ssd1325_nhd_128x64_f"] = u8g2_Setup_ssd1325_nhd_128x64_f;
	setup_map["u8g2_Setup_ssd1326_er_256x32_f"] = u8g2_Setup_ssd1326_er_256x32_f;
	setup_map["u8g2_Setup_ssd1326_i2c_er_256x32_f"] = u8g2_Setup_ssd1326_i2c_er_256x32_f;
	setup_map["u8g2_Setup_ssd1327_ea_w128128_f"] = u8g2_Setup_ssd1327_ea_w128128_f;
	setup_map["u8g2_Setup_ssd1327_i2c_ea_w128128_f"] = u8g2_Setup_ssd1327_i2c_ea_w128128_f;
	setup_map["u8g2_Setup_ssd1327_i2c_midas_128x128_f"] = u8g2_Setup_ssd1327_i2c_midas_128x128_f;
	setup_map["u8g2_Setup_ssd1327_i2c_seeed_96x96_f"] = u8g2_Setup_ssd1327_i2c_seeed_96x96_f;
	setup_map["u8g2_Setup_ssd1327_midas_128x128_f"] = u8g2_Setup_ssd1327_midas_128x128_f;
	setup_map["u8g2_Setup_ssd1327_seeed_96x96_f"] = u8g2_Setup_ssd1327_seeed_96x96_f;
	setup_map["u8g2_Setup_ssd1329_128x96_noname_f"] = u8g2_Setup_ssd1329_128x96_noname_f;
	setup_map["u8g2_Setup_ssd1606_172x72_f"] = u8g2_Setup_ssd1606_172x72_f;
	setup_map["u8g2_Setup_ssd1607_200x200_f"] = u8g2_Setup_ssd1607_200x200_f;
	setup_map["u8g2_Setup_ssd1607_gd_200x200_f"] = u8g2_Setup_ssd1607_gd_200x200_f;
	setup_map["u8g2_Setup_st75256_i2c_jlx172104_f"] = u8g2_Setup_st75256_i2c_jlx172104_f;
	setup_map["u8g2_Setup_st75256_i2c_jlx240160_f"] = u8g2_Setup_st75256_i2c_jlx240160_f;
	setup_map["u8g2_Setup_st75256_i2c_jlx256128_f"] = u8g2_Setup_st75256_i2c_jlx256128_f;
	setup_map["u8g2_Setup_st75256_i2c_jlx256160_f"] = u8g2_Setup_st75256_i2c_jlx256160_f;
	setup_map["u8g2_Setup_st75256_i2c_jlx25664_f"] = u8g2_Setup_st75256_i2c_jlx25664_f;
	setup_map["u8g2_Setup_st75256_jlx172104_f"] = u8g2_Setup_st75256_jlx172104_f;
	setup_map["u8g2_Setup_st75256_jlx240160_f"] = u8g2_Setup_st75256_jlx240160_f;
	setup_map["u8g2_Setup_st75256_jlx256128_f"] = u8g2_Setup_st75256_jlx256128_f;
	setup_map["u8g2_Setup_st75256_jlx256160_alt_f"] = u8g2_Setup_st75256_jlx256160_alt_f;
	setup_map["u8g2_Setup_st75256_jlx256160_f"] = u8g2_Setup_st75256_jlx256160_f;
	setup_map["u8g2_Setup_st75256_jlx25664_f"] = u8g2_Setup_st75256_jlx25664_f;
	setup_map["u8g2_Setup_st7565_64128n_f"] = u8g2_Setup_st7565_64128n_f;
	setup_map["u8g2_Setup_st7565_ea_dogm128_f"] = u8g2_Setup_st7565_ea_dogm128_f;
	setup_map["u8g2_Setup_st7565_ea_dogm132_f"] = u8g2_Setup_st7565_ea_dogm132_f;
	setup_map["u8g2_Setup_st7565_erc12864_f"] = u8g2_Setup_st7565_erc12864_f;
	setup_map["u8g2_Setup_st7565_jlx12864_f"] = u8g2_Setup_st7565_jlx12864_f;
	setup_map["u8g2_Setup_st7565_lm6059_f"] = u8g2_Setup_st7565_lm6059_f;
	setup_map["u8g2_Setup_st7565_lx12864_f"] = u8g2_Setup_st7565_lx12864_f;
	setup_map["u8g2_Setup_st7565_nhd_c12832_f"] = u8g2_Setup_st7565_nhd_c12832_f;
	setup_map["u8g2_Setup_st7565_nhd_c12864_f"] = u8g2_Setup_st7565_nhd_c12864_f;
	setup_map["u8g2_Setup_st7565_zolen_128x64_f"] = u8g2_Setup_st7565_zolen_128x64_f;
	setup_map["u8g2_Setup_st7567_64x32_f"] = u8g2_Setup_st7567_64x32_f;
	setup_map["u8g2_Setup_st7567_enh_dg128064_f"] = u8g2_Setup_st7567_enh_dg128064_f;
	setup_map["u8g2_Setup_st7567_enh_dg128064i_f"] = u8g2_Setup_st7567_enh_dg128064i_f;
	setup_map["u8g2_Setup_st7567_i2c_64x32_f"] = u8g2_Setup_st7567_i2c_64x32_f;
	setup_map["u8g2_Setup_st7567_jlx12864_f"] = u8g2_Setup_st7567_jlx12864_f;
	setup_map["u8g2_Setup_st7567_pi_132x64_f"] = u8g2_Setup_st7567_pi_132x64_f;
	setup_map["u8g2_Setup_st7586s_erc240160_f"] = u8g2_Setup_st7586s_erc240160_f;
	setup_map["u8g2_Setup_st7586s_s028hn118a_f"] = u8g2_Setup_st7586s_s028hn118a_f;
	setup_map["u8g2_Setup_st7588_i2c_jlx12864_f"] = u8g2_Setup_st7588_i2c_jlx12864_f;
	setup_map["u8g2_Setup_st7588_jlx12864_f"] = u8g2_Setup_st7588_jlx12864_f;
	setup_map["u8g2_Setup_st7920_128x64_f"] = u8g2_Setup_st7920_128x64_f;
	setup_map["u8g2_Setup_st7920_192x32_f"] = u8g2_Setup_st7920_192x32_f;
	setup_map["u8g2_Setup_st7920_p_128x64_f"] = u8g2_Setup_st7920_p_128x64_f;
	setup_map["u8g2_Setup_st7920_p_192x32_f"] = u8g2_Setup_st7920_p_192x32_f;
	setup_map["u8g2_Setup_st7920_s_128x64_f"] = u8g2_Setup_st7920_s_128x64_f;
	setup_map["u8g2_Setup_st7920_s_192x32_f"] = u8g2_Setup_st7920_s_192x32_f;
	setup_map["u8g2_Setup_t6963_128x64_f"] = u8g2_Setup_t6963_128x64_f;
	setup_map["u8g2_Setup_t6963_160x80_f"] = u8g2_Setup_t6963_160x80_f;
	setup_map["u8g2_Setup_t6963_240x128_f"] = u8g2_Setup_t6963_240x128_f;
	setup_map["u8g2_Setup_t6963_240x64_f"] = u8g2_Setup_t6963_240x64_f;
	setup_map["u8g2_Setup_t6963_256x64_f"] = u8g2_Setup_t6963_256x64_f;
	setup_map["u8g2_Setup_uc1601_128x32_f"] = u8g2_Setup_uc1601_128x32_f;
	setup_map["u8g2_Setup_uc1601_i2c_128x32_f"] = u8g2_Setup_uc1601_i2c_128x32_f;
	setup_map["u8g2_Setup_uc1604_i2c_jlx19264_f"] = u8g2_Setup_uc1604_i2c_jlx19264_f;
	setup_map["u8g2_Setup_uc1604_jlx19264_f"] = u8g2_Setup_uc1604_jlx19264_f;
	setup_map["u8g2_Setup_uc1608_240x128_f"] = u8g2_Setup_uc1608_240x128_f;
	setup_map["u8g2_Setup_uc1608_erc240120_f"] = u8g2_Setup_uc1608_erc240120_f;
	setup_map["u8g2_Setup_uc1608_erc24064_f"] = u8g2_Setup_uc1608_erc24064_f;
	setup_map["u8g2_Setup_uc1608_i2c_240x128_f"] = u8g2_Setup_uc1608_i2c_240x128_f;
	setup_map["u8g2_Setup_uc1608_i2c_erc240120_f"] = u8g2_Setup_uc1608_i2c_erc240120_f;
	setup_map["u8g2_Setup_uc1608_i2c_erc24064_f"] = u8g2_Setup_uc1608_i2c_erc24064_f;
	setup_map["u8g2_Setup_uc1610_ea_dogxl160_f"] = u8g2_Setup_uc1610_ea_dogxl160_f;
	setup_map["u8g2_Setup_uc1610_i2c_ea_dogxl160_f"] = u8g2_Setup_uc1610_i2c_ea_dogxl160_f;
	setup_map["u8g2_Setup_uc1611_ea_dogm240_f"] = u8g2_Setup_uc1611_ea_dogm240_f;
	setup_map["u8g2_Setup_uc1611_ea_dogxl240_f"] = u8g2_Setup_uc1611_ea_dogxl240_f;
	setup_map["u8g2_Setup_uc1611_ew50850_f"] = u8g2_Setup_uc1611_ew50850_f;
	setup_map["u8g2_Setup_uc1611_i2c_ea_dogm240_f"] = u8g2_Setup_uc1611_i2c_ea_dogm240_f;
	setup_map["u8g2_Setup_uc1611_i2c_ea_dogxl240_f"] = u8g2_Setup_uc1611_i2c_ea_dogxl240_f;
	setup_map["u8g2_Setup_uc1611_i2c_ew50850_f"] = u8g2_Setup_uc1611_i2c_ew50850_f;
	setup_map["u8g2_Setup_uc1638_160x128_f"] = u8g2_Setup_uc1638_160x128_f;
	setup_map["u8g2_Setup_uc1701_ea_dogs102_f"] = u8g2_Setup_uc1701_ea_dogs102_f;
	setup_map["u8g2_Setup_uc1701_mini12864_f"] = u8g2_Setup_uc1701_mini12864_f;
}