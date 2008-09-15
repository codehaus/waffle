/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action.intercept;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;

/**
 * <p>
 * This interceptor ensure that only {@link ActionMethod} annotated methods are invokable as {@code Actions}. Usage of
 * this will help protect your application against malicious attacks.
 * </p>
 * 
 * @author Micheal Ward
 */
public class SecurityMethodInterceptor implements MethodInterceptor {

    private final MessageResources messageResources;

    public SecurityMethodInterceptor() {
        this(new DefaultMessageResources());
    }

    public SecurityMethodInterceptor(MessageResources messageResources) {
        this.messageResources = messageResources;
    }

    /**
     * Will always return true (intercepts ALL action methods) {@inheritDoc}
     */
    public boolean accept(Method actionMethod) {
        return true; // intercept all!!!!
    }

    /**
     * <p>
     * Ensure that the action method to be invoked is annotated with the {@link ActionMethod} annotation. If no
     * annotation is present a {@link ActionMethodInvocationException} will be thrown.
     * </p>
     * <p>
     * {@inheritDoc}
     * </p>
     */
    public Object intercept(ControllerDefinition controllerDefinition, Method method, InterceptorChain chain,
            Object... arguments) throws IllegalAccessException, InvocationTargetException {
        if (method.isAnnotationPresent(ActionMethod.class)) {
            return chain.proceed(controllerDefinition, method, arguments);
        }

        // Only notify that the requested action could not invoked do NOT give any detailed information (Security Risk)
        String message = messageResources.getMessageWithDefault("actionMethodCannotBeInvoked",
                "Action method cannot be invoked");
        throw new ActionMethodInvocationException(message);
    }

}
