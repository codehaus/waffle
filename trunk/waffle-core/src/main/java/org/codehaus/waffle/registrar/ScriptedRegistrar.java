/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
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
