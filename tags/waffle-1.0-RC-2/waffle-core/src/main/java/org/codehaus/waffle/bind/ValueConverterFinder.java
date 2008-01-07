/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.bind;

/**
 * Finder interface for <code>ValueConverter</code>s registered per application.
 *
 * @author Mauro Talevi
 */
public interface ValueConverterFinder {

    ValueConverter findConverter(Class<?> type);

}
