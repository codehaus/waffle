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
package org.codehaus.waffle.action.intercept;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.controller.ControllerDefinition;

/**
 * This interceptor ensure that only annotated methods are invokable as <i>Actions</i>.  
 * Usage of this will help protect your application against malicious attacks.
 */
public class SecurityMethodInterceptor implements MethodInterceptor {

    public boolean accept(Method actionMethod) {
        return true; // intercept all!!!!
    }

    public Object intercept(ControllerDefinition controllerDefinition,
                            Method method,
                            InterceptorChain chain,
                            Object... arguments) throws IllegalAccessException, InvocationTargetException {
        if (method.isAnnotationPresent(ActionMethod.class)) {
            return chain.proceed(controllerDefinition, method, arguments);
        }

        // Only notify that the requested action could not invoked do NOT give any detailed information (Security Risk)
        throw new ActionMethodInvocationException("Requested action method cannot be invoke remotely.");
    }

}