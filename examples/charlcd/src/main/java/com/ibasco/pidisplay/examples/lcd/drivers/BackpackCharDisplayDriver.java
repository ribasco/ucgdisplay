package com.ibasco.pidisplay.examples.lcd.drivers;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;
import com.ibasco.pidisplay.core.enums.ScrollDirection;
import com.ibasco.pidisplay.core.enums.TextDirection;
import com.ibasco.pidisplay.examples.lcd.exceptions.BackpackDriverException;
import com.ibasco.pidisplay.examples.lcd.exceptions.I2CException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * <p>A proxy interface which allows control of the LCD via external microcontrollers (e.g. Arduino).</p>
 * <p>
 * Note: This class is NOT thread safe.
 *
 * @author Rafael Ibasco
 */
public class BackpackCharDisplayDriver implements CharDisplayDriver {
    private static final Logger log = LoggerFactory.getLogger(BackpackCharDisplayDriver.class);

    public static final int MSG_LCD = 0x78;
    public static final int MSG_LCD_GETSTATUS = 0x79;
    public static final int MSG_LCD_ACQUIRE = 0x7A;
    //public static final int MSG_LCD_RELEASE = 0x7B;
    public static final int MSG_LCD_SETBLIDLE = 0x7C;
    public static final int MSG_LCD_GETBLIDLE = 0x7D;
    public static final int MSG_LCD_HOME = 0x7E;
    public static final int MSG_LCD_DISPLAY = 0x7F;
    public static final int MSG_LCD_BLINK = 0x80;
    public static final int MSG_LCD_CURSOR = 0x81;
    public static final int MSG_LCD_AUTOSCROLL = 0x82;
    public static final int MSG_LCD_SCROLLDISPLAY = 0x83;
    public static final int MSG_LCD_TEXTDIRECTION = 0x84;
    public static final int MSG_LCD_CREATECHAR = 0x85;
    public static final int MSG_LCD_CLEAR = 0x86;
    public static final int MSG_LCD_SETCURSOR = 0x87;
    public static final int MSG_LCD_WRITE = 0x88;

    private int width;

    private int height;

    private BackpackDriver driver;

    public BackpackCharDisplayDriver(int width, int height, BackpackDriver driver) {
        this.width = width;
        this.height = height;
        this.driver = driver;
    }

    @Override
    public void home() {
        sendCommand(MSG_LCD_HOME, true);
    }

    @Override
    public void display(boolean state) {
        sendCommand(MSG_LCD_DISPLAY, state);
    }

    @Override
    public void blink(boolean state) {
        sendCommand(MSG_LCD_BLINK, state);
    }

    @Override
    public void cursor(boolean state) {
        sendCommand(MSG_LCD_CURSOR, state);
    }

    @Override
    public void autoscroll(boolean state) {
        sendCommand(MSG_LCD_AUTOSCROLL, state);
    }

    @Override
    public void scrollDisplay(ScrollDirection scrollDirection) {
        sendCommand(MSG_LCD_SCROLLDISPLAY, scrollDirection == ScrollDirection.LEFT);
    }

    @Override
    public void textDirection(TextDirection textDirection) {
        sendCommand(MSG_LCD_TEXTDIRECTION, textDirection == TextDirection.LEFT_TO_RIGHT);
    }

    @Override
    public void createChar(int num, byte[] charData) {
        if (charData == null)
            throw new NullPointerException("Char data must not be null");
        if (charData.length != 8)
            throw new IllegalArgumentException("Invalid char data size");
        if (num > 7)
            throw new IllegalArgumentException("Invalid character location. Number must be between 0 to 7");
        byte[] payload = new byte[9];
        payload[0] = (byte) num;
        System.arraycopy(charData, 0, payload, 1, 8);
        //sendCommand(MSG_LCD_CREATECHAR, payload);

        try {
            ResponsePacket response = sendRequest(MSG_LCD_CREATECHAR, payload).get();
            byte location = response.getPayload().get();
            if (response.isSuccess()) {
                log.info("Create char request sent: {}", location);
            } else {
                log.error("Problem create char for location: {}", location);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setBacklightIdleTime(int idleTime) {
        ByteBuffer payload = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        payload.putInt(idleTime);
        sendCommand(MSG_LCD_SETBLIDLE, payload.array());
    }

    public int getBacklightIdleTime() {
        try {
            ResponsePacket response = sendRequest(MSG_LCD_GETBLIDLE).get();
            if (response.isSuccess()) {
                return response.getPayload().getInt();
            } else {
                throw new RuntimeException("Error in response: " + response.getStatus() + " for header " + response.getRequestHeader());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAvailable() throws I2CException {
        boolean available = false;
        try {
            ResponsePacket response = sendRequest(MSG_LCD_GETSTATUS).get();
            if (response.isSuccess()) {
                available = response.getPayload().get() == 0x1;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new I2CException(e);
        }
        return available;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void clear() {
        sendCommand(MSG_LCD_CLEAR, true);
    }

    @Override
    public void setCursor(int x, int y) {
        sendCommand(MSG_LCD_SETCURSOR, (byte) x, (byte) y);
    }

    public BackpackDriver getDriver() {
        return driver;
    }

    @Override
    public void write(byte... data) {
        sendCommand(MSG_LCD_WRITE, data);
    }

    private void sendCommand(int requestHeader, boolean value) {
        sendCommand(requestHeader, (byte) ((value) ? 1 : 0));
    }

    private void sendCommand(int requestHeader, byte... data) {
        try {
            ByteBuffer tmp = ByteBuffer.allocate(data.length + 1).order(ByteOrder.LITTLE_ENDIAN);
            tmp.put((byte) requestHeader);
            tmp.put(data);
            driver.sendCommand((byte) MSG_LCD, tmp.array());
        } catch (BackpackDriverException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<ResponsePacket> sendRequest(int requestHeader, byte... data) {
        ByteBuffer tmp = ByteBuffer.allocate(data.length + 1).order(ByteOrder.LITTLE_ENDIAN);
        tmp.put((byte) requestHeader);
        if (data.length > 0)
            tmp.put(data);
        return driver.sendRequest(MSG_LCD, tmp.array());
    }
}
