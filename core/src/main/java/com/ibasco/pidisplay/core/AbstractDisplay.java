package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import com.ibasco.pidisplay.core.util.RegexTextProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Deprecated
abstract public class AbstractDisplay<T extends Graphics> implements Display<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractDisplay.class);

    private String name;

    private int width = -1;

    private int height = -1;

    private static final AtomicInteger displayCounter = new AtomicInteger();

    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    protected static final String CMD_DATE = "date";

    protected RegexTextProcessor textProcessor = new RegexTextProcessor();

    protected AbstractDisplay() {
        this(String.format("display-%d", displayCounter.incrementAndGet()));
    }

    private AbstractDisplay(String name) {
        this.name = name;
        registerVar(CMD_DATE, this::formatDateTime);
    }

    protected void registerVar(String variableName, Function<String, String> processor) {
        textProcessor.register(variableName, processor);
    }

    protected void unregisterVar(String variableName) {
        textProcessor.unregister(variableName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected <X extends Event> void fireEvent(X event) {
        EventDispatcher.dispatch(event);
    }

    @Override
    public void redraw() {
        //fireEvent(new DisplayEvent(DisplayEvent.DISPLAY_DRAW, this));
    }

    @Override
    public String toString() {
        return this.getName();
    }

    private String formatDateTime(String args) {
        String format = StringUtils.defaultString(args, DEFAULT_DATETIME_FORMAT);
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
}
