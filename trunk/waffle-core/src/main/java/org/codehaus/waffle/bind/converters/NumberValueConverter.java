/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Properties;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <p>
 * <code>ValueConverter</code> that converts a value to a Numbers using the <code>NumberFormat</code> instance
 * provided (which defaults to <code>NumberFormat.getInstance()</code>).
 * </p>
 * The message keys and default values used are:
 * <ul>
 * <li>"bind.error.number" ({@link #BIND_ERROR_NUMBER_KEY}): bind error in number parsing (message defaults to
 * {@link #DEFAULT_NUMBER_MESSAGE})</li>
 * <li>"bind.error.number.missing" ({@link #BIND_ERROR_NUMBER_MISSING_KEY}): number is <code>null</code> or empty
 * (message defaults to {@link #DEFAULT_NUMBER_MISSING_MESSAGE})</li>
 * </ul>
 * 
 * @author Mauro Talevi
 */
public class NumberValueConverter extends AbstractValueConverter {
    public static final String BIND_ERROR_NUMBER_KEY = "bind.error.number";
    public static final String BIND_ERROR_NUMBER_MISSING_KEY = "bind.error.number.missing";
    public static final String DEFAULT_NUMBER_MISSING_MESSAGE = "Missing number value for field {0}";
    public static final String DEFAULT_NUMBER_MESSAGE = "Invalid number {1} (using format {2}) for field {0}";

    private NumberFormat numberFormat;

    public NumberValueConverter(MessageResources messageResources) {
        this(messageResources, new Properties(), NumberFormat.getInstance());
    }

    public NumberValueConverter(MessageResources messageResources, Properties patterns, NumberFormat numberFormat) {
        super(messageResources, patterns);
        this.numberFormat = numberFormat;
    }

    public boolean accept(Type type) {
        if (type instanceof Class) {
            Class<?> rawType = (Class<?>) type;
            return Number.class.isAssignableFrom(rawType) || double.class.equals(rawType)
                    || float.class.equals(rawType) || long.class.equals(rawType) || int.class.equals(rawType);
        }
        return false;
    }

    public Object convertValue(String propertyName, String value, Type toType) {
        String fieldName = messageFor(propertyName, propertyName);
        if (missingValue(value)) {
            return convertMissingValue(BIND_ERROR_NUMBER_MISSING_KEY, DEFAULT_NUMBER_MISSING_MESSAGE, fieldName);
        }

        try {
            return numberFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            throw newBindException(BIND_ERROR_NUMBER_KEY, DEFAULT_NUMBER_MESSAGE, fieldName, value, numberFormat);
        }
    }

}
