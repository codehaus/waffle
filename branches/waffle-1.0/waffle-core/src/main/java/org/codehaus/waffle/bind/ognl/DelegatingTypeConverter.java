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

import java.lang.reflect.Member;
import java.util.Map;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;

import ognl.OgnlOps;
import ognl.TypeConverter;

/**
 * An implementation of Ognl's <code>TypeConverter</code> which handles Java 5 enums and will delegate
 * custom <code>ValueConverter</code>'s registered per application.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DelegatingTypeConverter implements TypeConverter {
    private static final String EMPTY = "";
    private final ValueConverterFinder valueConverterFinder;

    public DelegatingTypeConverter() {
        this.valueConverterFinder = new OgnlValueConverterFinder();
    }

    public DelegatingTypeConverter(ValueConverterFinder valueConverterFinder) {
        this.valueConverterFinder = valueConverterFinder;
    }

    /**
     * <b>Comments copied from Ognl</b>
     * <p/>
     * Converts the given value to a given type.  The OGNL context, target, member and
     * name of property being set are given.  This method should be able to handle
     * conversion in general without any context, target, member or property name specified.
     *
     * @param context      OGNL context under which the conversion is being done
     * @param target       target object in which the property is being set
     * @param member       member (Constructor, Method or Field) being set
     * @param propertyName property name being set
     * @param value        value to be converted
     * @param toType       type to which value is converted
     * @return Converted value Object of type toType or TypeConverter.NoConversionPossible to indicate that the
     *         conversion was not possible.
     */
    public Object convertValue(Map context,
                               Object target,
                               Member member,
                               String propertyName,
                               Object value,
                               Class toType) {
        return convertValue(propertyName, (String) value, toType);
    }

    /**
     * Simplified entry point for Ognl use in Waffle
     *
     * @param propertyName property name being set
     * @param value        value to be converted
     * @param toType       type to which value is converted
     * @return Converted value Object of type toType or TypeConverter.NoConversionPossible to indicate that the
     *         conversion was not possible.
     */
    @SuppressWarnings({"unchecked"})
    public Object convertValue(String propertyName, String value, Class toType) {
        if (toType.isEnum()) {
            if (EMPTY.equals(value)) {
                return null;
            }
            return Enum.valueOf(toType, value);
        }

        ValueConverter converter = valueConverterFinder.findConverter(toType);

        if (converter != null) {
            return converter.convertValue(propertyName, value, toType);
        }

        return OgnlOps.convertValue(value, toType);
    }

}
