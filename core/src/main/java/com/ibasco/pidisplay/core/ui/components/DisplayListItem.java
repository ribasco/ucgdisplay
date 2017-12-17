package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class DisplayListItem<T extends Graphics> {

    abstract protected DisplayText<T> getContent();
}
