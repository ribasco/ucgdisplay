package com.ibasco.pidisplay.core.util;

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
public class RegexTextProcessor implements TextProcessor {

    private static final Logger log = LoggerFactory.getLogger(RegexTextProcessor.class);

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
     * @param text
     *         The text to evaluate
     *
     * @return The evaluated text in the form of a byte array
     */
    @Override
    public String process(String text) {
        if (StringUtils.isEmpty(text))
            return StringUtils.EMPTY;

        int startTokenCount = StringUtils.countMatches(text, DEFAULT_VAR_PREFIX);
        int endTokenCount = StringUtils.countMatches(text, DEFAULT_VAR_POSTFIX);

        if (startTokenCount == 0 && endTokenCount == 0)
            return text;

        //Make sure the start and end token count are equal
        if (startTokenCount != endTokenCount)
            throw new RuntimeException(String.format("The count of the start and end brackets do not match (Start: %d, End: %d, Text: \"%s\")", startTokenCount, endTokenCount, text));

        Matcher matcher = customCharPattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String cmd = matcher.group(1);
            String args = matcher.group(3);
            if (varMap.containsKey(cmd)) {
                Function<String, String> processor = varMap.get(cmd);
                if (processor == null)
                    throw new NullPointerException("No available processor found for command '" + cmd + "'");
                log.debug("Match: 1 = {}, 3 = {}", cmd, args);
                matcher.appendReplacement(sb, processor.apply(args));
            }
        }
        matcher.appendTail(sb);

        log.debug("Evaluated Text: {}", sb.toString());
        return sb.toString();
    }
}
