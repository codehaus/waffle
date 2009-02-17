/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.ognl;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import ognl.TypeConverter;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;

/**
 * An implementation of Ognl's <code>TypeConverter</code> which delegates to 
 * <code>ValueConverter</code>'s registered per application and retrieved via the the
 * <code>ValueConverterFinder</code>.  
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DelegatingTypeConverter implements TypeConverter {
    private final ValueConverterFinder valueConverterFinder;
    private final BindMonitor bindMonitor;

    public DelegatingTypeConverter() {
        this(new OgnlValueConverterFinder(), new SilentMonitor());
    }

    public DelegatingTypeConverter(ValueConverterFinder valueConverterFinder, BindMonitor bindMonitor) {
        this.valueConverterFinder = valueConverterFinder;
        this.bindMonitor = bindMonitor;
    }

    /**
     * <b>Comments copied from Ognl</b> <p/> Converts the given value to a given type. The OGNL context, target, member
     * and name of property being set are given. This method should be able to handle conversion in general without any
     * context, target, member or property name specified.
     * 
     * @param context OGNL context under which the conversion is being done
     * @param target target object in which the property is being set
     * @param member member (Constructor, Method or Field) being set
     * @param propertyName property name being set
     * @param value value to be converted
     * @param toType type to which value is converted
     * @return Converted value Object of type toType or TypeConverter.NoConversionPossible to indicate that the
     *         conversion was not possible.
     */
    @SuppressWarnings("unchecked")
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value,
            Class toType) {
        return convertValue(propertyName, (String) value, genericParameterTypeFor((Method) member));
    }

    private Type genericParameterTypeFor(Method method) {
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (parameterTypes.length > 0) {
            Type type = parameterTypes[0];
            bindMonitor.genericParameterTypeFound(type, method);
            return type;
        }
        bindMonitor.genericParameterTypeNotFound(method);
        return null;
    }

    /**
     * Simplified entry point for Ognl use in Waffle
     * 
     * @param propertyName property name being set
     * @param value value to be converted
     * @param type Type to which value is converted
     * @return Converted value Object for type or the unconvertered value if no converter found
     */
    public Object convertValue(String propertyName, String value, Type type) {
        ValueConverter converter = valueConverterFinder.findConverter(type);

        if (converter != null) {
            bindMonitor.valueConverterFound(type, converter);
            return converter.convertValue(propertyName, value, type);
        } else {
            bindMonitor.valueConverterNotFound(type);
            return value;
        }
    }

}
