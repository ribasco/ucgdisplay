# systemd install
- python3-pip
- meson (sudo pip3 install meson)
- ninja-build
- gperf
- libmount-dev (version >= 2.30)
 	- apt-get install autopoint
	- apt-get install bison
	- apt-get install libtool
	- git clone https://github.com/karelzak/util-linux.git
- sudo apt-get install libcap-dev ninja-build gperf meson libmount-dev


# Windows requirements
- MinGW
- CMake 3.11+


# Steps for windows compilation
- Install MinGW 
- Add 'bin' of ming and msys to USER path environment variables 

UCGDisplay Configuration Requirements for GLCD
--------------------------------------------------------------------------------------------------------------------------------
1. Rotation
2. Communication Protocol

	- Software (Bit-bang):

        | Byte Procedure | Description  |
        |---|---|
        | u8x8_byte_4wire_sw_spi | Standard 8-bit SPI communication with "four pins" (SCK, MOSI, DC, CS) | 
        | u8x8_byte_3wire_sw_spi | 9-bit communication with "three pins" (SCK, MOSI, CS) | 
        | u8x8_byte_8bit_6800mode | Parallel interface, 6800 format |  
        | u8x8_byte_8bit_8080mode | Parallel interface, 8080 format  | 
        | u8x8_byte_sw_i2c | Two wire, I2C communication | 
        | u8x8_byte_ks0108 | Special interface for KS0108 controller | 
        -------------------------------------------------------------------------------------------------

	- Hardware: 
	
        | Type | Procedure      | Description               |   |   |
        |------|----------------|---------------------------|---|---|
        | SPI  | cb_byte_spi_hw | Raspberry Pi SPI Hardware |   |   |
        | I2C  | cb_byte_i2c_hw | Raspberry Pi I2C Hardware |   |   |
        |      |                |                           |   |   |
		
3. Display Type/Constructor
	- List of available U8G2 constructors here