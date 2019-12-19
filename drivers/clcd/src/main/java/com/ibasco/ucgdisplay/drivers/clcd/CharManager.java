/*-
 * ========================START=================================
 * UCGDisplay :: Character LCD driver
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
package com.ibasco.ucgdisplay.drivers.clcd;

import com.ibasco.ucgdisplay.drivers.clcd.util.RegexByteProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class CharManager {
    private CharDisplayDriver driver;

    private static final int MAX_LCD_CHARS = 8;

    private List<CharData> charCache = new ArrayList<>(8);

    private byte cacheIndex = 0;

    private RegexByteProcessor textProcessor = new RegexByteProcessor();

    private Set<CharData> charRegistry = new HashSet<>();

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
        return charRegistry.add(charData);
    }

    public boolean unregisterChar(CharData charData) {
        return charRegistry.remove(charData);
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
                    throw new IllegalStateException("Unable to allocate character. Character charCache is FULL");
            }
            if (!isRegistered(charData)) {
                if (autoRegister)
                    registerChar(charData);
                else
                    throw new IllegalStateException(String.format("Character '%s' is not yet registered", charData));
            }
            allocationIndex = cacheIndex++;
            driver.createChar(allocationIndex, charData.getBytes());
            log.info("Create Char: {}", allocationIndex);
            charCache.add(allocationIndex, charData);
        }
        return allocationIndex;
    }

    public CharData getCharData(String key) {
        if (StringUtils.isEmpty(key))
            return null;
        return charRegistry.stream().filter(cd -> key.equals(cd.getKey())).findFirst().orElse(null);
    }

    private boolean isRegistered(CharData charData) {
        return charRegistry.contains(charData);
    }

    private boolean isAllocated(CharData charData) {
        return charCache.contains(charData);
    }

    public byte getAllocationIndex(String key) {
        CharData cData = charCache.stream().filter(c -> c.getKey().equals(key)).findFirst().orElse(null);
        if (cData == null)
            return -1;
        return getAllocationIndex(cData);
    }

    public byte getAllocationIndex(CharData charData) {
        return (byte) charCache.indexOf(charData);
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
