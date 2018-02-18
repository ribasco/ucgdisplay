package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;

import java.util.HashMap;
import java.util.Map;

abstract public class CharManager {
    private CharDisplayDriver driver;

    private Map<String, CharData> charRegistry = new HashMap<>();

    private CharData[] charCache = new CharData[8];

    private int charIdx = 0;

    protected CharManager(CharDisplayDriver driver) {
        this.driver = driver;
    }

    public int register(CharData charData) {
        return -1;
    }

    public void unregister(CharData charData) {

    }

    abstract public byte[] processText();
}
