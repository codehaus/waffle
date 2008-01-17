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
package org.codehaus.waffle.registrar;

/**
 * A Registrar that provides registration methods for Ruby scripts.
 *
 * @author Michael Ward
 */
public abstract class AbstractRubyAwareRegistrar extends AbstractRegistrar implements RubyAwareRegistrar {
    private final RubyAwareRegistrar rubyAwareRegistrar;

    public AbstractRubyAwareRegistrar(Registrar delegate) {
        super(delegate);
        rubyAwareRegistrar = (RubyAwareRegistrar)delegate;
    }

    public void registerRubyScript(String key, String className) {
        rubyAwareRegistrar.registerRubyScript(key, className);
    }
}
