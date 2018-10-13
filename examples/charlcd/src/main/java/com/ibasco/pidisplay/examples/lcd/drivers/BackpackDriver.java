package com.ibasco.pidisplay.examples.lcd.drivers;

import com.ibasco.pidisplay.examples.lcd.exceptions.BackpackDriverException;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public interface BackpackDriver {
    void sendCommand(byte header, byte... data) throws BackpackDriverException;

    void sendCommand(byte header, ByteBuffer data) throws BackpackDriverException;

    CompletableFuture<ResponsePacket> sendRequest(final int header, final byte... data);

    CompletableFuture<ResponsePacket> sendRequest(int header, ByteBuffer data);
}
