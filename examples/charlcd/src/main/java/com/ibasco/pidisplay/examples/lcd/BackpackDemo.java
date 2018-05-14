package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.util.ByteUtils;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.drivers.lcd.hd44780.CharProxyDisplayDriver;
import com.ibasco.pidisplay.impl.charlcd.LcdController;
import com.ibasco.pidisplay.impl.charlcd.components.LcdPane;
import com.ibasco.pidisplay.impl.charlcd.components.LcdText;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.serial.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("Duplicates")
public class BackpackDemo {
    private static final Logger log = getLogger(BackpackDemo.class);

    private final GpioController gpio = GpioFactory.getInstance();

    private LcdController lcd;

    private CharDisplayDriver lcdDriver;

    private ScheduledExecutorService executorService;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private final I2CBus bus;

    private AtomicInteger inputCounter = new AtomicInteger(0);

    private LcdPane testPane;

    private LcdText header;

    private LcdText counter;

    private LcdText inputData;

    private LcdText footer;

    public static void main(String[] args) throws Exception {
        new BackpackDemo().run();
    }

    public BackpackDemo() throws IOException, I2CFactory.UnsupportedBusNumberException {
        //Initialize I2C Bus
        bus = I2CFactory.getInstance(I2CBus.BUS_1);

        ThreadFactory lcdThreadFactory = new ThreadFactory() {
            private AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("lcd-event-%d", threadNum.incrementAndGet()));
            }
        };

        executorService = Executors.newScheduledThreadPool(5, lcdThreadFactory);
        lcdDriver = new CharProxyDisplayDriver(0x15, 20, 4);
        lcd = new LcdController(lcdDriver);

        initDisplayComponents();
    }

    private void initDisplayComponents() {
        testPane = new LcdPane();
        header = new LcdText(0, 0, 20, 1, "Counter Test");
        counter = new LcdText(0, 1, 20, 1, "0");
        footer = new LcdText(0, 3, 20, 1, "Scroll Test");
        inputData = new LcdText(0, 2, 20, 1, String.valueOf(this.inputCounter.get()));
        header.setTextAlignment(TextAlignment.CENTER);
        counter.setTextAlignment(TextAlignment.LEFT);

        testPane.add(header);
        testPane.add(counter);
        testPane.add(inputData);
        testPane.add(footer);
    }

    private void run() throws Exception {
        log.info("Running LCD Display");
        Serial serial = setupSerialComms();

        lcd.show(testPane);

        final AtomicInteger ctr = new AtomicInteger(0);
        boolean state = false;
        int scrollLeft = 0;

        CompletableFuture.runAsync(() -> {
            while (!shutdown.get()) {
                ctr.compareAndSet(1000, 0);
                counter.setText(String.valueOf(ctr.getAndIncrement()));
                ThreadUtils.sleep(100);
            }
        });

        //Wait
        while (!shutdown.get()) {

            if (scrollLeft == 9)
                scrollLeft = 0;

            header.setVisible(state);
            footer.setScrollLeft(scrollLeft++);
            state = !state;
            ThreadUtils.sleep(500);
        }

        log.info("Shutting down...");
        serial.close();
        gpio.shutdown();
        executorService.shutdown();
        lcd.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    private Serial setupSerialComms() {
        // create an instance of the serial communications class
        final Serial serial = SerialFactory.createInstance();

        // create and register the serial data listener
        serial.addListener((SerialDataEventListener) event -> {
            try {
                byte[] data = event.getBytes();
                ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                int ctr = 0;
                while (buf.remaining() >= 5) {
                    ctr++;
                    byte header = buf.get();
                    byte flags = buf.get();
                    short size = buf.getShort();
                    if (size > 256) {
                        log.error("Invalid payload size: {}", size);
                        return;
                    }
                    byte[] payload = new byte[size];

                    if (size > 0)
                        buf.get(payload);
                    byte footer = buf.get();

                    if (footer == 0x0A) {
                        log.info("[Bytes Received: {}, {}] Header: {}, Flags: {}, Size: {}, Payload: {}, Footer: {}", data.length, ctr, Integer.toHexString(header), Integer.toHexString(flags), size, ByteUtils.bytesToHex(payload), Integer.toHexString(footer));
                        //Rotary Encoder
                        if (header == 0x20) {
                            if (payload[0] == 0x10) {
                                inputData.setText("Rotary: ---> [" + inputCounter.incrementAndGet() + "]");
                            } else if (payload[0] == 0x20) {
                                inputData.setText("Rotary: <--- [" + inputCounter.decrementAndGet() + "]");
                            }
                        }
                    } else {
                        log.error("Invalid Data = Header: {}, Flags: {}, Size: {}, Payload: {}, Footer: {}", Integer.toHexString(header), Integer.toHexString(flags), size, ByteUtils.bytesToHex(payload), Integer.toHexString(footer));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        try {
            // create serial config object
            SerialConfig config = new SerialConfig();

            // set default serial settings (device, baud rate, flow control, etc)
            ///dev/ttyS0
            //SerialPort.getDefaultPort()
            config.device("/dev/ttyAMA0")
                    .baud(Baud._115200)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            // display connection details
            log.info(" Connecting to: " + config.toString(),
                    " We are sending ASCII data on the serial port every 1 second.",
                    " Data received on serial port will be displayed below.");

            // open the default serial device/port with the configuration settings
            serial.open(config);

            log.info("Now listening for serial events");
            return serial;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
