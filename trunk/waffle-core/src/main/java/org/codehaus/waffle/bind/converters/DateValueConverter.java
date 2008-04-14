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
 * <code>ValueConverter</code> that converts Date values.
 * A <code>null</code>, empty or invalid value will cause a BindException to be thrown.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DateValueConverter implements ValueConverter {
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
        if (value == null || value.equals("")) {
            String message = messageResources.getMessageWithDefault("bind.error.date.missing", "Missing date value for field ", fieldName);
            throw new BindException(message);
        }

        String datePattern = messageResources.getMessageWithDefault("date.format", "dd/MM/yyyy");

        try {
            return (T) new SimpleDateFormat(datePattern).parse(value);
        } catch (ParseException e) {
            String message = messageResources.getMessageWithDefault("bind.error.date.invalid", "Date {1} invalid for field {0} with pattern {2}", fieldName, value, datePattern);
            throw new BindException(message);
        }
    }

}
