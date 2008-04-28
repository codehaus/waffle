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
package org.codehaus.waffle.bind;

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

    public <T> T transmute(String value, Class<T> toType) {
        if (isEmpty(value) && toType.isPrimitive()) {
            value = null; // this allows Ognl to use that primitives default value
        }
        return valueConverterFinder.findConverter(toType).convertValue(null, value, toType);
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
