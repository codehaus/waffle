/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import javax.servlet.ServletContext;

import org.codehaus.waffle.ComponentRegistry;

/**
 * Waffle requires that web apps extend this class for registrating actions, services and components
 * for use in their applications.
 *
 * @author Michael Ward
 * @author Mauro Talevi
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
    
    public Object getRegistered(Object typeOrInstance){
        return delegate.getRegistered(typeOrInstance);
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

    /**
     * Returns the component registry registered by the WaffleContextListener in the servlet context.
     * 
     * @return The ComponentRegistry
     */
    protected ComponentRegistry getComponentRegistry() {
        ServletContext servletContext = (ServletContext) getRegistered(ServletContext.class);
        return (ComponentRegistry) servletContext.getAttribute(ComponentRegistry.class.getName());
    }
}
