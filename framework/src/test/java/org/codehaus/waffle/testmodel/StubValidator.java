package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.ControllerDefinition;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.Validator;

public class StubValidator implements Validator {

    public void validate(ControllerDefinition controllerDefinition, ErrorsContext errorsContext) {
        throw new UnsupportedOperationException();
    }
}
