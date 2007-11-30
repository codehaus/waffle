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

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.annotation.DefaultActionMethod;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.ActionMonitor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
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
 * @author Mauro Talevi
 */
public abstract class AbstractMethodDefinitionFinder implements MethodDefinitionFinder {

    private static final String ARGUMENT_FORMAT = "'{'{0}'}'";
    private static final String PRAGMA_SEPARATOR = "|";
    private static final String PRAGMA_REGEX = "\\" + PRAGMA_SEPARATOR;

    private final Map<Class, Method> defaultMethodCache = new HashMap<Class, Method>();
    private final ServletContext servletContext;
    private final ArgumentResolver argumentResolver;
    private final ValueConverterFinder valueConverterFinder;
    private final MethodNameResolver methodNameResolver;
    private final ActionMonitor actionMonitor;

    public AbstractMethodDefinitionFinder(ServletContext servletContext,
                                          ArgumentResolver argumentResolver,
                                          MethodNameResolver methodNameResolver,
                                          ValueConverterFinder valueConverterFinder,
                                          ActionMonitor actionMonitor) {
        this.servletContext = servletContext;
        this.argumentResolver = argumentResolver;
        this.valueConverterFinder = valueConverterFinder;
        this.methodNameResolver = methodNameResolver;
        this.actionMonitor = actionMonitor;
    }

    public MethodDefinition find(Object controller,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws WaffleException {
        String methodName = methodNameResolver.resolve(request);

        if (isDefaultActionMethod(methodName)) {
            return findDefaultActionMethod(controller, request);
        } else if (isPragmaticActionMethod(methodName)) { // pragmatic definition takes precedence
            return findPragmaticActionMethod(controller, methodName, request, response);
        } else {
            return findActionMethod(controller, request, response, methodName);
        }
    }

    private boolean isDefaultActionMethod(String methodName) {
        return methodName == null;
    }

    private boolean isPragmaticActionMethod(String methodName) {
        return methodName.contains(PRAGMA_SEPARATOR);
    }

    private MethodDefinition findDefaultActionMethod(Object controller, HttpServletRequest request) {
        Class controllerType = controller.getClass();

        if (defaultMethodCache.containsKey(controllerType)) { // cache hit
            MethodDefinition methodDefinition = buildDefaultMethodDefinition(defaultMethodCache.get(controllerType), request);
            actionMonitor.defaultActionMethodCached(controllerType, methodDefinition);
            return methodDefinition;
        }

        MethodDefinition methodDefinition = null;
        for (Method method : controllerType.getMethods()) {
            if (method.isAnnotationPresent(DefaultActionMethod.class)) {
                defaultMethodCache.put(controllerType, method); // add to cache
                methodDefinition = buildDefaultMethodDefinition(method, request);
                break;
            }
        }

        if (methodDefinition != null) {
            actionMonitor.defaultActionMethodFound(methodDefinition);
            return methodDefinition;
        }
        throw new NoDefaultActionMethodException(controllerType.getName());
    }

    private MethodDefinition findPragmaticActionMethod(Object controller,
                                                       String methodName,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {
        Iterator<String> iterator = Arrays.asList(methodName.split(PRAGMA_REGEX)).iterator();
        methodName = iterator.next();

        List<Method> methods = findMethods(controller.getClass(), methodName);

        List<Object> arguments = resolveArguments(request, iterator);
        MethodDefinition methodDefinition = findPragmaticMethodDefinition(request, response, methods, arguments);
        actionMonitor.pragmaticActionMethodFound(methodDefinition);
        return methodDefinition;
    }

    private MethodDefinition findActionMethod(Object controller, HttpServletRequest request, HttpServletResponse response, String methodName) {
        List<Method> methods = findMethods(controller.getClass(), methodName);

        List<MethodDefinition> methodDefinitions = findMethodDefinitions(request, response, methods);

        if (methodDefinitions.size() > 1) {
            throw new AmbiguousActionSignatureMethodException("Method: '" + methodName + "' for controller: '" + controller.getClass() + "'");
        } else if (methodDefinitions.isEmpty()) {
            throw new NoMatchingActionMethodException(methodName, controller.getClass());
        }

        MethodDefinition methodDefinition = methodDefinitions.get(0);
        actionMonitor.actionMethodFound(methodDefinition);
        return methodDefinition;
    }

    private List<MethodDefinition> findMethodDefinitions(HttpServletRequest request, HttpServletResponse response, List<Method> methods) {
        List<MethodDefinition> methodDefinitions = new ArrayList<MethodDefinition>();

        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                List<Object> arguments = getArguments(method, request);
                try {
                    methodDefinitions.add(buildMethodDefinition(request, response, method, arguments));
                } catch (NoValidActionMethodException e) {
                    // continue
                }
            }
        }

        return methodDefinitions;
    }

