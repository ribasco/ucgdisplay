package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.ui.Graphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class ListViewTest {

    private static final Logger log = LoggerFactory.getLogger(ListViewTest.class);

    private ListView<Graphics, String> listView;

    @BeforeEach
    void setUp() {
        listView = new ListView<Graphics, String>(null) {
        };
    }

    @DisplayName("Test item selection by index")
    @Test
    void testSelection() {
        listView.getItems().add("Test");
        listView.getItems().add("Test2");
        listView.getItems().add("Test3");
        listView.getItems().add("Test4");
        listView.getItems().add("Test5");
        listView.select(2);
        listView.select(1);

        assertFalse(listView.getSelectedItems().contains("Test1"));
        assertTrue(listView.getSelectedItems().contains("Test2"));
        assertTrue(listView.getSelectedItems().contains("Test3"));
    }

    @Test
    void testSelectionByItem() {
        listView.getItems().add("Test");
        listView.getItems().add("Test2");
        listView.getItems().add("Test3");
        listView.getItems().add("Test4");
        listView.getItems().add("Test5");
        listView.select("Test3");
        listView.select("Test5");

        assertEquals(2, listView.getSelectedItems().size());
        assertEquals(2, listView.getSelectedIndices().size());
        assertTrue(listView.getSelectedItems().contains("Test3"));
        assertTrue(listView.getSelectedItems().contains("Test5"));
    }

    @DisplayName("Test clear individual selection")
    @Test
    void testClearSelection() {
        listView.getItems().add("Test");
        listView.getItems().add("Test2");
        listView.getItems().add("Test3");
        listView.getItems().add("Test4");
        listView.getItems().add("Test5");
        listView.select(2);
        assertTrue(listView.getSelectedItems().contains("Test3"));
        listView.clearSelection(2);
        assertFalse(listView.getSelectedItems().contains("Test3"));
    }

    @DisplayName("Test clear all selection")
    @Test
    void testClearAllSelection() {
        listView.getItems().add("Test");
        listView.getItems().add("Test2");
        listView.getItems().add("Test3");
        listView.getItems().add("Test4");
        listView.getItems().add("Test5");
        listView.selectAll();
        assertEquals(5, listView.getSelectedItems().size());
        listView.clearSelection();
        assertEquals(0, listView.getSelectedItems().size());
    }

    @DisplayName("Test select all items")
    @Test
    void testSelectAll() {
        listView.getItems().add("Test");
        listView.getItems().add("Test2");
        listView.getItems().add("Test3");
        listView.getItems().add("Test4");
        listView.getItems().add("Test5");
        listView.selectAll();

        assertEquals(5, listView.getSelectedItems().size());
        assertTrue(listView.getSelectedItems().contains("Test"));
        assertTrue(listView.getSelectedItems().contains("Test2"));
        assertTrue(listView.getSelectedItems().contains("Test3"));
        assertTrue(listView.getSelectedItems().contains("Test4"));
        assertTrue(listView.getSelectedItems().contains("Test5"));
    }
}