/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import org.codehaus.waffle.i18n.MessageResources;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * <code>ValueConverter</code> that converts CSV values to List of Strings. A <code>null</code> or empty value (once
 * trimmed) will be returned as an empty list (behaviour which can be overridden via the {@link #convertMissingValue}
 * method). The message keys and default values used are:
 * <ul>
 * <li>"bind.error.list" ({@link #BIND_ERROR_LIST_KEY}): list is <code>null</code> or empty (message defaults to
 * {@link #DEFAULT_LIST_MESSAGE})</li>
 * </ul>
 * The patterns are also optionally injectable via <code>Properties</code> in the constructor and take precedence over
 * the ones configured in the messages resources.
 * </p>
 * 
 * @author Mauro Talevi
 */
public class StringListValueConverter extends AbstractValueConverter {

    public static final String BIND_ERROR_LIST_KEY = "bind.error.list";
    public static final String DEFAULT_LIST_MESSAGE = "Invalid list value for field {0}";

    private static final String COMMA = ",";

    public StringListValueConverter(MessageResources messageResources) {
        this(messageResources, new Properties());
    }

    public StringListValueConverter(MessageResources messageResources, Properties patterns) {
        super(messageResources, patterns);
    }

    /**
     * Accepts parameterized types of raw type List and argument type String
     */
    public boolean accept(Type type) {
        return acceptList(type, String.class);
    }

    @SuppressWarnings( { })
    public Object convertValue(String propertyName, String value, Type toType) {

        if (missingValue(value)) {
            String fieldName = messageFor(propertyName, propertyName);
            return convertMissingValue(BIND_ERROR_LIST_KEY, DEFAULT_LIST_MESSAGE, fieldName);
        }

        return listValues(value);
    }

    protected List<String> listValues(String value) {
        return split(value, COMMA);
    }

    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return new ArrayList<String>();
    }

}
