/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarException;
import org.codehaus.waffle.registrar.ScriptedRegistrar;
import static org.picocontainer.Characteristics.CACHE;
import static org.picocontainer.Characteristics.NO_CACHE;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.InjectionFactory;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.SetterInjection;

import java.util.List;
import java.util.Properties;

/**
 * This Registrar is backed by PicoContainer for managing Dependency Injection.  This registrar
 * is passed to the custom registrar defined in the web.xml as a delegate.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class PicoRegistrar implements ScriptedRegistrar {
    private final MutablePicoContainer picoContainer;
    private final ParameterResolver parameterResolver;
    private final LifecycleStrategy lifecycleStrategy;
    private final RegistrarMonitor registrarMonitor;
    private final ComponentMonitor componentMonitor;
    private Injection injection = Injection.CONSTRUCTOR;

    public PicoRegistrar(MutablePicoContainer picoContainer,
                         ParameterResolver parameterResolver,
                         LifecycleStrategy lifecycleStrategy,
                         RegistrarMonitor registrarMonitor,
                         ComponentMonitor componentMonitor) {
        this.picoContainer = picoContainer;
        this.parameterResolver = parameterResolver;
        this.lifecycleStrategy = lifecycleStrategy;
        this.registrarMonitor = registrarMonitor;
        this.componentMonitor = componentMonitor;
    }

    public Registrar useInjection(Injection injection) {
        this.injection = injection;
        return this;
    }

    public boolean isRegistered(Object typeOrInstance) {
        if (typeOrInstance instanceof Class) {
            Class<?> type = (Class<?>) typeOrInstance;
            List<?> objects = picoContainer.getComponents(type);
            return objects.size() > 0;
        }
        return picoContainer.getComponent(typeOrInstance) != null;
    }

    public Object getRegistered(Object typeOrInstance) {
        if (typeOrInstance instanceof Class) {
            Class<?> type = (Class<?>) typeOrInstance;
            List<?> instances = picoContainer.getComponents(type);
            if ( instances.size() == 0 ){
                throw new RegistrarException("No component instance registered for type "+type);
            } else if ( instances.size() > 1 ){
                throw new RegistrarException("More than one component instance registered for type "+type);
            } else {
                return instances.get(0);
            }            
        }
        Object instance = picoContainer.getComponent(typeOrInstance);
        if ( instance == null ){
            throw new RegistrarException("No component instance registered for type or instance "+typeOrInstance);
        }
        return instance;
    }

    public Registrar register(Class<?> type, Object... parameters) {
        this.register(type, type, parameters);
        return this;
    }

    public Registrar register(Object key, Class<?> type, Object... parameters) {
        ComponentAdapter componentAdapter = buildComponentAdapter(key, type, parameters);
        registerCachedComponentAdapter(componentAdapter);
        registrarMonitor.componentRegistered(key, type, parameters);
        return this;
    }

    public Registrar registerInstance(Object instance) {
        this.registerInstance(instance, instance);
        return this;
    }

    public Registrar registerInstance(Object key, Object instance) {
        picoContainer.addComponent(key, instance);
        registrarMonitor.instanceRegistered(key, instance);
        return this;
    }

    public Registrar registerNonCaching(Class<?> type, Object... parameters) {
        this.registerNonCaching(type, type, parameters);
        return this;
    }

    public Registrar registerNonCaching(Object key, Class<?> type, Object... parameters) {
        ComponentAdapter componentAdapter = buildComponentAdapter(key, type, parameters);

        registerUnCachedComponentAdapter(componentAdapter);
        registrarMonitor.nonCachingComponentRegistered(key, type, parameters);
        return this;
    }

    public void registerUnCachedComponentAdapter(ComponentAdapter componentAdapter) {
        picoContainer.as(NO_CACHE).addAdapter(componentAdapter);
    }

    public void registerCachedComponentAdapter(ComponentAdapter componentAdapter) {
        picoContainer.as(CACHE).addAdapter(componentAdapter);
    }

    private Parameter[] picoParameters(Object[] parameters) {
        if (parameters.length == 0) {
            return null; // pico expects a null when no parameters
        }

        Parameter[] picoParameters = new Parameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            picoParameters[i] = parameterResolver.resolve(parameters[i]);
        }
        return picoParameters;
    }

    private ComponentAdapter buildComponentAdapter(Object key, Class<?> type, Object... parameters) {
        InjectionFactory componentAdapterFactory;

        if (injection == Injection.CONSTRUCTOR) {
            componentAdapterFactory = new ConstructorInjection();
        } else if (injection == Injection.SETTER) {
            componentAdapterFactory = new SetterInjection();
        } else {
            throw new IllegalArgumentException("Invalid injection " + injection);
        }

        return componentAdapterFactory.createComponentAdapter(componentMonitor, lifecycleStrategy, new Properties(), key, type, picoParameters(parameters));
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

    public void registerScript(String key, String scriptedClassName) {
        // does nothing!
    }

}
