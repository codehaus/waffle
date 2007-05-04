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
package com.thoughtworks.waffle.action.method.intercept;

import com.thoughtworks.waffle.action.ControllerDefinition;
import com.thoughtworks.waffle.action.method.MethodInvocationException;
import com.thoughtworks.waffle.action.method.annotation.ActionMethod;
import com.thoughtworks.waffle.action.method.annotation.DefaultActionMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This interceptor ensure that only annotated methods are invokable as <i>Actions</i>.  Usage of this will help protect
 * your application against malicious attacks.
 */
public class SecurityMethodInterceptor implements MethodInterceptor {

    public boolean accept(Method actionMethod) {
        return true; // intercept all!!!!
    }

    public Object intercept(ControllerDefinition controllerDefinition,
                            Method method,
                            InterceptorChain chain,
                            Object... arguments) throws IllegalAccessException, InvocationTargetException {
        if (method.isAnnotationPresent(DefaultActionMethod.class)) {
            return chain.proceed(controllerDefinition, method, arguments);
        } else if (method.isAnnotationPresent(ActionMethod.class)) {
            return chain.proceed(controllerDefinition, method, arguments);
        }

        // Only notify that the requested action could not invoked do NOT give any detailed information (Security Risk)
        throw new MethodInvocationException("Requested action method cannot be invoke remotely.");
    }

}
