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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.monitor.ActionMonitor;

/**
 * Annotation-based method definition finder.
 * This is the default default definition finder used by Waffle.
 * <p/>
 * <b>Note</b>: Pragmatic method calls will always take precedence.
 * 
 * @author Michael Ward
 */
public class AnnotatedMethodDefinitionFinder extends AbstractOgnlMethodDefinitionFinder {

    public AnnotatedMethodDefinitionFinder(ServletContext servletContext,
                                           ArgumentResolver argumentResolver,
                                           MethodNameResolver methodNameResolver,
                                           ValueConverterFinder valueConverterFinder, 
                                           ActionMonitor actionMonitor) {
        super(servletContext, argumentResolver, methodNameResolver, valueConverterFinder, actionMonitor);
    }

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
