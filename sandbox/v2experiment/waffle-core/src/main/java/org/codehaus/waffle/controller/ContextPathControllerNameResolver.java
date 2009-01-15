/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
