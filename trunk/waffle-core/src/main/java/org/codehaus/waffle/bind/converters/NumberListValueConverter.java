/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <p>
 * <code>ValueConverter</code> that converts a CSV value to a List. A <code>null</code> or empty value (once
 * trimmed) will be returned as an empty list (behaviour which can be overridden via the {@link #convertMissingValue}
 * method). The message keys and default values used are:
 * <ul>
 * <li>"bind.error.list" ({@link #BIND_ERROR_LIST_KEY}): list is <code>null</code> or empty (message defaults to
 * {@link #DEFAULT_LIST_MESSAGE})</li>
 * </ul>
 * The patterns are also optionally injectable via <code>Properties</code> in the constructor and take precedence over
 * the ones configured in the messages resources.
 * </p>
 * <p>
 * NOTE: the converter will first check if the values match the configured number regex pattern and only if it does will
 * it attempt to parse them (using the <code>NumberFormat</code> instance provided, which defaults to
 * <code>NumberFormat.getInstance()</code>) and if not successful returns the string values. The reason for the
 * presence of the preliminary number pattern matching is to disable the attempt of number parsing altogether for some
 * string values that may start with number and may be erronously parsed as numbers.
 * </p>
 * 
 * @author Mauro Talevi
 */
public class NumberListValueConverter extends AbstractValueConverter {

    public static final String BIND_ERROR_LIST_KEY = "bind.error.list";
    public static final String DEFAULT_LIST_MESSAGE = "Invalid list value for field {0}";

    private static final String COMMA = ",";
    private NumberFormat numberFormat;
    private Properties patterns;

    public NumberListValueConverter(MessageResources messageResources) {
        this(messageResources, NumberFormat.getInstance(), new Properties());
    }

    public NumberListValueConverter(MessageResources messageResources, NumberFormat numberFormat, Properties patterns) {
        super(messageResources);
        this.numberFormat = numberFormat;
        this.patterns = patterns;
    }

    /**
     * Accepts types of raw type List and argument type Number
     */
    public boolean accept(Type type) {
        if (type instanceof Class) {
            return List.class.isAssignableFrom((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Type argumentType = parameterizedType.getActualTypeArguments()[0];
            return List.class.isAssignableFrom((Class<?>) rawType) && Number.class.isAssignableFrom((Class<?>)argumentType);
        }
        return false;
    }

    @SuppressWarnings( { "unchecked" })
    public Object convertValue(String propertyName, String value, Type toType) {

        if (missingValue(value)) {
            String fieldName = messageFor(propertyName, propertyName);
            return convertMissingValue(BIND_ERROR_LIST_KEY, DEFAULT_LIST_MESSAGE, fieldName);
        }

        List<String> values = listValues(value);
        try {
            return toNumbers(values);
        } catch (ParseException e) {
            // failed to parse as numbers, return string values
        }
        return values;
    }

    private List<String> listValues(String value) {
        String[] values = value.split(COMMA);
        List<String> list = new ArrayList<String>();
        for (String current : values) {
            if (current.trim().length() > 0) {
                list.add(current);
            }
        }
        return list;
    }

    public Properties getPatterns() {
        return patterns;
    }

    public void changePatterns(Properties patterns) {
        this.patterns = patterns;
    }

    @SuppressWarnings("unchecked")
    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return new ArrayList();
    }

    protected List<Number> toNumbers(List<String> values) throws ParseException {
        List<Number> numbers = new ArrayList<Number>();
        for (String value : values) {
            numbers.add(numberFormat.parse(value));
        }
        return numbers;
    }

}
