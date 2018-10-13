package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.util.Node;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.drivers.lcd.hd44780.HD44780DisplayDriver;
import com.ibasco.pidisplay.drivers.lcd.hd44780.LcdTemplates;
import com.ibasco.pidisplay.drivers.lcd.hd44780.adapters.Mcp23017LcdAdapter;
import com.ibasco.pidisplay.drivers.lcd.hd44780.providers.MCP23017GpioProviderExt;
import com.ibasco.pidisplay.impl.charlcd.components.LcdMenuOld;
import com.ibasco.pidisplay.impl.charlcd.enums.LcdMenuCursorOrientation;
import com.ibasco.pidisplay.impl.charlcd.enums.LcdMenuItemStyle;
import com.ibasco.pidisplay.impl.charlcd.events.LcdMenuItemEvent;
import com.ibasco.pidisplay.impl.charlcd.events.LcdMenuNavEvent;
import com.pi4j.component.button.Button;
import com.pi4j.component.button.ButtonHoldListener;
import com.pi4j.component.button.ButtonReleasedListener;
import com.pi4j.component.button.impl.GpioButtonComponent;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("Duplicates")
public class HitachiLcdDemoOld {
    private static final Logger log = LoggerFactory.getLogger(HitachiLcdDemoOld.class);
    private static final byte ADDR_MCP23017 = 0x20;

    private final GpioController gpio = GpioFactory.getInstance();

    private ScheduledExecutorService executorService;
    private MCP23017GpioProviderExt mcpProvider;
    private final I2CBus bus;
    private final GpioPinDigitalOutput lcdBacklightPin;
    private final Button button1;
    private final Button button2;
    private final Button button3;
    private final Button button4;

    private HD44780DisplayDriver lcdDriver;

