/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

/**
 * Implementations of this interface will allow for scripts to be registered with Waffle.
 *
 * @author Michael Ward
 */
public interface ScriptedRegistrar {

    /**
     * Register a script with Waffle
     *
     * @param key the key this script should be registred under
     * @param scriptedClassName represent the name of the scripted class being registered
     */
    void registerScript(String key, String scriptedClassName);
    
}
