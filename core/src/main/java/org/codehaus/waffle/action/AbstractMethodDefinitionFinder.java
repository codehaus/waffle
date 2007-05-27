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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ognl.OgnlRuntime;
import ognl.TypeConverter;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.annotation.DefaultActionMethod;

/**
 * Abstract base implementation for all method definition finders
 * 
 * @author Michael Ward
 * @author Paul Hammant 
 * @author Mauro Talevi
 */
public abstract class AbstractMethodDefinitionFinder implements MethodDefinitionFinder {
    
    private static final String ARGUMENT_FORMAT = "'{'{0}'}'";
    private static final String PRAGMA_SEPARATOR = "|";
    private static final String PRAGMA_REGEX = "\\"+PRAGMA_SEPARATOR;
    
    private final Map<Class, Method> defaultMethodCache = new HashMap<Class, Method>();
    private final ServletContext servletContext;
    private final ArgumentResolver argumentResolver;
    private final TypeConverter typeConverter;
    private final MethodNameResolver methodNameResolver;

    public AbstractMethodDefinitionFinder(ServletContext servletContext,
                                          ArgumentResolver argumentResolver,
                                          TypeConverter typeConverter) {
        this(servletContext, argumentResolver, typeConverter, new RequestParameterMethodNameResolver());
    }

    public AbstractMethodDefinitionFinder(ServletContext servletContext,
                                          ArgumentResolver argumentResolver,
                                          TypeConverter typeConverter,
                                          MethodNameResolver methodNameResolver) {
        this.servletContext = servletContext;
        this.argumentResolver = argumentResolver;
        this.typeConverter = typeConverter;
        this.methodNameResolver = methodNameResolver;
    }

    public MethodDefinition find(Object controller,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws WaffleException {
        String methodName = methodNameResolver.resolve(request);

        if (methodName == null) {
            return findDefaultActionMethod(request, controller);
        } else if (isPragmaticActionMethod(methodName)) { // pragmatic definition takes precedence
            return handlePragmaticActionMethod(controller, methodName, request, response);
        }

        List<Method> methods = findMethods(controller.getClass(), methodName);

        List<MethodDefinition> methodDefinitions = findMethodDefinitions(request, response, methods);

        if (methodDefinitions.size() > 1) {
            throw new AmbiguousMethodSignatureException("Method: '" + methodName + "' for controller: '" + controller.getClass() + "'");
        } else if (methodDefinitions.isEmpty()) {
            throw new NoMatchingMethodException(methodName, controller.getClass());
        }

        return methodDefinitions.get(0);
    }

    /**
     * Returns the methods matching the type and name
     * 
     * @param type the Class in which to look for the method
     * @param methodName the method name
     * @return A List of methods
     * @throws NoMatchingMethodException if no methods match
     */
    private List<Method> findMethods(Class type, String methodName) {
        List<Method> methods = OgnlRuntime.getMethods(type, methodName, false);
        if (methods == null) {
            throw new NoMatchingMethodException(methodName, type);
        }
        return methods;
    }

    private List<MethodDefinition> findMethodDefinitions(HttpServletRequest request, HttpServletResponse response, List<Method> methods) {
        List<MethodDefinition> methodDefinitions = new ArrayList<MethodDefinition>();
    
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                List<Object> arguments = getArguments(method, request);
                try {
                    methodDefinitions.add(validateMethod(request, response, method, arguments));
                } catch ( InvalidMethodException e) {
                    // continue
                }                 
            }
        }
    
