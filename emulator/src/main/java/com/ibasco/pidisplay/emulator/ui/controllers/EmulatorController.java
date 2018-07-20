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
import com.ibasco.pidisplay.emulator.instructions.DdramSet;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.slf4j.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;

public class EmulatorController implements Initializable {

    public static final Logger log = getLogger(EmulatorController.class);

    @FXML
    private Canvas emulatorCanvas;

    @FXML
    private Button submitButton;

    private GlcdDriver glcd;

    private int yAddress = 0;

    private int xAddress = 0;

    private short[][] buffer = new short[32][16];

    private int dataCtr = 0;

    // generic redrawTask that redraws the canvas when new data arrives
    // (but not more often than 60 times per second).
    public abstract class CanvasRedrawTask<T> extends AnimationTimer {
        private final AtomicReference<T> data = new AtomicReference<T>(null);
        private final Canvas canvas;

        public CanvasRedrawTask(Canvas canvas) {
            this.canvas = canvas;
        }

        public void requestRedraw(T dataToDraw) {
            data.set(dataToDraw);
            //start(); // in case, not already started
        }

        public void handle(long now) {
            // check if new data is available
            T dataToDraw = data.getAndSet(null);
            if (dataToDraw != null) {
                redraw(canvas.getGraphicsContext2D(), dataToDraw);
            }
        }

        protected abstract void redraw(GraphicsContext context, T data);
    }

    // somewhere else in your concrete canvas implementation
    private CanvasRedrawTask<short[][]> redrawTask;

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