    private LcdMenuOld lcdMenu;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    public HitachiLcdDemoOld() throws IOException, I2CFactory.UnsupportedBusNumberException {
        //Initialize I2C Bus
        bus = I2CFactory.getInstance(I2CBus.BUS_1);

        // create custom MCP23017 GPIO provider
        mcpProvider = new MCP23017GpioProviderExt(bus, ADDR_MCP23017);

        //LCD
        lcdBacklightPin = gpio.provisionDigitalOutputPin(mcpProvider, MCP23017Pin.GPIO_B6, "LCD - Backlight");
        lcdBacklightPin.setShutdownOptions(true, PinState.LOW);

        //initialize executor service
        ThreadFactory lcdThreadFactory = new ThreadFactory() {
            private AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("lcd-event-%d", threadNum.incrementAndGet()));
            }
        };
        executorService = Executors.newScheduledThreadPool(5, lcdThreadFactory);

        //initialize lcdDriver adapter
        Mcp23017LcdAdapter lcdAdapter = new Mcp23017LcdAdapter(mcpProvider, LcdTemplates.ADAFRUIT_I2C_RGBLCD_MCP23017);

        //initialize lcd driver
        lcdDriver = new HD44780DisplayDriver(lcdAdapter, 20, 4);

        byte[] returnChar = new byte[]{
                0b00000,
                0b00100,
                0b01110,
                0b00100,
                0b00100,
                0b01100,
                0b00000,
                0b00000
        };

        lcdDriver.createChar(0, returnChar);

        //toggle lcd backlight
        lcdBacklightPin.high();

        //Initialize Buttons
        button1 = createButton(RaspiPin.GPIO_06, "Back", "back", 3500, buttonHoldListener);
        button2 = createButton(RaspiPin.GPIO_05, "Previous", "previous");
        button3 = createButton(RaspiPin.GPIO_04, "Next", "next");
        button4 = createButton(RaspiPin.GPIO_01, "Select", "select");

        Node<String> menuEntries = createMenuEntries();

        printNodeTree(menuEntries);

        //displayManager = new LcdDisplayManager(lcdDriver);

        lcdMenu = new LcdMenuOld(menuEntries);
        //Properties
        lcdMenu.setHeaderTextAlignment(TextAlignment.LEFT);
        lcdMenu.setCursorOrientation(LcdMenuCursorOrientation.LEFT);
        lcdMenu.setItemTextAlignment(TextAlignment.LEFT);
        lcdMenu.setItemStyle(LcdMenuItemStyle.NUMBERED);

        //Event Callbacks
        lcdMenu.setOnItemSelected(this::onItemSelected);
        lcdMenu.setOnItemFocus(this::onItemFocus);
        lcdMenu.setOnNavNext(this::onNavItem);
        lcdMenu.setOnNavPrevious(this::onNavItem);
        lcdMenu.setOnNavEnter(this::onNavItem);
        lcdMenu.setOnNavExit(this::onNavItem);
    }

    private void onItemFocus(LcdMenuItemEvent lcdMenuItemEvent) {
        log.debug("Item Focus: {}", lcdMenuItemEvent);
    }

    private void onItemSelected(LcdMenuItemEvent lcdMenuItemEvent) {
        log.info("Item Selected!: {} = {}", lcdMenuItemEvent.getSelectedItem().getName(), lcdMenuItemEvent.getSelectedIndex());
    }

    private void onNavItem(LcdMenuNavEvent event) {
        /*if (event.getSelectedNode() != null)
            log.info("{} = {}", event.getEventType(), event.getSelectedNode().getName());*/
    }

    private void printNodeTree(Node<String> root) {
        Node.recurse(root, this::print);
    }

    private void print(Node<String> node, int depth) {
        log.info("{}{}, Level: {}, Has Child: {}",
                StringUtils.repeat("\t", depth),
                node.getName(),
                depth,
                node.hasChildren());
    }

    private Node<String> createMenuEntries() {
        Node<String> top1 = new Node<>("Functions");

        top1.addChild(new Node<>("top1-a1"));
        top1.addChild(new Node<>("top1-a2"));
        top1.addChild(new Node<>("top1-a3"));
        top1.addChild(new Node<>("top1-a4"));
        top1.addChild(new Node<>("top1-a5"));

        Node<String> top1a5 = top1.getLast();
        top1a5.addChild(new Node<>("top1-a5-a"));
        top1a5.addChild(new Node<>("top1-a5-b"));

        Node<String> top1a5a = top1a5.getLast();
        top1a5a.addChild(new Node<>("top1-a5-b-1"));
        top1a5a.addChild(new Node<>("top1-a5-b-2"));
        top1a5a.addChild(new Node<>("top1-a5-b-3"));
        top1a5a.addChild(new Node<>("top1-a5-b-4"));
        top1a5a.addChild(new Node<>("top1-a5-b-5"));
        top1a5a.addChild(new Node<>("top1-a5-b-6"));
        top1a5a.addChild(new Node<>("top1-a5-b-7"));
        top1a5a.addChild(new Node<>("top1-a5-b-8"));
        top1a5a.addChild(new Node<>("top1-a5-b-9"));
        top1a5a.addChild(new Node<>("top1-a5-b-10"));

        top1a5.addChild(new Node<>("top1-a5-c"));
        top1a5.addChild(new Node<>("top1-a5-d"));

        Node<String> top2 = new Node<>("Settings");
        top2.addChild(new Node<>("Display"));
        top2.addChild(new Node<>("Power"));
        top2.addChild(new Node<>("Audio"));
        top2.addChild(new Node<>("System"));
        top2.addChild(new Node<>("Network"));
        top2.addChild(new Node<>("Clock"));

        Node<String> top3 = new Node<>("Status");
        top3.addChild(new Node<>("Network"));
        top3.addChild(new Node<>("Audio"));
        top3.addChild(new Node<>("top3-a3"));
        top3.addChild(new Node<>("top3-a4"));
        top3.addChild(new Node<>("top3-a5"));
        top3.addChild(new Node<>("top3-a6"));
        top3.addChild(new Node<>("top3-a7"));

        Node<String> top3a7 = top3.getLast();
        top3a7.addChild(new Node<>("top3-a7-a"));
        top3a7.addChild(new Node<>("top3-a7-b"));
        top3a7.addChild(new Node<>("top3-a7-c"));
        top3a7.addChild(new Node<>("top3-a7-d"));
        top3a7.addChild(new Node<>("top3-a7-e"));
        top3a7.addChild(new Node<>("top3-a7-f"));

        Node<String> rootNode = new Node<>("Main Menu ${cursor} ${date:MM-dd-yyyy hh:mm:ss} ${pageNum}/${pageCount}");
        rootNode.addChild(top1);
        rootNode.addChild(top2);
        rootNode.addChild(top3);
        rootNode.addChild(new Node<>("Top 4"));
        rootNode.addChild(new Node<>("Really long fucking text"));

        return rootNode;
    }

    private GpioButtonComponent createButton(Pin inputPin, String name, String tag) {
        return createButton(inputPin, name, tag, -1, null);
    }

    private GpioButtonComponent createButton(Pin buttonPin, String name, String tag, int holdInterval, ButtonHoldListener holdListener) {
        GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(buttonPin, name, PinPullResistance.PULL_UP);
        inputPin.setDebounce(64);
        GpioButtonComponent button = new GpioButtonComponent(inputPin, PinState.HIGH, PinState.LOW);
        button.setName(name);
        button.setTag(tag);
        button.addListener(buttonReleasedListener);
        if (holdListener != null) {
            button.addListener(holdInterval, holdListener);
        }
        return button;
    }

    private ButtonReleasedListener buttonReleasedListener = buttonEvent -> {
        boolean success;
        switch ((String) buttonEvent.getButton().getTag()) {
            case "back":
                success = lcdMenu.doExit();
                break;
            case "next":
                success = lcdMenu.doNext();
                break;
            case "previous":
                success = lcdMenu.doPrevious();
                break;
            case "select":
                if (lcdMenu.getSelectedItem().hasChildren()) {
                    success = lcdMenu.doEnter();
                    break;
                }
                success = lcdMenu.doSelect();
                break;
            default:
                success = false;
        }
        if (!success)
            log.warn("Button not executed");
    };

    private ButtonHoldListener buttonHoldListener = buttonEvent -> {
        log.info("Button {} held", buttonEvent.getButton().getName());
        switch ((String) buttonEvent.getButton().getTag()) {
            case "back":
                log.info("Back button held. Shutting down");
                shutdown.set(true);
                break;
            case "next":
                log.info("Next button held");
                break;
            case "previous":
                log.info("Previous button held");
                break;
            case "select":
                log.info("Select button held");
                break;
        }
        log.info("Exiting button hold listener");
    };

    public void run() throws Exception {
        log.info("Running LCD Display");
        while (!shutdown.get()) {
            delay(500);
        }
        log.info("Shutting down...");
        //((GpioButtonComponent) button1).close();
        gpio.shutdown();
        executorService.shutdown();
        //lcdMenu.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    private void delay(int interval) {
        ThreadUtils.sleep(interval);
    }

    public static void main(String[] args) {
        try {
            new HitachiLcdDemoOld().run();
            log.info("Shutdown Successful");
        } catch (Exception e) {
            log.error("An error has occured", e);
        }
    }
}
