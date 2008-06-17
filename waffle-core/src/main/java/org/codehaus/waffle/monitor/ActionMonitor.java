/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.HierarchicalArgumentResolver.Scope;
import org.codehaus.waffle.view.View;

/**
 * A monitor for action-related events
 * 
 * @author Mauro Talevi
 */
public interface ActionMonitor extends Monitor {

    void defaultActionMethodFound(MethodDefinition methodDefinition);

    void defaultActionMethodCached(Class<?> controllerType, MethodDefinition methodDefinition);

    void pragmaticActionMethodFound(MethodDefinition methodDefinition);

    void actionMethodFound(MethodDefinition methodDefinition);

    void actionMethodExecuted(ActionMethodResponse actionMethodResponse);

    void actionMethodExecutionFailed(Exception cause);

    void argumentNameResolved(String name, Object value, Scope scope);

    void argumentNameNotMatched(String name, String pattern);

    void methodNameResolved(String methodName, String methodKey, Set<String> keys);

    void methodIntercepted(Method method, Object[] arguments, Object returnValue);

    void responseIsCommitted(HttpServletResponse response);

    void viewDispatched(View view);

}
