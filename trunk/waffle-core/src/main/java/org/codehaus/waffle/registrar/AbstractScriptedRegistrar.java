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
 * A Registrar that provides registration methods for scripts.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractScriptedRegistrar extends AbstractRegistrar implements ScriptedRegistrar {
    private final ScriptedRegistrar scriptedRegistrar;

    public AbstractScriptedRegistrar(Registrar delegate) {
        super(delegate);
        scriptedRegistrar = (ScriptedRegistrar)delegate;
    }

    public void registerScript(String key, String className) {
        scriptedRegistrar.registerScript(key, className);
    }
}
