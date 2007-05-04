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
package com.thoughtworks.waffle.bind;

/**
 * Implementation of this interface will be responsible for converting specific type(s).
 *
 * @author Michael Ward
 */
public interface WaffleTypeConverter {

    boolean accept(Class type);

    Object convert(String propertyName, String value, Class toType) throws BindException;
}
