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

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.monitor.Monitor;

/**
 * Implementation of method name resolver which returns the value of a configurable action parameter key,
 * which defaults to 'method'.
 * <p/>
 * The resolved name is monitored along with the available parameter key set.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RequestParameterMethodNameResolver implements MethodNameResolver {
    private String methodParameterKey = "method";
    private Monitor monitor;

    public RequestParameterMethodNameResolver(Monitor monitor) {
        this.monitor = monitor;
    }

    public RequestParameterMethodNameResolver(RequestParameterMethodNameResolverConfig configuration, Monitor monitor) {
        this.methodParameterKey = configuration.getMethodParameterKey();
        this.monitor = monitor;
    }

    public String resolve(HttpServletRequest request) {
        String methodName = request.getParameter(methodParameterKey);
        monitor.methodNameResolved(methodName, methodParameterKey, request.getParameterMap().keySet());
        return methodName;
    }
}