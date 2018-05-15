package com.ibasco.pidisplay.examples.lcd.drivers;

import com.ibasco.pidisplay.core.util.ByteUtils;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.examples.lcd.exceptions.BackpackDriverException;
import com.ibasco.pidisplay.examples.lcd.exceptions.I2CException;
import com.ibasco.pidisplay.examples.lcd.exceptions.PacketException;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The I2C communications driver for the micro-controller backpack interface
 *
 * @author Rafael Ibasco
 */
public class BackpackI2CDriver implements BackpackDriver, Closeable {
    private static final Logger log = LoggerFactory.getLogger(BackpackI2CDriver.class);

    private static final int FLAGS_TRANSPORT = 0x1;   //0 = I2C, 1 = SERIAL
    private static final int FLAGS_MSG_TYPE = 0x2;  //0 = COMMAND, 1 = REQUEST

    private final I2CBus bus;

    private I2CDevice device;

    private int address;

    private final ExecutorService executorService;

    public static class RequestPacket {

    }

    public BackpackI2CDriver(final int address) throws IOException, I2CFactory.UnsupportedBusNumberException {
        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.address = address;
        this.executorService = Executors.newSingleThreadExecutor();
        initDevice();
    }

    private void initDevice() throws IOException {
        this.device = bus.getDevice(address);
    }

    public I2CDevice getDevice() {
        return device;
    }

    /**
     * Sends a command to the microcontroller in a fire and forget fashion
     *
     * @param header The header representing the command
     * @param data   The payload
     * @throws I2CException Thrown when an I/O occured during send operation
     */
    @Override
    public synchronized void sendCommand(byte header, byte... data) throws BackpackDriverException {
        sendMessage(header, false, data);
    }

    @Override
    public void sendCommand(byte header, ByteBuffer data) throws BackpackDriverException {
        sendMessage(header, false, data.array());
    }

    /**
     * Sends an initial request and receive a response from the micro controller
     *
     * @param header The request header
     * @param data   The payload
     * @return A {@link CompletableFuture} which returns a {@link ByteBuffer} instance of the response
     */
    @Override
    public CompletableFuture<ResponsePacket> sendRequest(final int header, final byte... data) {
        CompletableFuture<ResponsePacket> cf = new CompletableFuture<>();

        //Receive the data
        executorService.execute(() -> {
            byte[] packet = new byte[Packet.MAX_PACKET_SIZE];
            try {
                //Send message
                sendMessage((byte) header, true, data);

                //Wait for the reply
                int bytesRead = device.read(packet, 0, packet.length);
                log.debug("Received bytes: {}", bytesRead);

                //Validate length of received bytes
                if (bytesRead < Packet.MIN_PACKET_SIZE) {
                    cf.completeExceptionally(new I2CException("Not enough bytes received"));
                    return;
                }
                //Complete the response
                cf.complete(new ResponsePacket(packet));
            } catch (IOException | BackpackDriverException | PacketException e) {
                cf.completeExceptionally(e);
            }
        });

        return cf;
    }

    @Override
    public CompletableFuture<ResponsePacket> sendRequest(int header, ByteBuffer data) {
        return sendRequest(header, data.array());
    }

    private synchronized void sendMessage(int header, boolean isRequest, byte[] data) throws BackpackDriverException {
        try {
            byte flags = 0x0;

            //Set to i2c transport
            flags &= ~FLAGS_TRANSPORT; //i2c = 0, serial = 1 (i2c)

            if (isRequest)
                flags |= FLAGS_MSG_TYPE; //req = 1, cmd = 0 (request)
            else
                flags &= ~FLAGS_MSG_TYPE; //command

            //Write to buffer
            ByteBuffer buffer = ByteBuffer.allocate(Packet.MIN_PACKET_SIZE + data.length).order(ByteOrder.LITTLE_ENDIAN);
            buffer.put((byte) header); //header (1 byte)
            buffer.put(flags); //command flags (1 byte)
            buffer.putShort((short) data.length); //size (2 bytes)
            if (data.length > 0)
                buffer.put(data);
            buffer.put(Packet.MSG_FOOTER);
            buffer.flip();

            //Read the buffer
            byte[] out = new byte[buffer.remaining()];
            buffer.get(out);

            StringBuilder requestData = new StringBuilder();
            ByteUtils.printHexBytes(requestData, out);

            log.debug("Sending RequestPacket. (Data: {}, Payload Size: {})", requestData.toString(), data.length);

            device.write(out);

            ThreadUtils.sleep(12);
        } catch (IOException e) {
            if ("Remote I/O error".equalsIgnoreCase(e.getMessage())) {
                try {
                    log.warn("An error has occured. Re-initializing I2C Device (Data discarded)");
                    initDevice();
                } catch (IOException e1) {
                    throw new BackpackDriverException(e1);
                }
            }
        }
    }

    @Override
    public void close() {
        executorService.shutdown();
    }

   /*public static void main(String[] args) throws Exception {
        new BackpackI2CDriver(0x15).run();
    }

    private void run() throws ExecutionException, InterruptedException {
        ArrayList<CompletableFuture<?>> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            final int x = i;
            futures.add(
                    sendRequest((byte) 0x25, new byte[]{})
                            .thenAccept((buffer) -> processBuffer(x, buffer))
                            .exceptionally(throwable -> {
                                log.error("[RequestPacket {}] Error: {}", x, throwable.getMessage());
                                return null;
                            })
            );
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).get();
        log.info("Done");
        executorService.shutdown();
    }*/
}
