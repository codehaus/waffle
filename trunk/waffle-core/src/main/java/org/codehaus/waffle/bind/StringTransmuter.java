/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import java.lang.reflect.Type;

/**
 * Not to be confused with the {@link ValueConverter} this interface is used to simplify converting (transmuting) a
 * String value into a given type.
 *
 * @author Michael Ward
 */
public interface StringTransmuter {

    /**
     * Convert (transmute) the string value into the Type requested
     *
     * @param value the String value
     * @param toType the Object Type
     * @return The converted Object
     */
    Object transmute(String value, Type toType);
}
