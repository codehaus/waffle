/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.bind;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Default implementation of <code>ValueConverterFinder</code> which caches
 * converters found per type.
 * </p>
 * <p>
 * If no converters are specified, the <code>OgnlValueConverter</code> is used as default.
 * </p>
 *
 * @author Michael Ward
 * @author Mauro Talevi
 * @see OgnlValueConverter
 */
public class DefaultValueConverterFinder implements ValueConverterFinder {

    private static final ValueConverter DEFAULT_VALUE_CONVERTER = new OgnlValueConverter();
    private final Map<Class<?>, ValueConverter> cache = new HashMap<Class<?>, ValueConverter>();
    private final ValueConverter[] converters;

    public DefaultValueConverterFinder() {
        this.converters = new ValueConverter[]{DEFAULT_VALUE_CONVERTER};
    }

    public DefaultValueConverterFinder(ValueConverter... converters) {
        if (converters == null) {
            this.converters = new ValueConverter[]{DEFAULT_VALUE_CONVERTER};
        } else {
            this.converters = converters;
        }
    }

    public ValueConverter findConverter(Class<?> type) {
        if (cache.containsKey(type)) { // cache hit
            return cache.get(type);
        }

        for (ValueConverter converter : converters) {
            if (converter.accept(type)) {
                cache.put(type, converter);
                return converter;
            }
        }

        cache.put(type, null); // cache the null
        return null;
    }

}
