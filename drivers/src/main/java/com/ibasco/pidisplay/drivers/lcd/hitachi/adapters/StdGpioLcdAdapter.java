package com.ibasco.pidisplay.drivers.lcd.hitachi.adapters;

import com.ibasco.pidisplay.drivers.lcd.hitachi.BaseLcdGpioAdapter;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdPinMapConfig;
import com.ibasco.pidisplay.drivers.lcd.hitachi.enums.LcdPin;
import com.ibasco.pidisplay.drivers.lcd.hitachi.enums.LcdReadWriteState;
import com.ibasco.pidisplay.drivers.lcd.hitachi.enums.LcdRegisterSelectState;
import com.ibasco.pidisplay.drivers.lcd.hitachi.exceptions.InvalidPinMappingException;
import com.ibasco.pidisplay.drivers.lcd.hitachi.exceptions.LcdPinNotMappedException;
import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.pi4j.wiringpi.Gpio.delayMicroseconds;

/**
 * A Standard GPIO Lcd Adapter that should work with any {@link GpioProvider} implementation.
 *
 * @author Rafael Ibasco
 */
public class StdGpioLcdAdapter extends BaseLcdGpioAdapter {

    private static final Logger log = LoggerFactory.getLogger(StdGpioLcdAdapter.class);

    private GpioController controller = GpioFactory.getInstance();

    private GpioProvider provider;

    private GpioPinDigitalOutput rsPin;

    private GpioPinDigitalOutput enPin;

    private GpioPinDigitalMultipurpose rwPin;

    private GpioPinDigitalOutput[] dataPins = new GpioPinDigitalOutput[8];

    public StdGpioLcdAdapter(LcdPinMapConfig pinMapConfig) {
        this(GpioFactory.getDefaultProvider(), pinMapConfig);
    }

    public StdGpioLcdAdapter(GpioProvider provider, LcdPinMapConfig pinMapConfig) throws IllegalArgumentException {
        super(pinMapConfig);
        this.provider = provider;
    }

    @Override
    protected void validate(LcdPinMapConfig pinMapConfig) throws InvalidPinMappingException {
        for (LcdPin pin : LcdPin.getAllPins()) {
            if (pin.isRequired() && !isMapped(pin))
                throw new InvalidPinMappingException(pinMapConfig);
        }
    }

    @Override
    public void initialize() {
        this.rsPin = setupLcdPin(LcdPin.RS, PinState.LOW);
        this.enPin = setupLcdPin(LcdPin.EN, PinState.LOW);

        //Setup data pins
        final LcdPin[] allDataPins = LcdPin.getAllDataPins();
        for (int i = 0; i < allDataPins.length; i++) {
            this.dataPins[i] = setupLcdPin(allDataPins[i], PinState.LOW);
        }

        if (isMapped(LcdPin.RW)) {
            this.rwPin = this.controller.provisionDigitalMultipurposePin(this.provider, getMappedPin(LcdPin.RW),
                    LcdPin.RW.getName(), PinMode.DIGITAL_OUTPUT);
        }
    }

    @Override
    public void write4Bits(byte value) throws IOException {
        for (int i = 0; i < 4; i++)
            dataPins[i + 4].setState(PinState.getState((value >> i) & 0x01));
        pulseEnable();
    }

    @Override
    public void write8Bits(byte value) throws IOException {
        for (int i = 0; i < 8; i++)
            dataPins[i].setState(PinState.getState((value >> i) & 0x01));
    }

    @Override
    public void setRegSelectState(LcdRegisterSelectState state) {
        this.rsPin.setState(state.getPinState());
    }

    @Override
    public void setReadWriteState(LcdReadWriteState state) {
        if (isMapped(LcdPin.RW))
            this.rwPin.setState(state.getPinState());
    }

    @Override
    public void setEnableState(PinState state) {
        this.enPin.setState(state);
    }

    /**
     * Pulse the {@link LcdPin#EN} pin
     */
    private void pulseEnable() {
        this.enPin.low();
        this.enPin.high();
        this.enPin.low();
        delayMicroseconds(100);
    }

    private GpioPinDigitalOutput setupLcdPin(LcdPin lcdPin, PinState defaultState) {
        Pin mappedPin = getMappedPin(lcdPin);
        if (lcdPin.isRequired() && mappedPin == null)
            throw new LcdPinNotMappedException(lcdPin);
        GpioPinDigitalOutput out = (mappedPin != null) ? this.controller.provisionDigitalOutputPin(this.provider, mappedPin, lcdPin.getName(), defaultState) : null;
        if (out != null)
            log.debug("Successfully Provisioned Output Pin for '{}' with Mode '{}' (Mapped To: {})", out.getName(), out.getMode().getName(), mappedPin.getName());
        else
            log.debug("Could not provision pin for {}", lcdPin.getName());
        return out;
    }
}
