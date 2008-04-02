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

import static java.util.Arrays.asList;

import java.util.List;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;

/**
 * <code>ValueConverter</code> that converts a CSV value to a List of Strings.
 * A <code>null</code> value will cause a BindException to thrown.
 *
 * @author Mauro Talevi
 */
public class ListValueConverter implements ValueConverter {

    public boolean accept(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T convertValue(String propertyName, String value, Class<T> toType) {
        if ( value == null ){
            throw new BindException("Cannot convert null value for property "+propertyName);
        }
        return (T) asList(value.split(","));
    }

}
