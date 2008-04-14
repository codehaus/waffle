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

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.i18n.MessageResources;

/**
 * <code>ValueConverter</code> that converts a CSV value to a List. A <code>null</code> value will cause a
 * BindException to thrown. The converter also looks to see if the values in the list are integers or doubles and if so
 * parses them.
 * 
 * @author Mauro Talevi
 */
public class ListValueConverter implements ValueConverter {

    private final MessageResources messageResources;

    public ListValueConverter(MessageResources messageResources) {
        this.messageResources = messageResources;
    }

    public boolean accept(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    @SuppressWarnings( { "unchecked" })
    public <T> T convertValue(String propertyName, String value, Class<T> toType) {
        if (value == null) {
            String fieldName = messageResources.getMessageWithDefault(propertyName, propertyName);
            String message = messageResources.getMessageWithDefault("bind.error.list.missing",
                    "Missing list value for field {0}", fieldName);
            throw new BindException(message);
        }
        List<String> values = asList(value.split(","));        
        if ( values.size() == 0 ){
            return (T) values;
        }
        if ( areIntegers(values) ){
            return (T) toIntegers(values);
        } else if ( areDoubles(values)) {
            return (T) toDoubles(values);
        }
        return (T) values;
    }

    private boolean areIntegers(List<String> values) {
        try {
            parseInt(values.get(0));
            return true;
        } catch ( NumberFormatException e) {
            return false;
        }
    }

    private List<Integer> toIntegers(List<String> values) {
        List<Integer> list = new ArrayList<Integer>();
        for ( String value : values ){
            list.add(parseInt(value));
        }
        return list;
    }
    
    private boolean areDoubles(List<String> values) {
        try {
            parseDouble(values.get(0));
            return true;
        } catch ( NumberFormatException e) {
            return false;
        }
    }

    private List<Double> toDoubles(List<String> values) {
        List<Double> list = new ArrayList<Double>();
        for ( String value : values ){
            list.add(parseDouble(value));
        }
        return list;
    }
}
