package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Dialog;
import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

import java.util.Optional;

abstract public class DisplayDialog<T extends Graphics, B> extends DisplayNode<T> implements Dialog<B> {

    protected DisplayDialog() {
        super(null, null, null, null);
    }

    @Override
    protected void drawNode(T graphics) {

    }

    @Override
    public Optional<B> getResult() {
        return null;
    }
}
