package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.components.RotaryEncoder;
import com.ibasco.pidisplay.components.RotaryState;
import com.ibasco.pidisplay.core.EventDispatchPhase;
import com.ibasco.pidisplay.core.enums.AlertType;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.enums.TextWrapStyle;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.util.ByteUtils;
import com.ibasco.pidisplay.core.util.Node;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import com.ibasco.pidisplay.drivers.clcd.CharDisplayDriver;
import com.ibasco.pidisplay.drivers.clcd.CharProxyDisplayDriver;
import com.ibasco.pidisplay.impl.charlcd.LcdController;
import com.ibasco.pidisplay.impl.charlcd.components.LcdListView;
import com.ibasco.pidisplay.impl.charlcd.components.LcdPane;
import com.ibasco.pidisplay.impl.charlcd.components.LcdText;
import com.ibasco.pidisplay.impl.charlcd.components.LcdTextInput;
import com.ibasco.pidisplay.impl.charlcd.components.dialog.LcdAlertDialog;
import com.pi4j.component.button.ButtonHoldListener;
import com.pi4j.component.button.ButtonReleasedListener;
import com.pi4j.component.button.impl.GpioButtonComponent;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.serial.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("Duplicates")
public class HitachiLcdDemo {

    private static final Logger log = LoggerFactory.getLogger(HitachiLcdDemo.class);
    private static final byte ADDR_MCP23017 = 0x20;

    private final GpioController gpio = GpioFactory.getInstance();

    private ScheduledExecutorService executorService;
    //private MCP23017GpioProviderExt mcpProvider;
    private final I2CBus bus;
    //private final GpioPinDigitalOutput lcdBacklightPin;
    //private final Button button1;
    //private final Button btnSelect;

    private LcdController lcd;

    private CharDisplayDriver lcdDriver;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private RotaryEncoder rotaryEncoder;

    public HitachiLcdDemo() throws Exception {
        //Initialize I2C Bus
        bus = I2CFactory.getInstance(I2CBus.BUS_1);

        // create custom MCP23017 GPIO provider
        //mcpProvider = new MCP23017GpioProviderExt(bus, ADDR_MCP23017);

        //LCD
        //lcdBacklightPin = gpio.provisionDigitalOutputPin(mcpProvider, MCP23017Pin.GPIO_B6, "LCD - Backlight");
        //lcdBacklightPin.setShutdownOptions(true, PinState.LOW);

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
        //Mcp23017LcdAdapter lcdAdapter = new Mcp23017LcdAdapter(mcpProvider, LcdTemplates.ADAFRUIT_I2C_RGBLCD_MCP23017);

        //initialize lcd driver
        //lcdDriver = new HD44780DisplayDriver(lcdAdapter, 20, 4);
        lcdDriver = new CharProxyDisplayDriver(0x15, 20, 4);

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
        //lcdBacklightPin.high();

        //rotaryEncoder = new RotaryEncoder(RaspiPin.GPIO_05, RaspiPin.GPIO_04, -1);
        //rotaryEncoder.setListener(this::rotaryChange);

        //Initialize Buttons
        //button1 = createButton(RaspiPin.GPIO_06, "Back", "back", 3500, buttonHoldListener);
        //btnSelect = createButton(RaspiPin.GPIO_27, "Select", "select"); //rotary encoder

        //Node<String> menuEntries = createMenuEntries();

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

    private void testPlay(String filename) {
        try {
            File file = new File(filename);
            showAudioProps(file);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioInputStream din;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);

            log.info("Setting Equalizer Settings");
            // DecodedMpegAudioInputStream properties
            if (din instanceof javazoom.spi.PropertiesContainer) {
                Map properties = ((javazoom.spi.PropertiesContainer) din).properties();
                float[] equalizer = (float[]) properties.get("mp3.equalizer");
                equalizer[0] = 0.5f;
                equalizer[31] = 0.25f;
            }

            // Play now.
            rawplay(decodedFormat, din);
            in.close();
        } catch (Exception e) {
            //Handle exception.
        }
    }

