/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.bind.converters;

import static java.util.Arrays.asList;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <code>ValueConverter</code> that converts a CSV value to a List. A <code>null</code> or empty value (once
 * trimmed) will be returned as <code>null</code> (behaviour which can be overridden via the
 * {@link convertMissingValue()} method). The message keys and default values used are:
 * <ul>
 * <li>"bind.error.list" ({@link #BIND_ERROR_LIST_KEY}): list is <code>null</code> or empty (message defaults to
 * {@link #DEFAULT_LIST_MESSAGE})</li>
 * </ul>
 * The converter first attempts to parse the values as numbers (using the default <code>NumberFormat</code> instance)
 * and if not successful returns the string values.
 * 
 * @author Mauro Talevi
 */
public class ListValueConverter extends AbstractValueConverter {

    static final String BIND_ERROR_LIST_KEY = "bind.error.list";
    static final String DEFAULT_LIST_MESSAGE = "Invalid list value for field {0}";
    private static final String COMMA = ",";

    public ListValueConverter(MessageResources messageResources) {
        super(messageResources);
    }

    public boolean accept(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    @SuppressWarnings( { "unchecked" })
    public <T> T convertValue(String propertyName, String value, Class<T> toType) {

        if (missingValue(value)) {
            String fieldName = messageFor(propertyName, propertyName);
            return (T) convertMissingValue(BIND_ERROR_LIST_KEY, DEFAULT_LIST_MESSAGE, fieldName);
        }

        List<String> values = asList(value.split(COMMA));
        if (values.size() != 0) {
            try {
                return (T) toNumbers(values);
            } catch (ParseException e) {
                // failed to parse as numbers, return string values
            }
        }
        return (T) values;
    }

    private List<Number> toNumbers(List<String> values) throws ParseException {
        NumberFormat format = NumberFormat.getInstance();
        List<Number> list = new ArrayList<Number>();
        for (String value : values) {
            list.add(format.parse(value));
        }
        return list;
    }

}
