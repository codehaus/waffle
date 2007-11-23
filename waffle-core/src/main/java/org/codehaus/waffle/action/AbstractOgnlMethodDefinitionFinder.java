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

import static ognl.OgnlRuntime.getMethods;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletContext;

import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.monitor.ActionMonitor;

/**
 * Abstract method definition finder that uses Ognl to find methods
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractOgnlMethodDefinitionFinder extends AbstractMethodDefinitionFinder {

    public AbstractOgnlMethodDefinitionFinder(ServletContext servletContext,
                                           ArgumentResolver argumentResolver,
                                           MethodNameResolver methodNameResolver,
                                           ValueConverterFinder valueConverterFinder, 
                                           ActionMonitor actionMonitor) {
        super(servletContext, argumentResolver, methodNameResolver, valueConverterFinder, actionMonitor);
    }

    @SuppressWarnings({"unchecked"})
    protected List<Method> findMethods(Class<?> type, String methodName) {
        List<Method> methods = getMethods(type, methodName, false);
        if (methods == null) {
            throw new NoMatchingActionMethodException(methodName, type);
        }
        return methods;
    }
    
}
