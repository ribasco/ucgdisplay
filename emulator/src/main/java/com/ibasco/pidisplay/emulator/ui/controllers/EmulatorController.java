package com.ibasco.pidisplay.emulator.ui.controllers;

import com.ibasco.pidisplay.core.exceptions.XBMDecodeException;
import com.ibasco.pidisplay.core.util.XBMUtils;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.drivers.glcd.Glcd;
import com.ibasco.pidisplay.drivers.glcd.GlcdConfig;
import com.ibasco.pidisplay.drivers.glcd.GlcdDriver;
import com.ibasco.pidisplay.drivers.glcd.GlcdPinMapConfig;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdCommInterface;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdConfigException;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdDriverException;
import com.ibasco.pidisplay.emulator.GlcdEmulator;
import com.ibasco.pidisplay.emulator.GlcdInstruction;
import com.ibasco.pidisplay.emulator.gpio.JniGpioEventDispatcher;
import com.ibasco.pidisplay.emulator.ui.GlcdRedrawTask;
import com.ibasco.pidisplay.emulator.ui.GlcdScreen;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("Duplicates")
public class EmulatorController implements Initializable {

    private static final Logger log = getLogger(EmulatorController.class);

    @FXML
    public ColorPicker lcdBackgroundColor;

    @FXML
    public ColorPicker lcdForegroundColor;

    @FXML
    public ScrollPane emulatorPane;

    @FXML
    private GlcdScreen emulatorCanvas;

    private GlcdDriver glcd;

    private Color lcdBackground = Color.DODGERBLUE;

    private Color lcdForeground = Color.WHITE;

    private GlcdRedrawTask<short[][]> redrawTask;

    private XBMUtils.XBMData raspberryPiLogo;

    private GlcdEmulator emulator;

    @FXML
    public void onLcdBackgroundColorAction(ActionEvent actionEvent) {
        ColorPicker source = (ColorPicker) actionEvent.getSource();
        lcdBackground = source.getValue();
        redrawTask.redraw(emulator.getBuffer());
    }

    @FXML
    public void onLcdForegroundColor(ActionEvent actionEvent) {
        ColorPicker source = (ColorPicker) actionEvent.getSource();
        lcdForeground = source.getValue();
        redrawTask.redraw(emulator.getBuffer());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Configure GLCD
            GlcdConfig config = new GlcdConfig();
            config.setDisplay(Glcd.ST7920.D_128x64);
            config.setCommInterface(GlcdCommInterface.SPI_HW_4WIRE_ST7920);
            config.setRotation(GlcdRotation.ROTATION_NONE);
            config.setPinMapConfig(new GlcdPinMapConfig()
                    .map(GlcdPin.SPI_CLOCK, 14)
                    .map(GlcdPin.SPI_MOSI, 12)
                    .map(GlcdPin.CS, 10)
            );

            //8 bytes of row, 32 bytes of column
            try {
                log.info("START: GLCD_INIT");
                glcd = new GlcdDriver(config);
                glcd.setFont(GlcdFont.FONT_9X18B_MN);
                log.info("END: GLCD_INIT");

                emulator = new GlcdEmulator(new JniGpioEventDispatcher());
                emulator.setInstructionListener(this::onInstructionEvent);
                emulator.setDataListener(this::onDataEvent);

                lcdBackgroundColor.setValue(lcdBackground);
                lcdForegroundColor.setValue(lcdForeground);
                //lcdPane.setStyle("-fx-background-color: #000000; -fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                emulatorCanvas.setStyle("-fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

            } catch (GlcdConfigException e) {
                log.error("There was a problem with your configuration setup", e);
            }

            URI resRaspberryPiLogo = this.getClass().getClassLoader().getResource("images/raspberrypi-small.xbm").toURI();
            raspberryPiLogo = XBMUtils.decodeXbmFile(new File(resRaspberryPiLogo));

            redrawTask = new GlcdRedrawTask<short[][]>(emulatorCanvas) {
                @SuppressWarnings("Duplicates")
                @Override
                protected void redraw(GraphicsContext context, short[][] buf) {
                    drawLcd();
                    flush(context, buf);
                }
            };
        } catch (GlcdDriverException | URISyntaxException | XBMDecodeException e) {
            log.error(e.getMessage(), e);
        }

