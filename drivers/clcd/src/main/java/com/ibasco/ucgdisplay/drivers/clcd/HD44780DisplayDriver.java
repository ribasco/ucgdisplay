/*-
 * ========================START=================================
 * UCGDisplay :: Character LCD driver
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
package com.ibasco.ucgdisplay.drivers.clcd;

import com.ibasco.ucgdisplay.drivers.clcd.adapters.GpioLcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.enums.*;
import com.ibasco.ucgdisplay.drivers.clcd.exceptions.DisplayDriverException;
import com.pi4j.component.lcd.LCD;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;
import static com.pi4j.wiringpi.Gpio.delay;
import static com.pi4j.wiringpi.Gpio.delayMicroseconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Native implementation for HD44780 Character Display Driver. Compatible with the Pi4J {@link LCD} interface.
 *
 * Note: This class is NOT thread-safe
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("All")
public class HD44780DisplayDriver extends CharDisplayDriverBase {
    private static final Logger log = LoggerFactory.getLogger(HD44780DisplayDriver.class);

    //Maximum number of characters supported
    private static final int MAX_CHARACTERS = 80;

    // commands
    private static final byte LCD_CLEARDISPLAY = 0x01;
    private static final byte LCD_RETURNHOME = 0x02;
    private static final byte LCD_ENTRYMODESET = 0x04;
    private static final byte LCD_DISPLAYCONTROL = 0x08;
    private static final byte LCD_CURSORSHIFT = 0x10;
    private static final byte LCD_FUNCTIONSET = 0x20;
    private static final byte LCD_SETCGRAMADDR = 0x40;
    private static final byte LCD_SETDDRAMADDR = (byte) 0x80;

    // flags for display entry mode
    private static final byte LCD_ENTRYRIGHT = 0x00;
    private static final byte LCD_ENTRYLEFT = 0x02;
    private static final byte LCD_ENTRYSHIFTINCREMENT = 0x01;
    private static final byte LCD_ENTRYSHIFTDECREMENT = 0x00;

    // flags for display on/off control
    private static final byte LCD_DISPLAYON = 0x04;
    private static final byte LCD_DISPLAYOFF = 0x00;
    private static final byte LCD_CURSORON = 0x02;
    private static final byte LCD_CURSOROFF = 0x00;
    private static final byte LCD_BLINKON = 0x01;
    private static final byte LCD_BLINKOFF = 0x00;

    // flags for display/cursor shift
    private static final byte LCD_DISPLAYMOVE = 0x08;
    private static final byte LCD_CURSORMOVE = 0x00;
    private static final byte LCD_MOVERIGHT = 0x04;
    private static final byte LCD_MOVELEFT = 0x00;

    // flags for function set
    private static final byte LCD_8BITMODE = 0x10;
    private static final byte LCD_4BITMODE = 0x00;
    private static final byte LCD_2LINE = 0x08;
    private static final byte LCD_1LINE = 0x00;
    private static final byte LCD_5x10DOTS = 0x04;
    private static final byte LCD_5x8DOTS = 0x00;

    private byte displayFunction;
    private byte displayControl;
    private byte displayMode;

    private final int cols;
    private final int rows;

    private final byte[] rowOffsets = new byte[4];

    private LcdGpioAdapter lcdGpioAdapter;

    private Map<Integer, byte[]> charDataMap = new HashMap<>();

    /**
     * Monitor the state of the cursor offset
     */
    private byte cursorOffset = 0x0;

    /**
     * LCD Default Constructor. Uses RPI GPIO as the default adapter interface for data transmission.
     *
     * @param lcdPinMap
     *         The Pin mapping configuration
     */
    public HD44780DisplayDriver(LcdPinMapConfig lcdPinMap, int cols, int rows) throws IOException {
        this(new GpioLcdAdapter(GpioFactory.getDefaultProvider(), lcdPinMap), cols, rows);
    }

    /**
     * LCD Constructor. 4-bit communication is used by default.
     *
     * @param lcdGpioAdapter
     *         The adapter interface to use for communicating with the LCD device
     */
    public HD44780DisplayDriver(LcdGpioAdapter lcdGpioAdapter, int cols, int rows) throws IOException {
        this(lcdGpioAdapter, cols, rows, LcdOperationMode.FOURBIT);
    }

    /**
     * LCD Constructor. 5x8 Character Size is used by default.
     *
     * @param lcdGpioAdapter
     *         The adapter interface to use for communicating with the LCD device
     * @param cols
     *         The maximum number of supported columns on the LCD device
     * @param rows
     *         The maximum number of supported rows on the LCD device
     * @param operationMode
     *         The operation mode (4-bit or 8-bit)
     */
    public HD44780DisplayDriver(LcdGpioAdapter lcdGpioAdapter, int cols, int rows, LcdOperationMode operationMode) throws IOException {
        this(lcdGpioAdapter, cols, rows, operationMode, LcdCharSize.DOTS_5X8);
    }

    /**
     * LCD Constructor
     *
     * @param lcdGpioAdapter
     *         The adapter interface to use for communicating with the LCD device
     * @param cols
     *         The maximum number of supported columns on the LCD device
     * @param rows
     *         The maximum number of supported rows on the LCD device
     * @param operationMode
     *         The mode of operation to use for data transfer.
     * @param charSize
     *         The supported character size of the LCD device (5x8 or 5x10)
     */
    public HD44780DisplayDriver(LcdGpioAdapter lcdGpioAdapter, int cols, int rows, LcdOperationMode operationMode, LcdCharSize charSize) throws IOException {
        if (lcdGpioAdapter == null || operationMode == null || charSize == null)
            throw new IllegalArgumentException(String.format("Invalid arguments found in the constructor. (Adapter=%s, Operation Mode=%s, Char Size=%s)", lcdGpioAdapter, operationMode, charSize));
        this.lcdGpioAdapter = lcdGpioAdapter;
        this.displayFunction = (byte) (operationMode.getValue() | LCD_1LINE | charSize.getValue());
        this.cols = cols;
        this.rows = rows;
        initializeLcd(charSize);
    }

    /**
     * Initializes the LCD.
     *
     * @param charSize
     *         The character size supported by the LCD. (Use LCD_5x8DOTS or LCD_5x10DOTS)
     *
     * @throws IOException
     *         Thrown when there is a problem during the initialization phase
     */
    private void initializeLcd(LcdCharSize charSize) throws IOException {
        //Initialize Pins (if applicable)
        lcdGpioAdapter.initialize();

        //If rows > 1, initialize displayFunction to 2-LINE mode
        setInstruction(LcdInstruction.DISPLAY_FUNCTION, LCD_2LINE, rows > 1);

        //Verify the row and column sizes
        //Make sure we don't go beyod the maximum limit
        if ((cols * rows) > MAX_CHARACTERS)
            throw new IllegalStateException("Unsupported row/column size");

        //----------------------------------------------------------------------------------------------
        //Row Offsets
        //----------------------------------------------------------------------------------------------
        // Line 1 = 0x00 -> 0x27 (0 to 39)
        // Line 2 = 0x40 -> 0x67 (64 to 103)
        //==============================================================================================
        //  LCD - 20 x 4 (80 bytes/characters in total)
        //==============================================================================================
        //Row 0: 00	01	02	03	04	05	06	07	08	09	0A	0B	0C	0D	0E	0F	10	11	12	13 (Line 1-a)
        //Row 1: 40	41	42	43	44	45	46	47	48	49	4A	4B	4C	4D	4E	4F	50	51	52	53 (Line 2-a)
        //Row 2: 14	15	16	17	18	19	1A	1B	1C	1D	1E	1F	20	21	22	23	24	25	26	27 (Line 1-b) continuation of 1-a
        //Row 3: 54	55	56	57	58	59	5A	5B	5C	5D	5E	5F	60	61	62	63	64	65	66	67 (Line 2-b) continuation of 2-a
        //==============================================================================================
        //
        //==============================================================================================
        //  LCD - 20 x 2 (80 bytes/characters in total)
        //==============================================================================================
        //Row 0: 00	01	02	03	04	05	06	07	08	09	0A	0B	0C	0D	0E	0F	10	11	12	13 (Line 1-a)
        //Row 1: 40	41	42	43	44	45	46	47	48	49	4A	4B	4C	4D	4E	4F	50	51	52	53 (Line 2-a)
        //===============================================================================================
        //
        //==============================================================================================
        //  LCD - 16 x 2 (80 bytes/characters in total)
        //==============================================================================================
        //Row 0: 00	01	02	03	04	05	06	07	08	09	0A	0B	0C	0D	0E	0F	(Line 1-a)
        //Row 1: 40	41	42	43	44	45	46	47	48	49	4A	4B	4C	4D	4E	4F	(Line 2-a)
        //==============================================================================================
        //
        setRowOffsets(0x00, 0x40, 0x00 + cols, 0x40 + cols);

        // for some 1 line displays you can select a 10 pixel high font
        setInstruction(LcdInstruction.DISPLAY_FUNCTION, LCD_5x10DOTS, (charSize.getValue() != 0) && (rows == 1));

        // SEE PAGE 45/46 FOR INITIALIZATION SPECIFICATION!
        // according to datasheet, we need at least 40ms after power rises above 2.7V
        // before sending commands. Arduino can turn on way befer 4.5V so we'll wait 50
        //delayMicroseconds(50000);
        delay(40);

        // Now we pull both RS, EN and R/W low to begin commands
        lcdGpioAdapter.setRegSelectState(LcdRegisterSelectState.COMMAND);
        lcdGpioAdapter.setEnableState(PinState.LOW);
        lcdGpioAdapter.setReadWriteState(LcdReadWriteState.WRITE);

        //Initialization procedures for 8-bit interface
        if ((this.displayFunction & LCD_8BITMODE) == 0) {
            // according to the hitachi HD44780 datasheet figure 24, pg 46

            // start in 8bit mode, try to set 4 bit mode
            lcdGpioAdapter.write4Bits((byte) 0x03);
            delayMicroseconds(4500);

            // second
            lcdGpioAdapter.write4Bits((byte) 0x03);
            delayMicroseconds(4500);

            // third
            lcdGpioAdapter.write4Bits((byte) 0x03);
            delayMicroseconds(150);

            // finally, set to 8-bit interface
            lcdGpioAdapter.write4Bits((byte) 0x02);
        }
        //Initialization procedures for 4-bit interface
        else {
            // this is according to the hitachi HD44780 datasheet
            // page 45 figure 23

            // Send function set command sequence
            command(LCD_FUNCTIONSET | this.displayFunction);
            delayMicroseconds(4500);  // wait more than 4.1ms

            // second try
            command(LCD_FUNCTIONSET | this.displayFunction);
            delayMicroseconds(150);

            // third go
            command(LCD_FUNCTIONSET | this.displayFunction);
        }

        // finally, set # lines, font size, etc.
        command(LCD_FUNCTIONSET | this.displayFunction);

        // turn the display on with no cursor or blinking default
        this.displayControl = LCD_DISPLAYON | LCD_CURSOROFF | LCD_BLINKOFF;
        command(LCD_DISPLAYCONTROL | this.displayControl);
        clear();

        // Initialize to default text direction (for romance languages)
        this.displayMode = LCD_ENTRYLEFT | LCD_ENTRYSHIFTDECREMENT;
        // set the entry mode
        command(LCD_ENTRYMODESET | this.displayMode);
    }

    /**
     * @return The maximum number of rows supported
     */
    @Override
    public int getHeight() {
        return this.rows;
    }

    /**
     * @return The maximum number of columns supported
     */
    @Override
    public int getWidth() {
        return this.cols;
    }

    /**
     * Clears the data on the lcd screen and resets the cursor back to 0
     */
    @Override
    public void clear() {
        command(LCD_CLEARDISPLAY);  //
        // clear display, set cursor position to zero
        delayMicroseconds(1500);  // this command takes a long time
    }

    /**
     * Resets the cursor position back to 0
     */
    @Override
    public void home() {
        command(LCD_RETURNHOME);  // set cursor position to zero
        delayMicroseconds(2000);  // this command takes a long time!
    }

    /**
     * Sets the display state to either on or off. Note that the data will still be preserved when the state is OFF.
     *
     * @param state
     *         {@code true} to turn on the display otherwise {@code false} to turn off.
     */
    @Override
    public void display(boolean state) {
        setInstruction(LcdInstruction.DISPLAY_CONTROL, LCD_DISPLAYON, state);
        command(LCD_DISPLAYCONTROL | this.displayControl);
    }

    /**
     * Equivalent to blink/noBlink
     *
     * @param state
     *         Set to <code>True</code> to set cursor blink state
     */
    @Override
    public void blink(boolean state) {
        setInstruction(LcdInstruction.DISPLAY_CONTROL, LCD_BLINKON, state);
        command(LCD_DISPLAYCONTROL | this.displayControl);
    }

    /**
     * Equivalent to cursor/noCursor
     *
     * @param state
     *         Set to {@code True} to display the cursor {@code False} to disable it.
     */
    @Override
    public void cursor(boolean state) {
        setInstruction(LcdInstruction.DISPLAY_CONTROL, LCD_CURSORON, state);
        command(LCD_DISPLAYCONTROL | this.displayControl);
    }

    /**
     * This mode moves all the text one space to the left each time a letter is added.
     *
     * Right/Left justifification from the cursor position.
     *
     * @param state
     *         If {@code true} text will be moved one space to the left {@code false} to disable autoscrolling
     */
    @Override
    public void autoscroll(boolean state) {
        setInstruction(LcdInstruction.DISPLAY_MODE, LCD_ENTRYSHIFTINCREMENT, state);
        command(LCD_ENTRYMODESET | this.displayMode);
    }

    /**
     * Scroll the display either left or right. This scroll the display without changing the RAM
     *
     * @param direction
     *         The {@link ScrollDirection} enum
     */
    @Override
    public void scrollDisplay(ScrollDirection direction) {
        int scrollDirection = (ScrollDirection.LEFT == direction) ? 0x0 : 0x04;
        command(LCD_CURSORSHIFT | LCD_DISPLAYMOVE | scrollDirection);
    }

    /**
     * Sets the text flow direction. (Left to Right OR Right to Left).
     *
     * Ex: If you specify 'left to right', succeeding characters written will flow from left to right.
     *
     * @param textDirection
     *         The flow of text direction.
     */
    @Override
    public void textDirection(TextDirection textDirection) {
        setInstruction(LcdInstruction.DISPLAY_MODE, LCD_ENTRYLEFT, textDirection == TextDirection.LEFT_TO_RIGHT);
        command(LCD_ENTRYMODESET | this.displayMode);
    }

    /**
     * Create custom lcd characters
     *
     * @param num
     *         The number representing the character (0 to 7 range)
     * @param charData
     *         The custom character data
     */
    @Override
    public void createChar(int num, byte[] charData) {
        if (num > 7)
            throw new IllegalArgumentException("You can only specify a maximum of 8 custom characters (0 to 7)");
        if (charData.length > 8 || charData.length < 8)
            throw new IllegalArgumentException("A total of 8 bytes of data are required to define a custom character");
        num &= 0x7; // we only have 8 locations 0-7
        command(LCD_SETCGRAMADDR | (num << 3));
        for (int i = 0; i < 8; i++)
            write(charData[i]);
        command(LCD_SETDDRAMADDR);
    }

    /**
     * Sets the current cursor position
     *
     * @param row
     *         Number representing a row on the lcd where 0 indicates the first row.
     * @param column
     *         Number representing a column on the lcd where 0 indicates the first column.
     */
    @Override
    public void setCursor(final int column, final int row) {
        //fail fast
        if (column > this.cols - 1 || row > this.rows - 1)
            throw new IllegalArgumentException(String.format("Cursor out of bounds (Row: %d, Col: %d, Max Rows: %d, Max Cols: %d)", row, column, getHeight(), getWidth()));
        command(LCD_SETDDRAMADDR | (this.rowOffsets[row] + column));
    }

    /**
     * Send Data (Set RS to HIGH)
     *
     * @param data
     *         The byte data to be sent to the lcd
     */
    @Override
    public void write(byte... data) {
        for (byte b : data)
            send(b, LcdRegisterSelectState.DATA);
    }

    /**
     * Update the starting row offsets. Max of 4 rows supported.
     *
     * @param row0
     *         Row 0 Offset (1st row)
     * @param row1
     *         Row 1 Offest (2nd row)
     * @param row2
     *         Row 2 Offest (3rd row)
     * @param row3
     *         Row 3 Offset (4th row)
     */
    private void setRowOffsets(int row0, int row1, int row2, int row3) {
        this.rowOffsets[0] = (byte) row0;
        this.rowOffsets[1] = (byte) row1;
        this.rowOffsets[2] = (byte) row2;
        this.rowOffsets[3] = (byte) row3;
    }

    /**
     * Send Command (Set RS to LOW)
     *
     * @param cmd
     */
    private void command(int cmd) {
        send((byte) cmd, LcdRegisterSelectState.COMMAND);
    }

    /**
     * <p>Sends data/commands through the underlying interface</p>. This method is thread-safe.
     *
     * @param value
     *         The byte value to send over the interface
     * @param regSelectState
     *         Indicate whether you are sending in command mode or in data mode. Set {@link PinState#LOW} to set
     *         in <code>COMMAND</code> mode. Set to {@link PinState#HIGH} for <code>DATA</code> mode.
     */
    private void send(byte value, LcdRegisterSelectState regSelectState) {
        try {
            //Set Register Select State (Command or Data)
            lcdGpioAdapter.setRegSelectState(regSelectState);

            // If we have a mapped RW pin, make sure to set it to LOW (Write Operation)
            lcdGpioAdapter.setReadWriteState(LcdReadWriteState.WRITE);

            //Are we in 4-bit or 8-bit mode?
            if ((this.displayFunction & LCD_8BITMODE) != 0) {
                lcdGpioAdapter.write8Bits(value);
            } else {
                lcdGpioAdapter.write4Bits((byte) (value >> 4));
                lcdGpioAdapter.write4Bits(value);
            }
        } catch (IOException e) {
            throw new DisplayDriverException(String.format("Error occured during send(%s,  %s)", value, regSelectState), e);
        }
    }

    private void setInstruction(LcdInstruction instruction, byte bitAddress, boolean bitState) {
        switch (instruction) {
            case DISPLAY_MODE:
                this.displayMode = updateAndGetBit(this.displayMode, bitAddress, bitState);
                return;
            case DISPLAY_FUNCTION:
                this.displayFunction = updateAndGetBit(this.displayFunction, bitAddress, bitState);
                return;
            case DISPLAY_CONTROL:
                this.displayControl = updateAndGetBit(this.displayControl, bitAddress, bitState);
                return;
        }
        throw new IllegalStateException(String.format("Invalid LCD instruction '%s'", instruction));
    }

    /**
     * Toggle bit on/off
     *
     * @param bitFunction
     *         The byte value to be combined with the address
     * @param bitAddress
     *         The address of the bit to set
     * @param bitState
     *         The state of the bit
     *
     * @return The modified byte value
     */
    private byte updateAndGetBit(short bitFunction, byte bitAddress, boolean bitState) {
        return (byte) (bitState ? bitFunction | bitAddress : bitFunction & ~bitAddress);
    }
}
