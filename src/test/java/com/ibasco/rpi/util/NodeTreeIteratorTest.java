package com.ibasco.rpi.util;

import com.ibasco.pidisplay.util.Node;
import com.ibasco.pidisplay.util.NodeIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class NodeTreeIteratorTest {

    private Node<String> rootNode;

    private NodeIterator<String> nodeIterator;

    private static final Logger log = LoggerFactory.getLogger(NodeTreeIteratorTest.class);

    @BeforeEach
    void setupNodes() {
        Node<String> systemGroup = new Node<>("system");

        systemGroup.addChild(new Node<>("system1"));
        systemGroup.addChild(new Node<>("system2"));
        systemGroup.addChild(new Node<>("system3"));

        Node<String> system3 = systemGroup.getLast();
        system3.addChild(new Node<>("system3-a"));
        system3.addChild(new Node<>("system3-b"));
        system3.addChild(new Node<>("system3-c"));
        system3.addChild(new Node<>("system3-d"));

        Node<String> settings = new Node<>("settings");
        settings.addChild(new Node<>("settings1"));
        settings.addChild(new Node<>("settings2"));
        settings.addChild(new Node<>("settings3"));
        settings.addChild(new Node<>("settings4"));

        Node<String> settings3 = settings.getLast();
        settings3.addChild(new Node<>("settings 3-a"));
        settings3.addChild(new Node<>("settings 3-b"));
        settings3.addChild(new Node<>("settings 3-c"));
        settings3.addChild(new Node<>("settings 3-d"));
        settings3.addChild(new Node<>("settings 3-e"));
        settings3.addChild(new Node<>("settings 3-f"));

        rootNode = new Node<>();
        rootNode.addChild(systemGroup);
        rootNode.addChild(settings);
    }

    @BeforeEach
    void setupIterator() {
        nodeIterator = rootNode.iterator();
    }

    @Test
    void testSimpleLoop() {
        while (nodeIterator.hasNext()) {
            log.info("Node: {}", nodeIterator.next().getName());
        }
    }

    @Test
    @DisplayName("Test all Enter/Exit navigation functions")
    void testNavigationFunctions() {
        assertTrue(nodeIterator.hasNext());
        assertEquals(1, nodeIterator.depth());
        assertNotNull(nodeIterator.next());
        assertTrue(nodeIterator.isRoot());
        assertEquals("system", nodeIterator.current().getName());
        assertNotNull(nodeIterator.next());
        assertEquals("settings", nodeIterator.current().getName());
        assertTrue(nodeIterator.hasChildren());
        assertNotNull(nodeIterator.enter());
        assertEquals(2, nodeIterator.depth());
        assertFalse(nodeIterator.hasPrevious());
        assertTrue(nodeIterator.hasNext());
        assertEquals("settings2", nodeIterator.peekNext().getName());
        while (nodeIterator.hasNext()) {
            nodeIterator.next();
        }
        assertNotNull(nodeIterator.exit());
        assertFalse(nodeIterator.hasNext());
        while (nodeIterator.hasPrevious()) {
            nodeIterator.previous();
        }
        assertEquals("system", nodeIterator.current().getName());
        assertTrue(nodeIterator.hasChildren());
        assertNotNull(nodeIterator.enter());
        assertTrue(nodeIterator.hasNext());
        while (nodeIterator.hasNext()) {
            nodeIterator.next();
        }
        assertEquals("system3", nodeIterator.current().getName());
        assertTrue(nodeIterator.hasChildren());
        nodeIterator.enter();
        while (nodeIterator.hasNext()) {
            nodeIterator.next();
        }
        assertEquals(3, nodeIterator.depth());
        nodeIterator.exit();
        nodeIterator.exit();
        //log.info("Depth: {}, Current: {}", nodeIterator.depth(), nodeIterator.previous().getName());
    }
}