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

import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.action.intercept.InterceptorChain;
import org.codehaus.waffle.action.intercept.InterceptorChainImpl;
import org.codehaus.waffle.action.intercept.MethodInterceptor;
import org.codehaus.waffle.action.intercept.MethodInterceptorComparator;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Default implementation of action method executor, which uses an interceptor chain.
 * 
 * @author Michael Ward
 * @todo (mt) Rename to InterceptingActionMethodExecutor?
 */
public class DefaultActionMethodExecutor implements ActionMethodExecutor {
    private final Comparator<MethodInterceptor> comparator = new MethodInterceptorComparator();

    /**
     * If no 'action method' exists in the request parameter a View will be created with the Action's name.
     */
    public void execute(ActionMethodResponse actionMethodResponse,
                        ControllerDefinition controllerDefinition) throws MethodInvocationException {
        try {
            Object returnValue = handleInvocation(controllerDefinition);
            actionMethodResponse.setReturnValue(returnValue);
        } catch (IllegalAccessException e) {
            throw new MethodInvocationException(e.getMessage(), e); // todo (mward): lets make sure we don't reveal too much information
        } catch (InvocationTargetException e) {
            // set the cause of the exception as the return value
            actionMethodResponse.setReturnValue(e.getCause());
        }
    }

    private Object handleInvocation(ControllerDefinition controllerDefinition) throws IllegalAccessException, InvocationTargetException {
        ContextContainer container = RequestLevelContainer.get();

        List<MethodInterceptor> methodInterceptors = new ArrayList<MethodInterceptor>();
        methodInterceptors.addAll(container.getAllComponentInstancesOfType(MethodInterceptor.class));
        Collections.sort(methodInterceptors, comparator);

        methodInterceptors.add(new MethodInvokingMethodInterceptor());

        InterceptorChain chain = new InterceptorChainImpl(methodInterceptors);
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
