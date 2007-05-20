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

public interface MethodInterceptor {

    boolean accept(Method method);

    Object intercept(ControllerDefinition controllerDefinition,
                     Method method,
                     InterceptorChain chain,
                     Object... arguments) throws IllegalAccessException, InvocationTargetException;
}
