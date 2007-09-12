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

import ognl.OgnlOps;
import ognl.TypeConverter;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

/**
 * An extension of Ognl's <code>DefaultTypeConverter</code> which handles Java 5 enums and will delegate
 * custom <code>WaffleTypeConverter</code>'s registered per application.
 *
 * @author Michael Ward
 */
public class OgnlTypeConverter implements TypeConverter {
    private final WaffleTypeConverter[] waffleTypeConverters;
    private final Map<Class, WaffleTypeConverter> cache = new HashMap<Class, WaffleTypeConverter>();

    public OgnlTypeConverter() {
        this.waffleTypeConverters = new WaffleTypeConverter[0];
    }

    public OgnlTypeConverter(WaffleTypeConverter... waffleTypeConverters) {
        if (waffleTypeConverters == null) {
            this.waffleTypeConverters = new WaffleTypeConverter[0];
        } else {
            this.waffleTypeConverters = waffleTypeConverters;
        }
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
     * @return Converted value of type toType or TypeConverter.NoConversionPossible to indicate that the
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
     * @param propertyName
     * @param value
     * @param toType
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public Object convertValue(String propertyName, String value, Class toType) {
        if (toType.isEnum()) {
            if ("".equals(value)) {
                return null;
            }
            return Enum.valueOf(toType, value);
        }

        WaffleTypeConverter waffleTypeConverter = findConverter(toType);

        if (waffleTypeConverter != null) {
            return waffleTypeConverter.convert(propertyName, value, toType);
        }

        return OgnlOps.convertValue(value, toType);
    }

    private WaffleTypeConverter findConverter(Class toType) {
        if (cache.containsKey(toType)) { // cache hit
            return cache.get(toType);
        }

        for (WaffleTypeConverter waffleTypeConverter : waffleTypeConverters) {
            if (waffleTypeConverter.accept(toType)) {
                cache.put(toType, waffleTypeConverter);
                return waffleTypeConverter;
            }
        }

        cache.put(toType, null); // cache the null
        return null;
    }

}
