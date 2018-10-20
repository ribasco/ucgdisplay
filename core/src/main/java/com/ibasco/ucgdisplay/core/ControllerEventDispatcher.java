package com.ibasco.ucgdisplay.core;

import com.ibasco.ucgdisplay.core.ui.Graphics;

public class ControllerEventDispatcher extends MultiEventDispatcher {

    private EventHandlerManager eventHandlerManager;

    private InputEventDispatcher inputEventDispatcher;

    public <T extends Graphics> ControllerEventDispatcher(Controller<T> controller) {
        this(new InputEventDispatcher(controller), new EventHandlerManager(controller));
    }

    public ControllerEventDispatcher(InputEventDispatcher inputEventDispatcher, EventHandlerManager eventHandlerManager) {
        this.inputEventDispatcher = inputEventDispatcher;
        this.eventHandlerManager = eventHandlerManager;
        inputEventDispatcher.insertNextDispatcher(eventHandlerManager);
    }

    public final InputEventDispatcher getInputEventDispatcher() {
        return inputEventDispatcher;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return eventHandlerManager;
    }

    @Override
    public BasicEventDispatcher getFirstDispatcher() {
        return inputEventDispatcher;
    }

    @Override
    public BasicEventDispatcher getLastDispatcher() {
        return eventHandlerManager;
    }
}
