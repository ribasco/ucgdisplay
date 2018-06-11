package com.ibasco.pidisplay.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputDeviceTest {

    public static final Logger log = LoggerFactory.getLogger(InputDeviceTest.class);

    public InputDeviceTest() {
    }

    public void run() throws Exception {
        log.info("Running");

        /*InputDevice[] devices = InputDeviceManager.getInputDevices();

        if (devices == null) {
            log.error("No devices found");
            return;
        }

        int ctr = 1;
        for (InputDevice dev : devices) {
            log.info(String.format("%d) Device: %s, Total Events: %d", ctr++, dev.getDevicePath(), dev.getEventTypes().size()));

            for (InputEventType type : dev.getEventTypes()) {

                log.info("- Type: " + type.toString());
                for (InputEventCode code : type.getCodes()) {
                    log.info("\t" + code.toString());
                    if ("EV_ABS".equals(type.getKey())) {
                        log.info("\t\tABS data: " + code.getAbsData());
                    }
                }
            }
        }*/

        InputDevice dev = InputDeviceManager.queryDevice("/dev/input/event7");

        if (dev != null)
            log.info("Got Device: {}", dev);

        InputDeviceManager.addInputEventListener(this::rawInputEventHandler);
        InputDeviceManager.addDeviceStateEventListener(this::deviceStateHandler);
        InputDeviceManager.startInputEventMonitor();

        Thread.sleep(5000);

        InputDeviceManager.refreshDeviceCache();

        log.info("Device cache has been refreshed");

        System.out.println("Waiting for 20 seconds before stopping");

        Thread.sleep(20000);
        log.info("Stopping...");

        InputDeviceManager.stopInputEventMonitor();
    }

    private void deviceStateHandler(DeviceStateEvent deviceStateEvent) {
        log.info("Device State Event: (Device={}, Action={})", deviceStateEvent.getDevice().getName(), deviceStateEvent.getAction());
    }

    private void rawInputEventHandler(RawInputEvent rawInputEvent) {
        if (rawInputEvent.getType() == 1)
            log.info("Key Event = ({})", rawInputEvent);
        /*else if (rawInputEvent.getType() == 2)
            log.info("Mouse Event = {}" , rawInputEvent);*/
    }

    public static void main(String[] args) throws Exception {
        new InputDeviceTest().run();
    }
}
