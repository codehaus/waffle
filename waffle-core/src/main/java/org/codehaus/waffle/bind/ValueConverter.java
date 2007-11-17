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
 * Implementation of this interface will be responsible for converting values of specific type(s).
 *
 * @author Michael Ward
 */
public interface ValueConverter {

    boolean accept(Class<?> type);

    Object convertValue(String propertyName, String value, Class<?> toType) throws BindException;

}
