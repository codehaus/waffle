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
package org.codehaus.waffle.bind.ognl;

import org.codehaus.waffle.bind.ValueConverter;

import ognl.DefaultTypeConverter;
import ognl.TypeConverter;

/**
 * Ognl-based implementation of <code>ValueConverter</code>.
 *
 * @author Mauro Talevi
 */
public class OgnlValueConverter implements ValueConverter {

    private final TypeConverter converter;
    
    public OgnlValueConverter(){
        this(new DefaultTypeConverter());
    }
    
    public OgnlValueConverter(TypeConverter converter) {
        this.converter = converter;
    }

    public boolean accept(Class<?> type) {
        return true;
    }

    @SuppressWarnings({"unchecked"})
    public <T> T convertValue(String propertyName, String value, Class<T> toType) {
        return (T) converter.convertValue(null, null, null, propertyName, value, toType);
    }

}
