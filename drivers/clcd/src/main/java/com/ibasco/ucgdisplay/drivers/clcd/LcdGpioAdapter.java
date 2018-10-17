package com.ibasco.ucgdisplay.drivers.clcd;

import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdReadWriteState;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdRegisterSelectState;
import com.pi4j.io.gpio.PinState;

import java.io.IOException;

/**
 * The GPIO Adapter for the LCD.
 *
 * @author Rafael Ibasco
 */
public interface LcdGpioAdapter {
    /**
     * Perform any necessary initialization. (e.g. Setting pinmodes of digital output pins)
     */
    void initialize();

    /**
     * Sends a 4-bit value to the interface.
     *
     * @param value
     *         The 4-bit value to be transmitted over the LCD Interface
     *
     * @throws IOException
     *         Thrown when a problem occurs during transmission
     */
    void write4Bits(byte value) throws IOException;

    /**
     * Sends an 8-bit value to the interface.
     *
     * @param value
     *         The 8-bit value to be transmitted over the LCD Interface
     *
     * @throws IOException
     *         Thrown when a problem occurs during transmission
     */
    void write8Bits(byte value) throws IOException;

    /**
     * Sets the LCD's Register Select Pin state
     *
     * @param state
     *         The state of the Register Select Pin. Set to either  {@link LcdRegisterSelectState#COMMAND} or {@link
     *         LcdRegisterSelectState#DATA}
     */
    void setRegSelectState(LcdRegisterSelectState state);

    /**
     * Sets the LCD's Read/Write Pin state. Setting the state to WRITE will set the LCD RW pin to LOW while READ will
     * set the LCD RW pin to HIGH.
     *
     * @param state
     *         The state to set (READ/WRITE) for the LCD RW Pin
     */
    void setReadWriteState(LcdReadWriteState state);

    /**
     * Sets the LCD's Enable Pin state
     *
     * @param state
     *         The {@link PinState} to set for the LCD Enable pin.
     */
    void setEnableState(PinState state);
}
