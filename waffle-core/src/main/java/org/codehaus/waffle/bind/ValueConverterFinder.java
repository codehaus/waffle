/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.bind;

import java.lang.reflect.Type;

/**
 * Finder interface for {@code ValueConverters} registered per application.
 *
 * @author Mauro Talevi
 */
public interface ValueConverterFinder {

    /**
     * Should return the {@code ValueConverter} that is responsible for handling the type passed in.
     *
     * @param type the Type identifying the ValueConverter needed
     * @return the associated ValueConverter is returned or {@code null} if none was found. 
     */
    ValueConverter findConverter(Type type);

}
