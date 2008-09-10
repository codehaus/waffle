/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import java.lang.reflect.Type;

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
     * @param type the Type a value is to be bound to
     * @return A boolean <code>true</code> is type is compatible
     */
    boolean accept(Type type);

    /**
     * Converts a String value to an Object of a given type
     * 
     * @param propertyName the associated property name, which can be <code>null</code>, also needed to present
     *                     customized error messages.
     * @param value the String value
     * @param toType  the Object Type
     * @return The converted Object
     * @throws BindException if conversion fails
     */
    Object convertValue(String propertyName, String value, Type toType);

}
