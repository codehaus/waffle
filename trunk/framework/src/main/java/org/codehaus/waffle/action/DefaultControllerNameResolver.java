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

/**
 * Default implementations of name resolver which return the name of the last portion of the context path before the dot.
 * 
 * @author Michael Ward
 */
public class DefaultControllerNameResolver implements ControllerNameResolver {
    private static final String DOT_REGEX = "\\.";

    public DefaultControllerNameResolver() {
    }

    public String findControllerName(HttpServletRequest request) {
        String path = request.getPathInfo();

        if (path == null) {
            path = request.getRequestURI().replaceFirst(request.getContextPath(), "");
        }

        path = path.substring(1); // remove '/'
        return path.split(DOT_REGEX)[0];
    }

}
