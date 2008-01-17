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
 * Implementation of this interface will be responsible for converting String values to the specific type.  These are
 * registered with Waffle through the {@code web.xml}.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface ValueConverter {

    /**
     * Determines if converter is compatible with the given type
     * 
     * @param type the type of the field a value is to be bound to
     * @return A boolean <code>true</code> is type is compatible
     */
    boolean accept(Class<?> type);

    /**
     * Converts a String value to an Object of a given type
     * 
     * @param propertyName the associated property name, which can be <code>null</code>, also needed to present
     *                     customized error messages.
     * @param value the String value
     * @param toType  the Object type
     * @return The converted Object
     * @throws BindException if conversion fails
     */
    <T> T convertValue(String propertyName, String value, Class<T> toType);

}
