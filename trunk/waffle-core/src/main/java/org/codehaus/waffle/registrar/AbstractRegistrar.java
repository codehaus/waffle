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
 * Waffle requires that web apps extend this class for registrating actions, services and components
 * for use in their applications.
 *
 * @author Michael Ward
 */
public abstract class AbstractRegistrar implements Registrar {
    private final Registrar delegate;

    public AbstractRegistrar(Registrar delegate) {
        this.delegate = delegate;
    }

    public void register(Class<?> type, Object... parameters) {
        delegate.register(type, parameters);
    }

    public void register(Object key, Class<?> type, Object... parameters) {
        delegate.register(key, type, parameters);
    }

    public void registerInstance(Object instance) {
        delegate.registerInstance(instance);
    }

    public void registerInstance(Object key, Object instance) {
        delegate.registerInstance(key, instance);
    }

    public void registerNonCaching(Class<?> type, Object... parameters) {
        delegate.registerNonCaching(type, parameters);
    }

    public void registerNonCaching(Object key, Class<?> type, Object... parameters) {
        delegate.registerNonCaching(key, type, parameters);
    }

    public void application() {
        // does nothing by default
    }

    public void session() {
        // does nothing by default
    }

    public void request() {
        // does nothing by default
    }
}
