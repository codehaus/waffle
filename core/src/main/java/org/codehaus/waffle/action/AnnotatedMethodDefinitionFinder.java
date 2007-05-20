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

import org.codehaus.waffle.action.annotation.ActionMethod;
import ognl.TypeConverter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Annotation-based method definition finder. This is the default MethodDefinitionFinder used by Waffle.
 * <p/>
 * Pragmatic method calls will override what (if any) annotation is defined.
 * 
 * @author Michael Ward
 */
public class AnnotatedMethodDefinitionFinder extends AbstractMethodDefinitionFinder {

    public AnnotatedMethodDefinitionFinder(ServletContext servletContext,
                                           ArgumentResolver argumentResolver,
                                           TypeConverter typeConverter) {
        super(servletContext, argumentResolver, typeConverter);
    }

    public AnnotatedMethodDefinitionFinder(ServletContext servletContext,
                                           ArgumentResolver argumentResolver,
                                           TypeConverter typeConverter,
                                           MethodNameResolver methodNameResolver) {
        super(servletContext, argumentResolver, typeConverter, methodNameResolver);
    }

    protected List<MethodDefinition> findMethodDefinition(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  List<Method> methods) {
        List<MethodDefinition> methodDefinitions = new ArrayList<MethodDefinition>();

        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                List<Object> arguments = getArguments(method, request);
                MethodDefinition methodDefinition = validateMethod(request, response, method, arguments);

                if (methodDefinition != null) {
                    methodDefinitions.add(methodDefinition);
                }
            }
        }

        return methodDefinitions;
    }

    private List<Object> getArguments(Method method, HttpServletRequest request) {
        if (method.isAnnotationPresent(ActionMethod.class)) {
            ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
            List<String> arguments = new ArrayList<String>(actionMethod.parameters().length);

            for (String value : actionMethod.parameters()) {
                arguments.add("{" + value + "}"); // wrap in curly brackets to fit with default handling
            }

            return resolveArguments(request, arguments.iterator());
        }

        return Collections.emptyList();
    }

}
