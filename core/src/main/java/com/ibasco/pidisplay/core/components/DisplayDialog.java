package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.EventDispatchType;
import com.ibasco.pidisplay.core.EventHandler;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.events.DialogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@SuppressWarnings("WeakerAccess")
abstract public class DisplayDialog<T extends Graphics, B> extends DisplayNode<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayDialog.class);

    private ObservableProperty<B> result = new ObservableProperty<B>() {
        @Override
        protected void invalidated(B oldValue, B newValue) {
            log.debug("DISPLAY_DIALOG => Firing Result Receive Event");
            fireEvent(new DialogEvent<>(DialogEvent.DIALOG_RESULT, DisplayDialog.this, newValue));
        }
    };

    private ObservableProperty<EventHandler<DialogEvent>> onDialogResult;

    protected DisplayDialog() {
        super(null, null, null, null);
    }

    public EventHandler<DialogEvent> getOnDialogResult() {
        return onDialogResult().get();
    }

    public void setOnDialogResult(EventHandler<DialogEvent> eventHandler) {
        onDialogResult().set(eventHandler);
    }

    public ObservableProperty<EventHandler<DialogEvent>> onDialogResult() {
        if (onDialogResult == null) {
            onDialogResult = new ObservableProperty<EventHandler<DialogEvent>>() {
                @Override
                protected void invalidated(EventHandler<DialogEvent> oldVal, EventHandler<DialogEvent> newVal) {
                    if (newVal == null && oldVal != null) {
                        DisplayDialog.this.removeEventHandler(DialogEvent.DIALOG_RESULT, oldVal, EventDispatchType.BUBBLE);
                        return;
                    }
                    DisplayDialog.this.addEventHandler(DialogEvent.DIALOG_RESULT, get(), EventDispatchType.BUBBLE);
                }
            };
        }
        return onDialogResult;
    }

    public ObservableProperty<B> resultProperty() {
        return result;
    }

    public void setResult(B result) {
        this.result.set(result);
    }

    public Optional<B> getResult() {
        return Optional.ofNullable(result.get());
    }
}
