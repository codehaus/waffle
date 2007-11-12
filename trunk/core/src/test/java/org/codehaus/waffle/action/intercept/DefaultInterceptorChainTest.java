package org.codehaus.waffle.action.intercept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.controller.ControllerDefinition;
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
        final MethodInterceptor methodInterceptor = mockery.mock(MethodInterceptor.class);
        mockery.checking(new Expectations() {{
            one (methodInterceptor).accept(method);
            will(returnValue(true));
            one(methodInterceptor).intercept(with(same(controllerDefinition)),
                    with(same(method)),
                    with(any(InterceptorChain.class)),
                    with(equal(new Object[] {argument})));
            will(returnValue("hello"));
        }});

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(methodInterceptor);

        InterceptorChain interceptorChain = new DefaultInterceptorChain(interceptors);
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

        DefaultInterceptorChain interceptorChain = new DefaultInterceptorChain(interceptors);
        assertNull(interceptorChain.proceed(controllerDefinition, method, argument));
    }

}
