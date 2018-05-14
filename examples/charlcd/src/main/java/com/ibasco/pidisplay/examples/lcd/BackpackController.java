package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.examples.lcd.drivers.BackpackCharDisplayDriver;
import com.ibasco.pidisplay.examples.lcd.drivers.BackpackI2CDriver;
import com.ibasco.pidisplay.examples.lcd.drivers.Packet;
import com.ibasco.pidisplay.examples.lcd.drivers.ResponsePacket;
import com.ibasco.pidisplay.examples.lcd.exceptions.BackpackDriverException;
import com.ibasco.pidisplay.examples.lcd.exceptions.I2CException;
import com.ibasco.pidisplay.examples.lcd.exceptions.PacketException;
import com.pi4j.io.serial.*;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutionException;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("Duplicates")
public class BackpackController implements SerialDataEventListener, Closeable {

    private static final Logger log = getLogger(BackpackController.class);

    private BackpackI2CDriver i2cDriver;
    private BackpackCharDisplayDriver displayDriver;
    private Serial serialDriver;

    public static final int RESPONSE_OK = 0x0;
    public static final int RESPONSE_ERR = 0x1;

    //Power status
    public static final int BOOT_LOCKED = -2; //if locked, the micro-controller will not be able to power-on the RPi until it has been manually unlocked
    public static final int BOOT_TIMEOUT = -1; //a timeout has occured while booting, rpi is not responding
    public static final int STOPPING = 4; //pi is shutting down
    public static final int HALT = 5; //pi is halted/suspended (power supply is still activated)
    public static final int SHUTDOWN = 6; //, //pi is completely shutdown (power is no longer active)
    public static final int BOOT = 1; //pi has been turned on and booting
    public static final int INIT = 2; //pi os has been initialized and accessible via ssh, main program is still loading
    public static final int READY = 3; //pi has completed boot process and main program is running

    //Instructions
    private static final int MSG_SYS = 0x0;
    private static final int MSG_SYS_GETFREEMEM = 0x1;
    private static final int MSG_SYS_GETSTATUS = 0x2;
    private static final int MSG_SYS_SETSTATUS = 0x3;
    public static final int MSG_SYS_EEPROM_GET = 0x4;
    public static final int MSG_SYS_EEPROM_SET = 0x5;

    private static final int MSG_LED = 0x3C;
    private static final int MSG_LED_ON = 0X3D;
    private static final int MSG_LED_OFF = 0x3E;
    private static final int MSG_LED_FADE = 0x3F;
    private static final int MSG_LED_BLINK = 0x40;
    private static final int MSG_LED_PULSE = 0x41;

    private static final int MSG_7SEG = 0x8C;
    private static final int MSG_7SEG_SETSTATE = 0x8D;
    private static final int MSG_7SEG_SETCOLON_STATE = 0x8E;
    private static final int MSG_7SEG_SETCOLON_BLINK_STATE = 0x8F;
    private static final int MSG_7SEG_SETCOLON_BLINK_INTERVAL = 0x90;
    private static final int MSG_7SEG_SETEFFECT = 0x91;
    private static final int MSG_7SEG_SETEFFECT_STATE = 0x92;
    private static final int MSG_7SEG_SETTIME = 0x93;
    private static final int MSG_7SEG_SETNUMBER = 0x94;
    private static final int MSG_7SEG_SETDIGIT_BLINK_STATE = 0x95;
    private static final int MSG_7SEG_SETDIGIT_BLINK_INTERVAL = 0x96;
    private static final int MSG_7SEG_BRIGHTNESS = 0x97;
    private static final int MSG_7SEG_SETHOUR = 0x98;
    private static final int MSG_7SEG_SETMINUTES = 0x99;

    private static final int MSG_RELAY = 0x64;
    private static final int MSG_RELAY_SETSTATE = 0x65;
    private static final int MSG_RELAY_GETSTATE = 0x66;

    public static final int ROTARY_NONE = 0x0;
    public static final int ROTARY_LEFT = 0x1;
    public static final int ROTARY_RIGHT = 0x2;
    public static final int ROTARY_PRESS = 0x3;
    public static final int BUTTON_PRESSED = 0x4;
    public static final int BUTTON_RELEASED = 0x5;
    public static final int BUTTON_HELD = 0x6;
    public static final int BUTTON_LONG_PRESS = 0x7;
    public static final int TOGGLE_SW_STATE = 0x8;

