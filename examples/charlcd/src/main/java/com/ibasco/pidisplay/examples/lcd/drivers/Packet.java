package com.ibasco.pidisplay.examples.lcd.drivers;

import com.ibasco.pidisplay.core.util.ByteUtils;
import com.ibasco.pidisplay.examples.lcd.enums.MsgStatus;
import com.ibasco.pidisplay.examples.lcd.enums.MsgTransport;
import com.ibasco.pidisplay.examples.lcd.enums.MsgType;
import com.ibasco.pidisplay.examples.lcd.exceptions.PacketException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

import static org.slf4j.LoggerFactory.getLogger;

public class Packet {

    public static final Logger log = getLogger(Packet.class);

    static final int MIN_PACKET_SIZE = 5;
    static final int MAX_PAYLOAD_SIZE = 128;
    static final int MAX_PACKET_SIZE = MAX_PAYLOAD_SIZE + MIN_PACKET_SIZE;
    static final byte MSG_FOOTER = 0x0A;

    static final int RESPONSE_OK = 0x0;
    static final int RESPONSE_ERR = 0x1;

    private byte header;
    private byte flags;
    protected short size;
    private ByteBuffer payload;
    private MsgTransport transport;
    private MsgType type;
    private MsgStatus status;

    public Packet(ByteBuffer data) throws PacketException {
        decode(data);
    }

    private void decode(ByteBuffer data) throws PacketException {
        this.header = data.get();
        this.flags = data.get();
        initializeFlags(this.flags);
        this.size = data.getShort();

        StringBuilder responseData = new StringBuilder();
        ByteUtils.printHexBytes(responseData, data.array());

        int packetSize = this.size + MIN_PACKET_SIZE;
        log.debug("Declared Payload Size: {}", this.size);
        log.debug("Computed Packet Size: {}", packetSize);

        //Validate total packet size (payload size + min packet size)
        if (packetSize > MAX_PACKET_SIZE)
            throw new PacketException(String.format("Size mismatch (MAX = %d, ACTUAL = %d)", MAX_PACKET_SIZE, packetSize));

        //Extract payload if available
        if (this.size > 0) {
            byte[] payload = new byte[this.size];
            data.get(payload);
            this.payload = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN);
        }

        //Validate footer
        if (data.get() != MSG_FOOTER)
            throw new PacketException("Corruption in the received data packet: " + StringUtils.truncate(responseData.toString(), 100));
    }

    private void initializeFlags(byte bFlags) {
        BitSet flags = BitSet.valueOf(new byte[]{bFlags});
        this.transport = MsgTransport.valueOf(flags.get(0) ? 0x1 : 0x0);
        this.type = getMsgType(flags);
        this.status = MsgStatus.valueOf(flags.get(3));
    }

    private MsgType getMsgType(BitSet flags) {
        BitSet msgType = new BitSet(8);
        msgType.set(0, flags.get(1));
        msgType.set(1, flags.get(2));
        byte[] aMsgType = msgType.toByteArray();
        if (aMsgType.length > 0)
            return MsgType.valueOf(msgType.toByteArray()[0]);
        return null;
    }

    public byte getHeader() {
        return header;
    }

    public byte getFlags() {
        return flags;
    }

    public MsgTransport getTransport() {
        return transport;
    }

    public MsgType getType() {
        return type;
    }

    public MsgStatus getStatus() {
        return status;
    }

    public ByteBuffer getPayload() {
        return payload;
    }

    public int getSize() {
        return size;
    }

    protected int bitRead(int value, int bit) {
        return (((value) >> (bit)) & 0x01);
    }

    protected int bitClear(int value, int bit) {
        return value &= ~(1L << bit);
    }

    protected int bitSet(int value, int bit) {
        return (int) (value | (1L << bit));
    }

    protected int bitWrite(int value, int bit, int bitValue) {
        return (bitValue == 1 ? bitSet(value, bit) : bitClear(value, bit));
    }
}
