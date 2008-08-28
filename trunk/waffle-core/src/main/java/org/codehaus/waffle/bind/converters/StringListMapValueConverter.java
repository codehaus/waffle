/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <p>
 * <code>ValueConverter</code> that converts a text value to a Map of String Lists indexed by Strings.
 * </p>
 * <p>
 * A value of the form
 * 
 * <pre>
 * a=x\n
 * b=x,y\n
 * c=x,y,z
 * </pre>
 * 
 * would be converted to a map
 * 
 * <pre>
 * Map&lt;String, List&lt;String&gt;&gt; map = new HashMap&lt;String, List&lt;String&gt;&gt;();
 * map.put(&quot;a&quot;, asList(&quot;x&quot;));
 * map.put(&quot;b&quot;, asList(&quot;x&quot;, &quot;y&quot;));
 * map.put(&quot;c&quot;, asList(&quot;x&quot;, &quot;y&quot;, &quot;z&quot;));
 * </pre>
 * 
 * A <code>null</code> or empty value (once trimmed) will be returned as an empty <code>HashMap</code> (behaviour which
 * can be overridden via the {@link #convertMissingValue} method), while an invalid value will cause a BindException to
 * be thrown. The message keys and default values used are:
 * <ul>
 * <li>"bind.error.map" ({@link #BIND_ERROR_MAP_KEY}): bind error in map parsing (message defaults to
 * {@link #DEFAULT_MAP_MESSAGE})</li>
 * <li>"map.newline.separator" ({@link #NEWLINE_SEPARATOR_KEY}): newline separator used in parsing (defaults to
 * {@link #DEFAULT_NEWLINE_SEPARATOR})</li>
 * <li>"map.key.separator" ({@link #KEY_SEPARATOR_KEY}): key separator used in parsing (defaults to
 * {@link #DEFAULT_KEY_SEPARATOR})</li>
 * <li>"map.list.separator" ({@link #LIST_SEPARATOR_KEY}): list separator used in parsing (defaults to
 * {@link #DEFAULT_LIST_SEPARATOR})</li>
 * </ul>
 * The separators are also optionally injectable via <code>Properties</code> in the constructor and take precedence over
 * the ones configured in the messages resources.
 * </p>
 * 
 * @author Mauro Talevi
 */
public class StringListMapValueConverter extends AbstractValueConverter {

    public static final String BIND_ERROR_MAP_KEY = "bind.error.map";
    public static final String DEFAULT_MAP_MESSAGE = "Invalid map value for field {0}";
    public static final String NEWLINE_SEPARATOR_KEY = "map.newline.separator";
    public static final String KEY_SEPARATOR_KEY = "map.key.separator";
    public static final String LIST_SEPARATOR_KEY = "map.list.separator";
    public static final String DEFAULT_NEWLINE_SEPARATOR = "\n";
    public static final String DEFAULT_KEY_SEPARATOR = "=";
    public static final String DEFAULT_LIST_SEPARATOR = ",";

    public StringListMapValueConverter(MessageResources messageResources) {
        this(messageResources, new Properties());
    }

    public StringListMapValueConverter(MessageResources messageResources, Properties patterns) {
        super(messageResources, patterns);
    }

    /**
     * Accepts parameterized types of type Map<String,List<String>>
     */
    public boolean accept(Type type) {
        return acceptMapOfLists(type, String.class, String.class);
    }

    public Object convertValue(String propertyName, String value, Type toType) {

        if (missingValue(value)) {
            String fieldName = messageFor(propertyName, propertyName);
            return convertMissingValue(BIND_ERROR_MAP_KEY, DEFAULT_MAP_MESSAGE, fieldName);
        }

        String newlineSeparator = patternFor(NEWLINE_SEPARATOR_KEY, DEFAULT_NEWLINE_SEPARATOR);
        String keySeparator = patternFor(KEY_SEPARATOR_KEY, DEFAULT_KEY_SEPARATOR);
        String listSeparator = patternFor(LIST_SEPARATOR_KEY, DEFAULT_LIST_SEPARATOR);
        List<String> lines = split(value, newlineSeparator);
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String line : lines) {
            List<String> parts = split(line, keySeparator);
            if (parts.size() > 1) {
                String csv = parts.get(1);
                map.put(parts.get(0), split(csv, listSeparator));
            }
        }
        return map;
    }

    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return new HashMap<String, List<String>>();
    }

}
