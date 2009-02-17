/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.action.intercept.MethodInterceptor;

import java.util.Collection;

/**
 * Responsible for executing (aka firing) the Action method on a controller object.
 *
 * @author Michael Ward
 */
public interface ActionMethodExecutor {

    /**
     * Invoke the action method
     *
     * @param actionMethodResponse the response from the action methods invocation
     * @param controllerDefinition the current controller definition
     * @param methodInterceptors
     * @throws ActionMethodInvocationException
     */
    void execute(ActionMethodResponse actionMethodResponse,
                 ControllerDefinition controllerDefinition, Collection<MethodInterceptor> methodInterceptors) throws ActionMethodInvocationException;
}