    private void showAudioProps(File file) throws IOException, UnsupportedAudioFileException {
        AudioFileFormat baseFileFormat;
        AudioFormat baseFormat;
        baseFileFormat = AudioSystem.getAudioFileFormat(file);
        baseFormat = baseFileFormat.getFormat();
        String author = null;
        Integer bitrate = null;
        Long duration = null;

        // TAudioFileFormat properties
        if (baseFileFormat instanceof TAudioFileFormat) {
            Map properties = baseFileFormat.properties();
            author = (String) properties.get("author");
            duration = (Long) properties.get("duration");
            /*key = "mp3.id3tag.v2";
            InputStream tag = (InputStream) properties.get(key);*/
        }
        // TAudioFormat properties
        if (baseFormat instanceof TAudioFormat) {
            Map properties = baseFormat.properties();
            bitrate = (Integer) properties.get("bitrate");
        }
        log.info("Author: {}, Bitrate: {}, Duration: {}", author, bitrate, duration);
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] buffer = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null) {
            // Start
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1) {
                nBytesRead = din.read(buffer, 0, buffer.length);
                if (nBytesRead != -1) {
                    nBytesWritten = line.write(buffer, 0, nBytesRead);
                }
                delay(15);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    private void showTest1() {

        /*
        if (!lcd.grabInputFocus()) {
            log.error("Unable to grab input focus for controller : {}", lcd);
            return;
        } else
            log.info("Successfully acquired input focus for controller {}", lcd);*/

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

        LcdText lblUsername = new LcdText(0, 0, 20, 1, "Username: ");
        lblUsername.setTextAlignment(TextAlignment.CENTER);
        LcdText lblPassword = new LcdText(0, 2, 20, 1, "Password: ");
        lblPassword.setTextAlignment(TextAlignment.CENTER);

        LcdTextInput tbUsername = new LcdTextInput(0, 1, 20, 1);
        tbUsername.setTextAlignment(TextAlignment.RIGHT);
        LcdTextInput tbPassword = new LcdTextInput(0, 3, 20, 1);
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
                    mainContent.setText("Holy: " + i);
                    delay(200);
                }
            }
        });

        CompletableFuture.runAsync(() -> {
            String soundPath = getClass().getClassLoader().getResource("Alert2.mp3").getPath();
            log.info("Playing MP3 Sound: {}", soundPath);
            testPlay(soundPath);
        });
    }


    private void showTest2() {

        LcdPane alarmClockPane = new LcdPane();
        LcdText day = new LcdText(0, 0, 20, 1, "");
        day.setTextAlignment(TextAlignment.LEFT);
        LcdText date = new LcdText(0, 1, 20, 1, "");
        date.setTextAlignment(TextAlignment.LEFT);
        LcdText time = new LcdText(0, 3, 20, 1, "");
        time.setTextAlignment(TextAlignment.LEFT);

        alarmClockPane.add(day);
        alarmClockPane.add(date);
        alarmClockPane.add(time);

        alarmClockPane.activeProperty().addListener((observable, oldValue, newValue) -> {
            log.info("Alarm Clock Pane Shown");
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    /*String file = getClass().getClassLoader().getResource("Alert2.mp3").getPath();
                    testPlay(file);*/
                }
            }).whenComplete((aVoid, throwable) -> log.info("Play music complete"));
            log.info("Has Children: {}", alarmClockPane.hasChildren());
        });

        lcd.clear();
        lcd.show(alarmClockPane);

        CompletableFuture.runAsync(() -> {
            while (!shutdown.get()) {
                ZonedDateTime dateTime = ZonedDateTime.now();

                String dayValue = dateTime.format(DateTimeFormatter.ofPattern("EEEE, z Z"));
                String dateValue = dateTime.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
                String timeValue = dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));

                day.setText(dayValue);
                date.setText(dateValue);
                time.setText(timeValue);

                delay(500);
            }
        }).whenComplete((aVoid, throwable) -> {
            if (throwable != null)
                log.error("Error", throwable);
            shutdown.set(true);
        });
    }

    private void showListViewTest() {
        LcdListView<String> listView = new LcdListView<>();
        listView.getItems().add("Item 1 ${CUA}");
        listView.getItems().add("Item 2");
        listView.getItems().add("Item 3");
        listView.getItems().add("Item 4");
        listView.getItems().add("Item 5");
        listView.getItems().add("Item 6");
        listView.getItems().add("Item 7");
        listView.getItems().add("Item 8");
        listView.getItems().add("Item 9");
        listView.getItems().add("Item 10");

        lcd.show(listView);

        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                delay(5000);
                log.info("REMOVING ITEMS");
                listView.getItems().remove(0);
            }
        });
    }

    private Serial setupSerialComms() {
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

    private void run() throws Exception {
        log.info("Running LCD Display");
        //showListViewTest();

        Serial ser = setupSerialComms();

        GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, "ALERT_PIN");
        inputPin.addListener((GpioPinListenerDigital) event -> log.info("Alert Pin Triggered: {}", event.getState().getValue()));

        LcdPane testPane = new LcdPane();
        LcdText header = new LcdText(0, 0, 20, 1, "Counter Test");
        header.setTextAlignment(TextAlignment.CENTER);
        LcdText counter = new LcdText(0, 1, 20, 1, "0");
        LcdText footer = new LcdText(0, 3, 20, 1, "Scrolling");

        counter.setTextAlignment(TextAlignment.LEFT);
        testPane.add(header);
        testPane.add(counter);
        testPane.add(footer);
        lcd.show(testPane);

        I2CDevice device = ((CharProxyDisplayDriver) lcdDriver).getDevice();
        int r = device.read();
        log.info("Got data from slave: 0x{}", Integer.toHexString(r));


        int ctr = 0;
        boolean state = false;
        int scrollLeft = 0;
        //Wait
        while (!shutdown.get()) {
            if (ctr > 1000)
                ctr = 0;
            if (scrollLeft == 9)
                scrollLeft = 0;
            counter.setText(String.valueOf(ctr++));
            header.setVisible(state);
            footer.setScrollLeft(scrollLeft++);
            state = !state;
            ThreadUtils.sleep(500);
        }

        log.info("Shutting down...");
        ser.close();
        gpio.shutdown();
        executorService.shutdown();
        lcd.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
}
