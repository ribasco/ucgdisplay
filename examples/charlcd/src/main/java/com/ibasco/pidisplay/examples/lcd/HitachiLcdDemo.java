package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.components.RotaryEncoder;
import com.ibasco.pidisplay.components.RotaryState;
import com.ibasco.pidisplay.core.EventDispatchPhase;
import com.ibasco.pidisplay.core.enums.AlertType;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.enums.TextWrapStyle;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.util.Node;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.drivers.lcd.hd44780.Hd44780DisplayDriver;
import com.ibasco.pidisplay.drivers.lcd.hd44780.LcdTemplates;
import com.ibasco.pidisplay.drivers.lcd.hd44780.adapters.Mcp23017LcdAdapter;
import com.ibasco.pidisplay.impl.charlcd.LcdController;
import com.ibasco.pidisplay.impl.charlcd.components.LcdPane;
import com.ibasco.pidisplay.impl.charlcd.components.LcdText;
import com.ibasco.pidisplay.impl.charlcd.components.LcdTextBox;
import com.ibasco.pidisplay.impl.charlcd.components.dialog.LcdAlertDialog;
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

    private LcdController lcd;

    private Hd44780DisplayDriver lcdDriver;

    private Hd44780DisplayDriver lcdDriver2;

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
        Mcp23017LcdAdapter lcdAdapter = new Mcp23017LcdAdapter(mcpProvider, LcdTemplates.ADAFRUIT_I2C_RGBLCD_MCP23017);

        //initialize lcd driver
        lcdDriver = new Hd44780DisplayDriver(lcdAdapter, 20, 4);

        /*LcdPinMapConfig gpioLcdPinMap = new LcdPinMapConfig()
                .map(LcdPin.RS, RaspiPin.GPIO_21)
                .map(LcdPin.EN, RaspiPin.GPIO_24)
                .map(LcdPin.DATA_4, RaspiPin.GPIO_25)
                .map(LcdPin.DATA_5, RaspiPin.GPIO_26)
                .map(LcdPin.DATA_6, RaspiPin.GPIO_07)
                .map(LcdPin.DATA_7, RaspiPin.GPIO_05);

        StdGpioLcdAdapter gpioLcdAdapter = new StdGpioLcdAdapter(gpioLcdPinMap);
        lcdDriver2 = new LcdDriver(gpioLcdAdapter, 16, 2);*/

        lcd = new LcdController(lcdDriver);

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

    private LcdPane pane1 = new LcdPane();
    private LcdPane pane2 = new LcdPane();
    private LcdPane pane3 = new LcdPane();
    private List<LcdPane> paneList = new ArrayList<>();
    private LcdText label1 = new LcdText();
    private AtomicInteger startPos = new AtomicInteger(0);
    private LcdAlertDialog<String> alert = new LcdAlertDialog<>(AlertType.INFO);

    private ButtonReleasedListener buttonReleasedListener = buttonEvent -> {
        boolean success;
        switch ((String) buttonEvent.getButton().getTag()) {
            case "back":
                //success = lcdMenu.doExit();
                //lcd.hide();
                LcdPane group = paneList.get(startPos.incrementAndGet());
                log.info("Switching to next display : {}", group);
                lcd.show(group);
                break;
            case "next":
                //success = lcdMenu.doNext();
                break;
            case "previous":
                //success = lcdMenu.doPrevious();
                break;
            case "select":
                //log.info("Setting Result");
                //alert.setResult("Hello");
                log.info("Focus Current: {}", lcd.focusNext());
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
        ThreadUtils.sleep(interval);
    }

    public static void main(String[] args) throws Exception {
        new HitachiLcdDemo().run();
    }



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
        log.info("Switching to next display : {}", group);
        lcd.show(group);
    }

    private List<LcdPane> createPanes(String textFormat, int total) {
        List<LcdPane> lcdPanes = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            LcdPane pane = new LcdPane();
            pane.add(new LcdText(0, 0, String.format("Page Header (%d)", i)));
            pane.add(new LcdText(0, 1, String.format(textFormat, i)));
            pane.setName("Pane" + (i + 4));
            lcdPanes.add(pane);
        }
        return lcdPanes;
    }

    public void run() throws Exception {
        log.info("Running LCD Display");

        if (!lcd.grabInputFocus()) {
            log.error("Unable to grab input focus for controller : {}", lcd);
            return;
        } else
            log.info("Successfully acquired input focus for controller {}", lcd);

        pane1.setName("Pane1");
        pane2.setName("Pane2");
        pane3.setName("Pane3");

        pane1.add(label1);
        pane2.add(new LcdText("Pane #2"));
        pane3.add(new LcdText(0, 0, 20, 1, "Header"));
        pane3.add(new LcdText(0, 1, 15, 2, "Content"));
        pane3.add(new LcdText(15, 1, 5, 2, "Body Icon"));
        pane3.add(new LcdText(0, 3, 20, 1, "Footer"));

        LcdPane inputPane = new LcdPane();

        LcdText lblUsername = new LcdText(0, 0, "Username: ");
        lblUsername.setWidth(20);
        lblUsername.setTextAlignment(TextAlignment.RIGHT);
        LcdText lblPassword = new LcdText(0, 2, "Password: ");
        lblPassword.setWidth(20);
        lblPassword.setTextAlignment(TextAlignment.RIGHT);

        LcdTextBox tbUsername = new LcdTextBox(0, 1, 20, 1);
        tbUsername.setTextAlignment(TextAlignment.RIGHT);
        LcdTextBox tbPassword = new LcdTextBox(0, 3, 20, 1);
        tbPassword.setTextAlignment(TextAlignment.RIGHT);

        inputPane.add(lblUsername);
        inputPane.add(tbUsername);
        inputPane.add(lblPassword);
        inputPane.add(tbPassword);

        paneList.add(pane1);
        paneList.add(pane2);
        paneList.add(pane3);
        paneList.add(inputPane);

        paneList.addAll(createPanes("Item %d", 50));

        LcdText header = ((LcdText) pane3.getChildren().get(0));
        LcdText mainContent = (LcdText) pane3.getChildren().get(1);
        LcdText rightContent = (LcdText) pane3.getChildren().get(2);
        LcdText footer = (LcdText) pane3.getChildren().get(3);

        header.setTextAlignment(TextAlignment.RIGHT);
        footer.setTextAlignment(TextAlignment.CENTER);

        lcd.clear();
        lcd.addEventHandler(DisplayEvent.ANY, event -> log.info("Event Bubble = {}", event), EventDispatchPhase.CAPTURE);

        lcd.show(paneList.get(startPos.get()));

        label1.setWidth(20);
        label1.setHeight(4);
        label1.setTextAlignment(TextAlignment.RIGHT);
        label1.setTextWrapStyle(TextWrapStyle.WORD);
        label1.setText("Old pirates, yes, they rob I, Sold I to the merchant ships, Minutes after they took I From the bottomless pit. But my hand was made strong By the hand of the Almighty. We forward in this generation Triumphantly. Won't you help to sing These songs of freedom? 'Cause all I ever have, Redemption songs, Redemption songs. Emancipate yourself from mental slavery, None but ourselves can free our minds. Have no fear for atomic energy, 'Cause none of them can stop the time. How long shall they kill our prophets, While we stand aside and look? Some say it's just a part of it, We've got to fulfill the book. Won't you help to sing These songs of freedom? 'Cause all I ever have, Redemption songs, Redemption songs, Redemption songs. Emancipate yourself from mental slavery, None but ourselves can free our mind. Have no fear for atomic energy, 'Cause none of them can stop the time. How long shall they kill our prophets, While we stand aside and look? Some say it's just a part of it, We've got to fulfill the book. Won't you help to sing, These songs of freedom? 'Cause all I ever had, Redemption songs. All I ever had, Redemption songs These songs of freedom Songs of freedom");

        //Scroll text demo
        CompletableFuture.runAsync(() -> {
            while (!shutdown.get()) {
                //log.info("Line Count: {}, Width: {}", label1.getLineCount(), label1.getWidth());
                for (int i = 0, x = 0; i < 50; i++, x++) {
                    label1.setScrollTop(i);
                    //log.info("Scrolling Line: {}", i);
                    delay(1000);
                }
            }
        });

        //Toggle visible states
        CompletableFuture.runAsync(new Runnable() {
            boolean state = false;
            @Override
            public void run() {
                while (!shutdown.get()) {
                    state = !state;
                    //mainContent.setVisible(state);
                    header.setVisible(!state);
                    footer.setVisible(state);
                    rightContent.setVisible(!state);
                    //pane3.setVisible(state);
                    delay(1000);
                }
            }
        });

        //Set counter
        CompletableFuture.runAsync(() -> {
            while (!shutdown.get()) {
                for (int i = 0, x = 0; i < 101; i++, x++) {
                    /*if (mainContent.isActive())
                        log.info("Updating Counter: {}", i);*/
                    mainContent.setText("Counter: " + i);
                    delay(100);
                }
            }
        });

        //Wait
        while (!shutdown.get()) {
            ThreadUtils.sleep(500);
        }

        log.info("Shutting down...");
        gpio.shutdown();
        executorService.shutdown();
        lcd.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
}
