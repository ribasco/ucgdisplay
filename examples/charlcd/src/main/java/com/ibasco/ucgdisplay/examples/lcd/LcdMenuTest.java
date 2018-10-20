package com.ibasco.ucgdisplay.examples.lcd;

import com.ibasco.ucgdisplay.core.EventDispatchPhase;
import com.ibasco.ucgdisplay.core.enums.TextAlignment;
import com.ibasco.ucgdisplay.core.events.ListViewItemEvent;
import com.ibasco.ucgdisplay.core.ui.CharGraphics;
import com.ibasco.ucgdisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.ucgdisplay.examples.lcd.drivers.BackpackI2CDriver;
import com.ibasco.ucgdisplay.examples.lcd.exceptions.I2CException;
import com.ibasco.ucgdisplay.impl.charlcd.LcdController;
import com.ibasco.ucgdisplay.impl.charlcd.components.LcdListView;
import com.ibasco.ucgdisplay.impl.charlcd.components.LcdPane;
import com.ibasco.ucgdisplay.impl.charlcd.components.LcdText;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ibasco.ucgdisplay.examples.lcd.BackpackController.*;
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
        lcdListView.setItemFactory(this::itemFactory);
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
        lcdListView.addEventHandler(ListViewItemEvent.ITEM_ENTER_FOCUS, event -> log.info("Enter Focus: {}", event.getIndex()), EventDispatchPhase.CAPTURE);
        lcdListView.addEventHandler(ListViewItemEvent.ITEM_EXIT_FOCUS, event -> log.info("Exit Focus: {}", event.getIndex()), EventDispatchPhase.CAPTURE);
        lcdListView.addEventHandler(ListViewItemEvent.ITEM_SELECTED, event -> log.info("Item Selected: {}", event.getIndex()), EventDispatchPhase.CAPTURE);
        lcdListView.addEventHandler(ListViewItemEvent.ITEM_DESELECTED, event -> log.info("Item Deselected: {}", event.getIndex()), EventDispatchPhase.CAPTURE);

        for (int i = 0; i < 100; i++) {
            lcdListView.getItems().add("Entry: " + i);
        }

        lcdController.show(lcdListView);

        while (!shutdown.get()) {
            ThreadUtils.sleep(35000);
        }

        bpController.close();
    }

    private long _previousMillis = 0;
    private long interval = 500;
    private boolean state = true;

    private <Y> void itemFactory(LcdListView<Y> listView, Y item, CharGraphics graphics, boolean focused, boolean selected, int pageIndex, int realIndex) {
        long current = System.currentTimeMillis();
        if ((current - _previousMillis) >= interval) {
            state = !state;
            _previousMillis = current;
        }

        String text = StringUtils.left(Objects.toString(item, ""), graphics.getWidth());

        if (focused) {
            graphics.drawChar(listView.getItemIconFocused());
        } else if (selected) {
            graphics.drawChar(listView.getItemIconSelected());
        } else {
            graphics.drawChar(listView.getItemIcon());
        }

        if (focused) {
            if (state) {
                graphics.drawText(" ");
                graphics.drawText(text);
            } else {
                graphics.drawText(" ");
            }
        } else {
            graphics.drawText(" ");
            graphics.drawText(text);
        }
    }

    private void inputEventHandler(int eventCode, int eventValue) {
        switch (eventCode) {
            case ROTARY_NONE:
                break;
            case ROTARY_LEFT:
                lcdListView.doPrevious();
                break;
            case ROTARY_RIGHT:
                lcdListView.doNext();
                break;
            case BUTTON_HELD:
                break;
            case BUTTON_PRESSED:
                break;
            case BUTTON_RELEASED:
                if (eventValue == BUTTON_C) {
                    lcdListView.select(lcdListView.getFocusedIndex());
                } else if (eventValue == BUTTON_B) {
                    try {
                        shutdown.set(true);
                        bpController.close();
                        lcdController.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
