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
package org.codehaus.waffle.bind.ognl;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;

/**
 * <p>
 * Implementation of <code>ValueConverterFinder</code> which caches converters 
 * found per type and uses <code>OgnlValueConverter</code> as default converter.
 * </p>
 *
 * @author Michael Ward
 * @author Mauro Talevi
 * @see OgnlValueConverter
 */
public class OgnlValueConverterFinder implements ValueConverterFinder {

    private static final ValueConverter OGNL_VALUE_CONVERTER = new OgnlValueConverter();
    private final List<ValueConverter> DEFAULT_CONVERTERS = asList(OGNL_VALUE_CONVERTER);
    private final Map<Class<?>, ValueConverter> cache = new HashMap<Class<?>, ValueConverter>();
    private final List<ValueConverter> converters;

    public OgnlValueConverterFinder() {
        this.converters = DEFAULT_CONVERTERS;
    }

    public OgnlValueConverterFinder(ValueConverter... converters) {
        if (converters != null) {
            this.converters = new ArrayList<ValueConverter>();
            this.converters.addAll(asList(converters));
            this.converters.addAll(DEFAULT_CONVERTERS);
        } else {
            this.converters = DEFAULT_CONVERTERS;            
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
