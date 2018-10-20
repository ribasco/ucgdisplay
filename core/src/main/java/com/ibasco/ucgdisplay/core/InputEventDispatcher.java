package com.ibasco.ucgdisplay.core;

import com.ibasco.ucgdisplay.core.events.InputEvent;
import com.ibasco.ucgdisplay.core.events.KeyEvent;
import com.ibasco.ucgdisplay.core.events.MouseEvent;
import com.ibasco.ucgdisplay.core.ui.Graphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.ibasco.ucgdisplay.core.input.InputEventCode.*;
import static com.ibasco.ucgdisplay.core.input.InputEventType.EV_KEY;
import static com.ibasco.ucgdisplay.core.input.InputEventType.EV_REL;

public class InputEventDispatcher extends BasicEventDispatcher {

    public static final Logger log = LoggerFactory.getLogger(InputEventDispatcher.class);

    private Controller<? extends Graphics> controller;

    private boolean shiftDown, ctrlDown, altDown;

    <T extends Graphics> InputEventDispatcher(Controller<T> controller) {
        this.controller = controller;
    }

    public final Controller<? extends Graphics> getController() {
        return controller;
    }

    @Override
    public Event dispatchEvent(Event event, EventDispatchPhase dispatchType) {
        if (InputEvent.RAW_INPUT == event.getEventType()) {
            InputEvent inputEvent = processRawInputEvent((InputEvent) event);

            if (inputEvent == null) {
                log.warn("Input Event {} not supported. Ignoring", event);
                event.consume();
                return event;
            }

            DisplayParent<? extends Graphics> parent = controller.getDisplay();
            if (parent != null) {
                //Get a list of focusable nodes we can dispatch to
                List<? extends DisplayNode> focusableChildNodes = parent.getChildren().stream().filter(DisplayNode::isFocusable).collect(Collectors.toList());
                for (DisplayNode focusableNode : focusableChildNodes) {
                    EventHandlerManager eventHandlerManager = focusableNode.eventHandlerManager;
                    log.debug("INPUT_DISPATCHER => Dispatching to child node = {} ({}, {})", focusableNode, dispatchType, event);
                    event = eventHandlerManager.dispatchEvent(inputEvent, dispatchType);
                }
            }
            return inputEvent;
        }
        return event;
    }

    private InputEvent processRawInputEvent(InputEvent event) {
        //EV_KEY
        if (event.getInputEventType() == EV_KEY) {
            int code = event.getInputEventCode();
            int value = event.getValue();

            //Mouse Events: middle-click (274), left-click (272), right-click (273)
            if (BTN_MIDDLE == code || BTN_LEFT == code || BTN_RIGHT == code) {
                if (0 == value) {
                    return new MouseEvent(MouseEvent.MOUSE_RELEASED, event.getInputEventData());
                } else if (1 == value) {
                    return new MouseEvent(MouseEvent.MOUSE_PRESS, event.getInputEventData());
                }
            }
            //Keyboard Events
            if (1 == event.getValue()) {
                //Monitor modifier key press
                if (code == KEY_LEFTSHIFT || code == KEY_RIGHTSHIFT) {
                    shiftDown = true;
                } else if (code == KEY_LEFTALT || code == KEY_RIGHTALT) {
                    altDown = true;
                } else if (code == KEY_LEFTCTRL || code == KEY_RIGHTCTRL) {
                    ctrlDown = true;
                }
                return new KeyEvent(KeyEvent.KEY_PRESSED, event.getInputEventData(), shiftDown, altDown, ctrlDown);
            } else if (0 == event.getValue()) {
                //Monitor modifier key press
                if (code == KEY_LEFTSHIFT || code == KEY_RIGHTSHIFT) {
                    shiftDown = false;
                } else if (code == KEY_LEFTALT || code == KEY_RIGHTALT) {
                    altDown = false;
                } else if (code == KEY_LEFTCTRL || code == KEY_RIGHTCTRL) {
                    ctrlDown = false;
                }
                return new KeyEvent(KeyEvent.KEY_RELEASED, event.getInputEventData());
            } else if (2 == event.getValue()) {
                return new KeyEvent(KeyEvent.KEY_REPEAT, event.getInputEventData());
            }
        }
        //EV_REL
        else if (event.getInputEventType() == EV_REL) {
            //REL_X or REL_Y
            if (event.getInputEventCode() == 0 || event.getInputEventCode() == 1) {
                return new MouseEvent(MouseEvent.MOUSE_MOVE, event.getInputEventData());
            }
        }
        return null;
    }
}
