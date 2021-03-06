/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Collection;

import org.codehaus.waffle.action.intercept.DefaultInterceptorChain;
import org.codehaus.waffle.action.intercept.InterceptorChain;
import org.codehaus.waffle.action.intercept.MethodInterceptor;
import org.codehaus.waffle.action.intercept.MethodInterceptorComparator;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.monitor.ActionMonitor;

/**
 * Implementation of action method executor, which uses an interceptor chain.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class InterceptingActionMethodExecutor implements ActionMethodExecutor {
   
    private final Comparator<MethodInterceptor> comparator = new MethodInterceptorComparator();
    private final ActionMonitor actionMonitor;

    public InterceptingActionMethodExecutor(ActionMonitor actionMonitor) {
        this.actionMonitor = actionMonitor;
    }

    /**
     * If no 'action method' exists in the request parameter a View will be created with the Action's name.
     */
    public void execute(ActionMethodResponse actionMethodResponse,
                        ControllerDefinition controllerDefinition,
                        Collection<MethodInterceptor> methodInterceptors) throws ActionMethodInvocationException {
        try {
            Object returnValue = handleInvocation(controllerDefinition, methodInterceptors);
            actionMethodResponse.setReturnValue(returnValue);
            actionMonitor.actionMethodExecuted(actionMethodResponse);
        } catch (IllegalAccessException e) {
            throw new ActionMethodInvocationException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause instanceof ActionMethodException) {
                // ActionMethodExceptions will be processed by ActionMethodResponseHandlers
                actionMethodResponse.setReturnValue(cause);
            } else if (cause instanceof ActionMethodInvocationException) {
                // If cause is ActionMethodInvocationException it should be re-thrown
                throw (ActionMethodInvocationException) cause;
            } else {
                throw new ActionMethodInvocationException(cause.getMessage(), cause);
            }
        }
    }

    private Object handleInvocation(ControllerDefinition controllerDefinition, Collection<MethodInterceptor> methodInterceptors) throws IllegalAccessException, InvocationTargetException {

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.addAll(methodInterceptors);
        Collections.sort(interceptors, comparator);

        interceptors.add(new MethodInvokingMethodInterceptor());

        InterceptorChain chain = new DefaultInterceptorChain(interceptors, actionMonitor);
        MethodDefinition methodDefinition = controllerDefinition.getMethodDefinition();
        Method method = methodDefinition.getMethod();
        List<Object> methodArguments = methodDefinition.getMethodArguments();
        return chain.proceed(controllerDefinition, method, methodArguments.toArray());
    }

    /**
     * This actually invokes the underlying action method
     */
    private static class MethodInvokingMethodInterceptor implements MethodInterceptor {
        public boolean accept(Method method) {
            return true;
        }

        public Object intercept(ControllerDefinition controllerDefinition,
                                Method method,
                                InterceptorChain chain,
                                Object... arguments) throws IllegalAccessException, InvocationTargetException {
            return method.invoke(controllerDefinition.getController(), arguments);
        }
    }

}
