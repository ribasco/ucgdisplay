package com.ibasco.pidisplay.drivers.lcd.adapters;

import com.ibasco.pidisplay.drivers.lcd.BaseLcdGpioAdapter;
import com.ibasco.pidisplay.drivers.lcd.LcdPinMapConfig;
import com.ibasco.pidisplay.drivers.lcd.enums.LcdPin;
import com.ibasco.pidisplay.drivers.lcd.enums.LcdReadWriteState;
import com.ibasco.pidisplay.drivers.lcd.enums.LcdRegisterSelectState;
import com.ibasco.pidisplay.drivers.lcd.exceptions.InvalidPinMappingException;
import com.ibasco.pidisplay.drivers.lcd.pins.ShiftOutRegPin;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Shift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Shift Register LCD Adapter. Applicable only for Shift Out registers (e.g. 74HC595). Please note that daisy chained
 * shift-out registers
 * are not yet supported at this time.
 *
 * @author Rafael Ibasco
 */
//TODO: Add support for daisy chained shift-out registers
public class ShiftRegLcdAdapter extends BaseLcdGpioAdapter {

    private static final Logger log = LoggerFactory.getLogger(ShiftRegLcdAdapter.class);

    /**
     * Holds the current state of the shift register
     *
     * <pre>
     * ================================================
     * Local Address to Shift-Register Mapping:
     * ================================================
     *  Address #:  00 01 02 03 04 05 06 07
     *   SR Pin #:  QA QB QC QD QE QF QG QH
     * </pre>
     */
    private byte state;

    /**
     * The GPIO Provider for driving the data, latch and clock pins
     */
    private GpioProvider provider;

    /**
     * Shift Register Data Output Pin
     */
    private GpioPinDigitalOutput dataPin;

    /**
     * Shift Register Latch Output Pin
     */
    private GpioPinDigitalOutput latchPin;

    /**
     * Shift Register Clock Output Pin
     */
    private GpioPinDigitalOutput clockPin;

    /**
     * Default Constructor.
     *
     * @param provider
     *         The {@link GpioProvider} that will be driving the Data, Latch and Clock pins of the shift register
     * @param dataPin
     *         The data pin of the Shift Register
     * @param latchPin
     *         The latch pin of the Shift Register
     * @param clockPin
     *         The clock pin of the Shift Register
     * @param lcdPinMap
     *         The {@link LcdPinMapConfig} instance containing the pin mapping configuration
     *
     * @throws IllegalArgumentException
     *         Thrown when pin mapping validation fails
     */
    public ShiftRegLcdAdapter(GpioProvider provider, Pin dataPin, Pin latchPin, Pin clockPin, LcdPinMapConfig lcdPinMap) throws IllegalArgumentException {
        super(lcdPinMap);
        GpioController controller = GpioFactory.getInstance();
        this.provider = provider;
        //Provision Output Pins for Data, Latch and Clock
        this.dataPin = controller.provisionDigitalOutputPin(provider, dataPin, "Shift Register - Data Pin", PinState.LOW);
        this.latchPin = controller.provisionDigitalOutputPin(provider, latchPin, "Shift Register - Latch Pin", PinState.LOW);
        this.clockPin = controller.provisionDigitalOutputPin(provider, clockPin, "Shift Register - Clock Pin", PinState.LOW);
    }

    @Override
    public void initialize() {
        log.debug("Initialized Shift Register Adapter");
        log.debug("Using Data Pin: {}", this.dataPin.getPin().getAddress());
        log.debug("Using Latch Pin: {}", this.latchPin.getPin().getAddress());
        log.debug("Using Data Pin: {}", this.clockPin.getPin().getAddress());
    }

    @Override
    protected void validate(LcdPinMapConfig pinMapConfig) throws InvalidPinMappingException {
        for (Map.Entry<LcdPin, Pin> pin : pinMapConfig.getAllPins()) {
            //make sure we are only using the Shift Out Register Pins
            if (!ShiftOutRegPin.PROVIDER_NAME.equals(pin.getValue().getProvider()))
                throw new InvalidPinMappingException(String.format("Pin Provider not supported '%s'. Please use pins from the ShiftOutRegPin class", pin.getValue().getProvider()), pinMapConfig);
            //verify that the pin supports digital output mode
            if (pin.getValue().getSupportedPinModes().contains(PinMode.DIGITAL_OUTPUT)) {
                throw new InvalidPinMappingException("The provider does not support digital output mode", pinMapConfig);
            }
        }
    }

    @Override
    public void write4Bits(byte value) throws IOException {
        byte tmpState = (byte) (value & 0x0F);

        setPinValue(LcdPin.DATA_4, tmpState & 0x1);
        tmpState >>= 1;
        setPinValue(LcdPin.DATA_5, tmpState & 0x1);
        tmpState >>= 1;
        setPinValue(LcdPin.DATA_6, tmpState & 0x1);
        tmpState >>= 1;
        setPinValue(LcdPin.DATA_7, tmpState & 0x1);

        pulseEnable();
    }

    @Override
    public void write8Bits(byte value) {
        throw new RuntimeException("Woops Not yet implemented");
    }

    @Override
    public void setRegSelectState(LcdRegisterSelectState state) {
        setPinValue(LcdPin.RS, state.getPinState());
    }

    @Override
    public void setReadWriteState(LcdReadWriteState state) {
        if (isMapped(LcdPin.RW))
            setPinValue(LcdPin.RW, state.getPinState());
    }

    @Override
    public void setEnableState(PinState state) {
        setPinValue(LcdPin.EN, state);
    }

    /**
     * Sends a signal to the designated {@link LcdPin}.
     *
     * @param pin
     *         The {@link LcdPin} to send a digital signal to
     * @param state
     *         The pin state to transmit (1 = HIGH, 0 = LOW)
     */
    private void setPinValue(LcdPin pin, int state) {
        setPinValue(pin, PinState.getState(state));
    }

    /**
     * Sends a signal to the designated {@link LcdPin}.
     *
     * @param pin
     *         The {@link LcdPin} to send a digital signal to
     * @param state
     *         The pin state to transmit
     */
    private void setPinValue(LcdPin pin, PinState state) {
        Pin mappedPin = Objects.requireNonNull(getMappedPin(pin), String.format("Lcd Pin '%s' does not have a valid mapping", pin.getName()));
        //convert to the actual bit address
        byte bitAddress = (byte) Math.pow(2, mappedPin.getAddress());
        if (state == PinState.HIGH)
            this.state |= bitAddress;
        else
            this.state &= ~bitAddress;
    }

    /**
     * Pulse the Enable pin from LOW to HIGH then back to LOW
     */
    private void pulseEnable() {
        setEnableState(PinState.LOW); //LOW
        shiftDataOut();
        setEnableState(PinState.HIGH);    // HIGH
        shiftDataOut();
        setEnableState(PinState.LOW); //LOW
        shiftDataOut();
    }

    /**
     * Shifts the 8-bit data out to the register.
     */
    private void shiftDataOut() {
        this.latchPin.low();
        Shift.shiftOut((byte) this.dataPin.getPin().getAddress(), (byte) this.clockPin.getPin().getAddress(), (byte) Shift.MSBFIRST, this.state);
        this.latchPin.high();
    }
}
