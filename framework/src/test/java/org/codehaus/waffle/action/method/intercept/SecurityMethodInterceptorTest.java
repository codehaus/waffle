package org.codehaus.waffle.action.method.intercept;

import org.codehaus.waffle.action.ControllerDefinition;
import org.codehaus.waffle.action.method.MethodInvocationException;
import org.codehaus.waffle.action.method.annotation.ActionMethod;
import org.codehaus.waffle.action.method.annotation.DefaultActionMethod;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.lang.reflect.Method;
import java.util.List;

public class SecurityMethodInterceptorTest extends MockObjectTestCase {

    public void testAccept() {
        SecurityMethodInterceptor methodInterceptor = new SecurityMethodInterceptor();

        for (Method method : List.class.getMethods()) {
            assertTrue(methodInterceptor.accept(method));
        }
    }

    public void testInterceptMethod() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition("foo", new MyController(), null);

        // Mock InterceptorChain
        Mock mockChain = mock(InterceptorChain.class);
        mockChain.expects(once())
                .method("proceed");
        InterceptorChain chain = (InterceptorChain) mockChain.proxy();

        Method method = MyController.class.getMethod("methodWithActionMethod");
        MethodInterceptor interceptor = new SecurityMethodInterceptor();
        interceptor.intercept(controllerDefinition, method, chain);
    }

    public void testInterceptDefaultActionMethod() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition("foo", new MyController(), null);

        // Mock InterceptorChain
        Mock mockChain = mock(InterceptorChain.class);
        mockChain.expects(once())
                .method("proceed");
        InterceptorChain chain = (InterceptorChain) mockChain.proxy();

        Method method = MyController.class.getMethod("methodWithDefaultActionMethod");
        MethodInterceptor interceptor = new SecurityMethodInterceptor();
        interceptor.intercept(controllerDefinition, method, chain);
    }
    
    public void testInterceptThrowsMethodInvocationException() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition("foo", new MyController(), null);

        // Mock InterceptorChain
        Mock mockChain = mock(InterceptorChain.class);
        mockChain.expects(never())
                .method("proceed");
        InterceptorChain chain = (InterceptorChain) mockChain.proxy();

        Method method = MyController.class.getMethod("methodWithoutAnnotation");
        MethodInterceptor interceptor = new SecurityMethodInterceptor();
        try {
            interceptor.intercept(controllerDefinition, method, chain);
            fail("MethodInvocationException expected");
        } catch (MethodInvocationException expected) {
            // expected
        }
    }

    public class MyController {

        @DefaultActionMethod
        public void methodWithDefaultActionMethod() {

        }

        @ActionMethod
        public void methodWithActionMethod() {

        }

        public void methodWithoutAnnotation() {

        }
    }
}
