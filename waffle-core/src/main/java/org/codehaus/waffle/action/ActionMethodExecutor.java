/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import org.codehaus.waffle.controller.ControllerDefinition;

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
     * @throws ActionMethodInvocationException
     */
    void execute(ActionMethodResponse actionMethodResponse,
                 ControllerDefinition controllerDefinition) throws ActionMethodInvocationException;
}
