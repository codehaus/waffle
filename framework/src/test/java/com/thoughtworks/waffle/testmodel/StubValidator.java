package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.action.ControllerDefinition;
import com.thoughtworks.waffle.validation.ErrorsContext;
import com.thoughtworks.waffle.validation.Validator;

public class StubValidator implements Validator {

    public void validate(ControllerDefinition controllerDefinition, ErrorsContext errorsContext) {
        throw new UnsupportedOperationException();
    }
}
