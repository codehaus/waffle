/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import java.lang.reflect.Type;

import org.codehaus.waffle.bind.ValueConverter;

/**
 * <code>ValueConverter</code> that converts Enum values. 
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class EnumValueConverter implements ValueConverter {
  
    private static final String EMPTY = "";

    public boolean accept(Type type) {
        if (type instanceof Class ){
            return ((Class<?>) type).isEnum();
        }
        return false;
    }

    @SuppressWarnings( { "unchecked" })
    public Object convertValue(String propertyName, String value, Type type) {
        if (EMPTY.equals(value)) {
            return null;
        }
        return Enum.valueOf((Class) type, value);
    }

}
