/*-
 * ========================START=================================
 * UCGDisplay :: Character LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.drivers.clcd.util;

import com.ibasco.ucgdisplay.drivers.clcd.exceptions.NoAvailableByteProcessorException;
import com.ibasco.ucgdisplay.drivers.clcd.exceptions.TokenCountMismatchException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Simple Text Processor.
 *
 * @author Rafael Ibasco
 */
public class RegexByteProcessor {

    private static final Logger log = LoggerFactory.getLogger(RegexByteProcessor.class);

    private Pattern customCharPattern = Pattern.compile("\\$\\{([\\w]*)(:([\\w\\-:\\s]*))?}", Pattern.DOTALL);

    private final Map<String, Function<String, String>> varMap = new HashMap<>();

    private static final String DEFAULT_VAR_PREFIX = "${";

    private static final String DEFAULT_VAR_POSTFIX = "}";

    public void register(String variableName, Function<String, String> processor) {
        varMap.put(variableName, processor);
    }

    public void unregister(String variableName) {
        varMap.remove(variableName);
    }

    /**
     * Evaluates and replaces custom character variables
     *
     * @param text The text to evaluate
     * @return The evaluated text in the form of a byte array
     */
    @SuppressWarnings("Duplicates")
    public byte[] process(String text) {
        if (StringUtils.isEmpty(text))
            return null;

        int startTokenCount = StringUtils.countMatches(text, DEFAULT_VAR_PREFIX);
        int endTokenCount = StringUtils.countMatches(text, DEFAULT_VAR_POSTFIX);

        if (startTokenCount == 0 && endTokenCount == 0)
            return text.getBytes();

        //Make sure the start and end token count are equal
        if (startTokenCount != endTokenCount)
            throw new TokenCountMismatchException(String.format("The count of the start and end brackets do not match (Start: %d, End: %d, Text: \"%s\")", startTokenCount, endTokenCount, text));

        Matcher matcher = customCharPattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String cmd = matcher.group(1);
            String args = matcher.group(3);
            if (varMap.containsKey(cmd)) {
                Function<String, String> processor = varMap.get(cmd);
                if (processor == null)
                    throw new NoAvailableByteProcessorException("No available processor found for command '" + cmd + "'");
                log.debug("Match: 1 = {}, 3 = {}", cmd, args);
                matcher.appendReplacement(sb, processor.apply(args));
            }
        }
        matcher.appendTail(sb);

        log.debug("Evaluated Text: {}", sb.toString());
        return sb.toString().getBytes();
    }
}
