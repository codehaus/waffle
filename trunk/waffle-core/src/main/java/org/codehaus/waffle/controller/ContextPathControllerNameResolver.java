/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.controller;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.monitor.ControllerMonitor;

/**
 * Default implementations of name resolver which return the name of the last portion of the context path before the dot.
 * 
 * @author Michael Ward
 */
public class ContextPathControllerNameResolver implements ControllerNameResolver {
    private static final String DOT_REGEX = "\\.";
    private final ControllerMonitor controllerMonitor;

    public ContextPathControllerNameResolver(ControllerMonitor controllerMonitor) {
        this.controllerMonitor = controllerMonitor;
    }

    public String findControllerName(HttpServletRequest request) {
        String path = request.getPathInfo();

        if (path == null) {
            path = request.getRequestURI().replaceFirst(request.getContextPath(), "");            
        }

        path = path.substring(1); // remove '/'
        String name = path.split(DOT_REGEX)[0];
        controllerMonitor.controllerNameResolved(name, path);
        return name;
    }

}
