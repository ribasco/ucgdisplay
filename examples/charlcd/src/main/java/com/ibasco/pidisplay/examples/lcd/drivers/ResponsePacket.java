package com.ibasco.pidisplay.examples.lcd.drivers;

import com.ibasco.pidisplay.examples.lcd.exceptions.PacketException;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponsePacket extends Packet {

    private static final Logger log = getLogger(ResponsePacket.class);

    private int requestHeader;
    private byte responseStatus;

    public ResponsePacket(byte[] tmp) throws PacketException {
        this(ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN));
    }

    public ResponsePacket(ByteBuffer tmp) throws PacketException {
        super(tmp);
        if (this.getSize() >= 2) {
            this.requestHeader = this.getPayload().get();
            this.responseStatus = this.getPayload().get();
        }
    }

    public int getRequestHeader() {
        return requestHeader;
    }

    public boolean isSuccess() {
        return responseStatus == RESPONSE_OK;
    }
}
