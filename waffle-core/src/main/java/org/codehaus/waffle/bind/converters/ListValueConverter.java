/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.codehaus.waffle.i18n.MessageResources;

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
public class ListValueConverter extends AbstractValueConverter {

    public static final String BIND_ERROR_LIST_KEY = "bind.error.list";
    public static final String DEFAULT_LIST_MESSAGE = "Invalid list value for field {0}";

    private static final String COMMA = ",";

    public ListValueConverter(MessageResources messageResources) {
        this(messageResources, new Properties());
    }

    public ListValueConverter(MessageResources messageResources, Properties patterns) {
        super(messageResources, patterns);
    }

    public boolean accept(Type type) {
        if (type instanceof Class) {
            return List.class.isAssignableFrom((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            return List.class.isAssignableFrom((Class<?>) rawType);
        }
        return false;
    }

    @SuppressWarnings( { "unchecked" })
    public Object convertValue(String propertyName, String value, Type toType) {

        if (missingValue(value)) {
            String fieldName = messageFor(propertyName, propertyName);
            return convertMissingValue(BIND_ERROR_LIST_KEY, DEFAULT_LIST_MESSAGE, fieldName);
        }

        return listValues(value);
    }

    protected List<String> listValues(String value) {
        String[] values = value.split(COMMA);
        List<String> list = new ArrayList<String>();
        for (String current : values) {
            if (current.trim().length() > 0) {
                list.add(current);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return new ArrayList();
    }

}
