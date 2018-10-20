package com.ibasco.ucgdisplay.components;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class RotaryEncoder {
    private static final Logger log = LoggerFactory.getLogger(RotaryEncoder.class);

    private Lock lock = new ReentrantLock(true);

    private final GpioPinDigitalInput inputA;
    private final GpioPinDigitalInput inputB;
    private final GpioController gpio;

    private long encoderValue = 0;
    private int lastEncoded = 0;
    private boolean firstPass = true;

    private RotaryEncoderListener listener;

    public RotaryEncoder(Pin pinA, Pin pinB, long initalValue) {
        encoderValue = initalValue;
        gpio = GpioFactory.getInstance();

        inputA = gpio.provisionDigitalInputPin(pinA, "PinA", PinPullResistance.PULL_UP);
        inputB = gpio.provisionDigitalInputPin(pinB, "PinB", PinPullResistance.PULL_UP);
        //inputA.setDebounce(64);
        //inputB.setDebounce(64);

        GpioPinListenerDigital inputListener = (GpioPinDigitalStateChangeEvent gpdsce) -> {
            log.info("Got Event: {} for {} = {}", gpdsce, gpdsce.getPin().getName(), gpdsce.getState());
            if (lock.tryLock()) {
                int stateA = inputA.getState().getValue();
                int stateB = inputB.getState().getValue();
                try {
                    calcEncoderValue(stateA, stateB);
                } finally {
                    lock.unlock();
                }
            }
        };

        inputA.addListener(inputListener);
        inputB.addListener(inputListener);
    }

    public long getValue() {
        return encoderValue;
    }

    public void setListener(RotaryEncoderListener listener) {
        this.listener = listener;
    }

    private void calcEncoderValue(int stateA, int stateB) {

        // converting the 2 pin value to single number to end up with 00, 01, 10 or 11
        int encoded = (stateA << 1) | stateB;

        if (firstPass) {
            firstPass = false;
        } else {
            // going up states, 01, 11
            // going down states 00, 10

            int sum = lastEncoded << 2 + encoded;
            if (sum == 13 || sum == 4 || sum == 2 || sum == 11) {
                encoderValue++;
                listener.change(encoderValue, RotaryState.DOWN);
            }
            if (sum == 14 || sum == 7 || sum == 1 || sum == 8) {
                encoderValue--;
                listener.change(encoderValue, RotaryState.UP);
            }
        }
        lastEncoded = encoded;
    }
}