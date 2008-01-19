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
package org.codehaus.waffle.action.intercept;

import org.codehaus.waffle.controller.ControllerDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Manages the collection of MethodInterceptors registered for use with Application</p>
 */
public interface InterceptorChain {

    /**
     * Continues on to the next MethodInterceptor or invokes the Controller's action method.
     *
     * @param controllerDefinition the controller definition instance which owns the action method being invoked
     * @param method the actual action method to be invoked
     * @param args are the argument values to satisfy the action method invocation
     * @return the result from the action method's invocation, or result from this or another MethodInterceptor
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    Object proceed(ControllerDefinition controllerDefinition,
                   Method method,
                   Object ... args) throws IllegalAccessException, InvocationTargetException ;
}
