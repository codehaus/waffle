/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action.intercept;

import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.controller.ControllerDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>This interceptor ensure that only {@link ActionMethod} annotated methods are invokable as {@code Actions}.
 * Usage of this will help protect your application against malicious attacks.
 */
public class SecurityMethodInterceptor implements MethodInterceptor {

    /**
     * Will always return true (intercepts ALL action methods)
     *
     * {@inheritDoc}
     */
    public boolean accept(Method actionMethod) {
        return true; // intercept all!!!!
    }

    /**
     * Ensure that the action method tobe invoked is annotated with the {@link ActionMethod} annotation.  If no annotation
     * is present a {@link ActionMethodInvocationException} will be thrown.
     *
     * {@inheritDoc}
     */
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
