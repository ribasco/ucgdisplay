package com.ibasco.ucgdisplay.core.events;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.ui.Graphics;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DisplayEvent<T extends Graphics> extends Event {

    public static final EventType<DisplayEvent> ANY = new EventType<>(Event.ANY, "DISPLAY_ANY");

    public static final EventType<DisplayEvent> DISPLAY_SHOW = new EventType<>(ANY, "DISPLAY_SHOW");

    public static final EventType<DisplayEvent> DISPLAY_SHOWING = new EventType<>(ANY, "DISPLAY_SHOWING");

    public static final EventType<DisplayEvent> DISPLAY_SHOWN = new EventType<>(ANY, "DISPLAY_SHOWN");

    public static final EventType<DisplayEvent> DISPLAY_HIDE = new EventType<>(ANY, "DISPLAY_HIDE");

    public static final EventType<DisplayEvent> DISPLAY_HIDING = new EventType<>(ANY, "DISPLAY_HIDING");

    public static final EventType<DisplayEvent> DISPLAY_HIDDEN = new EventType<>(ANY, "DISPLAY_HIDDEN");

    public static final EventType<DisplayEvent> DISPLAY_CLOSE = new EventType<>(ANY, "DISPLAY_CLOSE");

    public static final EventType<DisplayEvent> DISPLAY_CLOSING = new EventType<>(ANY, "DISPLAY_CLOSING");

    public static final EventType<DisplayEvent> DISPLAY_CLOSED = new EventType<>(ANY, "DISPLAY_CLOSED");

    private DisplayNode<T> display;

    public DisplayEvent(EventType<? extends Event> eventType, DisplayNode<T> display) {
        super(eventType);
        this.display = display;
    }

    public DisplayNode<T> getDisplay() {
        return display;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisplayEvent<?> that = (DisplayEvent<?>) o;

        return new EqualsBuilder()
                .append(eventType, that.eventType)
                .append(getClass(), that.getClass())
                .append(display, that.display)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(eventType)
                .append(getClass())
                .append(display)
                .toHashCode();
    }

    @Override
    public String toString() {
        return super.toString() + " (" + this.display + ")";
    }
}
