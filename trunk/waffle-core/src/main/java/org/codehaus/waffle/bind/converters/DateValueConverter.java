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

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.i18n.MessageResources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <code>ValueConverter</code> that converts Date values.  The date format is configurable via the message resources bundle.
 * A <code>null</code>, empty or invalid value will cause a BindException to be thrown.
 * The message keys and default values used are:
 * <ul>
 *  <li>"bind.error.date" ({@link #BIND_ERROR_DATE_KEY}): bind error in date parsing (message defaults to {@link #DEFAULT_DATE_MESSAGE})</li>
 *  <li>"bind.error.date.missing" ({@link #BIND_ERROR_DATE_MISSING_KEY}): date is <code>null</code> or empty (message defaults to {@link #DEFAULT_DATE_MISSING_MESSAGE})</li>
 *  <li>"date.format" ({@link #DATE_FORMAT_KEY}): date format used in parsing (defaults to {@link #DEFAULT_DATE_FORMAT})</li>
 * </ul>
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DateValueConverter implements ValueConverter {
    static final String BIND_ERROR_DATE_KEY = "bind.error.date";
    static final String BIND_ERROR_DATE_MISSING_KEY = "bind.error.date.missing";
    static final String DATE_FORMAT_KEY = "date.format";
    static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    static final String DEFAULT_DATE_MESSAGE = "Invalid date {1} (using format {2}) for field {0}";
    static final String DEFAULT_DATE_MISSING_MESSAGE = "Missing date value for field {0}";
    private final MessageResources messageResources;

    public DateValueConverter(MessageResources messageResources) {
        this.messageResources = messageResources;
    }

    public boolean accept(Class<?> type) {
        return Date.class.isAssignableFrom(type);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T convertValue(String propertyName, String value, Class<T> toType) {
        String fieldName = messageResources.getMessageWithDefault(propertyName, propertyName);
        if (value == null || value.trim().length() == 0) {
            String message = messageResources.getMessageWithDefault(BIND_ERROR_DATE_MISSING_KEY, DEFAULT_DATE_MISSING_MESSAGE, fieldName);
            throw new BindException(message);
        }

        String dateFormat = messageResources.getMessageWithDefault(DATE_FORMAT_KEY, DEFAULT_DATE_FORMAT);

        try {
            return (T) new SimpleDateFormat(dateFormat).parse(value);
        } catch (ParseException e) {
            String message = messageResources.getMessageWithDefault(BIND_ERROR_DATE_KEY, DEFAULT_DATE_MESSAGE, fieldName, value, dateFormat);
            throw new BindException(message);
        }
    }

}
