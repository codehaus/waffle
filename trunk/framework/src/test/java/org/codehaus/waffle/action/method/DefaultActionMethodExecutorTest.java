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

import org.codehaus.waffle.action.ControllerDefinition;
import org.codehaus.waffle.context.PicoContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.testmodel.FakeController;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.jmock.MockObjectTestCase;

import java.lang.reflect.Method;

public class DefaultActionMethodExecutorTest extends MockObjectTestCase {
    private ActionMethodExecutor actionMethodExecutor = new DefaultActionMethodExecutor();

    protected void setUp() throws Exception {
        RequestLevelContainer.set(new PicoContextContainer(new DefaultPicoContainer()));
    }

    protected void tearDown() throws Exception {
        RequestLevelContainer.set(null);
    }

    public void testMethodWithNoArgsFiredOnController() throws Exception {
        FakeController fakeController = new FakeController();

        MethodDefinition methodDefinition = new MethodDefinition(FakeController.class.getMethod("sayHello"));
        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

        assertNull(actionMethodResponse.getReturnValue());
        assertEquals("hello", fakeController.getName());
    }

    public void testFireWithArguments() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("foobar");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

        assertNull(actionMethodResponse.getReturnValue());
        assertEquals("foobar", fakeController.getName());
    }

    public void testFireWhenArgumentValuesIsNull() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument(null);

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

        assertNull(actionMethodResponse.getReturnValue());
        assertNull(fakeController.getName());
    }

    public void testFireMethodWithReturnType() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("passThruMethod", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("mmmWaffles");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);
        assertEquals("mmmWaffles", actionMethodResponse.getReturnValue());
    }

    public void testFireWrapsInvocationTargetExceptionInMethodResponse() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("methodThrowsException", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("mmmWaffles");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();

        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);
        Exception rootCause = (Exception) actionMethodResponse.getReturnValue();
        assertEquals("mmmWaffles", rootCause.getMessage());
    }

}
