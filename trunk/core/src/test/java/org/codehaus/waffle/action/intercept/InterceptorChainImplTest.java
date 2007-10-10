package org.codehaus.waffle.action.intercept;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RunWith(JMock.class)
public class InterceptorChainImplTest {
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void interceptorAcceptsMethod() throws Exception {
        final ControllerDefinition controllerDefinition = new ControllerDefinition(null, null, null);
        final Method method = this.getClass().getMethods()[0];
        final Object argument = "foobar";

        // Mock MethodInterceptor
        final MethodInterceptor methodInterceptor = context.mock(MethodInterceptor.class);
        context.checking(new Expectations() {{
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

        InterceptorChain interceptorChain = new InterceptorChainImpl(interceptors);
        Assert.assertEquals("hello", interceptorChain.proceed(controllerDefinition, method, argument));
    }

    @Test
    public void interceptorDoesNotAcceptMethod() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition(null, null, null);
        final Method method = this.getClass().getMethods()[0];
        Object argument = "foobar";

        // Mock MethodInterceptor
        final MethodInterceptor methodInterceptor = context.mock(MethodInterceptor.class);
        context.checking(new Expectations() {{
            one (methodInterceptor).accept(method);
            will(returnValue(false));
        }});

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(methodInterceptor);

        InterceptorChainImpl interceptorChain = new InterceptorChainImpl(interceptors);
        Assert.assertNull(interceptorChain.proceed(controllerDefinition, method, argument));
    }

}
