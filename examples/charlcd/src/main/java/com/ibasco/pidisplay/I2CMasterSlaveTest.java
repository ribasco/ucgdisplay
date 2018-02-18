package com.ibasco.pidisplay;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.util.Console;
import com.pi4j.wiringpi.GpioUtil;
import org.slf4j.Logger;

import java.text.DecimalFormat;

import static org.slf4j.LoggerFactory.getLogger;

public class I2CMasterSlaveTest {

    private static final Logger log = getLogger(I2CMasterSlaveTest.class);

    private static final Console console = new Console();

    public static void main(String[] args) throws Exception {
        new I2CMasterSlaveTest().run();
    }

    private void run() throws Exception {
        log.info("Running I2C Communication Test");

        GpioUtil.enableNonPrivilegedAccess();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "I2C test program");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        I2CBus bus = I2CFactory.getInstance(1);

        try {
            I2CDevice device = bus.getDevice(0x15);
            int ctr = 0, itr = 0;
            // continue running program until user exits using CTRL-C
            DecimalFormat df = new DecimalFormat("000");
            while (console.isRunning()) {
                if (ctr > 100) {
                    itr++;
                    ctr = 0;
                    log.info("Iteration: {}", itr);
                }
                String text = "Hello" + df.format(ctr++);
                device.write(10, text.getBytes());
                Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            bus.close();
        }
        console.emptyLine();
    }
}
