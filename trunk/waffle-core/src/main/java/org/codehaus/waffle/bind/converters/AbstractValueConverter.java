/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.i18n.MessageResources;

/**
 * Abstract <code>ValueConverter</code> that holds utility functionality common to all value converters.
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractValueConverter implements ValueConverter {

    private final MessageResources messageResources;
    private Properties patterns;

    protected AbstractValueConverter(MessageResources messageResources, Properties patterns) {
        this.messageResources = messageResources;
        this.patterns = patterns;
    }

    /**
     * Determines if the value is missing.
     * 
     * @param value the String value
     * @return A boolean, <code>true</code> if value is <code>null</code> or trimmed length is 0.
     */
    protected boolean missingValue(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Handles the case of a missing value. By default it return a <code>null</code> converted value, but can be
     * overridden to throw a BindException
     * 
     * @param key the error message key
     * @param defaultMessage the default message if key is not found
     * @param parameters the message formatting parameters
     * @return A converted object when value is missing, <code>null</code> by default.
     */
    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return null;
    }

    /**
     * Accepts parameterized types of raw type List and argument of the type passed in
     */
    protected boolean acceptList(Type type, Class<?> argumentClass) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Type argumentType = parameterizedType.getActualTypeArguments()[0];
            return List.class.isAssignableFrom((Class<?>) rawType)
                    && argumentClass.isAssignableFrom((Class<?>) argumentType);
        }
        return false;
    }

    /**
     * Accepts parameterized types of raw type Map and arguments of the type passed in
     */
    protected boolean acceptMap(Type type, Class<?> argumentClass0, Class<?> argumentClass1) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Type argumentType0 = parameterizedType.getActualTypeArguments()[0];
            Type argumentType1 = parameterizedType.getActualTypeArguments()[1];
            return Map.class.isAssignableFrom((Class<?>) rawType)
                    && argumentClass0.isAssignableFrom((Class<?>) argumentType0)
                    && acceptList(argumentType1, argumentClass1);
        }
        return false;
    }

    protected BindException newBindException(String key, String defaultMessage, Object... parameters) {
        String message = messageResources.getMessageWithDefault(key, defaultMessage, parameters);
        return new BindException(message);
    }

    protected String messageFor(String key, String defaultMessage, Object... parameters) {
        return messageResources.getMessageWithDefault(key, defaultMessage, parameters);
    }

    protected boolean matches(String value, String regex) {
        return value != null && value.matches(regex);
    }

    protected String patternFor(String key, String defaultPattern) {
        if (patterns.containsKey(key)) {
            return patterns.getProperty(key);
        }
        return messageFor(key, defaultPattern);
    }

    public Properties getPatterns() {
        return patterns;
    }

    public void changePatterns(Properties patterns) {
        this.patterns = patterns;
    }

    protected List<String> split(String value, String separator) {
        String[] values = value.split(separator);
        List<String> list = new ArrayList<String>();
        for (String current : values) {
            if (current.trim().length() > 0) {
                list.add(current.trim());
            }
        }
        return list;
    }
}
