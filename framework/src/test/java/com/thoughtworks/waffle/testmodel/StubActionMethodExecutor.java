package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.action.ControllerDefinition;
import com.thoughtworks.waffle.action.method.ActionMethodExecutor;
import com.thoughtworks.waffle.action.method.ActionMethodResponse;
import com.thoughtworks.waffle.action.method.MethodInvocationException;

/**
 * NOTE: This class cannot be an inner class because it is registered to Pico in a test and therefor
 * the test would would also have to be registered to pico.
 */
public class StubActionMethodExecutor implements ActionMethodExecutor {

    public void execute(ActionMethodResponse actionMethodResponse, ControllerDefinition controllerDefinition) throws MethodInvocationException {
    }
}
