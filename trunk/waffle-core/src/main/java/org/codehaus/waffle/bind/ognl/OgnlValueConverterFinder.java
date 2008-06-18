/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.ognl;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;

/**
 * <p>
 * Implementation of <code>ValueConverterFinder</code> which caches converters found per type and uses
 * <code>OgnlValueConverter</code> as default converter.
 * </p>
 * <p>
 * Converters can be either injected at instantiation or registered after instantiation.
 * </p>
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 * @see OgnlValueConverter
 */
public class OgnlValueConverterFinder implements ValueConverterFinder {

    private static final ValueConverter OGNL_VALUE_CONVERTER = new OgnlValueConverter();
    private final Map<Type, ValueConverter> cache = new HashMap<Type, ValueConverter>();
    private final List<ValueConverter> converters;

    public OgnlValueConverterFinder() {
        this(new ValueConverter[] {});
    }

    public OgnlValueConverterFinder(ValueConverter... converters) {
        this.converters = new ArrayList<ValueConverter>();
        if (converters != null) {
            this.converters.addAll(asList(converters));
            this.converters.add(OGNL_VALUE_CONVERTER);
        } else {
            this.converters.add(OGNL_VALUE_CONVERTER);
        }
    }

    public ValueConverter findConverter(Type type) {
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

    public void registerConverter(ValueConverter converter) {
        converters.add(converter);
    }

}
