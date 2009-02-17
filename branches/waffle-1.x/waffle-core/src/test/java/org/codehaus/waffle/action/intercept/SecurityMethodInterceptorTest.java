package org.codehaus.waffle.action.intercept;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class SecurityMethodInterceptorTest {

    private Mockery mockery = new Mockery();
    
    @Test
    public void canAccept() {
        SecurityMethodInterceptor methodInterceptor = new SecurityMethodInterceptor();

        for (Method method : List.class.getMethods()) {
            assertTrue(methodInterceptor.accept(method));
        }
    }

    @Test
    public void canInterceptMethod() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition("foo", new MyController(), null);

        // Mock InterceptorChain
        final InterceptorChain chain = mockery.mock(InterceptorChain.class);
        mockery.checking(new Expectations() {
            {
                one(chain).proceed(with(any(ControllerDefinition.class)), with(any(Method.class)), with(any(Object[].class)));
            }
        });

        Method method = MyController.class.getMethod("methodWithActionMethod");
        MethodInterceptor interceptor = new SecurityMethodInterceptor();
        interceptor.intercept(controllerDefinition, method, chain);
    }

    @Test
    public void canInterceptDefaultActionMethod() throws Exception {
        
        ControllerDefinition controllerDefinition = new ControllerDefinition("foo", new MyController(), null);
        
        // Mock InterceptorChain
        final InterceptorChain chain = mockery.mock(InterceptorChain.class);
        mockery.checking(new Expectations() {
            {
                one(chain).proceed(with(any(ControllerDefinition.class)), with(any(Method.class)), with(any(Object[].class)));
            }
        });

        Method method = MyController.class.getMethod("methodWithDefaultActionMethod");
        MethodInterceptor interceptor = new SecurityMethodInterceptor();
        interceptor.intercept(controllerDefinition, method, chain);
    }
    
    @Test(expected=ActionMethodInvocationException.class)
    public void cannotInterceptMethodInvocation() throws Exception {
        ControllerDefinition controllerDefinition = new ControllerDefinition("foo", new MyController(), null);

        // Mock InterceptorChain
        final InterceptorChain chain = mockery.mock(InterceptorChain.class);
        mockery.checking(new Expectations() {
            {
                never(chain).proceed(with(any(ControllerDefinition.class)), with(any(Method.class)), with(any(Object[].class)));
            }
        });

        Method method = MyController.class.getMethod("methodWithoutAnnotation");
        MethodInterceptor interceptor = new SecurityMethodInterceptor();
        interceptor.intercept(controllerDefinition, method, chain);
    }

    public class MyController {

        @ActionMethod(asDefault=true)
        public void methodWithDefaultActionMethod() {

        }

        @ActionMethod
        public void methodWithActionMethod() {

        }

        public void methodWithoutAnnotation() {

        }
    }
}
