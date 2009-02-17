/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ActionMonitor;

/**
 * <p>
 * Annotation-based method definition finder. This is the default default definition finder used by Waffle.
 * </p>
 * <p>
 * <b>Note</b>: Pragmatic method calls always take precedence over other types.
 * </p>
 * 
 * @author Michael Ward
 */
public class AnnotatedMethodDefinitionFinder extends AbstractOgnlMethodDefinitionFinder {

    public AnnotatedMethodDefinitionFinder(ServletContext servletContext, ArgumentResolver argumentResolver,
            MethodNameResolver methodNameResolver, StringTransmuter stringTransmuter, ActionMonitor actionMonitor,
            MessageResources messageResources) {
        super(servletContext, argumentResolver, methodNameResolver, stringTransmuter, actionMonitor, messageResources);
    }

    /**
     * Inspects the method's {@code ActionMethod} annotation to determine the parameter names to use to resolve the
     * argument values.
     * 
     * @param method the action method to be invoked
     * @param request the HttpServetRequest
     * @return the resolved list of arguments needed to satisfy the action method invocation
     */
    protected List<Object> getArguments(Method method, HttpServletRequest request) {
        if (method.isAnnotationPresent(ActionMethod.class)) {
            ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
            List<String> arguments = new ArrayList<String>(actionMethod.parameters().length);

            for (String value : actionMethod.parameters()) {
                arguments.add(formatArgument(value));
            }

            return resolveArguments(request, arguments.iterator());
        }

        return Collections.emptyList();
    }

}
