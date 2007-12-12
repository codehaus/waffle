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

    public Registrar useInjection(Injection injectionType) {
        delegate.useInjection(injectionType);
        return this;
    }

    public boolean isRegistered(Object typeOrInstance){
        return delegate.isRegistered(typeOrInstance);
    }
    
    public Registrar register(Class<?> type, Object... parameters) {
        delegate.register(type, parameters);
        return this;
    }

    public Registrar register(Object key, Class<?> type, Object... parameters) {
        delegate.register(key, type, parameters);
        return this;
    }

    public Registrar registerInstance(Object instance) {
        delegate.registerInstance(instance);
        return this;
    }

    public Registrar registerInstance(Object key, Object instance) {
        delegate.registerInstance(key, instance);
        return this;
    }

    public Registrar registerNonCaching(Class<?> type, Object... parameters) {
        delegate.registerNonCaching(type, parameters);
        return this;
    }

    public Registrar registerNonCaching(Object key, Class<?> type, Object... parameters) {
        delegate.registerNonCaching(key, type, parameters);
        return this;
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
