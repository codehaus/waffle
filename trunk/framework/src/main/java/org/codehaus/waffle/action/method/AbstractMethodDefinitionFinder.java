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

import org.codehaus.waffle.action.annotation.DefaultActionMethod;
import org.codehaus.waffle.WaffleException;
import ognl.OgnlRuntime;
import ognl.TypeConverter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Abstract base implementation for all method definition finders
 * 
 * @author Michael Ward
 * @author Paul Hammant 
 */
public abstract class AbstractMethodDefinitionFinder implements MethodDefinitionFinder {
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
        } else if (methodName.contains("|")) { // pragmatic definition takes precedence
            return handlePragmaticActionMethod(controller, methodName, request, response);
        }

        // noinspection unchecked
        List<Method> methods = OgnlRuntime.getMethods(controller.getClass(), methodName, false);

        if (methods == null) {
            throw new NoMatchingMethodException(methodName, controller.getClass());
        }

        List<MethodDefinition> methodDefinitions = findMethodDefinition(request, response, methods);

        if (methodDefinitions.size() > 1) {
            throw new AmbiguousMethodSignatureException("Method: '" + methodName + "' for controller: '" + controller.getClass() + "'");
        } else if (methodDefinitions.isEmpty()) {
            throw new NoMatchingMethodException(methodName, controller.getClass());
        }

        return methodDefinitions.get(0);
    }

    protected abstract List<MethodDefinition> findMethodDefinition(HttpServletRequest request,
                                                                   HttpServletResponse response,
                                                                   List<Method> methods);

    protected List<Object> resolveArguments(HttpServletRequest request, Iterator<String> arguments) {
        List<Object> resolvedArguments = new ArrayList<Object>(10);

        while (arguments.hasNext()) {
            String name = arguments.next();
            resolvedArguments.add(argumentResolver.resolve(request, name));
        }

        return resolvedArguments;
    }

    protected MethodDefinition validateMethod(HttpServletRequest request,
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
                    return null;
                }
            }

            if (hasEquivalentParameterTypes(methodDefinition)) {
                return methodDefinition;
            }
        }

        return null;
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
        if ("".equals(value) && type.isPrimitive()) {
            value = null; // this allows Ognl to use that primitives default value
        }

        return typeConverter.convertValue(null, null, null, null, value, type);
    }

    private MethodDefinition handlePragmaticActionMethod(Object action,
                                                         String methodName,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {
        Iterator<String> iterator = Arrays.asList(methodName.split("\\|")).iterator();
        methodName = iterator.next();

        //noinspection unchecked
        List<Method> methods = OgnlRuntime.getMethods(action.getClass(), methodName, false);

        if (methods == null) {
            throw new NoMatchingMethodException(methodName, action.getClass());
        }

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
                MethodDefinition methodDefinition = validateMethod(request, response, method, arguments);

                if (methodDefinition != null) {
                    validMethods.add(methodDefinition);
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

        return null;
    }

    private MethodDefinition buildMethodDefinitionForDefaultActionMethod(Method method, HttpServletRequest request) {
        MethodDefinition methodDefinition = new MethodDefinition(method);
        DefaultActionMethod defaultActionMethod = method.getAnnotation(DefaultActionMethod.class);
        List<String> parms = new ArrayList<String>(defaultActionMethod.parameters().length);

        for (String value : defaultActionMethod.parameters()) {
            parms.add("{" + value + "}");
        }

        // resolve argument and add to the methodDefinition
        for (Object argument : resolveArguments(request, parms.iterator())) {
            methodDefinition.addMethodArgument(argument);
        }

        return methodDefinition;
    }
}
