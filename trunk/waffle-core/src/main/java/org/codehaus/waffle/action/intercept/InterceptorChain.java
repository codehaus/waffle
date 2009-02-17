/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
