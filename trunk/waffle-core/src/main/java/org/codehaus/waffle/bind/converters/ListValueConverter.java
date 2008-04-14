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

import java.util.List;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.i18n.MessageResources;

/**
 * <code>ValueConverter</code> that converts a CSV value to a List of Strings. 
 * A <code>null</code> value will cause a BindException to thrown.
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
                    "Missing list value for field {0} for list {1}", fieldName);
            throw new BindException(message);
        }
        return (T) asList(value.split(","));
    }

}
