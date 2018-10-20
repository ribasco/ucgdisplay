package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.DisplayParent;
import com.ibasco.ucgdisplay.core.EventDispatchPhase;
import com.ibasco.ucgdisplay.core.EventHandler;
import com.ibasco.ucgdisplay.core.beans.ObservableProperty;
import com.ibasco.ucgdisplay.core.events.DialogEvent;
import com.ibasco.ucgdisplay.core.ui.Graphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@SuppressWarnings("WeakerAccess")
abstract public class Dialog<T extends Graphics, B> extends DisplayParent<T> {

    private static final Logger log = LoggerFactory.getLogger(Dialog.class);

    private ObservableProperty<B> result = new ObservableProperty<B>() {
        @Override
        protected void invalidated(B oldValue, B newValue) {
            log.debug("DISPLAY_DIALOG => Firing Result Receive Event");
            fireEvent(new DialogEvent<>(DialogEvent.DIALOG_RESULT, Dialog.this, newValue));
        }
    };

    private ObservableProperty<EventHandler<DialogEvent>> onDialogResult;

    protected Dialog() {
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
                        Dialog.this.removeEventHandler(DialogEvent.DIALOG_RESULT, oldVal, EventDispatchPhase.BUBBLE);
                        return;
                    }
                    Dialog.this.addEventHandler(DialogEvent.DIALOG_RESULT, get(), EventDispatchPhase.BUBBLE);
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
