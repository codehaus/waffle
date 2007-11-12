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

import org.codehaus.waffle.controller.ControllerDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class DefaultInterceptorChain implements InterceptorChain {
    private final Iterator<MethodInterceptor> iterator;
    private Object returnValue;

    public DefaultInterceptorChain(List<MethodInterceptor> interceptors) {
        this.iterator = interceptors.iterator();
    }

    public Object proceed(ControllerDefinition controllerDefinition,
                          Method method,
                          Object... arguments) throws IllegalAccessException, InvocationTargetException {
        if (iterator.hasNext()) {
            MethodInterceptor methodInterceptor = iterator.next();
            if (methodInterceptor.accept(method)) {
                returnValue = methodInterceptor.intercept(controllerDefinition, method, this, arguments);
            } else {
                return proceed(controllerDefinition, method, arguments); // recursive
            }
        }

        return returnValue;
    }
}
