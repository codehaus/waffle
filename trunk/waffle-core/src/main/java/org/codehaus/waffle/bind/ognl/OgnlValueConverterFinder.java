/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.ognl;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.bind.converters.EnumValueConverter;

/**
 * <p>
 * Implementation of <code>ValueConverterFinder</code> which uses
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

    private static final OgnlValueConverter OGNL_VALUE_CONVERTER = new OgnlValueConverter();

    private static final List<? extends ValueConverter> INITIAL_CONVERTERS = asList(new EnumValueConverter());
    
    private final List<ValueConverter> converters;

    public OgnlValueConverterFinder() {
        this(new ValueConverter[] {});
    }

    public OgnlValueConverterFinder(ValueConverter... converters) {
        this.converters = new ArrayList<ValueConverter>();
        if (converters != null) {
            this.converters.addAll(asList(converters));
            this.converters.addAll(INITIAL_CONVERTERS);
        } else {
            this.converters.addAll(INITIAL_CONVERTERS);
        }
    }

    public ValueConverter findConverter(Type type) {
        for (ValueConverter converter : converters) {
            if (converter.accept(type)) {
                return converter;
            }
        }
        return OGNL_VALUE_CONVERTER;
    }

    public void registerConverter(ValueConverter converter) {
        converters.add(converter);
    }

}
