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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <p>
 * <code>ValueConverter</code> that converts a CSV value to a List. A <code>null</code> or empty value (once
 * trimmed) will be returned as an empty list (behaviour which can be overridden via the
 * {@link convertMissingValue()} method). The message keys and default values used are:
 * <ul>
 * <li>"bind.error.list" ({@link #BIND_ERROR_LIST_KEY}): list is <code>null</code> or empty (message defaults to
 * {@link #DEFAULT_LIST_MESSAGE})</li>
 * <li>"list.number.pattern" ({@link #NUMBER_PATTERN_KEY}): pattern to use to identify the list as parseable numbers
 * (defaults to {@link #DEFAULT_NUMBER_PATTERN})</li>
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
public class ListValueConverter extends AbstractValueConverter {

    public static final String BIND_ERROR_LIST_KEY = "bind.error.list";
    public static final String DEFAULT_LIST_MESSAGE = "Invalid list value for field {0}";
    public static final String NUMBER_PATTERN_KEY = "list.number.pattern";
    public static final String DEFAULT_NUMBER_PATTERN = "[0-9.-]*";
    
    private static final String COMMA = ",";
    private NumberFormat numberFormat;
    private Properties patterns;

    public ListValueConverter(MessageResources messageResources) {
        this(messageResources, NumberFormat.getInstance(), new Properties());
    }

    public ListValueConverter(MessageResources messageResources, NumberFormat numberFormat, Properties patterns) {
        super(messageResources);
        this.numberFormat = numberFormat;
        this.patterns = patterns;
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

        List<String> values = listValues(value);
        if (areNumbers(values)) {
            try {
                return (T) toNumbers(values);
            } catch (ParseException e) {
                // failed to parse as numbers, return string values
            }
        }
        return (T) values;
    }

    private List<String> listValues(String value) {
        String[] values = value.split(COMMA);        
        List<String> list = new ArrayList<String>();
        for ( String current : values ){
            if ( current.trim().length() > 0 ){
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
    
    protected boolean areNumbers(List<String> values) {
        if (values.size() == 0) {
            return false; // return empty list
        }
        String numberPattern = patternFor(patterns, NUMBER_PATTERN_KEY, DEFAULT_NUMBER_PATTERN);
        for (String value : values) {
            if (!matches(value, numberPattern)) {
                return false;
            }
        }
        return true;
    }
    
    protected List<Number> toNumbers(List<String> values) throws ParseException {
        List<Number> numbers = new ArrayList<Number>();
        for (String value : values) {
            numbers.add(numberFormat.parse(value));
        }
        return numbers;
    }

}
