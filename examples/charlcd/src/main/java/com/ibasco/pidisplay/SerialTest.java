package com.ibasco.pidisplay;

import com.ibasco.pidisplay.core.util.ByteUtils;
import com.pi4j.io.serial.*;
import com.pi4j.util.Console;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.slf4j.LoggerFactory.getLogger;

public class SerialTest {
    private static final Logger log = getLogger(SerialTest.class);

    private static final Console console = new Console();

    public static void main(String[] args) throws Exception {
        new SerialTest().run();
    }

    private void run() throws Exception {
        // print program title/header
        console.title("<-- The Pi4J Project -->", "Serial Communication Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create an instance of the serial communications class
        final Serial serial = SerialFactory.createInstance();

        // create and register the serial data listener
        serial.addListener((SerialDataEventListener) event -> {

            // NOTE! - It is extremely important to read the data received from the
            // serial port.  If it does not get read from the receive buffer, the
            // buffer will continue to grow and consume memory.

            // print out the data received to the console
            try {
                //console.println("[HEX DATA]   " + event.getHexByteString());
                ByteBuffer buf = event.getByteBuffer().order(ByteOrder.LITTLE_ENDIAN);

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

                log.info("Header: {}, Flags: {}, Size: {}, Payload: {}, Footer: {}", Integer.toHexString(header), Integer.toHexString(flags), size, ByteUtils.bytesToHex(payload), Integer.toHexString(footer));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            // create serial config object
            SerialConfig config = new SerialConfig();

            // set default serial settings (device, baud rate, flow control, etc)
            //
            // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
            // NOTE: this utility method will determine the default serial port for the
            //       detected platform and board/model.  For all Raspberry Pi models
            //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
            //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
            //       environment configuration.
            config.device(SerialPort.getDefaultPort())
                    .baud(Baud._115200)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            // display connection details
            console.box(" Connecting to: " + config.toString(),
                    " We are sending ASCII data on the serial port every 1 second.",
                    " Data received on serial port will be displayed below.");


            // open the default serial device/port with the configuration settings
            serial.open(config);
            console.println("Now listening for serial events");
            // continuous loop to keep the program running until the user terminates the program
            while (console.isRunning()) {
                /*try {
                    // write a formatted string to the serial transmit buffer
                    serial.write("CURRENT TIME: " + new Date().toString());

                    // write a individual bytes to the serial transmit buffer
                    serial.write((byte) 13);
                    serial.write((byte) 10);

                    // write a simple string to the serial transmit buffer
                    serial.write("Second Line");

                    // write a individual characters to the serial transmit buffer
                    serial.write('\r');
                    serial.write('\n');

                    // write a string terminating with CR+LF to the serial transmit buffer
                    serial.writeln("Third Line");
                }
                catch(IllegalStateException ex){
                    ex.printStackTrace();
                }*/

                // wait 1 second before continuing
                Thread.sleep(1000);
            }

        } catch (IOException ex) {
            console.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
            return;
        }
    }
}
