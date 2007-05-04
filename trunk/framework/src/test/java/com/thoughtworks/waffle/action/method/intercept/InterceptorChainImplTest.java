package com.thoughtworks.waffle.action.method.intercept;

import com.thoughtworks.waffle.action.ControllerDefinition;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InterceptorChainImplTest extends MockObjectTestCase {

    public void testProceed() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition(null, null, null);
        Method method = this.getClass().getMethods()[0];
        Object argument = "foobar";

        // Mock MethodInterceptor
        Mock mockMethodInterceptor = mock(MethodInterceptor.class);
        mockMethodInterceptor.expects(once())
                .method("accept")
                .with(same(method))
                .will(returnValue(true));
        mockMethodInterceptor.expects(once())
                .method("intercept")
                .with(same(controllerDefinition), same(method), isA(InterceptorChain.class), eq(new Object[]{argument}))
                .will(returnValue("hello"));
        MethodInterceptor methodInterceptor = (MethodInterceptor) mockMethodInterceptor.proxy();

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(methodInterceptor);

        InterceptorChain interceptorChain = new InterceptorChainImpl(interceptors);
        assertEquals("hello", interceptorChain.proceed(controllerDefinition, method, argument));
    }

    public void testProceedWhenAcceptIsFalse() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition(null, null, null);
        Method method = this.getClass().getMethods()[0];
        Object argument = "foobar";

        // Mock MethodInterceptor
        Mock mockMethodInterceptor = mock(MethodInterceptor.class);
        mockMethodInterceptor.expects(once())
                .method("accept")
                .with(same(method))
                .will(returnValue(false));
        MethodInterceptor methodInterceptor = (MethodInterceptor) mockMethodInterceptor.proxy();

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(methodInterceptor);

        InterceptorChainImpl interceptorChain = new InterceptorChainImpl(interceptors);
        assertNull(interceptorChain.proceed(controllerDefinition, method, argument));
    }

}
