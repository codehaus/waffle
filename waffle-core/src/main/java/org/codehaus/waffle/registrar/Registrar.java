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
 * <p>
 * Defines the available methods for registering components (including controllers).
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
     * Use the given injection type for component instantiation. 
     * Defaults to {@link Injection.CONSTRUCTOR}.
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
     * Registers a component in the current context.
     * 
     * @param type represents both the key and type the object will be registered under
     * @param parameters any parameters needed to satisfy the component being registered
     * @return The current Registrar which allows for chaining registration calls.
     */
    Registrar register(Class<?> type, Object... parameters);

    /**
     * Registers a component in the current context under the given key.
     * 
     * @param key represents the key the object will be registered under
     * @param type represents the component type
     * @param parameters any parameters needed to satisfy the component being registered
     * @return The current Registrar which allows for chaining registration calls.
     */
    Registrar register(Object key, Class<?> type, Object... parameters);

    /**
     * Registers a component instance directly in the current context.
     * 
     * @param instance to be registered
     * @return The current Registrar which allows for chaining registration calls.
     */
    Registrar registerInstance(Object instance);

    /**
     * Registers a component instance directly in the current context under the given key.
     * 
     * @param key the key the instance is to be registered under
     * @param instance to be registered
     * @return The current Registrar which allows for chaining registration calls.
     */
    Registrar registerInstance(Object key, Object instance);

    /**
     * Registers a component in non-caching mode, ie with new instance created for each class with a defined dependency
     * 
     * @param type represents both the key and type the object will be registered under
     * @param parameters any parameters needed to satisfy the component being registered
     * @return The current Registrar which allows for chaining registration calls.
     */
    Registrar registerNonCaching(Class<?> type, Object... parameters);

    /**
     * Registers a component under the given key in non-caching mode, ie with new instance created for each class with a defined dependency
     * 
     * @param key represents the key the object will be registered under
     * @param type represents the component type
     * @param parameters any parameters needed to satisfy the component being registered
     * @return The current Registrar which allows for chaining registration calls.
     */
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
