/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
