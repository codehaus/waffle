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
package com.thoughtworks.waffle.registrar.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import com.thoughtworks.waffle.registrar.Registrar;

/**
 * This Registrar is backed by PicoContainer for managing Dependency Injection.  This registrar
 * is passed to the custom registrar defined in the web.xml as a delegate.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class PicoRegistrar implements Registrar {
    private final MutablePicoContainer picoContainer;

    public PicoRegistrar(MutablePicoContainer picoContainer) {
        this.picoContainer = picoContainer;
    }

    public void register(Class clazz, Object ... parameters) {
        this.register(clazz, clazz, parameters);
    }

    public void register(Object key, Class clazz, Object ... parameters) {
        if (parameters.length == 0) {
            picoContainer.registerComponentImplementation(key, clazz);
        } else {
            picoContainer.registerComponentImplementation(key, clazz, picoParameters(parameters));
        }
    }

    public void registerInstance(Object instance) {
        this.registerInstance(instance, instance);
    }

    public void registerInstance(Object key, Object instance) {
        picoContainer.registerComponentInstance(key, instance);
    }

    public void registerNonCaching(Class clazz, Object ... parameters) {
        this.registerNonCaching(clazz, clazz, parameters);
    }

    public void registerNonCaching(Object key, Class clazz, Object ... parameters) {
        ConstructorInjectionComponentAdapter componentAdapter;

        if (parameters.length == 0) {
            componentAdapter = new ConstructorInjectionComponentAdapter(key, clazz);
        } else {
            componentAdapter = new ConstructorInjectionComponentAdapter(key, clazz, picoParameters(parameters));
        }

        picoContainer.registerComponent(componentAdapter);
    }

    public void registerComponentAdapter(ComponentAdapter componentAdapter) {
        picoContainer.registerComponent(componentAdapter);
    }

    private Parameter[] picoParameters(Object[] parameters) {
        Parameter[] picoParameters = new Parameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            picoParameters[i] = new ConstantParameter(parameters[i]);
        }        
        return null;
    }

}
