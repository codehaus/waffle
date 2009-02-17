/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.ArrayList;

import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.context.pico.PicoContextContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.FakeController;
import org.codehaus.waffle.action.intercept.MethodInterceptor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InterceptingActionMethodExecutorTest {

    private ActionMethodExecutor actionMethodExecutor = new InterceptingActionMethodExecutor(new SilentMonitor());

    @Before
    public void setUp() throws Exception {
        RequestLevelContainer.set(new PicoContextContainer());
    }

    @After
    public void tearDown() throws Exception {
        RequestLevelContainer.set(null);
    }

    @Test
    public void canInvokeNoArgumentActionMethod() throws Exception {
        FakeController fakeController = new FakeController();

        MethodDefinition methodDefinition = new MethodDefinition(FakeController.class.getMethod("sayHello"));
        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, new ArrayList<MethodInterceptor>());

        Assert.assertNull(actionMethodResponse.getReturnValue());
        Assert.assertEquals("hello", fakeController.getName());
    }

    @Test
    public void canInvokeActionMethodWithArgumentValue() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("foobar");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, new ArrayList<MethodInterceptor>());

        assertNull(actionMethodResponse.getReturnValue());
        assertEquals("foobar", fakeController.getName());
    }

    @Test
    public void canHandleNullArgumentValues() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument(null);

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, new ArrayList<MethodInterceptor>());

        assertNull(actionMethodResponse.getReturnValue());
        assertNull(fakeController.getName());
    }

    @Test
    public void canReturnValueFromActionMethod() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("passThruMethod", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("mmmWaffles");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, new ArrayList<MethodInterceptor>());
        assertEquals("mmmWaffles", actionMethodResponse.getReturnValue());
    }

    @Test
    public void canWrapCauseOfInvocationTargetExceptionAsActionMethodInvocationException() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("methodThrowsException", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("mmmWaffles");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();

        try {
            actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, new ArrayList<MethodInterceptor>());
        } catch (ActionMethodInvocationException e) {
            Throwable rootCause = e.getCause();
            assertEquals("mmmWaffles", rootCause.getMessage());
        }
    }

    @Test
    public void canReturnOriginalExceptionIfTypeIsActionMethodInvocationException() throws Exception {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("actionThrowsActionMethodInvocationException", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("BEARS!");

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();

        try {
            actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, new ArrayList<MethodInterceptor>());
        } catch (ActionMethodInvocationException e) {
            assertEquals("BEARS!", e.getMessage());
        }
    }

    @Test
    public void canSetActionResponseValueToExceptionIfTypeIsActionMethodException() throws NoSuchMethodException {
        FakeController fakeController = new FakeController();
        Method method = FakeController.class.getMethod("actionThrowsActionMethodException");
        MethodDefinition methodDefinition = new MethodDefinition(method);

        ControllerDefinition controllerDefinition = new ControllerDefinition("FakeController", fakeController, methodDefinition);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();

        actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, new ArrayList<MethodInterceptor>());

        assertTrue(actionMethodResponse.getReturnValue() instanceof ActionMethodException);
    }

}
