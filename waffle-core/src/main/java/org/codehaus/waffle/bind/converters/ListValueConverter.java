/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.bind.converters;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <code>ValueConverter</code> that converts a CSV value to a List. A <code>null</code> value will cause a
 * BindException to thrown. 
 * 
 * The message keys and default values used are:
 * <ul>
 *  <li>"bind.error.list" ({@link #BIND_ERROR_LIST_KEY}): list is <code>null</code> or empty (message defaults to {@link #DEFAULT_LIST_MESSAGE})</li>
 * </ul>
 *  
 * The converter also looks to see if the values in the list are numbers (in order: longs, ints, doubles or floats) and if so
 * parses them to the appropriate Number instance.
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

        if ( missingValue(value)){
            String fieldName = messageFor(propertyName, propertyName);
            return (T)convertMissingValue(BIND_ERROR_LIST_KEY, DEFAULT_LIST_MESSAGE, fieldName);
        }

        List<String> values = asList(value.split(COMMA));        
        if ( values.size() == 0 ){
            return (T) values;
        }
        if ( areLongs(values) ){
            return (T) toLongs(values);
        } else if ( areIntegers(values) ){
            return (T) toIntegers(values);
        } else if ( areDoubles(values)) {
            return (T) toDoubles(values);
        } else if ( areFloats(values) ){
            return (T) toFloats(values);
        } 
        return (T) values;
    }

    private boolean areLongs(List<String> values) {
        try {
            parseLong(values.get(0));
            return true;
        } catch ( NumberFormatException e) {
            return false;
        }
    }

    private List<Long> toLongs(List<String> values) {
        List<Long> list = new ArrayList<Long>();
        for ( String value : values ){
            list.add(parseLong(value));
        }
        return list;
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

    private boolean areFloats(List<String> values) {
        try {
            parseFloat(values.get(0));
            return true;
        } catch ( NumberFormatException e) {
            return false;
        }
    }

    private List<Float> toFloats(List<String> values) {
        List<Float> list = new ArrayList<Float>();
        for ( String value : values ){
            list.add(parseFloat(value));
        }
        return list;
    }
    
}