    @Override
    public void close() throws IOException {
        serialDriver.close();
        i2cDriver.close();
    }

    @FunctionalInterface
    public interface BackpackInputEventHandler {
        void handleEvent(int eventCode, int eventValue);
    }

    private BackpackInputEventHandler inputEventHandler;

    public enum SegmentEffect {
        NONE(0),
        ROTATING(1),
        KNIGHT_RIDER(2);

        int code;

        SegmentEffect(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }


    public enum LedType {
        LCD(0),
        STATUS(1),
        TOGGLE_SWITCH(2),
        SNOOZE(3);

        int code;

        LedType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public BackpackController(BackpackI2CDriver i2cDriver) {
        this.i2cDriver = i2cDriver;
        this.displayDriver = new BackpackCharDisplayDriver(20, 4, i2cDriver);
        this.serialDriver = createSerial();
        assert serialDriver != null;
        serialDriver.addListener();
    }

    public void setInputEventHandler(BackpackInputEventHandler eventHandler) {
        this.inputEventHandler = eventHandler;
    }

    @Override
    public void dataReceived(SerialDataEvent event) {
        try {
            log.info("Serial Data received");
            byte[] data = event.getBytes();

            ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            int ctr = 0;

            while (buf.remaining() >= 5) {
                ctr++;
                Packet p = new Packet(buf);

                //Handle input events
                if (p.getHeader() == 0x6 && p.getSize() == 2) {
                    byte eventCode = p.getPayload().get();
                    byte eventValue = p.getPayload().get();
                    inputEventHandler.handleEvent(eventCode, eventValue);
                }
                //Handle display events
                else if (p.getHeader() == 0x78) {
                    byte data1 = p.getPayload().get();
                    byte data2 = p.getPayload().get();
                    log.info("Display Event (Data 1: {}, Data 2: {})", data1, data2);

                } else {
                    log.warn("Unhandled serialDriver event (Header: {}, Size: {})", p.getHeader(), p.getSize());
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (PacketException e) {
            e.printStackTrace();
        }
    }

    public Serial getSerialDriver() {
        return serialDriver;
    }

    public BackpackI2CDriver getI2CDriver() {
        return i2cDriver;
    }

    public BackpackCharDisplayDriver getCharDisplayDriver() {
        return displayDriver;
    }

    private Serial createSerial() {
        // create an instance of the serialDriver communications class
        final Serial serial = SerialFactory.createInstance();
        try {
            // create serialDriver config object
            SerialConfig config = new SerialConfig();

            // set default serialDriver settings (device, baud rate, flow control, etc)
            ///dev/ttyS0
            config.device("/dev/ttyAMA0")
                    .baud(Baud._115200)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            // display connection details
            log.info(" Connecting to: " + config.toString(),
                    " We are sending ASCII data on the serialDriver port every 1 second.",
                    " Data received on serialDriver port will be displayed below.");

            // open the default serialDriver device/port with the configuration settings
            serial.open(config);

            log.info("Now listening for serialDriver events");
            return serial;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public static int RGB(int red, int green, int blue, int white) {
        return white | blue << 8 | green << 16 | red << 24;
    }

    public boolean getRelayState(int relayNum) {
        try {
            ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_RELAY_GETSTATE);
            payload.put((byte) relayNum);
            ResponsePacket response = i2cDriver.sendRequest((byte) MSG_RELAY, payload.array()).get();
            byte num = response.getPayload().get();
            if (response.isSuccess() && (relayNum == num)) {
                byte state = response.getPayload().get();
                return (state == 0x1);
            }
            return false;
        } catch (InterruptedException | ExecutionException e) {
            throw new BackpackDriverException(e);
        }
    }

    public boolean setRelayState(int relayNum, boolean on) {
        try {
            ByteBuffer payload = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_RELAY_SETSTATE);
            payload.put((byte) relayNum);
            payload.put((byte) (on ? 1 : 0));
            ResponsePacket response = i2cDriver.sendRequest((byte) MSG_RELAY, payload.array()).get();
            log.info("Got response for relay: {}", response.getPayload().get());
            return response.isSuccess();
        } catch (InterruptedException | ExecutionException e) {
            throw new BackpackDriverException(e);
        }
    }

    public void setSegmentState(boolean on) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETSTATE);
        payload.put((byte) (on ? 0x1 : 0x0));
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentEffect(SegmentEffect effect) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETEFFECT);
        payload.put((byte) effect.getCode());
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentEffectState(boolean on) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETEFFECT_STATE);
        payload.put((byte) (on ? 0x1 : 0x0));
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentColonState(boolean on) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETCOLON_STATE);
        payload.put((byte) (on ? 0x1 : 0x0));
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentColonBlinkState(boolean on) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETCOLON_BLINK_STATE);
        payload.put((byte) (on ? 0x1 : 0x0));
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentColonBlinkInterval(int interval) {
        ByteBuffer payload = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETCOLON_BLINK_INTERVAL);
        payload.putInt(interval);
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentTime(int hour, int minutes) {
        ByteBuffer payload = ByteBuffer.allocate(9).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETTIME);
        payload.putInt(hour);
        payload.putInt(minutes);
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentNumber(int number) {
        ByteBuffer payload = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETNUMBER);
        payload.putInt(number);
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentDigitBlinkState(boolean on, int pos) {
        ByteBuffer payload = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETDIGIT_BLINK_STATE);
        payload.put((byte) (on ? 0x1 : 0x0));
        payload.put((byte) pos);
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentDigitBlinkInterval(int interval) {
        ByteBuffer payload = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETDIGIT_BLINK_INTERVAL);
        payload.putInt(interval);
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    /**
     * Set segment display brightness
     *
     * @param brightness Brightness level (Min: 0, Max: 7)
     */
    public void setSegmentBrightness(int brightness) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_BRIGHTNESS);
        payload.put((byte) brightness);
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentHour(int hour) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.put((byte) MSG_7SEG_SETHOUR);
        payload.put((byte) hour);
        i2cDriver.sendCommand((byte) MSG_7SEG, payload);
    }

    public void setSegmentMinutes(int minutes) throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_7SEG_SETMINUTES);
            payload.put((byte) minutes);
            i2cDriver.sendCommand((byte) MSG_7SEG, payload);
        } catch (BackpackDriverException e) {
            throw new I2CException(e);
        }
    }

    public void setLedOn(LedType type, int brightnessPct, int color) throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(7).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_LED_ON);
            payload.put((byte) type.getCode());
            payload.put((byte) brightnessPct);
            payload.putInt(color);
            i2cDriver.sendCommand((byte) MSG_LED, payload.array());
        } catch (BackpackDriverException e) {
            throw new I2CException(e);
        }
    }

    public void setLedOff(LedType type) throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_LED_OFF);
            payload.put((byte) type.getCode());
            i2cDriver.sendCommand((byte) MSG_LED, payload.array());
        } catch (BackpackDriverException e) {
            throw new I2CException(e);
        }
    }

    public void setLedBlink(LedType type, int brightness, int interval, int color) throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(11).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_LED_BLINK);
            payload.put((byte) type.getCode());
            payload.put((byte) brightness);
            payload.putInt(interval);
            payload.putInt(color);
            i2cDriver.sendCommand((byte) MSG_LED, payload.array());
        } catch (BackpackDriverException e) {
            throw new I2CException(e);
        }
    }

    public void setLedPulse(LedType type, int brightness, int interval, int color) throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(11).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_LED_PULSE);
            payload.put((byte) type.getCode());
            payload.put((byte) brightness);
            payload.putInt(interval);
            payload.putInt(color);
            i2cDriver.sendCommand((byte) MSG_LED, payload.array());
        } catch (BackpackDriverException e) {
            throw new I2CException(e);
        }
    }

    public void setLedFade(LedType type, int interval, int color) throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(10).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_LED_FADE);
            payload.put((byte) type.getCode());
            payload.putInt(interval);
            payload.putInt(color);
            i2cDriver.sendCommand((byte) MSG_LED, payload.array());
        } catch (BackpackDriverException e) {
            throw new I2CException(e);
        }
    }

    public int getSysFreeMem() throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_SYS_GETFREEMEM);

            ResponsePacket response = i2cDriver.sendRequest((byte) MSG_SYS, payload.array()).get();

            if (response.getRequestHeader() == MSG_SYS_GETFREEMEM && response.isSuccess()) {
                return response.getPayload().getInt();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new I2CException(e);
        }
        return -1;
    }

    public boolean setSysStatus(int statusCode) throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_SYS_SETSTATUS);
            payload.putInt(statusCode);

            ResponsePacket response = i2cDriver.sendRequest((byte) MSG_SYS, payload.array()).get();
            return response.getRequestHeader() == MSG_SYS_SETSTATUS && response.isSuccess();
        } catch (InterruptedException | ExecutionException e) {
            throw new I2CException(e);
        }
    }

    public int getSysStatus() throws I2CException {
        try {
            ByteBuffer payload = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
            payload.put((byte) MSG_SYS_GETSTATUS);
            ResponsePacket response = i2cDriver.sendRequest((byte) MSG_SYS, payload.array()).get();
            int requestHeader = response.getRequestHeader();
            if (requestHeader == MSG_SYS_GETSTATUS && response.isSuccess()) {
                return response.getPayload().getInt();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new I2CException(e);
        }
        return -1;
    }

    private void testLed() throws I2CException {
        log.info("Led On");
        setLedOn(LedType.STATUS, 50, RGB(0, 255, 0, 0));
        ThreadUtils.sleep(3000);

        log.info("Led Off");
        setLedOff(LedType.STATUS);
        ThreadUtils.sleep(3000);

        log.info("Led Fade");
        setLedFade(LedType.STATUS, 1, RGB(0, 255, 0, 0));
        ThreadUtils.sleep(3000);

        log.info("Led Blink");
        setLedBlink(LedType.STATUS, 100, 100, RGB(0, 255, 0, 0));
        ThreadUtils.sleep(3000);

        log.info("Led Pulse");
        setLedPulse(LedType.STATUS, 100, 100, RGB(0, 255, 0, 0));
        ThreadUtils.sleep(3000);
    }

    private void testSevenSegment() {
        log.info("Segment: ON");
        setSegmentState(true);
        ThreadUtils.sleep(3000);

        log.info("Segment: OFF");
        setSegmentState(false);
        ThreadUtils.sleep(3000);

        log.info("Segment: ON");
        setSegmentState(true);
        ThreadUtils.sleep(3000);

        log.info("Segment Colon Blink: ON");
        setSegmentColonBlinkState(true);
        ThreadUtils.sleep(10000);

        log.info("Segment Colon Blink: OFF");
        setSegmentColonBlinkState(false);
        ThreadUtils.sleep(5000);

        log.info("Segment Colon Blink Interval to 100");
        setSegmentColonBlinkState(true);
        setSegmentColonBlinkInterval(100);
        ThreadUtils.sleep(10000);
        setSegmentColonBlinkState(false);

        log.info("Segment Colon: ON");
        setSegmentColonState(true);
        ThreadUtils.sleep(3000);

        log.info("Segment Colon: OFF");
        setSegmentColonState(false);
        ThreadUtils.sleep(3000);

        log.info("Segment Effect: ROTATING");
        setSegmentEffect(SegmentEffect.ROTATING);
        setSegmentEffectState(true);
        ThreadUtils.sleep(3000);

        log.info("Segment Effect: KNIGHT RIDER");
        setSegmentEffect(SegmentEffect.KNIGHT_RIDER);
        ThreadUtils.sleep(3000);

        log.info("Segment Effect: NONE");
        setSegmentEffect(SegmentEffect.NONE);
        ThreadUtils.sleep(3000);

        log.info("Segment Effect: STOP");
        setSegmentEffectState(false);
        ThreadUtils.sleep(3000);

        setSegmentState(true);

        log.info("Segment Set Time: 12:30");
        setSegmentTime(12, 30);
        ThreadUtils.sleep(3000);

        log.info("Segment Set Number: 9999");
        setSegmentNumber(9999);
        ThreadUtils.sleep(3000);

        log.info("Segment Digit Blink: 0");
        setSegmentDigitBlinkState(true, 0);
        ThreadUtils.sleep(1000);

        log.info("Segment Digit Blink: 1");
        setSegmentDigitBlinkState(true, 1);
        ThreadUtils.sleep(1000);

        log.info("Segment Digit Blink: 2");
        setSegmentDigitBlinkState(true, 2);
        ThreadUtils.sleep(1000);

        log.info("Segment Digit Blink: 3");
        setSegmentDigitBlinkState(true, 3);
        ThreadUtils.sleep(1000);

        log.info("Segment Digit Blink Interval: 2000");
        setSegmentDigitBlinkInterval(2000);
        ThreadUtils.sleep(3000);

        log.info("Segment Set Brightness");
        for (int i = 0; i < 8; i++) {
            log.info("Brightness = {}", i);
            setSegmentBrightness(i);
            ThreadUtils.sleep(2000);
        }
    }
}
