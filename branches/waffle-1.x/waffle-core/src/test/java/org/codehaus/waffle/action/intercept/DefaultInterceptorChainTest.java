package org.codehaus.waffle.action.intercept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DefaultInterceptorChainTest {
    private final Mockery mockery = new Mockery();

    @Test
    public void canAcceptMethod() throws Exception {
        final ControllerDefinition controllerDefinition = new ControllerDefinition(null, null, null);
        final Method method = this.getClass().getMethods()[0];
        final Object argument = "foobar";

        // Mock MethodInterceptor
        final Object[] arguments = new Object[] {argument};
        final String returnValue = "hello";

        final MethodInterceptor methodInterceptor = mockery.mock(MethodInterceptor.class);
        mockery.checking(new Expectations() {{
            one (methodInterceptor).accept(method);
            will(returnValue(true));
            one(methodInterceptor).intercept(with(same(controllerDefinition)),
                    with(same(method)),
                    with(any(InterceptorChain.class)),
                    with(equal(arguments)));
            will(returnValue(returnValue));
        }});

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(methodInterceptor);

        // Mock ActionMonitor
        final ActionMonitor actionMonitor = mockery.mock(ActionMonitor.class);
        mockery.checking(new Expectations() {{
            one (actionMonitor).methodIntercepted(method, arguments, returnValue);
        }});
        
        InterceptorChain interceptorChain = new DefaultInterceptorChain(interceptors, actionMonitor);
        assertEquals("hello", interceptorChain.proceed(controllerDefinition, method, argument));
    }

    @Test
    public void canRefuseMethod() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition(null, null, null);
        final Method method = this.getClass().getMethods()[0];
        Object argument = "foobar";

        // Mock MethodInterceptor
        final MethodInterceptor methodInterceptor = mockery.mock(MethodInterceptor.class);
        mockery.checking(new Expectations() {{
            one (methodInterceptor).accept(method);
            will(returnValue(false));
        }});

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(methodInterceptor);

        DefaultInterceptorChain interceptorChain = new DefaultInterceptorChain(interceptors, new SilentMonitor());
        assertNull(interceptorChain.proceed(controllerDefinition, method, argument));
    }

}
