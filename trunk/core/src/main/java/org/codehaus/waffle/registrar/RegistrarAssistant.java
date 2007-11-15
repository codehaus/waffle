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

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextLevel;

import java.lang.reflect.Constructor;

/**
 * Manages the Registrar defined in the applications web.xml and executes the method(s) annotated
 * according to the ContextLevel being handled.
 *
 * @author Michael Ward
 */
public class RegistrarAssistant {
    private final Class<?> registrarClass;
    private final Constructor<?> constructor;

    public RegistrarAssistant(Class<?> registrarClass) {
        try {
            this.registrarClass = registrarClass;
            this.constructor = registrarClass.getConstructor(Registrar.class);
        } catch (NoSuchMethodException e) {
            throw new WaffleException(e);
        }
    }

    public void executeDelegatingRegistrar(Registrar delegateRegistrar, ContextLevel contextLevel) throws WaffleException {
        try {
            Registrar registrar = (Registrar) constructor.newInstance(delegateRegistrar);

            if (ContextLevel.APPLICATION.equals(contextLevel)) {
                registrar.application();
            } else if (ContextLevel.SESSION.equals(contextLevel)) {
                registrar.session();
            } else if (ContextLevel.REQUEST.equals(contextLevel)) {
                registrar.request();
            }
        } catch (Exception e) {
            throw new WaffleException("Unable to use registrar [" + registrarClass + "]", e);
        }
    }

}
