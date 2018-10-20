package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.ui.Graphics;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class TextInputTest {

    public static final Logger log = LoggerFactory.getLogger(TextInputTest.class);

    private TextInput textInput;

    @BeforeEach
    void setUp() {
        textInput = new TextInput(5, 1, 5, 3) {
            private Text content;

            @Override
            protected int fixCaretPos(int pos) {
                return 0;
            }

            @Override
            protected Text getContent() {
                if (content == null) {
                    content = new Text() {
                        @Override
                        protected void drawNode(Graphics graphics) {

                        }
                    };
                }
                return content;
            }

            @Override
            protected void updateCaretPos() {

            }

            @Override
            protected int calcXOffset() {
                return 0;
            }

            @Override
            protected int calcYOffset() {
                return 0;
            }
        };
    }

    @Test
    @DisplayName("Test set text")
    void testSetText() {
        textInput.setText("Hello World");
        assertEquals("Hello World", textInput.getContent().getText());
        assertEquals(11, textInput.getContent().getLength());
        log.debug("Caret: {}, X = {}, Y = {}", textInput.getCaret(), textInput.getCaretX(), textInput.getCaretY());
    }

    @Test
    @DisplayName("Test set null text")
    void testSetNullText() {
        textInput.setText(null);
        log.debug("Content: {}", textInput.getContent().getText());
        assertNull(textInput.getContent().getText());
    }

    @Test
    @DisplayName("Test append text")
    void testAppendText() {
        textInput.appendText("Hello");
        textInput.appendText("World");
        assertEquals("HelloWorld", textInput.getContent().getText());
    }

    @Test
    @DisplayName("Test clear text")
    void testClearText() {
        textInput.appendText("Hello");
        textInput.clear();
        assertNotNull(textInput.getContent().getText());
        assertTrue(StringUtils.isEmpty(textInput.getContent().getText()));
        assertEquals(0, textInput.getContent().getLength());
    }

    @Test
    @DisplayName("Test insert text")
    void testInsertText() {
        textInput.setText("test");
        textInput.insertText(0, "1");
        assertEquals("1test", textInput.getContent().getText());
        textInput.insertText(5, "2");
        assertEquals("1test2", textInput.getContent().getText());
    }

    @Test
    @DisplayName("Test delete text")
    void testDeleteText() {
        textInput.setText("Hello");
        assertEquals(4, textInput.getCaret());
        textInput.deleteText(2);
        assertEquals("Hel", textInput.getContent().getText());
        assertEquals(2, textInput.getCaret());
        assertThrows(StringIndexOutOfBoundsException.class, () -> textInput.deleteText(5));
        assertEquals("Hel", textInput.getContent().getText());
        assertEquals(2, textInput.getCaret());
    }
}