                GlcdEmulator emulator = new GlcdEmulator(glcd);
                emulator.setInstructionListener(this::onInstructionEvent);
                emulator.setDataListener(this::onDataEvent);
                log.info("END: GLCD_INIT");
            } catch (GlcdConfigException e) {
                log.error("There was a problem with your configuration setup", e);
            }


            redrawTask = new CanvasRedrawTask<short[][]>(emulatorCanvas) {
                @SuppressWarnings("Duplicates")
                @Override
                protected void redraw(GraphicsContext context, short[][] buf) {
                    drawLcd();
                    flush(context, buf);
                }
            };
            redrawTask.start();

        } catch (GlcdDriverException e) {
            e.printStackTrace();
        }
        drawLcd();
    }

    @FXML
    public void flushBufferAction(ActionEvent actionEvent) {
        flush(emulatorCanvas.getGraphicsContext2D(), buffer);
    }

    private void onDataEvent(byte data) {
        //TODO: SWAP Order to fix display issues
        if (dataCtr == 0) {
            buffer[yAddress][xAddress] = (short) (((data & 0xff) << 8) & 0xffff);
        } else {
            buffer[yAddress][xAddress] |= data & 0xff;
            xAddress = ++xAddress & 0xf;
        }

        redrawTask.requestRedraw(buffer);

        dataCtr = ++dataCtr & 0x1;
    }

    @SuppressWarnings("Duplicates")
    private void flush(GraphicsContext gc, short[][] buffer) {
        int width = 5, height = 5;

        for (int y = 0; y < 32; y++) {
            int pixelOffset = 0;

            for (int x = 0; x < 16; x++) {
                short data = buffer[y][x];

                for (int bitIndex = 16; bitIndex > 0; bitIndex--) {
                    int bit = data & (1 << bitIndex);
                    int pxOffset = (x > 7) ? -128 : 0;
                    int pX = (pixelOffset + pxOffset) * width;
                    int pyOffset = (x > 7) ? 32 : 0;
                    int pY = (pyOffset + y) * height;

                    //log.info("\tPixel: X={}, Y={}, Pixel Offset = {} ({}, Bit{}: {})", pX, pY, pixelOffset, Integer.toBinaryString(data), bitIndex, bit);
                    gc.setFill(bit > 0 ? Color.WHITE : Color.DODGERBLUE);
                    gc.fillRect(pX, pY, width - 1, height - 1);
                    pixelOffset++;
                }
            }
        }
    }

    private void flushOld() {
        GraphicsContext gc = emulatorCanvas.getGraphicsContext2D();
        int width = 5, height = 5;

        for (int y = 0; y < 32; y++) {
            int pixelOffset = 0;

            for (int x = 0; x < 16; x++) {
                short data = buffer[y][x];

                for (int bitIndex = 0; bitIndex < 16; bitIndex++) {
                    int bit = data & (1 << bitIndex);
                    int pX = pixelOffset * width, pY = y * height, pW = 0, pH = 0;

                    if (bit > 0)
                        gc.setFill(Color.WHITE);
                    else
                        gc.setFill(Color.DODGERBLUE);

                    gc.fillRect(pX, pY, width - 1, height - 1);

                    pixelOffset++;
                }
            }
        }
    }

    private void onInstructionEvent(GlcdInstruction instruction) {
        switch (instruction.getFlag()) {
            case GlcdInstruction.F_CGRAM_SET:
                break;
            case GlcdInstruction.F_DDRAM_SET:
                DdramSet ins = (DdramSet) instruction;
                if (ins.getAddressType() == DdramSet.ADDRESS_X) {
                    xAddress = ins.getAddress() & DdramSet.ADDRESS_X;
                } else if (ins.getAddressType() == DdramSet.ADDRESS_Y) {
                    yAddress = ins.getAddress() & DdramSet.ADDRESS_Y;
                }
                break;
            case GlcdInstruction.F_DISPLAY_CLEAR:
                //not yet implemented
                break;
            case GlcdInstruction.F_ENTRY_MODE_SET:
                //not yet implemented
                break;
            case GlcdInstruction.F_HOME:
                //not yet implemented
                break;
            case GlcdInstruction.F_DISPLAY_CURSOR_CONTROL:
                //not yet implemented
                break;
            case GlcdInstruction.F_FUNCTION_SET:
                //not yet implemented
                break;
            case GlcdInstruction.F_DISPLAY_CONTROL:
                //not yet implemented
                break;
            default:
                break;
        }
    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        clearBuffer();
        drawLcd();
    }

    public void drawLcd() {
        GraphicsContext gc = emulatorCanvas.getGraphicsContext2D();
        int columns = 128, rows = 64, width = 5, height = 5;
        gc.setLineWidth(0.1);
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < columns; ++col) {
                int x = width * col;
                int y = height * row;
                gc.strokeRect(x, y, width, height);
                gc.setFill(Color.DODGERBLUE);
                gc.fillRect(x, y, width, height);
            }
        }
    }

    public void clearBuffer() {
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 16; x++) {
                buffer[y][x] = 0x0;
            }
        }
    }

    @FXML
    public void drawPixelAction(ActionEvent actionEvent) {
        log.info("Draw test");

        clearBuffer();

        CompletableFuture.runAsync(() -> {
            for (int y = 0; y < 32; y++) {
                for (int x = 0; x < 16; x++) {
                    buffer[y][x] = (short) 0xffff;
                    redrawTask.requestRedraw(buffer);
                    ThreadUtils.sleep(50);
                }
            }
            log.info("Done");
        });

    }

    @FXML
    public void handleDrawDemoAction(ActionEvent actionEvent) {
        log.info("Drawing disc...");


        //glcd.drawDisc(20, 20, 20, U8G2_DRAW_ALL);
        /*glcd.drawCircle(20, 20, 20, U8G2_DRAW_ALL);
        glcd.drawLine(10, 10, 30, 40);
        glcd.drawDisc(40, 40, 10, U8G2_DRAW_LOWER_RIGHT);*/
        try {
            glcd.setFont(GlcdFont.FONT_8X13_TR);
            URI resRaspberryPiLogo = this.getClass().getClassLoader().getResource("raspberrypi-small.xbm").toURI();
            XBMUtils.XBMData raspberryPiLogo = XBMUtils.decodeXbmFile(new File(resRaspberryPiLogo));

            CompletableFuture.runAsync(() -> {
                while (true) {
                    for (int i = 0; i < 100; i++) {
                        glcd.clearBuffer();
                        glcd.setBitmapMode(1);
                        glcd.drawXBM(-20, 0, 95, 74, raspberryPiLogo.getData());
                        glcd.drawString(55, 15, "GLCD");
                        glcd.drawString(55, 30, "Emulator");
                        glcd.sendBuffer();
                        ThreadUtils.sleep(10);
                    }
                }
            });
        } catch (URISyntaxException | XBMDecodeException e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void onWriteTileAction(ActionEvent actionEvent) {
        log.info("Write Tile");
    }
}
