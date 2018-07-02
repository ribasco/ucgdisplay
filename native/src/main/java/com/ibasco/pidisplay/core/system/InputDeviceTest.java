package com.ibasco.pidisplay.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputDeviceTest {

    public static final Logger log = LoggerFactory.getLogger(InputDeviceTest.class);

    public InputDeviceTest() {
    }

    public void run() throws Exception {
        log.info("Running");

        //InputDevice[] devices = InputDeviceManager.getInputDevices();

        /*if (devices != null) {
            int ctr = 1;
            for (InputDevice dev : devices) {
                if (dev == null)
                    continue;
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
            }

            InputDevice dev = InputDeviceManager.queryDevice("/dev/input/event0");

            if (dev != null)
                log.info("Got Device: {}", dev);
        }*/

        //log.info("Initializing Listeners");
        InputDeviceManager.addInputEventListener(this::rawInputEventHandler);
        InputDeviceManager.addDeviceStateEventListener(this::deviceStateHandler);

        //log.info("Starting input event monitor");
        InputDeviceManager.startInputEventMonitor();

        while (true)
            Thread.sleep(100);
    }

    private void deviceStateHandler(DeviceStateEvent deviceStateEvent) {
        if (deviceStateEvent != null)
            log.info("Device State Event: (Device={}, Action={}, Path={})", deviceStateEvent.getDevice().getName(), deviceStateEvent.getAction(), deviceStateEvent.getDevice().getDevicePath());
        else
            log.info("null device state event");
    }

    private void rawInputEventHandler(RawInputEvent rawInputEvent) {
        if (rawInputEvent == null) {
            log.info("Null input event");
            return;
        }
        if (rawInputEvent.getType() == 1)
            log.info("Key Event = ({})", rawInputEvent);
        else if (rawInputEvent.getType() == 2)
            log.info("Mouse Event = {}", rawInputEvent);
        else {
            log.info("Unhandled Event: {}", rawInputEvent);
        }
    }

    public static void main(String[] args) throws Exception {
        new InputDeviceTest().run();
    }
}