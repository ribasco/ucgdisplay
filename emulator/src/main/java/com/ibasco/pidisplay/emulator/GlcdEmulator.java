package com.ibasco.pidisplay.emulator;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.core.gpio.GpioEventService;
import com.ibasco.pidisplay.drivers.glcd.Glcd;
import com.ibasco.pidisplay.drivers.glcd.GlcdConfig;
import com.ibasco.pidisplay.drivers.glcd.GlcdDriver;
import com.ibasco.pidisplay.drivers.glcd.GlcdPinMapConfig;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdCommInterface;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.pidisplay.emulator.processors.GlcdSpiProcessor;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({"WeakerAccess", "unused", "Duplicates"})
public class GlcdEmulator {
    public static final Logger log = getLogger(GlcdEmulator.class);

    //Display Start Byte
    private static final int CMD_INSTRUCTION = 0xF8;
    private static final int CMD_DATA = 0xFA;

    //Display Instruction Flags
    public static final int F_DISPLAY_CLEAR = 0x1;
    public static final int F_HOME = 0x2;
    public static final int F_ENTRY_MODE_SET = 0x4;
    public static final int F_DISPLAY_CONTROL = 0x8;
    public static final int F_DISPLAY_CURSOR_CONTROL = 0x10;
    public static final int F_FUNCTION_SET = 0x20;
    public static final int F_CGRAM_SET = 0x40;
    public static final int F_DDRAM_SET = 0x80;

    private GlcdDriver glcd;
    private Map<String, Integer> messageLog = new HashMap<>();
    private BitSet bitBuffer;
    private ByteBuffer dataBuffer;

    private boolean processingInstruction = false;
    private int[] instructionRegister = new int[2];
    //private int registerCounter = 0;
    private int dataRegister = 0;
    private int bitIndex = 7;
    private int commandIndex = 0;
    private int totalBytes = 0;
    private int byteSize = 0;
    private int aCtr = 0;
    private byte[] address = new byte[2];

    private GlcdGpioEventProcessor protocol;

    private InstructionListener instructionListener;

    private DataListener dataListener;

    @FunctionalInterface
    public interface InstructionListener {
        void onInstructionEvent(GlcdInstruction instruction);
    }

    @FunctionalInterface
    public interface DataListener {
        void onDataEvent(byte data);
    }

    public GlcdEmulator(GlcdDriver driver) {
        GpioEventService.addListener(this::gpioEventHandler);
        this.glcd = driver;
        this.protocol = new GlcdSpiProcessor();
        this.protocol.setByteEventHandler(this::onByteAvailable);
    }

    public DataListener getDataListener() {
        return dataListener;
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public InstructionListener getInstructionListener() {
        return instructionListener;
    }

    public void setInstructionListener(InstructionListener instructionListener) {
        this.instructionListener = instructionListener;
    }

    private void gpioEventHandler(GpioEvent gpioEvent) {
        messageLog.computeIfAbsent(gpioEvent.getDesc(), s -> gpioEvent.getMsg());
        protocol.processGpioEvent(gpioEvent);
    }

    private void processInstruction(int value) {
        //log.info("\t\t- Name: {} = {}", ByteUtils.toHexString(false, (byte) instruction), getInstructionName(instruction));
        GlcdInstruction instruction = GlcdInstructionFactory.createInstruction(value);
        if (instruction != null && instructionListener != null) {
            instructionListener.onInstructionEvent(instruction);
        } else {
            log.warn("Unknown/Invalid instruction: {}", value);
        }
    }

    private int registerSelect = 0;

    private int[] registerData = new int[2];

    private int registerCounter = 0;

    private static final int RS_INSTRUCTION = 0xF8;

    private static final int RS_DATA = 0xFA;


    /**
     * This method is called when a byte is ready for processing
     *
     * @param b
     *         The byte to be processed
     */
    private void onByteAvailable(int b) {
        //Select register
        if (b == RS_INSTRUCTION || b == RS_DATA) {
            registerSelect = b;
        } else {
            //Store data to register
            registerData[registerCounter] = b;

            //re-assemble after having collected the two consecutive bytes (high & low)
            if (registerCounter == 1) {
                int val = registerData[0] | (registerData[1] >> 4);
                if (registerSelect == RS_INSTRUCTION) {
                    processInstruction(val);
                } else if (registerSelect == RS_DATA) {
                    //log.info("+ DATA: {}", ByteUtils.toHexString(false, (byte) val));
                    processData(val);
                } else {
                    throw new RuntimeException("Unknown register select type");
                }
            }

            registerCounter++;
            //constrain values between 0 and 1 only
            registerCounter &= 0x1;
        }
    }

    private void processData(int data) {
        if (dataListener != null)
            dataListener.onDataEvent((byte) data);
    }

    public static void main(String[] args) throws Exception {
        //Configure GLCD
        GlcdConfig config = new GlcdConfig();
        config.setDisplay(Glcd.ST7920.D_128x64);
        config.setCommInterface(GlcdCommInterface.SPI_SW_4WIRE_ST7920);
        config.setRotation(GlcdRotation.ROTATION_180);
        config.setDeviceAddress(0x10);
        config.setPinMapConfig(new GlcdPinMapConfig()
                .map(GlcdPin.SPI_CLOCK, 14)
                .map(GlcdPin.SPI_MOSI, 12)
                .map(GlcdPin.CS, 10)
        );
        new GlcdEmulator(new GlcdDriver(config)).run();
    }

    private void run() {
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 64; y++) {
                glcd.drawPixel(x, y);
            }
        }
        log.info("START_SENDBUFFER");
        glcd.sendBuffer();
        log.info("END_SENDBUFFER");
    }
}
