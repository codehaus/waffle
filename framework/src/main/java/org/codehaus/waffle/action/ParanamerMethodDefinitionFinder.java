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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ognl.TypeConverter;

import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;

/**
 * Pananamer-based method definition finder.
 * 
 * @author Paul Hammant 
 */
public class ParanamerMethodDefinitionFinder extends AbstractMethodDefinitionFinder {
    private final Paranamer paranamer = new CachingParanamer();

    public ParanamerMethodDefinitionFinder(ServletContext servletContext,
                                           ArgumentResolver argumentResolver,
                                           TypeConverter typeConverter) {
        super(servletContext, argumentResolver, typeConverter);
    }

    public ParanamerMethodDefinitionFinder(ServletContext servletContext,
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
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = null;
        
        try {
            parameterNames = findParameterNames(method);            
        } catch ( ParameterNamesNotFoundException e ){
            Class<?> declaringClass = method.getDeclaringClass();
            int rc = paranamer.areParameterNamesAvailable(declaringClass.getClassLoader(), declaringClass, method.getName());
            if (rc == Paranamer.NO_PARAMETER_NAMES_LIST ) {
                throw new MatchingMethodException("No parameter names list found by paranamer "+paranamer);
            } else if (rc == Paranamer.NO_PARAMETER_NAMES_FOR_CLASS ) {
                throw new MatchingMethodException("No parameter names found for class '" + declaringClass.getName() + "' by paranamer "+paranamer);
            } else if (rc == Paranamer.NO_PARAMETER_NAMES_FOR_CLASS_AND_MEMBER) {
                throw new MatchingMethodException("No parameter names found for class '" + declaringClass.getName() + "' and method '" + method.getName()+ "' by paranamer "+paranamer);
            } else if (rc == Paranamer.PARAMETER_NAMES_FOUND ){
                throw new MatchingMethodException("Invalid parameter names list for paranamer "+paranamer);
            }
        }
        List<String> arguments = new ArrayList<String>(parameterNames.length);

        // these should always be of the same length
        assert parameterTypes.length == parameterNames.length;

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];

            if (HttpServletRequest.class.isAssignableFrom(parameterType)
                    || HttpServletResponse.class.isAssignableFrom(parameterType)
                    || HttpSession.class.isAssignableFrom(parameterType)
                    || ServletContext.class.isAssignableFrom(parameterType)) {
                // do nothing
            } else {
                arguments.add("{" + parameterNames[i] + "}");
            }
        }

        return resolveArguments(request, arguments.iterator());
    }

    private String[] findParameterNames(Method method) {
        return paranamer.lookupParameterNames(method);
    }

}
