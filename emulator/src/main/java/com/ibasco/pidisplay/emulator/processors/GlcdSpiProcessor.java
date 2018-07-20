package com.ibasco.pidisplay.emulator.processors;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.emulator.GlcdGpioEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("Duplicates")
public class GlcdSpiProcessor extends GlcdGpioEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(GlcdSpiProcessor.class);

    private int bitIndex = 7;
    private byte tmp = 0;

    @Override
    public void processGpioEvent(GpioEvent event) {
        int msg = event.getMsg();
        switch (msg) {
            case U8X8_MSG_BYTE_INIT:
                break;
            case U8X8_MSG_BYTE_START_TRANSFER:
                //log.debug("({}) START TRANSFER", ++commandIndex);
                //totalBytes = 0;
                break;
            case U8X8_MSG_BYTE_END_TRANSFER:
                //int bitSize = 0;
                //log.debug("({}) END TRANSFER", commandIndex);
                break;
            /*
             * Marks the start of the byte transfer operation
             */
            case U8X8_MSG_BYTE_SEND:
                //marks as the start of the byte send procedure
                int byteSize = event.getValue();
                //bitSize = 8 * byteSize;
                bitIndex = 7; //set the starting bit index
                //totalBytes += byteSize; //monitor the nu                                   mber of bytes transmitted for this operation
                break;
            /*
             * No processing required here. We can ignore
             */
            case U8X8_MSG_GPIO_D0: //spi-clock
                break;
            /*
             * The incoming data (in bits) will be translated and re-assembled to their original form
             */
            case U8X8_MSG_GPIO_D1: //spi-data
                //log.info("-Bit: {}", event.getValue());
                tmp ^= (-(event.getValue()) ^ tmp) & (1 << bitIndex--);

                if (bitIndex < 0) {
                    //log.info("\t({}) Byte = {}, Size = {}", byteCtr++, ByteUtils.toHexString(false, tmp), byteSize);
                    onByteEvent(Byte.toUnsignedInt(tmp));
                    tmp = 0;
                }

                bitIndex &= 0x7;
                break;
            default:
                break;
        }
    }
}
