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
