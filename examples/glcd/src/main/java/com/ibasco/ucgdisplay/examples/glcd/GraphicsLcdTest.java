package com.ibasco.ucgdisplay.examples.glcd;

import com.ibasco.ucgdisplay.core.input.InputEventType;
import com.ibasco.ucgdisplay.core.system.InputDeviceManager;
import com.ibasco.ucgdisplay.core.system.RawInputEvent;
import com.ibasco.ucgdisplay.core.util.XBMUtils;
import com.ibasco.ucgdisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.ucgdisplay.drivers.glcd.Glcd;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdConfig;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdDriver;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdPinMapConfig;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <table>
 * <thead>
 * <tr>
 * <th>Byte Procedure</th>
 * <th>Description</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>u8x8_byte_4wire_sw_spi</td>
 * <td>Standard 8-bit SPI communication with "four pins" (SCK, MOSI, DC, CS)</td>
 * </tr>
 * <tr>
 * <td>u8x8_byte_3wire_sw_spi</td>
 * <td>9-bit communication with "three pins" (SCK, MOSI, CS)</td>
 * </tr>
 * <tr>
 * <td>u8x8_byte_8bit_6800mode</td>
 * <td>Parallel interface, 6800 format</td>
 * </tr>
 * <tr>
 * <td>u8x8_byte_8bit_8080mode</td>
 * <td>Parallel interface, 8080 format</td>
 * </tr>
 * <tr>
 * <td>u8x8_byte_sw_i2c</td>
 * <td>Two wire, I2C communication</td>
 * </tr>
 * <tr>
 * <td>u8x8_byte_ks0108</td>
 * <td>Special interface for KS0108 controller</td>
 * </tr>
 * </tbody>
 * </table>
 */
public class GraphicsLcdTest {

    public static final Logger log = LoggerFactory.getLogger(GraphicsLcdTest.class);

    private GlcdDriver glcd;

    private AtomicInteger x = new AtomicInteger(0);
    private AtomicInteger y = new AtomicInteger(0);
    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private byte[] ironman_bits = {
            0x00, 0x00, (byte) 0xfc, 0x03, 0x00, 0x00, 0x00, (byte) 0xc0, (byte) 0xff, 0x3f, 0x00, 0x00,
            0x00, (byte) 0xf8, (byte) 0xff, (byte) 0xff, 0x01, 0x00, 0x00, (byte) 0x86, (byte) 0xff, 0x1f, 0x06, 0x00,
            (byte) 0x80, (byte) 0x81, (byte) 0xff, 0x1f, 0x18, 0x00, (byte) 0xc0, (byte) 0x80, (byte) 0xff, 0x1f, 0x30, 0x00,
            0x20, (byte) 0x80, (byte) 0xff, 0x1f, (byte) 0xc0, 0x00, 0x10, 0x00, (byte) 0xff, 0x0f, (byte) 0x80, 0x01,
            0x08, 0x00, (byte) 0xff, 0x0f, 0x00, 0x01, 0x08, 0x00, (byte) 0xff, 0x0f, 0x00, 0x02,
            0x08, 0x00, (byte) 0xff, 0x0f, 0x00, 0x02, 0x08, 0x00, (byte) 0xfe, 0x07, 0x00, 0x02,
            0x08, 0x00, (byte) 0xfe, 0x07, 0x00, 0x02, 0x08, 0x00, (byte) 0xfe, 0x07, 0x00, 0x02,
            0x08, 0x00, 0x00, 0x00, 0x00, 0x06, 0x08, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x08, 0x00, 0x00, 0x00, 0x00, 0x06, 0x0c, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x0c, 0x00, 0x00, 0x00, 0x00, 0x06, 0x0c, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x0c, 0x00, 0x00, 0x00, 0x00, 0x06, 0x0c, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x0c, 0x00, 0x00, 0x00, 0x00, 0x06, 0x0c, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x06, 0x00, 0x00, 0x00, 0x00, 0x06, 0x06, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x06, 0x00, 0x00, 0x00, 0x00, 0x0e, (byte) 0xc6, 0x07, 0x00, 0x00, 0x7c, 0x0e,
            (byte) 0xe6, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x0e, (byte) 0xe6, (byte) 0xff, 0x03, (byte) 0xf8, (byte) 0xff, 0x0c,
            0x02, 0x3f, 0x00, (byte) 0x80, 0x1f, 0x0c, 0x02, 0x00, 0x00, 0x00, 0x00, 0x0c,
            0x03, 0x00, 0x00, 0x00, 0x00, 0x0c, 0x03, 0x00, 0x00, 0x00, 0x00, 0x0c,
            0x03, 0x00, 0x00, 0x00, 0x00, 0x0c, 0x03, 0x00, 0x00, 0x00, 0x00, 0x0c,
            0x03, 0x00, 0x00, 0x00, 0x00, 0x0c, 0x07, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x07, 0x00, 0x00, 0x00, 0x00, 0x06, 0x16, 0x00, 0x00, 0x00, 0x00, 0x06,
            0x2e, 0x00, 0x00, 0x00, (byte) 0x80, 0x07, 0x6e, 0x00, 0x00, 0x00, 0x40, 0x07,
            0x6e, 0x00, 0x00, 0x00, 0x60, 0x07, (byte) 0xde, 0x00, 0x00, 0x00, (byte) 0xb0, 0x07,
            (byte) 0xde, 0x00, 0x00, 0x00, (byte) 0xb0, 0x07, (byte) 0x9c, 0x01, 0x00, 0x00, (byte) 0xd8, 0x03,
            (byte) 0xbc, 0x01, 0x00, 0x00, (byte) 0xf8, 0x03, 0x3c, 0x01, 0x00, 0x00, (byte) 0xe8, 0x03,
            0x7c, 0x03, 0x00, 0x00, (byte) 0xf4, 0x03, (byte) 0xfc, 0x02, 0x00, 0x00, (byte) 0xf4, 0x03,
            (byte) 0xfc, (byte) 0xc2, (byte) 0xff, 0x1f, (byte) 0xfa, 0x03, (byte) 0xf8, 0x25, (byte) 0xff, 0x27, (byte) 0xfa, 0x01,
            (byte) 0xf8, 0x38, 0x00, 0x60, (byte) 0xf9, 0x01, (byte) 0xf8, 0x10, 0x00, (byte) 0xc0, (byte) 0xf8, 0x01,
            (byte) 0xf0, 0x00, 0x00, 0x00, (byte) 0xf8, 0x01, (byte) 0xf0, 0x00, 0x00, 0x00, (byte) 0xf8, 0x01,
            (byte) 0xf0, 0x00, (byte) 0xf8, 0x00, (byte) 0xf8, 0x00, (byte) 0xe0, (byte) 0xc1, (byte) 0xff, 0x0f, (byte) 0xf8, 0x00,
            (byte) 0xc0, (byte) 0xe3, (byte) 0xff, 0x1f, 0x7c, 0x00, (byte) 0x80, (byte) 0xff, (byte) 0xff, 0x7f, 0x3e, 0x00,
            0x00, (byte) 0xff, (byte) 0xff, 0x7f, 0x0f, 0x00, 0x00, (byte) 0xf6, 0x00, (byte) 0xfe, 0x07, 0x00,
            0x00, 0x3c, 0x00, (byte) 0xb0, 0x01, 0x00, 0x00, 0x08, 0x00, 0x40, 0x00, 0x00};