        return methodDefinitions;
    }
    
    protected abstract List<Object> getArguments(Method method, HttpServletRequest request);

    protected List<Object> resolveArguments(HttpServletRequest request, Iterator<String> arguments) {
        List<Object> resolvedArguments = new ArrayList<Object>(10);

        while (arguments.hasNext()) {
            String name = arguments.next();
            resolvedArguments.add(argumentResolver.resolve(request, name));
        }

        return resolvedArguments;
    }

    private MethodDefinition validateMethod(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Method method,
                                              List<Object> arguments) {
        Class<?>[] actualParameterTypes = method.getParameterTypes();
        MethodDefinition methodDefinition = new MethodDefinition(method);

        if (actualParameterTypes.length >= arguments.size()) { // still in the running
            Iterator<Object> iterator = arguments.iterator();

            for (Class<?> type : actualParameterTypes) {
                if (HttpServletRequest.class.isAssignableFrom(type)) {
                    methodDefinition.addMethodArgument(request);
                } else if (HttpServletResponse.class.isAssignableFrom(type)) {
                    methodDefinition.addMethodArgument(response);
                } else if (HttpSession.class.isAssignableFrom(type)) {
                    methodDefinition.addMethodArgument(request.getSession());
                } else if (ServletContext.class.isAssignableFrom(type)) {
                    methodDefinition.addMethodArgument(servletContext);
                } else if (iterator.hasNext()) {
                    methodDefinition.addMethodArgument(iterator.next());
                } else {
                    // not valid
                    throw new InvalidMethodException(method.getName());
                }
            }

            if (hasEquivalentParameterTypes(methodDefinition)) {
                return methodDefinition;
            }
        }

        throw new InvalidMethodException(method.getName());
    }

    /**
     * Wraps value in curly brackets to fit with default handling
     * @param value the argument value
     * @return A formatted argument
     */
    protected String formatArgument(String value) {
        return MessageFormat.format(ARGUMENT_FORMAT,value);
    }

    private boolean hasEquivalentParameterTypes(MethodDefinition methodDefinition) {
        Class[] methodParameterTypes = methodDefinition.getMethod().getParameterTypes();
        List<Object> methodArguments = methodDefinition.getMethodArguments();

        if (methodParameterTypes.length != methodArguments.size()) {
            return false; // different signatures lengths
        }

        for (int i = 0; i < methodParameterTypes.length; i++) {
            Class methodParameterType = methodParameterTypes[i];

            // the types must be assignable to be considered a valid method (assume true if actualParameterType is null)
            if (methodArguments.get(i) != null) {
                Class<? extends Object> type = methodArguments.get(i).getClass();

                if (!methodParameterType.isAssignableFrom(type)) {
                    if (String.class.equals(type)) {
                        try {
                            // Can the String be converted to the parameter type? If so convert it...
                            String value = (String) methodArguments.get(i);
                            methodArguments.set(i, convertValue(value, methodParameterType));
                        } catch (Exception e) {
                            return false; // Can't convert String value
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private Object convertValue(String value, Class type) {
        if (isEmpty(value) && type.isPrimitive()) {
            value = null; // this allows Ognl to use that primitives default value
        }

        return typeConverter.convertValue(null, null, null, null, value, type);
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    private boolean isPragmaticActionMethod(String methodName) {
        return methodName.contains(PRAGMA_SEPARATOR);
    }

    private MethodDefinition handlePragmaticActionMethod(Object action,
                                                         String methodName,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {
        Iterator<String> iterator = Arrays.asList(methodName.split(PRAGMA_REGEX)).iterator();
        methodName = iterator.next();

        List<Method> methods = findMethods(action.getClass(), methodName);

        List<Object> arguments = resolveArguments(request, iterator);
        return findMethodDefinitionForPragmatic(request, response, methods, arguments);
    }

    private MethodDefinition findMethodDefinitionForPragmatic(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              List<Method> methods,
                                                              List<Object> arguments) {
        List<MethodDefinition> validMethods = new ArrayList<MethodDefinition>();

        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                try {
                    validMethods.add(validateMethod(request, response, method, arguments));
                } catch ( InvalidMethodException e) {
                    // continue
                }                 
            }
        }

        if (validMethods.size() > 1) {
            String methodName = validMethods.get(0).getMethod().getName();
            throw new AmbiguousMethodSignatureException("Method: " + methodName);
        } else if (validMethods.isEmpty()) {
            throw new NoMatchingMethodException(methods.get(0).getName(), null); // TODO - not null
        }

        return validMethods.get(0); // todo ... should we cache the method?
    }

    private MethodDefinition findDefaultActionMethod(HttpServletRequest request, Object action) {
        Class clazz = action.getClass();

        if (defaultMethodCache.containsKey(clazz)) { // cache hit
            return buildMethodDefinitionForDefaultActionMethod(defaultMethodCache.get(clazz), request);
        }

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(DefaultActionMethod.class)) {
                defaultMethodCache.put(clazz, method); // add to cache
                return buildMethodDefinitionForDefaultActionMethod(method, request);
            }
        }

        throw new NoDefaultMethodException(clazz.getName());
    }

    private MethodDefinition buildMethodDefinitionForDefaultActionMethod(Method method, HttpServletRequest request) {
        MethodDefinition methodDefinition = new MethodDefinition(method);
        DefaultActionMethod defaultActionMethod = method.getAnnotation(DefaultActionMethod.class);
        List<String> arguments = new ArrayList<String>(defaultActionMethod.parameters().length);

        for (String value : defaultActionMethod.parameters()) {
            arguments.add(formatArgument(value));
        }

        // resolve argument and add to the methodDefinition
        for (Object argument : resolveArguments(request, arguments.iterator())) {
            methodDefinition.addMethodArgument(argument);
        }

        return methodDefinition;
    }

}
