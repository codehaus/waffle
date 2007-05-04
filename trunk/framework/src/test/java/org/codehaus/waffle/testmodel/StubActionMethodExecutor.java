package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.ControllerDefinition;
import org.codehaus.waffle.action.method.ActionMethodExecutor;
import org.codehaus.waffle.action.method.ActionMethodResponse;
import org.codehaus.waffle.action.method.MethodInvocationException;

/**
 * NOTE: This class cannot be an inner class because it is registered to Pico in a test and therefor
 * the test would would also have to be registered to pico.
 */
public class StubActionMethodExecutor implements ActionMethodExecutor {

    public void execute(ActionMethodResponse actionMethodResponse, ControllerDefinition controllerDefinition) throws MethodInvocationException {
    }
}
