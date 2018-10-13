package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.drivers.lcd.hd44780.CharDisplayDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferOverflowException;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LcdCharGraphicsTest {

    public static final Logger log = LoggerFactory.getLogger(LcdCharGraphicsTest.class);

    CharDisplayDriver driver;

    LcdCharGraphics graphics;

    @BeforeEach
    void setUp() {
        driver = mock(CharDisplayDriver.class);
        when(driver.getWidth()).thenReturn(20);
        when(driver.getHeight()).thenReturn(4);
        graphics = new LcdCharGraphics(driver);
    }

    @Test
    void testHuh() throws Exception {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("HELLO");
            }
        }, 5000);
        Thread.sleep(10000);
    }

    @Test
    void testWriteFlush() {
        log.debug("Flush #1: ");
        graphics.setCursor(0, 0);
        graphics.drawText("12345 67890");
        graphics.setCursor(5, 2);
        graphics.drawText("12345");
        graphics.setCursor(16, 2);
        graphics.drawText("1");
        graphics.setCursor(8, 3);
        graphics.drawText("1234567 8901");
        graphics.flush();
        //assertEquals(29, graphics.bytesFlushed());

        log.debug("Flush #2: ");
        graphics.setCursor(0, 0);
        graphics.drawText("12345 67890");
        graphics.setCursor(5, 2);
        graphics.drawText("12345");
        graphics.setCursor(16, 2);
        graphics.drawText("1");
        graphics.setCursor(8, 3);
        graphics.drawText("1234567 8901");
        graphics.flush();
        //assertEquals(0, graphics.bytesFlushed());

        log.debug("Flush #3: ");
        graphics.setCursor(0, 0);
        graphics.drawText("12345 67890");
        graphics.setCursor(5, 2);
        graphics.drawText("12345");
        graphics.setCursor(16, 2);
        graphics.drawText("2");
        graphics.setCursor(8, 3);
        graphics.drawText("1234567 8901");
        graphics.flush();
        //assertEquals(1, graphics.bytesFlushed());

        graphics.setCursor(0, 0);
        graphics.drawText("123456789");
        graphics.flush();
    }

    @Test
    void testFillBuffer() {
        int size = graphics.getWidth() * graphics.getHeight();
        for (int i = 0; i < size; i++) {
            graphics.drawText("x");
        }
        assertThrows(BufferOverflowException.class, () -> graphics.drawText("x"));
    }
}