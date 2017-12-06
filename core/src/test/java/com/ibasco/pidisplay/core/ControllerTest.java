package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SuppressWarnings("unchecked")
class ControllerTest {

    private Controller controller;

    private EventDispatchQueue mockDispatchQueue;

    private Graphics mockGraphics;

    private static class TestController extends Controller<Graphics> {
        protected TestController(Graphics graphics) {
            super(graphics);
        }
    }

    @BeforeEach
    void setUp() {
        mockGraphics = mock(Graphics.class);
        mockDispatchQueue = mock(EventDispatchQueue.class);
        controller = new TestController(mockGraphics);
        controller.setDispatchQueue(mockDispatchQueue);
    }

    @Test
    @DisplayName("Test show parent node")
    void testShowDisplay() {
        DisplayParent testPane = mock(DisplayParent.class, withSettings().useConstructor(null, null).defaultAnswer(Answers.CALLS_REAL_METHODS));

        when(testPane.controllerProperty()).thenReturn(new ObservableProperty(controller));
        when(testPane.getController()).thenReturn(controller);
        when(testPane.toString()).thenReturn("Mock Controller");
        when(testPane.isActive()).thenCallRealMethod();

        assertFalse(testPane.isActive());

        controller.show(testPane);

        verify(mockDispatchQueue, times(1)).postEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_SHOWING, testPane));
        verify(mockDispatchQueue, times(1)).postEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_SHOWN, testPane));

        assertNotNull(controller.getDisplay());
        assertEquals(testPane, controller.getDisplay());
        assertFalse(controller.isShutdown());
        assertTrue(testPane.isActive());
        assertTrue(testPane.isAttached());
    }

    @Test
    @DisplayName("Test hide display")
    void testHideDisplay() {

    }

    @Test
    @DisplayName("Test close display")
    void testCloseDisplay() {

    }

    @Test
    @DisplayName("Test shutdown sequence")
    void testShutdown() throws InterruptedException {
        assertFalse(controller.isShutdown());
        controller.shutdown();
        verify(controller.getEventDispatchQueue(), atLeastOnce()).shutdown();
        assertTrue(controller.isShutdown());
    }
}