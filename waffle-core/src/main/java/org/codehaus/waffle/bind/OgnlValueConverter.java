/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.bind;

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

    public Object convertValue(String propertyName, String value, Class<?> toType) throws BindException {
        return converter.convertValue(null, null, null, propertyName, value, toType);
    }

}
