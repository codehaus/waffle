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
package org.codehaus.waffle.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.monitor.ActionMonitor;

/**
 * Hierarchical implementation attempts to resolve the arguments value through the 
 * following ordered scoped (returning the first not null value found):
 * 
 * <ol>
 *  <li>1. PARAMETER</li>
 *  <li>2. REQUEST attribute</li>
 *  <li>3. SESSION attribute</li>
 *  <li>4. APPLICATION attribute</li>
 * </ol>
 * 
 * If none are found, returns <code>null</code>
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class HierarchicalArgumentResolver implements ArgumentResolver {

    public enum Scope {
        PARAMETER, REQUEST, SESSION, APPLICATION
    }

    private final Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
    private final ServletContext servletContext;
    private final ActionMonitor actionMonitor;

    public HierarchicalArgumentResolver(ServletContext servletContext, ActionMonitor actionMonitor) {
        this.servletContext = servletContext;
        this.actionMonitor = actionMonitor;
    }

    public Object resolve(HttpServletRequest request, String name) {
        Matcher matcher = pattern.matcher(name);

        if (matcher.matches()) {
            name = matcher.group(1);
            Object value = request.getParameter(name);
            Scope scope = Scope.PARAMETER;

            if (value == null) {
                value = request.getAttribute(name);
                scope = Scope.REQUEST;
                if (value == null) {
                    HttpSession session = request.getSession();

                    if (session != null) {
                        value = session.getAttribute(name);
                        scope = Scope.SESSION;
                    }

                    if (value == null) {
                        value = servletContext.getAttribute(name);
                        scope = Scope.APPLICATION;
                    }
                }
            }
            actionMonitor.argumentNameResolved(name, value, scope);
            return value; // return value, could be null
        }

        actionMonitor.argumentNameNotMatched(name, pattern.pattern());
        return name; // return name as the value
    }

}
