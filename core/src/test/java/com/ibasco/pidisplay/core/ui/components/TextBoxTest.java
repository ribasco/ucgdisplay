package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.ui.Graphics;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class TextBoxTest {

    public static final Logger log = LoggerFactory.getLogger(TextBoxTest.class);

    private TextBox textBox;

    @BeforeEach
    void setUp() {
        textBox = new TextBox(5, 1, 5, 3) {
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
        textBox.setText("Hello World");
        assertEquals("Hello World", textBox.getContent().getText());
        assertEquals(11, textBox.getContent().getLength());
        log.debug("Caret: {}, X = {}, Y = {}", textBox.getCaret(), textBox.getCaretX(), textBox.getCaretY());
    }

    @Test
    @DisplayName("Test set null text")
    void testSetNullText() {
        textBox.setText(null);
        log.debug("Content: {}", textBox.getContent().getText());
        assertNull(textBox.getContent().getText());
    }

    @Test
    @DisplayName("Test append text")
    void testAppendText() {
        textBox.appendText("Hello");
        textBox.appendText("World");
        assertEquals("HelloWorld", textBox.getContent().getText());
    }

    @Test
    @DisplayName("Test clear text")
    void testClearText() {
        textBox.appendText("Hello");
        textBox.clear();
        assertNotNull(textBox.getContent().getText());
        assertTrue(StringUtils.isEmpty(textBox.getContent().getText()));
        assertEquals(0, textBox.getContent().getLength());
    }

    @Test
    @DisplayName("Test insert text")
    void testInsertText() {
        textBox.setText("test");
        textBox.insertText(0, "1");
        assertEquals("1test", textBox.getContent().getText());
        textBox.insertText(5, "2");
        assertEquals("1test2", textBox.getContent().getText());
    }

    @Test
    @DisplayName("Test delete text")
    void testDeleteText() {
        textBox.setText("Hello");
        assertEquals(4, textBox.getCaret());
        textBox.deleteText(2);
        assertEquals("Hel", textBox.getContent().getText());
        assertEquals(2, textBox.getCaret());
        assertThrows(StringIndexOutOfBoundsException.class, () -> textBox.deleteText(5));
        assertEquals("Hel", textBox.getContent().getText());
        assertEquals(2, textBox.getCaret());
    }
}