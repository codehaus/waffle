/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import org.codehaus.waffle.monitor.ActionMonitor;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Implementation of method name resolver which returns the value of a configurable action parameter key,
 * which defaults to 'method'.
 * </p>
 * <p>
 * The resolved name is monitored along with the available parameter key set.
 * </p>
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RequestParameterMethodNameResolver implements MethodNameResolver {
    private String methodParameterKey = "method";
    private ActionMonitor actionMonitor;

    public RequestParameterMethodNameResolver(ActionMonitor actionMonitor) {
        this.actionMonitor = actionMonitor;
    }

    public RequestParameterMethodNameResolver(RequestParameterMethodNameResolverConfig configuration, ActionMonitor actionMonitor) {
        this.methodParameterKey = configuration.getMethodParameterKey();
        this.actionMonitor = actionMonitor;
    }

    /**
     * This implementation determines the method name from the request parameters (the default parameter name
     * used is <code><b>method</b></code>).
     *
     * @param request
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public String resolve(HttpServletRequest request) {
        String methodName = request.getParameter(methodParameterKey);
        actionMonitor.methodNameResolved(methodName, methodParameterKey, request.getParameterMap().keySet());
        return methodName;
    }
}
