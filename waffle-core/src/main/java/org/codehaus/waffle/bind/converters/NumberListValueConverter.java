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
 * <code>ValueConverter</code> that converts a CSV value to a List of Numbers. It extends
 * {@link org.codehaus.waffle.bind.converters.ListValueConverter ListValueConverter} to provide number parsing using the
 * <code>NumberFormat</code> instance provided (which defaults to <code>NumberFormat.getInstance()</code>) and if
 * not successful returns the string values.
 * </p>
 * 
 * @author Mauro Talevi
 */
public class NumberListValueConverter extends ListValueConverter {

    private NumberFormat numberFormat;

    public NumberListValueConverter(MessageResources messageResources) {
        this(messageResources, new Properties(), NumberFormat.getInstance());
    }

    public NumberListValueConverter(MessageResources messageResources, Properties patterns, NumberFormat numberFormat) {
        super(messageResources, patterns);
        this.numberFormat = numberFormat;
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
            return List.class.isAssignableFrom((Class<?>) rawType)
                    && Number.class.isAssignableFrom((Class<?>) argumentType);
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
            List<Number> numbers = new ArrayList<Number>();
            for (String numberValue : values) {
                numbers.add(numberFormat.parse(numberValue));
            }
            return numbers;
        } catch (ParseException e) {
            // failed to parse as numbers, return string values
            // TODO should we throw a bind exception here?
        }
        return values;
    }

}
