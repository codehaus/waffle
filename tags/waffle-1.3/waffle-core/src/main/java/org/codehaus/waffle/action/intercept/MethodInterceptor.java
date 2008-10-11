/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action.intercept;

import org.codehaus.waffle.controller.ControllerDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>A MethodInterceptor is a simple interface that allows you to intercept ActionMethods before and after
 * they have been invoked. This before and after interception provides AOP type of functionality.</p>
 */
public interface MethodInterceptor {

    /**
     * Determines if the implementation should intercept the call to the Action Method.
     *
     * @param method is the action method that is to be invoked (or intercepted)
     * @return true if this should intercept the invocation
     */
    boolean accept(Method method);

    /**
     * This method allows an ActionMethod call to be intercepted.  To continue onto the next MethodInterceptor,
     * or ActionMethod, the implementation should call
     * {@link InterceptorChain#proceed(ControllerDefinition, Method, Object[])}.
     * 
     * @param controllerDefinition the controller definition instance which owns the action method being invoked
     * @param method the actual action method to be invoked
     * @param chain is the InterceptorChain managing the method interceptors
     * @param arguments are the argument values to satisfy the action method invocation
     * @return the result from the action method's invocation, or result from this or another MethodInterceptor
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    Object intercept(ControllerDefinition controllerDefinition,
                     Method method,
                     InterceptorChain chain,
                     Object... arguments) throws IllegalAccessException, InvocationTargetException;
}
