package com.ibasco.pidisplay.drivers.glcd.enums;

/*
#define U8X8_PIN_D0 0
#define U8X8_PIN_SPI_CLOCK 0
#define U8X8_PIN_D1 1
#define U8X8_PIN_SPI_DATA 1
#define U8X8_PIN_D2 2
#define U8X8_PIN_D3 3
#define U8X8_PIN_D4 4
#define U8X8_PIN_D5 5
#define U8X8_PIN_D6 6
#define U8X8_PIN_D7 7

#define U8X8_PIN_E 8
#define U8X8_PIN_CS 9			//parallel, SPI
#define U8X8_PIN_DC 10            // parallel, SPI
#define U8X8_PIN_RESET 11        // parallel, SPI, I2C

#define U8X8_PIN_I2C_CLOCK 12    // 1 = Input/high impedance, 0 = drive low
#define U8X8_PIN_I2C_DATA 13    // 1 = Input/high impedance, 0 = drive low

#define U8X8_PIN_CS1 14            // KS0108 extra chip select
#define U8X8_PIN_CS2 15            //KS0108 extra chip select
*/

import java.util.Arrays;
import java.util.List;

/**
 * All possible PIN configurations that can be found on your Graphics LCD device
 *
 * @author Rafael Ibasco
 */
public enum GlcdPin {
    /**
     * Data pin 0 (also known as SPI CLOCK)
     */
    D0(0),
    /**
     * Data pin 1 (also known as SPI DATA)
     */
    D1(1),
    /**
     * Data Pin 2
     */
    D2(2),
    /**
     * Data Pin 3
     */
    D3(3),
    /**
     * Data Pin 4
     */
    D4(4),
    /**
     * Data Pin 5
     */
    D5(5),
    /**
     * Data Pin 6
     */
    D6(6),
    /**
     * Data Pin 7
     */
    D7(7),
    /**
     * Enable Pin
     */
    EN(8),
    /**
     * Chip Select Pin
     */
    CS(9),
    /**
     * Data/Command Pin
     */
    DC(10),
    /**
     * Reset Pin
     */
    RESET(11),
    /**
     * I2C Clock Pin (SCL)
     */
    I2C_CLOCK(12),
    /**
     * I2C Data Pin (SDA)
     */
    I2C_DATA(13),
    /**
     * Chip Select 1 (KS0108 extra chip select)
     */
    CS1(14),
    /**
     * Chip Select 2 (KS0108 extra chip select)
     */
    CS2(15),
    /**
     * Alias for D0. Added for readability
     */
    SPI_CLOCK(0),
    /**
     * Alias for D1. Added for readability
     */
    SPI_MOSI(1);

    private int index;

    GlcdPin(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static List<GlcdPin> getPins() {
        return Arrays.asList(GlcdPin.values());
    }
}
