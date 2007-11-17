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
package org.codehaus.waffle.registrar;

/**
 * Defines the available methods for registering Controllers and Components.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface Registrar {
    
    void register(Class<?> type, Object... parameters);

    void register(Object key, Class<?> type, Object... parameters);

    void registerInstance(Object instance);

    void registerInstance(Object key, Object instance);

    void registerNonCaching(Class<?> type, Object... parameters);

    void registerNonCaching(Object key, Class<?> type, Object... parameters);

    void application();

    void session();

    void request();
    
}
