[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=TC2KWPSJPAQ66)


# Some plugins to consider using:

    <!-- GENERATE LICENSE HEADERS IN SOURCE FILES -->
    <plugin>
    	<groupId>org.codehaus.mojo</groupId>
    	<artifactId>license-maven-plugin</artifactId>
    </plugin>

 
| Controller | Display Name    | Supported Modes       | Display Size | Setup Procedure                          | 
|------------|-----------------|-----------------------|--------------|------------------------------------------|
| ssd1305    | 128x32_noname   | 4WSPI,6800,8080       | 128x32       | u8g2_Setup_ssd1305_128x32_noname_f       | 
| ssd1305    | 128x32_noname   | I2C                   | 128x32       | u8g2_Setup_ssd1305_i2c_128x32_noname_f   | 
| ssd1305    | 128x64_adafruit | 4WSPI,6800,8080       | 128x64       | u8g2_Setup_ssd1305_128x64_adafruit_f     | 
| ssd1305    | 128x64_adafruit | I2C                   | 128x64       | u8g2_Setup_ssd1305_i2c_128x64_adafruit_f | 
| ssd1306    | 128x64_noname   | 4WSPI,3WSPI,6800,8080 | 128x64       | u8g2_Setup_ssd1306_128x64_noname_f       | 
| ssd1306    | 128x64_vcomh0   | 4WSPI,3WSPI,6800,8080 | 128x64       | u8g2_Setup_ssd1306_128x64_vcomh0_f       | 
| ssd1306    | 128x64_alt0     | 4WSPI,3WSPI,6800,8080 | 128x64       | u8g2_Setup_ssd1306_128x64_alt0_f         | 



Hello again Oliver,

I'm in the process of developing a java wrapper library for U8G2 and I'm currently thinking of ways to automate the code generation for the supported controllers. 

Looking at your codebuild source, I see that the list of controllers are all hard coded into one array. That said, I would like to ask if you are open to making any of these proposed changes below: 

1. Move the array to it's own header file which I can then use as a reference for my code-generation utility in c++. 

2. Move the array to a file in plain-text (json/yaml/ini) form

3. Provide an option in codebuild to output the list of controllers (along with thier meta data):

    sample output (tab delimited): 
    
	| Controller | Display Name    | Supported Modes       | Display Size | Setup Procedure                          | 
	|------------|-----------------|-----------------------|--------------|------------------------------------------|
	| ssd1305    | 128x32_noname   | 4WSPI,6800,8080       | 128x32       | u8g2_Setup_ssd1305_128x32_noname_f       | 
	| ssd1305    | 128x32_noname   | I2C                   | 128x32       | u8g2_Setup_ssd1305_i2c_128x32_noname_f   | 
	| ssd1305    | 128x64_adafruit | 4WSPI,6800,8080       | 128x64       | u8g2_Setup_ssd1305_128x64_adafruit_f     | 
	| ssd1305    | 128x64_adafruit | I2C                   | 128x64       | u8g2_Setup_ssd1305_i2c_128x64_adafruit_f | 
	| ssd1306    | 128x64_noname   | 4WSPI,3WSPI,6800,8080 | 128x64       | u8g2_Setup_ssd1306_128x64_noname_f       | 
	| ssd1306    | 128x64_vcomh0   | 4WSPI,3WSPI,6800,8080 | 128x64       | u8g2_Setup_ssd1306_128x64_vcomh0_f       | 
	| ssd1306    | 128x64_alt0     | 4WSPI,3WSPI,6800,8080 | 128x64       | u8g2_Setup_ssd1306_128x64_alt0_f         |
	

Note: I'm ok for the first option as that would not require any development in your part.		