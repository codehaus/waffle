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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This implementation attempts to resolve the arguments value through the following order (returning the
 * first not null value found):
 *
 * 1. Parameter
 * 2. Request attribute
 * 3. Session attribute
 * 4. Application attribute
 *
 * else returns null
 */
public class HierarchicalArgumentResolver implements ArgumentResolver {
    private final Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
    private final ServletContext servletContext;

    public HierarchicalArgumentResolver(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Object resolve(HttpServletRequest request, String name) {
        Matcher matcher = pattern.matcher(name);

        if (matcher.matches()) {
            name = matcher.group(1);
            Object value = request.getParameter(name);

            if (value == null) {
                value = request.getAttribute(name);

                if (value == null) {
                    HttpSession session = request.getSession();

                    if (session != null) {
                        value = session.getAttribute(name);
                    }

                    if (value == null) {
                        value = servletContext.getAttribute(name);
                    }
                }
            }

            return value; // not found return null
        }

        return name; // return name as the value
    }

}
