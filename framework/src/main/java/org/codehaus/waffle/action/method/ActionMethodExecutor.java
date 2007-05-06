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
package org.codehaus.waffle.action.method;

import org.codehaus.waffle.controller.ControllerDefinition;

/**
 * Responsible for executing (aka firing) the Action method on a controller object.
 *
 * @author Michael Ward
 */
public interface ActionMethodExecutor {

    void execute(ActionMethodResponse actionMethodResponse, ControllerDefinition controllerDefinition) throws MethodInvocationException;
}
