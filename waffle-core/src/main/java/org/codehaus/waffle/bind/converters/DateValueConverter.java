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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.codehaus.waffle.i18n.MessageResources;

/**
 * <code>ValueConverter</code> that converts Date values. The date format is configurable via the message resources
 * bundle. A <code>null</code> or empty value (once trimmed) will be returned as <code>null</code> (behaviour which
 * can be overridden via the {@link convertMissingValue()} method), while an invalid value will cause a BindException to
 * be thrown. The message keys and default values used are:
 * <ul>
 * <li>"bind.error.date" ({@link #BIND_ERROR_DATE_KEY}): bind error in date parsing (message defaults to
 * {@link #DEFAULT_DATE_MESSAGE})</li>
 * <li>"bind.error.date.missing" ({@link #BIND_ERROR_DATE_MISSING_KEY}): date is <code>null</code> or empty
 * (message defaults to {@link #DEFAULT_DATE_MISSING_MESSAGE})</li>
 * <li>"date.format" ({@link #DATE_FORMAT_KEY}): date format used in parsing (defaults to
 * {@link #DEFAULT_DATE_FORMAT})</li>
 * <li>"date.format.day" ({@link #DAY_FORMAT_KEY}): date format used in parsing properties that end in "Day"
 * (defaults to {@link #DEFAULT_DAY_FORMAT})</li>
 * <li>"date.format.time" ({@link #TIME_FORMAT_KEY}): date format used in parsing properties that end in "Time"
 * (defaults to {@link #DEFAULT_TIME_FORMAT})</li>
 * </ul>
 * The patterns are also optionally injectable via <code>Properties</code> in the constructor and take precedence over
 * the ones configured in the messages resources.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DateValueConverter extends AbstractValueConverter {
    static final String BIND_ERROR_DATE_KEY = "bind.error.date";
    static final String BIND_ERROR_DATE_MISSING_KEY = "bind.error.date.missing";
    static final String DATE_FORMAT_KEY = "date.format";
    static final String DAY_FORMAT_KEY = "date.format.day";
    static final String TIME_FORMAT_KEY = "date.format.time";
    static final String DAY_SUFFIX = "Day";
    static final String TIME_SUFFIX = "Time";
    static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    static final String DEFAULT_DAY_FORMAT = "dd/MM/yyyy";
    static final String DEFAULT_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    static final String DEFAULT_DATE_MESSAGE = "Invalid date {1} (using format {2}) for field {0}";
    static final String DEFAULT_DATE_MISSING_MESSAGE = "Missing date value for field {0}";

    private Properties patterns;
    
    public DateValueConverter(MessageResources messageResources) {
        this(messageResources, new Properties());
    }

    public DateValueConverter(MessageResources messageResources, Properties patterns) {
        super(messageResources);
        this.patterns = patterns;
    }

    public boolean accept(Class<?> type) {
        return Date.class.isAssignableFrom(type);
    }

    @SuppressWarnings( { "unchecked" })
    public <T> T convertValue(String propertyName, String value, Class<T> toType) {
        String fieldName = messageFor(propertyName, propertyName);
        if (missingValue(value)) {
            return (T) convertMissingValue(BIND_ERROR_DATE_MISSING_KEY, DEFAULT_DATE_MISSING_MESSAGE, fieldName);
        }

        SimpleDateFormat dateFormat = dateFormatFor(propertyName);

        try {
            return (T) dateFormat.parse(value);
        } catch (ParseException e) {
            throw newBindException(BIND_ERROR_DATE_KEY, DEFAULT_DATE_MESSAGE, fieldName, value, dateFormat.toPattern());
        }
    }

    private enum DateType {
        DAY, TIME, DATE
    };

    /**
     * Retrieves the date format for the given property name
     * 
     * @param propertyName the property name
     * @return The SimpleDateFormat
     */
    private SimpleDateFormat dateFormatFor(String propertyName) {
        DateType dateType = dateType(propertyName);
        String pattern = null;
        switch (dateType) {
            case DAY:
                pattern = patternFor(DAY_FORMAT_KEY, DEFAULT_DAY_FORMAT);
                break;
            case TIME:
                pattern = patternFor(TIME_FORMAT_KEY, DEFAULT_TIME_FORMAT);
                break;
            default:
                pattern = patternFor(DATE_FORMAT_KEY, DEFAULT_DATE_FORMAT);
        }
        return new SimpleDateFormat(pattern);
    }

    private String patternFor(String key, String defaultPattern) {
        if ( patterns.containsKey(key)) {
            return patterns.getProperty(key);
        }
        return messageFor(key, defaultPattern);
    }

    private DateType dateType(String propertyName) {
        if (endsWith(propertyName, DAY_SUFFIX)) {
            return DateType.DAY;
        } else if (endsWith(propertyName, TIME_SUFFIX)) {
            return DateType.TIME;
        }
        return DateType.DATE;
    }

    private boolean endsWith(String propertyName, String suffix) {
        return propertyName != null && propertyName.endsWith(suffix);
    }

}
