/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.validation;

import org.codehaus.waffle.controller.ControllerDefinition;

public interface Validator {

    void validate(ControllerDefinition controllerDefinition, ErrorsContext errorsContext);

}
