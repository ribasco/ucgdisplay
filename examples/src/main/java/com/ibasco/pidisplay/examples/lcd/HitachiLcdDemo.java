package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.components.RotaryEncoder;
import com.ibasco.pidisplay.components.RotaryState;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.enums.TextWrapStyle;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import com.ibasco.pidisplay.core.util.Node;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdTemplates;
import com.ibasco.pidisplay.drivers.lcd.hitachi.adapters.Mcp23017LcdAdapter;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdManager;
import com.ibasco.pidisplay.impl.lcd.hitachi.components.LcdPane;
import com.ibasco.pidisplay.impl.lcd.hitachi.components.LcdText;
import com.pi4j.component.button.Button;
import com.pi4j.component.button.ButtonHoldListener;
import com.pi4j.component.button.ButtonReleasedListener;
import com.pi4j.component.button.impl.GpioButtonComponent;
import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("Duplicates")
public class HitachiLcdDemo {

    private static final Logger log = LoggerFactory.getLogger(HitachiLcdDemo.class);
    private static final byte ADDR_MCP23017 = 0x20;

    private final GpioController gpio = GpioFactory.getInstance();

    private ScheduledExecutorService executorService;
    private MCP23017GpioProvider mcpProvider;
    private final I2CBus bus;
    private final GpioPinDigitalOutput lcdBacklightPin;
    private final Button button1;
    private final Button btnSelect;

    private LcdManager lcdManager;

    private LcdDriver lcdDriver;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private RotaryEncoder rotaryEncoder;

    public HitachiLcdDemo() throws Exception {
        //Initialize I2C Bus
        bus = I2CFactory.getInstance(I2CBus.BUS_1);

        // create custom MCP23017 GPIO provider
        mcpProvider = new MCP23017GpioProvider(bus, ADDR_MCP23017);

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
        Mcp23017LcdAdapter lcdAdapter = new Mcp23017LcdAdapter(mcpProvider, LcdTemplates.ADAFRUIT_I2C_RGBLCD);

        //initialize lcd driver
        lcdDriver = new LcdDriver(lcdAdapter, 20, 4);

        lcdManager = new LcdManager(lcdDriver);

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

        rotaryEncoder = new RotaryEncoder(RaspiPin.GPIO_05, RaspiPin.GPIO_04, -1);
        rotaryEncoder.setListener(this::rotaryChange);

        //Initialize Buttons
        button1 = createButton(RaspiPin.GPIO_06, "Back", "back", 3500, buttonHoldListener);
        btnSelect = createButton(RaspiPin.GPIO_01, "Select", "select");

        Node<String> menuEntries = createMenuEntries();

        //printNodeTree(menuEntries);
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
                //success = lcdMenu.doExit();
                break;
            case "next":
                //success = lcdMenu.doNext();
                break;
            case "previous":
                //success = lcdMenu.doPrevious();
                break;
            case "select":
                log.info("Select Button Pressed");
                lcdManager.setActiveDisplay(null);
                break;
            default:
                success = false;
        }
        /*if (!success)
            log.warn("Button not executed");*/
    };

    private ButtonHoldListener buttonHoldListener = buttonEvent -> {
        log.info("Button {} held", buttonEvent.getButton().getName());
        switch ((String) buttonEvent.getButton().getTag()) {
            case "back":
                log.info("Back button held. Shutting down");
                shutdown.set(true);
                break;
            case "select":
                log.info("Select button held");
                break;
        }
        log.info("Exiting button hold listener");
    };

    private void delay(int interval) {
        ThreadUtils.sleepUninterrupted(interval);
    }

    public static void main(String[] args) throws Exception {
        new HitachiLcdDemo().run();
    }

    private LcdPane pane1 = new LcdPane();
    private LcdPane pane2 = new LcdPane();
    private LcdPane pane3 = new LcdPane();
    private List<LcdPane> paneList = new ArrayList<>();
    private LcdText label1 = new LcdText();
    private AtomicInteger startPos = new AtomicInteger(0);

    private void rotaryChange(long l, RotaryState state) {
        log.info("Rotary Change: {}, State: {}", startPos, state);

        if (state == RotaryState.DOWN) {
            if ((startPos.get() + 1) <= (paneList.size() - 1))
                startPos.incrementAndGet();
        } else {
            if ((startPos.get() - 1) >= 0)
                startPos.decrementAndGet();
        }

        LcdPane group = paneList.get(startPos.get());
        lcdManager.setActiveDisplay(group);
    }

    public void run() throws Exception {
        log.info("Running LCD Display");

        pane1.setName("Pane #1");
        pane2.setName("Pane #2");
        pane3.setName("Pane #3");

        pane1.add(label1);
        pane2.add(new LcdText("Hello World"));

        pane3.add(new LcdText(0, 0, 20, 1, "Header"));
        pane3.add(new LcdText(0, 1, 15, 2, "Content"));
        pane3.add(new LcdText(15, 1, 5, 2, "Body Icon"));
        pane3.add(new LcdText(0, 3, 20, 1, "Footer"));
        paneList.add(pane1);
        paneList.add(pane2);
        paneList.add(pane3);

        LcdText header = ((LcdText) pane3.getChildren().get(0));
        LcdText content = (LcdText) pane3.getChildren().get(1);
        LcdText footer = (LcdText) pane3.getChildren().get(3);
        header.setTextAlignment(TextAlignment.CENTER);
        footer.setTextAlignment(TextAlignment.CENTER);

        lcdManager.setActiveDisplay(paneList.get(startPos.get()));

        label1.setWidth(20);
        label1.setHeight(4);
        label1.setTextAlignment(TextAlignment.RIGHT);
        label1.setTextWrapStyle(TextWrapStyle.WORD);
        label1.setText("Old pirates, yes, they rob I, Sold I to the merchant ships, Minutes after they took I From the bottomless pit. But my hand was made strong By the hand of the Almighty. We forward in this generation Triumphantly. Won't you help to sing These songs of freedom? 'Cause all I ever have, Redemption songs, Redemption songs. Emancipate yourself from mental slavery, None but ourselves can free our minds. Have no fear for atomic energy, 'Cause none of them can stop the time. How long shall they kill our prophets, While we stand aside and look? Some say it's just a part of it, We've got to fulfill the book. Won't you help to sing These songs of freedom? 'Cause all I ever have, Redemption songs, Redemption songs, Redemption songs. Emancipate yourself from mental slavery, None but ourselves can free our mind. Have no fear for atomic energy, 'Cause none of them can stop the time. How long shall they kill our prophets, While we stand aside and look? Some say it's just a part of it, We've got to fulfill the book. Won't you help to sing, These songs of freedom? 'Cause all I ever had, Redemption songs. All I ever had, Redemption songs These songs of freedom Songs of freedom");

        CompletableFuture.runAsync(() -> {
            boolean state = false;
            log.info("Line Count: {}, Width: {}", label1.lineCount(), label1.getWidth());
            for (int i = 0, x = 0; i < 100; i++, x++) {
                state = !state;
                label1.setScrollTop(i);
                //label1.setScrollLeft(x);
                content.setText("Counter: " + i);
                //content.setVisible(state);
                header.setVisible(!state);
                footer.setVisible(state);
                //pane3.setVisible(state);
                delay(1000);
            }
        });

        //Wait
        while (!shutdown.get()) {
            ThreadUtils.sleepUninterrupted(500);
        }

        log.info("Shutting down...");
        gpio.shutdown();
        executorService.shutdown();
        EventDispatcher.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
}
