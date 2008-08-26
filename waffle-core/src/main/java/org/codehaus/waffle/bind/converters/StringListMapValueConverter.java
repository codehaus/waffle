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
 * </p>
 * 
 * @author Mauro Talevi
 */
public class StringListMapValueConverter extends AbstractValueConverter {

    public static final String BIND_ERROR_MAP_KEY = "bind.error.map";
    public static final String DEFAULT_MAP_MESSAGE = "Invalid map value for field {0}";
    protected static final String NL = "\n";
    protected static final String EQUAL = "=";
    protected static final String COMMA = ",";

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
        return acceptMap(type, String.class, String.class);
    }

    public Object convertValue(String propertyName, String value, Type toType) {

        if (missingValue(value)) {
            String fieldName = messageFor(propertyName, propertyName);
            return convertMissingValue(BIND_ERROR_MAP_KEY, DEFAULT_MAP_MESSAGE, fieldName);
        }

        List<String> lines = split(value, NL);
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String line : lines) {
            List<String> parts = split(line, EQUAL);
            if (parts.size() > 1) {
                String csv = parts.get(1);
                map.put(parts.get(0), split(csv, COMMA));
            }
        }
        return map;
    }

    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return new HashMap<String, List<String>>();
    }

}
