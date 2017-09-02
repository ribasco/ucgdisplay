package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.events.DisplayEvent;

import java.util.Objects;

import static com.ibasco.pidisplay.core.events.EventDispatcher.dispatch;

abstract public class DisplayScreen<T extends Graphics> extends DisplayComponent<T> {

    protected ObservableProperty<DisplayGroup<T>> primaryDisplay = new ObservableProperty<>();

    public DisplayScreen() {
        super(-1, -1);
        primaryDisplay.set(null);
    }

    public DisplayScreen(DisplayGroup<T> display) {
        super(display.getWidth(), display.getHeight());
        this.primaryDisplay.set(display);
    }

    public DisplayGroup<T> getPrimaryDisplay() {
        return primaryDisplay.get();
    }

    public void setPrimary(DisplayGroup<T> primaryDisplay) {
        this.primaryDisplay.set(primaryDisplay);
    }

    @Override
    public void draw(T graphics) {
        this.primaryDisplay.get().draw(graphics);
    }

    public void show() {
        Objects.requireNonNull(primaryDisplay.get(), "No primary display has been assigned");
        dispatch(new DisplayEvent(DisplayEvent.DISPLAY_SHOW, primaryDisplay.get()));
    }

    public void hide() {
        Objects.requireNonNull(primaryDisplay.get(), "No primary display has been assigned");
        dispatch(new DisplayEvent(DisplayEvent.DISPLAY_HIDE, Objects.requireNonNull(this, "Display cannot be null")));
    }
}
