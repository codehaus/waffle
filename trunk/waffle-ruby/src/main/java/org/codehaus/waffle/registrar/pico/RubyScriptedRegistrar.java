/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.registrar.ScriptedRegistrar;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Cached;

/**
 * Pico-based ScriptedRegistrar that allows to register Ruby scripts
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RubyScriptedRegistrar extends PicoRegistrar implements ScriptedRegistrar {
    public RubyScriptedRegistrar(MutablePicoContainer picoContainer, ParameterResolver parameterResolver,
            LifecycleStrategy lifecycleStrategy, RegistrarMonitor registrarMonitor, ComponentMonitor componentMonitor) {
        super(picoContainer, parameterResolver, lifecycleStrategy, registrarMonitor, componentMonitor);
    }

    public void registerScript(String key, String className) {
        this.registerComponentAdapter(new Cached(new RubyScriptComponentAdapter(key, className)));
    }


}