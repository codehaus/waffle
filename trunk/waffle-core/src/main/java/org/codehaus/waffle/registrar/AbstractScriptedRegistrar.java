/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import org.codehaus.waffle.WaffleException;

/**
 * A Registrar that provides registration methods for scripts.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractScriptedRegistrar extends AbstractRegistrar implements ScriptedRegistrar {
    private final ScriptedRegistrar scriptedRegistrar;

    public AbstractScriptedRegistrar(Registrar delegate) {
        super(delegate);
        if ( delegate instanceof ScriptedRegistrar ){
            scriptedRegistrar = (ScriptedRegistrar) delegate;            
        } else {
            throw new WaffleException("Delegate is not a ScriptedRegistrar: "+delegate);
        }
    }

    public void registerScript(String key, String className) {
        scriptedRegistrar.registerScript(key, className);
    }
}
