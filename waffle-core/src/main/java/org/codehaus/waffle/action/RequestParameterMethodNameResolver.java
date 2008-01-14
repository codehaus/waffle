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

import org.codehaus.waffle.monitor.ActionMonitor;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Implementation of method name resolver which returns the value of a configurable action parameter key,
 * which defaults to 'method'.
 * </p><br/>
 * The resolved name is monitored along with the available parameter key set.
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
