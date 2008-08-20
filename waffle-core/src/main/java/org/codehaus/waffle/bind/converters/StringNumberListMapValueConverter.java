/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <p>
 * <code>ValueConverter</code> that converts a text value to a Map of Number Lists indexed by Strings. It provides
 * number parsing of the string values using the <code>NumberFormat</code> instance provided (which defaults to
 * <code>NumberFormat.getInstance()</code>) and if not successful returns the string values themselves.
 * </p>
 * <p>
 * A value of the form
 * 
 * <pre>
 * a=1\n
 * b=1,2\n
 * c=1,2,3
 * </pre>
 * 
 * would be converted to a map
 * 
 * <pre>
 * Map&lt;String, List&lt;? extends Number&gt;&gt; map = new HashMap&lt;String, List&lt;? extends Number&gt;&gt;();
 * map.put(&quot;a&quot;, asList(1));
 * map.put(&quot;b&quot;, asList(1, 2));
 * map.put(&quot;c&quot;, asList(1, 2, 3));
 * </pre>
 * 
 * </p>
 * 
 * @author Mauro Talevi
 */
public class StringNumberListMapValueConverter extends AbstractValueConverter {

    public static final String BIND_ERROR_MAP_KEY = "bind.error.map";
    public static final String DEFAULT_MAP_MESSAGE = "Invalid map value for field {0}";
    private static final String NL = "\n";
    private static final String EQUAL = "=";
    private static final String COMMA = ",";

    private NumberFormat numberFormat;

    public StringNumberListMapValueConverter(MessageResources messageResources) {
        this(messageResources, new Properties(), NumberFormat.getInstance());
    }

    public StringNumberListMapValueConverter(MessageResources messageResources, Properties patterns,
            NumberFormat numberFormat) {
        super(messageResources, patterns);
        this.numberFormat = numberFormat;
    }

    /**
     * Accepts parameterized types of raw type Map and argument types String and Number
     */
    public boolean accept(Type type) {
        return acceptMap(type, String.class, Number.class);
    }

    public Object convertValue(String propertyName, String value, Type toType) {

        if (missingValue(value)) {
            String fieldName = messageFor(propertyName, propertyName);
            return convertMissingValue(BIND_ERROR_MAP_KEY, DEFAULT_MAP_MESSAGE, fieldName);
        }

        List<String> lines = split(value, NL);
        Map<String, List<Number>> map = new HashMap<String, List<Number>>();
        for (String line : lines) {
            List<String> parts = split(line, EQUAL);
            if (parts.size() > 1) {
                try {
                    String csv = parts.get(1);
                    List<String> values = split(csv, COMMA);
                    List<Number> numbers = new ArrayList<Number>();
                    for (String numberValue : values) {
                        numbers.add(numberFormat.parse(numberValue));
                    }
                    map.put(parts.get(0), numbers);
                } catch (ParseException e) {
                    // failed to parse as numbers, return string values
                    // TODO should we throw a bind exception here?
                }
            }
        }
        return map;
    }

    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return new ArrayList<Number>();
    }

}
