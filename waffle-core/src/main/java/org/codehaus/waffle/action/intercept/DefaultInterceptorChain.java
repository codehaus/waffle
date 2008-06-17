/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action.intercept;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.monitor.ActionMonitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * <p>This is Waffle's default implementation of the {@link org.codehaus.waffle.action.intercept.InterceptorChain}
 * which iterates over each {@link org.codehaus.waffle.action.intercept.MethodInterceptor} registered with Waffle.
 * Each method interceptor will have an opportunity to intercept the ActionMethod o be invoked.
 * </p>
 */
public class DefaultInterceptorChain implements InterceptorChain {
    private final Iterator<MethodInterceptor> iterator;
    private final ActionMonitor actionMonitor;
    private Object returnValue;

    public DefaultInterceptorChain(List<MethodInterceptor> interceptors, ActionMonitor actionMonitor) {
        this.iterator = interceptors.iterator();
        this.actionMonitor = actionMonitor;
    }

    /**
     * {@inheritDoc}
     */
    public Object proceed(ControllerDefinition controllerDefinition,
                          Method method,
                          Object... arguments) throws IllegalAccessException, InvocationTargetException {
        if (iterator.hasNext()) {
            MethodInterceptor methodInterceptor = iterator.next();
            if (methodInterceptor.accept(method)) {
                returnValue = methodInterceptor.intercept(controllerDefinition, method, this, arguments);
                actionMonitor.methodIntercepted(method, arguments, returnValue);
            } else {
                return proceed(controllerDefinition, method, arguments); // recursive
            }
        }

        return returnValue;
    }
}
