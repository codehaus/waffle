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
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;

/**
 * This Registrar is backed by PicoContainer for managing Dependency Injection.  This registrar
 * is passed to the custom registrar defined in the web.xml as a delegate.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class PicoRegistrar implements Registrar, RubyAwareRegistrar {
    private final MutablePicoContainer picoContainer;
    private final RegistrarMonitor registrarMonitor;

    public PicoRegistrar(MutablePicoContainer picoContainer, RegistrarMonitor registrarMonitor) {
        this.picoContainer = picoContainer;
        this.registrarMonitor = registrarMonitor;
    }

    public void register(Class<?> type, Object ... parameters) {
        this.register(type, type, parameters);
    }

    public void register(Object key, Class<?> type, Object ... parameters) {
        if (parameters.length == 0) {
            picoContainer.registerComponentImplementation(key, type);
        } else {
            picoContainer.registerComponentImplementation(key, type, picoParameters(parameters));
        }
        registrarMonitor.componentRegistered(key, type, parameters);
    }

    public void registerInstance(Object instance) {
        this.registerInstance(instance, instance);
    }

    public void registerInstance(Object key, Object instance) {
        picoContainer.registerComponentInstance(key, instance);
        registrarMonitor.instanceRegistered(key, instance);
    }

    public void registerNonCaching(Class<?> type, Object ... parameters) {
        this.registerNonCaching(type, type, parameters);
    }

    public void registerNonCaching(Object key, Class<?> type, Object ... parameters) {
        ConstructorInjectionComponentAdapter componentAdapter;

        if (parameters.length == 0) {
            componentAdapter = new ConstructorInjectionComponentAdapter(key, type);
        } else {
            componentAdapter = new ConstructorInjectionComponentAdapter(key, type, picoParameters(parameters));
        }

        picoContainer.registerComponent(componentAdapter);
        registrarMonitor.nonCachingComponentRegistered(key, type, parameters);
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
        Parameter[] picoParameters = new Parameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            picoParameters[i] = new ConstantParameter(parameters[i]);
        }        
        return picoParameters;
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
