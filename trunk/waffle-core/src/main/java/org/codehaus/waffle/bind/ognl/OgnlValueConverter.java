/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.ognl;

import java.lang.reflect.Type;

import ognl.DefaultTypeConverter;
import ognl.TypeConverter;

import org.codehaus.waffle.bind.ValueConverter;

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

    public boolean accept(Type type) {
        return type instanceof Class;
    }

    @SuppressWarnings({"unchecked"})
    public Object convertValue(String propertyName, String value, Type toType) {
        return converter.convertValue(null, null, null, propertyName, value, (Class)toType);
    }

}
