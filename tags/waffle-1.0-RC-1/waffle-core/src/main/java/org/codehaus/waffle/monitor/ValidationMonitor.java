/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.monitor;

import org.codehaus.waffle.controller.ControllerDefinition;

/**
 * A monitor for validation-related events
 * 
 * @author Mauro Talevi
 */
public interface ValidationMonitor extends Monitor {

    void controllerValidatorNotFound(String controllerValidatorName, String controllerName);

    void methodDefinitionNotFound(ControllerDefinition controllerDefinition);

    void validationFailed(Exception cause);

}
