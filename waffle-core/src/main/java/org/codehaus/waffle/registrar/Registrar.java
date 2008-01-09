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
 * <p>Defines the available methods for registering Controllers and Components.
 * By default Constructor based dependency injection is utilized.
 * </p>
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface Registrar {

    enum Injection {
        CONSTRUCTOR, // default
        SETTER
    }
    
    /**
     * Use the given injection type for component instantiation
     * 
     * @param injection the Injection to use
     * @return The Registrar
     */
    Registrar useInjection(Injection injection);

    /**
     * Determines if a component is already registered
     * 
     * @param typeOrInstance the component Class type or Object instance/key
     * @return A boolean flag, <code>true</code> if component is registered
     */
    boolean isRegistered(Object typeOrInstance);

    /**
     * @param type represent both the key and type the object will be registered under
     * @param parameters any parameters needed to satisfy the component being registered
     */
    Registrar register(Class<?> type, Object... parameters);

    /**
     * @param key represent the key the object will be registered under
     * @param type represent the component type
     * @param parameters any parameters needed to satisfy the component being registered
     */
    Registrar register(Object key, Class<?> type, Object... parameters);

    /**
     * Register an instance directly with the current context.
     *
     * @param instance to be registered
     * @return The current Registrar which allows for chaining registration calls.
     */
    Registrar registerInstance(Object instance);

    /**
     * Register an instance directly with the current context under the given key.
     *
     * @param key the key the instance is to be registered under
     * @param instance to be registered
     * @return The current Registrar which allows for chaining registration calls.
     */
    Registrar registerInstance(Object key, Object instance);

    Registrar registerNonCaching(Class<?> type, Object... parameters);

    Registrar registerNonCaching(Object key, Class<?> type, Object... parameters);

    /**
     * Components registered in this method will be availables for the life of the Application.
     *
     * @see org.codehaus.waffle.context.ContextLevel#APPLICATION
     * @see javax.servlet.ServletContextListener
     */
    void application();

    /**
     * Components registered in this method will be availables for the life of a Users session.
     *
     * @see org.codehaus.waffle.context.ContextLevel#SESSION
     * @see javax.servlet.http.HttpSessionListener
     */
    void session();

    /**
     * Components registered in this method will be availables for the life of a request.
     *
     * @see org.codehaus.waffle.context.ContextLevel#REQUEST
     */
    void request();
    
}