        drawLcd();
    }

    private void onInstructionEvent(GlcdInstruction instruction) {
        //log.info("Instruction: {}", instruction);
    }

    private void onDataEvent(short[][] buffer) {
        //log.info("Data: {}", buffer.length);
        redrawTask.redraw(buffer);
    }

    @FXML
    public void flushBufferAction() {
        flush(emulatorCanvas.getGraphicsContext2D(), emulator.getBuffer());
    }

    private int displayWidth = 128, displayHeight = 64;

    private int pixelWidth = 10, pixelHeight = 10;

    /**
     * Flush buffer to display
     */
    private void flush(GraphicsContext gc, short[][] buffer) {
        for (int y = 0; y < 32; y++) {
            int pixelIndex = 0;

            for (int x = 0; x < 16; x++) {
                short data = buffer[y][x];

                //Start from MSB to LSB
                for (int bitIndex = 16; bitIndex > 0; bitIndex--) {
                    int bit = data & (1 << bitIndex);
                    int pxOffset = (x > 7) ? -128 : 0;
                    int pyOffset = (x > 7) ? 32 : 0;
                    int pX = (pixelIndex + pxOffset) * pixelWidth;
                    int pY = (pyOffset + y) * pixelHeight;

                    gc.setFill(bit > 0 ? lcdForeground : lcdBackground);
                    gc.fillRect(pX, pY, pixelWidth - 1, pixelHeight - 1);

                    pixelIndex++;
                }
            }
        }
    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        clearBuffer();
        drawLcd();
    }

    private void drawLcd() {
        GraphicsContext gc = emulatorCanvas.getGraphicsContext2D();

        //Update Canvas Size
        emulatorCanvas.setWidth(displayWidth * pixelWidth);
        emulatorCanvas.setHeight(displayHeight * pixelHeight);

        //Set pixel border width
        gc.setLineWidth(0.1);

        for (int row = 0; row < displayHeight; ++row) {
            for (int col = 0; col < displayWidth; ++col) {
                int x = pixelWidth * col;
                int y = pixelHeight * row;
                gc.strokeRect(x, y, pixelWidth, pixelHeight);
                gc.setFill(lcdBackground);
                gc.fillRect(x, y, pixelWidth, pixelHeight);
            }
        }
    }

    public void clearBuffer() {
        emulator.clearBuffer();
        redrawTask.redraw(emulator.getBuffer());
    }

    @FXML
    public void drawPixelAction(ActionEvent actionEvent) {
        clearBuffer();
        short[][] buffer = emulator.getBuffer();
        CompletableFuture.runAsync(() -> {
            for (int y = 0; y < 32; y++) {
                for (int x = 0; x < 16; x++) {
                    buffer[y][x] = (short) 0xffff;
                    redrawTask.redraw(buffer);
                    ThreadUtils.sleep(50);
                }
            }
        });

    }

    private void drawU8G2Logo() {
        glcd.setFontMode(1);

        glcd.setFontDirection(0);
        glcd.setFont(GlcdFont.FONT_INB16_MF);
        glcd.drawString(0, 22, "U");

        glcd.setFontDirection(1);
        glcd.setFont(GlcdFont.FONT_INB19_MN);
        glcd.drawString(14, 8, "8");

        glcd.setFontDirection(0);
        glcd.setFont(GlcdFont.FONT_INB16_MF);
        glcd.drawString(36, 22, "g");
        glcd.drawString(48, 22, "2");

        glcd.drawHLine(2, 25, 34);
        glcd.drawHLine(3, 26, 34);
        glcd.drawVLine(32, 22, 12);
        glcd.drawVLine(33, 23, 12);
    }

    private void drawRpiLogo() {
        glcd.setBitmapMode(1);
        if (raspberryPiLogo != null)
            glcd.drawXBM(40, -5, 95, 74, raspberryPiLogo.getData());
    }

    @FXML
    public void handleDrawDemoAction(ActionEvent actionEvent) {
        log.info("Drawing disc...");

        try {
            glcd.setFont(GlcdFont.FONT_8X13_TR);


            CompletableFuture.runAsync(() -> {
                while (true) {
                    for (int i = 0; i < 100; i++) {
                        glcd.clearBuffer();
                        drawU8G2Logo();
                        glcd.setFont(GlcdFont.FONT_ASTRAGAL_NBP_TR);
                        glcd.drawString(20, 50, "on (" + i + ")");
                        drawRpiLogo();
                        glcd.sendBuffer();
                        ThreadUtils.sleep(1000);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onWriteTileAction(ActionEvent actionEvent) {
        log.info("Write Tile");
    }

    @FXML
    public void saveImageAction(ActionEvent actionEvent) {
        WritableImage wim = new WritableImage(displayWidth * pixelWidth, displayHeight * pixelHeight);
        emulatorCanvas.snapshot(null, wim);
        File file = new File("CanvasImage.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
            log.info("Saved file to : {}", file.getAbsolutePath());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
