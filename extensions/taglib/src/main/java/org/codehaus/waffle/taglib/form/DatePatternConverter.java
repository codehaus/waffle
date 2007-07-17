package org.codehaus.waffle.taglib.form;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts a java date format to the javascript calendar date format.
 *
 * @author Nico Steppat
 */
public class DatePatternConverter implements StringConverter {
    private static final Map<String, String> CONVERTER = new HashMap<String, String>();
    private static final Pattern REGEX_PATTERN = Pattern.compile("(hh)|(dd)|(MM)|(yyyy)");

    static {
        CONVERTER.put("hh", "%H");
        CONVERTER.put("mm", "%M");
        CONVERTER.put("MM", "%m");
        CONVERTER.put("dd", "%d");
        CONVERTER.put("yyyy", "%Y");
    }

    /**
     * Converts the datePattern.
     * <p/>
     * hh to %H mm to %M MM to %m dd to %d yyyy to %Y
     *
     * @return the converted date pattern
     */
    public String convert(String datePattern) {
        Matcher matcher = REGEX_PATTERN.matcher(datePattern);
        while (matcher.find()) {
            datePattern = datePattern.replace(matcher.group(), CONVERTER.get(matcher.group()));
        }
        return datePattern;
    }

}
