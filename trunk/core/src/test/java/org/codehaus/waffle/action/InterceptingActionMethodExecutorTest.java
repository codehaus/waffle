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
package org.codehaus.waffle.action;

import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.context.pico.PicoContextContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.testmodel.FakeController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.lang.reflect.Method;

public class InterceptingActionMethodExecutorTest {
    private ActionMethodExecutor actionMethodExecutor = new InterceptingActionMethodExecutor();

    @Before
    public void setUp() throws Exception {
        RequestLevelContainer.set(new PicoContextContainer(new DefaultPicoContainer()));
    }

    @After
    public void tearDown() throws Exception {
        RequestLevelContainer.set(null);
    }

    @Test
    public void executeShouldInvokeNoArgumentActionMethod() throws Exception {
        FakeController fakeController = new FakeController();

        MethodDefinition methodDefinition = new MethodDefinition(FakeController.class.getMethod("sayHello"));
        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

        Assert.assertNull(actionMethodResponse.getReturnValue());
        Assert.assertEquals("hello", fakeController.getName());
    }

    @Test
    public void executeShouldInvokeActionMethodWithArgumentValue() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("foobar");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

        Assert.assertNull(actionMethodResponse.getReturnValue());
        Assert.assertEquals("foobar", fakeController.getName());
    }

    @Test
    public void executeShouldHandleNullArgumentValues() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument(null);

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

        Assert.assertNull(actionMethodResponse.getReturnValue());
        Assert.assertNull(fakeController.getName());
    }

    @Test
    public void executeShouldReturnValueFromActionMethod() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("passThruMethod", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("mmmWaffles");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);
        Assert.assertEquals("mmmWaffles", actionMethodResponse.getReturnValue());
    }

    @Test
    public void executeShouldWrapCauseOfInvocationTargetExceptionAsActionMethodInvocationException() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("methodThrowsException", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("mmmWaffles");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();

        try {
            actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);
        } catch (ActionMethodInvocationException e) {
            Throwable rootCause = e.getCause();
            Assert.assertEquals("mmmWaffles", rootCause.getMessage());
        }
    }

    @Test
    public void executeShouldReturnOriginalExceptionIfTypeIsActionMethodInvocationException() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("actionThrowsActionMethodInvocationException", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("BEARS!");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();

        try {
            actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);
        } catch (ActionMethodInvocationException e) {
            Assert.assertEquals("BEARS!", e.getMessage());
        }
    }

    @Test
    public void shouldSetActionResponseValueToExceptionIfTypeIsActionMethodException() throws NoSuchMethodException {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("actionThrowsActionMethodException");
        MethodDefinition methodDefinition = new MethodDefinition(method);

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();

        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

        Assert.assertTrue(actionMethodResponse.getReturnValue() instanceof ActionMethodException);
    }

}
