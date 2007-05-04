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
package com.thoughtworks.waffle.registrar;

import com.thoughtworks.waffle.WaffleException;
import com.thoughtworks.waffle.context.ContextLevel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the Registrar defined in the applications web.xml and executes the method(s) annotated
 * according to the ContextLevel being handled.
 *
 * @author Michael Ward
 */
public class RegistrarAssistant {
    private final Set<Method> applicationMethods = new HashSet<Method>();
    private final Set<Method> sessionMethods = new HashSet<Method>();
    private final Set<Method> requestMethods = new HashSet<Method>();
    private final Class registrarClass;
    private final Constructor constructor;

    public RegistrarAssistant(Class registrarClass) {
        try {
            this.registrarClass = registrarClass;
            this.constructor = registrarClass.getConstructor(Registrar.class);

            findRegistrationMethods();
        } catch (NoSuchMethodException e) {
            throw new WaffleException(e);
        }
    }

    /**
     * This is only done once
     */
    private void findRegistrationMethods() {
        Method[] methods = registrarClass.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RegisterWithApplication.class)) {
                applicationMethods.add(method);
            } else if (method.isAnnotationPresent(RegisterWithSession.class)) {
                sessionMethods.add(method);
            } else if (method.isAnnotationPresent(RegisterWithRequest.class)) {
                requestMethods.add(method);
            }
        }
    }

    public void executeDelegatingRegistrar(Registrar delegateRegistrar, ContextLevel contextLevel) throws WaffleException {
        try {
            Registrar registrar = (Registrar) constructor.newInstance(delegateRegistrar);

            if (ContextLevel.APPLICATION.equals(contextLevel)) {
                execute(applicationMethods, registrar);
            } else if (ContextLevel.SESSION.equals(contextLevel)) {
                execute(sessionMethods, registrar);
            } else if (ContextLevel.REQUEST.equals(contextLevel)) {
                execute(requestMethods, registrar);
            }
        } catch (Exception e) {
            throw new WaffleException("Unable to use registrar [" + registrarClass + "]", e);
        }
    }

    private void execute(Set<Method> methods, Registrar registrar) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {            
            method.invoke(registrar);
        }
    }

}
