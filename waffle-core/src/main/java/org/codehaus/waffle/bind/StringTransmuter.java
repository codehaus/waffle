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
     * @param toType the Object type
     * @return The converted Object
     */
    <T> T transmute(String value, Class<T> toType);
}
