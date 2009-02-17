/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import java.lang.reflect.Type;

/**
 * This implementation uses the {@link org.codehaus.waffle.bind.ValueConverterFinder} and its resulting
 * {@link ValueConverter} to transform a String value into the specified type.
 * 
 * @author Michael Ward
 */
public class DefaultStringTransmuter implements StringTransmuter {
    private final ValueConverterFinder valueConverterFinder;

    public DefaultStringTransmuter(ValueConverterFinder valueConverterFinder) {
        this.valueConverterFinder = valueConverterFinder;
    }

    public Object transmute(String value, Type toType) {
        if (isEmpty(value) && isPrimitive(toType)) {
            value = null; // this allows Ognl to use that primitives default value
        }
        return valueConverterFinder.findConverter(toType).convertValue(null, value, toType);
    }

    private boolean isPrimitive(Type toType) {
        if ( toType instanceof Class ){
            return ((Class<?>)toType).isPrimitive();
        }
        return false;
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
