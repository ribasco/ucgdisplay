package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.InputEventData;
import com.ibasco.pidisplay.core.enums.InputEventType;
import com.ibasco.pidisplay.core.events.KeyEvent;
import com.ibasco.pidisplay.core.events.RawInputEvent;
import com.ibasco.pidisplay.core.ui.Graphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class InputEventDispatcher extends BasicEventDispatcher {

    public static final Logger log = LoggerFactory.getLogger(InputEventDispatcher.class);

    private Controller<? extends Graphics> controller;

    public <T extends Graphics> InputEventDispatcher(Controller<T> controller) {
        this.controller = controller;
    }

    public final Controller<? extends Graphics> getController() {
        return controller;
    }

    @Override
    public Event dispatchEvent(Event event, EventDispatchPhase dispatchType) {
        if (RawInputEvent.RAW_INPUT == event.getEventType()) {
            RawInputEvent rie = (RawInputEvent) event;
            InputEventData eventData = rie.getRawInputEventData();

            if (!((InputEventType.EV_KEY == eventData.getType()) && (eventData.getValue() == 0))) {
                event.consume();
                return event;
            }

            DisplayParent<? extends Graphics> parent = controller.getDisplay();
            if (parent != null) {
                List<? extends DisplayNode> children = parent.getChildren()
                        .stream()
                        .filter(DisplayNode::isFocusable)
                        .collect(Collectors.toList());
                for (DisplayNode node : children) {
                    EventHandlerManager eventHandlerManager = node.eventHandlerManager;
                    log.debug("INPUT_DISPATCHER => Dispatching to child node = {} ({}, {})", node, dispatchType, event);
                    event = eventHandlerManager.dispatchEvent(processRawInputEvent(rie), dispatchType);
                }
            }
        }
        return event;
    }

    private Event processRawInputEvent(RawInputEvent event) {
        InputEventData data = event.getRawInputEventData();
        if (InputEventType.EV_KEY == data.getType() && data.getValue() == 0) {
            return new KeyEvent(KeyEvent.KEY_PRESS, event.getRawInputEventData().getCode(), event.getRawInputEventData().getCode().getCharCode());
        }
        return event;
    }
}
