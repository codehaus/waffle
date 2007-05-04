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
package org.codehaus.waffle.action.method;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of method name resolver which returns the value of a configurable action parameter key,
 * which defaults to 'method'.
 * 
 * @author Michael Ward
 */
public class RequestParameterMethodNameResolver implements MethodNameResolver {
    private String actionParameterKey = "method";

    public RequestParameterMethodNameResolver() {
        // default
    }

    public RequestParameterMethodNameResolver(RequestParameterMethodNameResolverConfig configuration) {
        this.actionParameterKey = configuration.getMethodParameterKey();
    }

    public String resolve(HttpServletRequest request) {
        return request.getParameter(actionParameterKey);
    }
}