    private XBMUtils.XBMData handcursor;

    private XBMUtils.XBMData raspberryPiLogo;

    private XBMUtils.XBMData mousecursor;

    private GraphicsLcdTest() {
        //Configure GLCD
        GlcdConfig config = new GlcdConfig();
        config.setDisplay(Glcd.ST7920.D_128x64);
        config.setBusInterface(GlcdBusInterface.SPI_HW_4WIRE_ST7920);
        config.setRotation(GlcdRotation.ROTATION_180);
        config.setDeviceAddress(0x10);
        config.setPinMapConfig(new GlcdPinMapConfig()
                .map(GlcdPin.SPI_CLOCK, 14)
                .map(GlcdPin.SPI_MOSI, 12)
                .map(GlcdPin.CS, 10)
        );

        glcd = new GlcdDriver(config);
        InputDeviceManager.addInputEventListener(this::onInputEvent);
        InputDeviceManager.startInputEventMonitor();
    }

    public void run() throws Exception {
        URI resHandCursor = this.getClass().getClassLoader().getResource("handcursor.xbm").toURI();
        URI resRaspberryPiLogo = this.getClass().getClassLoader().getResource("raspberrypi-small.xbm").toURI();
        URI resMouseCursorBig = this.getClass().getClassLoader().getResource("mousecursor24x24.xbm").toURI();

        handcursor = XBMUtils.decodeXbmFile(new File(resHandCursor));
        raspberryPiLogo = XBMUtils.decodeXbmFile(new File(resRaspberryPiLogo));
        mousecursor = XBMUtils.decodeXbmFile(new File(resMouseCursorBig));

        //byte[] xbmData = XBMUtils.decodeXbmFile(new File("/home/pi/left4dead2.xbm"));

        CompletableFuture.runAsync(() -> {
            while (!shutdown.get()) {
                for (int a = 65; a > 0; a--) {
                    String date = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    String time = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));

                    glcd.clearBuffer();

                    glcd.setBitmapMode(1);
                    glcd.drawXBM(x.get(), y.get(), 25, 24, mousecursor.getData());
                    glcd.drawXBM(-19, 0, 95, 74, raspberryPiLogo.getData());

                    glcd.setFont(GlcdFont.FONT_PROFONT12_TF);
                    int charHeight_1 = glcd.getMaxCharHeight();
                    glcd.drawString(55, 15, "GLCD Demo");

                    glcd.setFont(GlcdFont.FONT_9X15_TF);
                    int charHeight_2 = glcd.getMaxCharHeight();
                    glcd.drawString(55, 15 + charHeight_2, date);
                    glcd.drawString(55, (15 * 2) + charHeight_2, time);

                    glcd.sendBuffer();

                    ThreadUtils.sleep(5);
                }
            }
        });
    }

    private void onInputEvent(RawInputEvent rawInputEvent) {
        if (rawInputEvent.getType() == InputEventType.EV_KEY) {
            log.info("Input Event: {}", rawInputEvent);
        } else if (rawInputEvent.getType() == InputEventType.EV_REL) {
            if (handcursor == null)
                return;
            if (rawInputEvent.getCode() == 0) {
                x.addAndGet(rawInputEvent.getValue());
                if (x.get() > 128)
                    x.set(128);
                else if (x.get() < 0)
                    x.set(0);
            } else if (rawInputEvent.getCode() == 1) {
                y.addAndGet(rawInputEvent.getValue());
                if (y.get() > 64)
                    y.set(64);
                else if (y.get() < 0)
                    y.set(0);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new GraphicsLcdTest().run();
    }
}
