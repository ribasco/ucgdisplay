package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.core.EventDispatchPhase;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.events.ListViewItemEvent;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.examples.lcd.drivers.BackpackI2CDriver;
import com.ibasco.pidisplay.examples.lcd.exceptions.I2CException;
import com.ibasco.pidisplay.impl.charlcd.LcdController;
import com.ibasco.pidisplay.impl.charlcd.components.LcdListView;
import com.ibasco.pidisplay.impl.charlcd.components.LcdPane;
import com.ibasco.pidisplay.impl.charlcd.components.LcdText;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ibasco.pidisplay.examples.lcd.BackpackController.*;
import static org.slf4j.LoggerFactory.getLogger;

public class LcdMenuTest {
    public static final Logger log = getLogger(LcdMenuTest.class);
    private LcdController lcdController;
    private AtomicBoolean shutdown = new AtomicBoolean(false);
    private BackpackController bpController;
    private LcdPane lcdPane;
    private LcdPane lcdPaneHeader;
    private LcdText textHeader;
    private LcdText textRotary;
    private LcdText textToggleSw;
    LcdListView<String> lcdListView;
    private int rotaryValue = 100;

    public LcdMenuTest() throws IOException, I2CFactory.UnsupportedBusNumberException {
        BackpackI2CDriver bpI2CDriver = new BackpackI2CDriver(0x15);
        bpController = new BackpackController(bpI2CDriver);
        bpController.setInputEventHandler(this::inputEventHandler);
        lcdController = new LcdController(bpController.getCharDisplayDriver());
        lcdPane = new LcdPane();
        lcdPaneHeader = new LcdPane();

        lcdListView = new LcdListView<>();
        //lcdListView.setTopPos(0);
        //lcdListView.setHeight(2);

        textHeader = new LcdText(0, 0, 20, 1, "");
        textRotary = new LcdText(0, 1, 20, 1, "");
        textToggleSw = new LcdText(0, 2, 20, 1, "");
        textHeader.setTextAlignment(TextAlignment.RIGHT);
        textRotary.setTextAlignment(TextAlignment.RIGHT);
        textToggleSw.setTextAlignment(TextAlignment.RIGHT);

        lcdPane.add(textHeader);
        lcdPane.add(textRotary);
        lcdPane.add(textToggleSw);

        lcdPaneHeader.add(lcdListView);
    }

    public static void main(String[] args) throws Exception {
        new LcdMenuTest().run();
    }

    public void run() throws Exception {
        lcdListView.addEventHandler(ListViewItemEvent.ITEM_FOCUSED, event -> log.info("Item Focus: {}", event.getIndex()), EventDispatchPhase.CAPTURE);
        lcdListView.addEventHandler(ListViewItemEvent.ITEM_SELECTED, event -> log.info("Item Selected: {}", event.getIndex()), EventDispatchPhase.CAPTURE);

        for (int i = 0; i < 100; i++) {
            lcdListView.getItems().add("Entry: " + i);
        }

        lcdController.show(lcdListView);
        //textHeader.setText("Event Test");

        while (!shutdown.get()) {
            ThreadUtils.sleep(35000);
        }

        bpController.close();
    }

    private void inputEventHandler(int eventCode, int eventValue) {
        switch (eventCode) {
            case ROTARY_NONE:
                break;
            case ROTARY_LEFT:
                log.info("Left");
                lcdListView.doPrevious();
                break;
            case ROTARY_RIGHT:
                log.info("Right");
                lcdListView.doNext();
                break;
            case BUTTON_HELD:
                log.info("Button {} Held", eventValue);
                break;
            case BUTTON_PRESSED:
                log.info("Button {} Pressed", eventValue);
                break;
            case BUTTON_RELEASED:
                log.info("Button {} Released", eventValue);
                break;
            case TOGGLE_SW_STATE:
                textToggleSw.setText("Toggle Switch: " + ((eventValue >= 1) ? "ON" : "OFF"));
                boolean on = eventValue >= 1;
                try {
                    if (on)
                        bpController.setLedOn(LedType.TOGGLE_SWITCH, 100, RGB(255, 0, 0, 0));
                    else
                        bpController.setLedOff(LedType.TOGGLE_SWITCH);
                } catch (I2CException e) {
                    log.error(e.getMessage(), e);
                }
                break;
            default:
                log.error("Unknown Event: {} = {}", eventCode, eventValue);
                break;
        }
    }
}
