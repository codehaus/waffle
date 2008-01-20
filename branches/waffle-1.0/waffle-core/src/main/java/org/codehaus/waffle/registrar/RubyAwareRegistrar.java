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
 * Implementations of this interface will allow for Ruby scripts to be registred with Waffle.
 *
 * @author Michael Ward
 */
public interface RubyAwareRegistrar {

    /**
     * Register a ruby script with Waffle
     *
     * @param key the name this script should be registred under
     * @param className represent the name of the Ruby class being registred
     */
    void registerRubyScript(String key, String className);
    
}
