package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.core.beans.InputEventData;
import com.ibasco.pidisplay.core.util.ByteUtils;
import com.pi4j.io.file.LinuxFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class DevInputTest {

    private static final Logger log = LoggerFactory.getLogger(DevInputTest.class);

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    public static void main(String[] args) throws Exception {
        DevInputTest inputTest = new DevInputTest();
        Runtime.getRuntime().addShutdownHook(new Thread(inputTest::shutdownHook));
        inputTest.run();
    }

    private void shutdownHook() {
        log.info("Shutting Down Test Program");
        shutdown.set(true);
    }

    private static final long IOCTL_ID = 2148025602L;

    private static final long IOCTL_NAME = 2164278534L;

    private void run() throws Exception {
        final File fs = new File("/dev/input/event0");
        if (!fs.exists() || !fs.canRead())
            throw new Exception("Could not access the device (Either it does not exist or you do not have read permission)");
        log.info("Running Dev Input Test");
        try (LinuxFile file = new LinuxFile(fs.getCanonicalPath(), "r")) {
            while (!shutdown.get()) {
                byte[] data = new byte[16];
                int bytesRead = file.read(data, 0, data.length);
                if (bytesRead > 0) {
                    InputEventData inputEvent = createInputEventData(data, bytesRead);
                    //Only process key events for now
                    //if (inputEvent.getType() == EV_KEY)
                        log.info("Event = {}", inputEvent);
                }
            }
            log.info("Exiting Keyboard Monitor");
        }
    }

    private InputEventData createInputEventData(byte[] data, int bufferSize) {
        ByteBuffer buffer = ByteUtils.wrapDirectBuffer(data);
        int tvSeconds = buffer.getInt();
        int tvMicroSeconds = buffer.getInt();
        short type = buffer.getShort();
        short code = buffer.getShort();
        int value = buffer.getInt();
        return new InputEventData(tvSeconds, tvMicroSeconds, type, code, value);
    }
}
