package com.ibasco.pidisplay.emulator.protocols;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.emulator.GlcdCommProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("Duplicates")
public class GlcdSpiCommProtocol extends GlcdCommProtocol {

    private static final Logger log = LoggerFactory.getLogger(GlcdSpiCommProtocol.class);

    private int bitIndex = 7;
    private byte data = 0;

    public GlcdSpiCommProtocol(ByteEventHandler byteEventHandler) {
        super(byteEventHandler);
    }

    @Override
    public void decode(GpioEvent event) {
        int msg = event.getMsg();
        switch (msg) {
            case U8X8_MSG_BYTE_INIT:
                break;
            case U8X8_MSG_BYTE_START_TRANSFER:
                break;
            case U8X8_MSG_BYTE_END_TRANSFER:
                break;
            // Marks the start of the byte transfer operation
            case U8X8_MSG_BYTE_SEND:
                bitIndex = 7; //set the starting bit index
                break;
            case U8X8_MSG_GPIO_D0: //spi-clock
                //for every clock tick, check if we have a whole byte
                if (event.getValue() == 1) {
                    if (bitIndex < 0) {
                        emitByteEvent(Byte.toUnsignedInt(data));
                        data = 0;
                    }
                    bitIndex &= 0x7;
                }
                break;
            // The incoming data (in bits) will be collected and re-assembled into a byte
            case U8X8_MSG_GPIO_D1: //spi-data
                data ^= (-(event.getValue()) ^ data) & (1 << bitIndex--);
                break;
            default:
                break;
        }
    }
}
