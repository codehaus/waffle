package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.Validator;

public class StubValidator implements Validator {

    public void validate(ControllerDefinition controllerDefinition, ErrorsContext errorsContext, Object controllerValidator) {
        throw new UnsupportedOperationException();
    }
}
