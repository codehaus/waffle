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
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RubyAwareRegistrar;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.CachingComponentAdapter;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapterFactory;
import org.picocontainer.defaults.LifecycleStrategy;
import org.picocontainer.defaults.SetterInjectionComponentAdapterFactory;

/**
 * This Registrar is backed by PicoContainer for managing Dependency Injection.  This registrar
 * is passed to the custom registrar defined in the web.xml as a delegate.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class PicoRegistrar implements Registrar, RubyAwareRegistrar {
    private final MutablePicoContainer picoContainer;
    private final LifecycleStrategy lifecycleStrategy;
    private final RegistrarMonitor registrarMonitor;
    private Injection injection = Injection.CONSTRUCTOR;

    public PicoRegistrar(MutablePicoContainer picoContainer,
                         LifecycleStrategy lifecycleStrategy,
                         RegistrarMonitor registrarMonitor) {
        this.picoContainer = picoContainer;
        this.lifecycleStrategy = lifecycleStrategy;
        this.registrarMonitor = registrarMonitor;
    }

    public Registrar useInjection(Injection injection) {
        this.injection = injection;
        return this;
    }

    public boolean isRegistered(Object typeOrInstance) {
        if (typeOrInstance instanceof Class) {
            Class<?> type = (Class<?>) typeOrInstance;
            return picoContainer.getComponentInstancesOfType(type).size() > 0;
        }
        return picoContainer.getComponentInstance(typeOrInstance) != null;
    }

    public Registrar register(Class<?> type, Object... parameters) {
        this.register(type, type, parameters);
        return this;
    }

    public Registrar register(Object key, Class<?> type, Object... parameters) {
        ComponentAdapter componentAdapter = buildComponentAdapter(key, type, parameters);
        CachingComponentAdapter cachingComponentAdapter = new CachingComponentAdapter(componentAdapter);
        this.registerComponentAdapter(cachingComponentAdapter);
        registrarMonitor.componentRegistered(key, type, parameters);

        return this;
    }

    public Registrar registerInstance(Object instance) {
        this.registerInstance(instance, instance);
        return this;
    }

    public Registrar registerInstance(Object key, Object instance) {
        picoContainer.registerComponentInstance(key, instance);
        registrarMonitor.instanceRegistered(key, instance);
        return this;
    }

    public Registrar registerNonCaching(Class<?> type, Object... parameters) {
        this.registerNonCaching(type, type, parameters);
        return this;
    }

    public Registrar registerNonCaching(Object key, Class<?> type, Object... parameters) {
        ComponentAdapter componentAdapter = buildComponentAdapter(key, type, parameters);

        picoContainer.registerComponent(componentAdapter);
        registrarMonitor.nonCachingComponentRegistered(key, type, parameters);
        return this;
    }

    public void registerRubyScript(String key, String className) {
        ComponentAdapter componentAdapter = new RubyScriptComponentAdapter(key, className);
        CachingComponentAdapter cachingComponentAdapter = new CachingComponentAdapter(componentAdapter);
        this.registerComponentAdapter(cachingComponentAdapter);
    }

    public void registerComponentAdapter(ComponentAdapter componentAdapter) {
        picoContainer.registerComponent(componentAdapter);
    }

    private Parameter[] picoParameters(Object[] parameters) {
        if(parameters.length == 0) {
            return null; // pico expects a null when no parameters
        }

        Parameter[] picoParameters = new Parameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            picoParameters[i] = new ConstantParameter(parameters[i]);
        }
        return picoParameters;
    }

    private ComponentAdapter buildComponentAdapter(Object key, Class<?> type, Object... parameters) {
        ComponentAdapterFactory componentAdapterFactory;

        if (injection == Injection.CONSTRUCTOR) {
            componentAdapterFactory = new ConstructorInjectionComponentAdapterFactory(false, lifecycleStrategy);
        } else if (injection == Injection.SETTER) {
            componentAdapterFactory = new SetterInjectionComponentAdapterFactory(false, lifecycleStrategy);
        } else {
            throw new IllegalArgumentException("Invalid injection " + injection);
        }

        return componentAdapterFactory.createComponentAdapter(key, type, picoParameters(parameters));
    }

    public void application() {
        // does nothing!
    }

    public void session() {
        // does nothing!
    }

    public void request() {
        // does nothing!
    }

}