    private MethodDefinition buildDefaultMethodDefinition(Method method, HttpServletRequest request) {
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

    private MethodDefinition findPragmaticMethodDefinition(HttpServletRequest request, HttpServletResponse response,
                                                           List<Method> methods, List<Object> arguments) {
        List<MethodDefinition> methodDefinitions = new ArrayList<MethodDefinition>();

        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                try {
                    methodDefinitions.add(buildMethodDefinition(request, response, method, arguments));
                } catch (NoValidActionMethodException e) {
                    // continue
                }
            }
        }

        if (methodDefinitions.size() > 1) {
            String methodName = methodDefinitions.get(0).getMethod().getName();
            throw new AmbiguousActionSignatureMethodException("Method: " + methodName);
        } else if (methodDefinitions.isEmpty()) {
            // TODO - avoid null
            throw new NoMatchingActionMethodException(methods.get(0).getName(), null);
        }

        return methodDefinitions.get(0); // TODO ... should we cache the method?
    }

    private MethodDefinition buildMethodDefinition(HttpServletRequest request,
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
                } else if (MessagesContext.class.isAssignableFrom(type)) {
                    ContextContainer requestContainer = RequestLevelContainer.get();
                    MessagesContext messageContext = requestContainer.getComponentInstanceOfType(MessagesContext.class);
                    methodDefinition.addMethodArgument(messageContext);
                } else if (iterator.hasNext()) {
                    methodDefinition.addMethodArgument(iterator.next());
                } else {
                    // no valid method found
                    throw new NoValidActionMethodException(method.getName());
                }
            }

            if (hasEquivalentParameterTypes(methodDefinition)) {
                return methodDefinition;
            }
        }

        throw new NoValidActionMethodException(method.getName());
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
                Class<?> type = methodArguments.get(i).getClass();

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
        return valueConverterFinder.findConverter(type).convertValue(null, value, type);
    }

    private boolean isEmpty(String value) {
        return isDefaultActionMethod(value) || value.length() == 0;
    }

    // Protected methods, accessible by  subclasses

    /**
     * Wraps value in curly brackets to fit with default handling
     *
     * @param value the argument value
     * @return A formatted argument
     */
    protected String formatArgument(String value) {
        return MessageFormat.format(ARGUMENT_FORMAT, value);
    }

    /**
     * Resolves arguments by name
     *
     * @param request   the HttpServletRequest
     * @param arguments the List of argument names
     * @return The List of resolved argument objects
     * @see ArgumentResolver
     */
    protected List<Object> resolveArguments(HttpServletRequest request, Iterator<String> arguments) {
        List<Object> resolvedArguments = new ArrayList<Object>(10);

        while (arguments.hasNext()) {
            String name = arguments.next();
            resolvedArguments.add(argumentResolver.resolve(request, name));
        }

        return resolvedArguments;
    }

    // Abstract methods - implementable by subclasses 

    /**
     * Returns the method arguments contained in the request
     *
     * @param method  the Method
     * @param request the HttpServetRequest
     * @return the list of arguments
     */
    protected abstract List<Object> getArguments(Method method, HttpServletRequest request);

    /**
     * Returns the methods matching the type and name
     *
     * @param type       the Class in which to look for the method
     * @param methodName the method name
     * @return A List of methods
     * @throws NoMatchingActionMethodException
     *          if no methods match
     */
    protected abstract List<Method> findMethods(Class<?> type, String methodName);


}
