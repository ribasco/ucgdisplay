package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;
import com.ibasco.pidisplay.core.util.RegexByteProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.slf4j.LoggerFactory.getLogger;

public class CharManager {
    private CharDisplayDriver driver;

    private static final int MAX_LCD_CHARS = 8;

    private List<CharData> cache = new ArrayList<>(8);

    private byte cacheIndex = 0;

    private RegexByteProcessor textProcessor = new RegexByteProcessor();

    private Set<CharData> registry = new HashSet<>();

    public static final Logger log = getLogger(CharManager.class);

    private boolean cycleAllocation = true;

    private boolean autoRegister = true;

    public CharManager(CharDisplayDriver driver) {
        this.driver = driver;
        this.textProcessor.register("c", this::charProcessor); //${c:left_arrow}, ${d:date('hh/mm/yyyy')}
    }

    private String charProcessor(String key) {
        CharData cData = getCharData(key);
        if (isAllocated(cData)) {
            byte allocationIndex = getAllocationIndex(cData);
            return new String(new byte[]{allocationIndex});
        }
        return key;
    }

    public boolean registerChar(CharData charData) {
        return registry.add(charData);
    }

    public boolean unregisterChar(CharData charData) {
        return registry.remove(charData);
    }

    public void registerProcessor(String variableName, Function<String, String> processor) {
        textProcessor.register(variableName, processor);
    }

    public void unregisterProcessor(String variableName) {
        textProcessor.unregister(variableName);
    }

    public byte allocateChar(CharData charData) {
        byte allocationIndex = getAllocationIndex(charData);
        if (allocationIndex <= -1) {
            if (cacheIndex > (MAX_LCD_CHARS - 1)) {
                if (cycleAllocation)
                    cacheIndex = 0;
                else
                    throw new IllegalStateException("Unable to allocate character. Character cache is FULL");
            }
            if (!isRegistered(charData)) {
                if (autoRegister)
                    registerChar(charData);
                else
                    throw new IllegalStateException(String.format("Character '%s' is not yet registered", charData));
            }
            allocationIndex = cacheIndex++;
            driver.createChar(allocationIndex, charData.getBytes());
            cache.add(allocationIndex, charData);
        }
        return allocationIndex;
    }

    public CharData getCharData(String key) {
        if (StringUtils.isEmpty(key))
            return null;
        return registry.stream().filter(cd -> key.equals(cd.getKey())).findFirst().orElse(null);
    }

    private boolean isRegistered(CharData charData) {
        return registry.contains(charData);
    }

    private boolean isAllocated(CharData charData) {
        return cache.contains(charData);
    }

    public byte getAllocationIndex(String key) {
        CharData cData = cache.stream().filter(c -> c.getKey().equals(key)).findFirst().orElse(null);
        if (cData == null)
            return -1;
        return getAllocationIndex(cData);
    }

    public byte getAllocationIndex(CharData charData) {
        return (byte) cache.indexOf(charData);
    }

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public boolean isCycleAllocation() {
        return cycleAllocation;
    }

    public void setCycleAllocation(boolean cycleAllocation) {
        this.cycleAllocation = cycleAllocation;
    }

    public byte[] processText(String text) {
        return textProcessor.process(text);
    }
}
