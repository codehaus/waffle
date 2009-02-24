/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import static java.text.MessageFormat.format;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.ActionMonitor;

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

    private final Map<Class<?>, Method> defaultMethodCache = new HashMap<Class<?>, Method>();
    private final ServletContext servletContext;
    private final ArgumentResolver argumentResolver;
    private final StringTransmuter stringTransmuter;
    private final MethodNameResolver methodNameResolver;
    private final ActionMonitor actionMonitor;
    protected final MessageResources messageResources;
    private static final ArrayList NO_ARGS = new ArrayList();

    public AbstractMethodDefinitionFinder(ServletContext servletContext, ArgumentResolver argumentResolver,
            MethodNameResolver methodNameResolver, StringTransmuter stringTransmuter, ActionMonitor actionMonitor,
            MessageResources messageResources) {
        this.servletContext = servletContext;
        this.argumentResolver = argumentResolver;
        this.stringTransmuter = stringTransmuter;
        this.methodNameResolver = methodNameResolver;
        this.actionMonitor = actionMonitor;
        this.messageResources = messageResources;
    }

    public MethodDefinition find(Object controller, HttpServletRequest request, HttpServletResponse response, MessagesContext messageContext)
            throws WaffleException {
        String methodName = methodNameResolver.resolve(request);

        if (methodName == null) {
            return findDefaultActionMethod(controller, request);
        } else if (isPragmaticActionMethod(methodName)) { // pragmatic definition takes precedence
            return findPragmaticActionMethod(controller, methodName, request, response, messageContext);
        } else {
            return findActionMethod(controller, request, response, methodName, messageContext);
        }
    }

    private boolean isPragmaticActionMethod(String methodName) {
        return methodName.contains(PRAGMA_SEPARATOR);
    }

    private MethodDefinition findDefaultActionMethod(Object controller, HttpServletRequest request) {
        Class<?> controllerType = controller.getClass();

        if (defaultMethodCache.containsKey(controllerType)) { // cache hit
            MethodDefinition methodDefinition = buildDefaultMethodDefinition(defaultMethodCache.get(controllerType),
                    request);
            actionMonitor.defaultActionMethodCached(controllerType, methodDefinition);
            return methodDefinition;
        }

        for (Method method : controllerType.getMethods()) {
            if (isDefaultActionMethod(method)) {
                defaultMethodCache.put(controllerType, method); // add to cache
                MethodDefinition methodDefinition = buildDefaultMethodDefinition(method, request);
                actionMonitor.defaultActionMethodFound(methodDefinition);
                return methodDefinition;
            }
        }

        throw new NoDefaultActionMethodException(controllerType.getName());
    }

    private boolean isDefaultActionMethod(Method method) {
        ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
        return actionMethod != null && actionMethod.asDefault();
    }

    private MethodDefinition findPragmaticActionMethod(Object controller, String methodName,
                                                       HttpServletRequest request, HttpServletResponse response, MessagesContext messageContext) {
        Iterator<String> iterator = Arrays.asList(methodName.split(PRAGMA_REGEX)).iterator();
        methodName = iterator.next();

        List<Method> methods = findMethods(controller.getClass(), methodName);

        List<Object> arguments = resolveArguments(request, iterator);
        MethodDefinition methodDefinition = findPragmaticMethodDefinition(request, response, methods, arguments, messageContext);
        actionMonitor.pragmaticActionMethodFound(methodDefinition);
        return methodDefinition;
    }

    private MethodDefinition findActionMethod(Object controller, HttpServletRequest request,
                                              HttpServletResponse response, String methodName, MessagesContext messageContext) {
        List<Method> methods = findMethods(controller.getClass(), methodName);


        List<MethodDefinition> methodDefinitions = findMethodDefinitions(request, response, methods, messageContext);

        if (methodDefinitions.size() > 1) {
            String message = messageResources.getMessageWithDefault("ambiguousActionMethodSignature",
                    "ActionMethod ''{0}'' has ambiguous signature among methods ''{1}''", methodName, methods);
            throw new AmbiguousActionMethodSignatureException(message);
        } else if (methodDefinitions.isEmpty()) {
            String message = messageResources.getMessageWithDefault("noMatchingMethodFound",
                    "No matching methods for name ''{0}''", methodName);
            throw new NoMatchingActionMethodException(message, controller.getClass());
        }

        MethodDefinition methodDefinition = methodDefinitions.get(0);
        actionMonitor.actionMethodFound(methodDefinition);
        return methodDefinition;
    }

    private List<MethodDefinition> findMethodDefinitions(HttpServletRequest request, HttpServletResponse response,
                                                         List<Method> methods, MessagesContext messageContext) {
        List<MethodDefinition> methodDefinitions = new ArrayList<MethodDefinition>();

        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                List<Object> arguments;
                if (method.getParameterTypes().length == 0) {
                    // methods with no args, need not invoke Paranamer..
                    arguments = NO_ARGS;
                } else {
                    arguments = getArguments(method, request);
                }
                try {
                    methodDefinitions.add(buildMethodDefinition(request, response, method, arguments, messageContext));
                } catch (NoValidActionMethodException e) {
                    // continue
                }
            }
        }

        return methodDefinitions;
    }

    private MethodDefinition buildDefaultMethodDefinition(Method method, HttpServletRequest request) {
        MethodDefinition methodDefinition = new MethodDefinition(method);
        if (!isDefaultActionMethod(method)) {
            String message = messageResources.getMessageWithDefault("noDefaultActionMethod", 
                    "Method ''{0}'' is not annotated with @ActionMethod(asDefault=true)", method);
            throw new NoDefaultActionMethodException(message);
        }
        ActionMethod defaultActionMethod = method.getAnnotation(ActionMethod.class);
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
                                                           List<Method> methods, List<Object> arguments, MessagesContext messageContext) {
        List<MethodDefinition> methodDefinitions = new ArrayList<MethodDefinition>();

        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                try {
                    methodDefinitions.add(buildMethodDefinition(request, response, method, arguments, messageContext));
                } catch (NoValidActionMethodException e) {
                    // continue
                }
            }
        }

        if (methodDefinitions.size() > 1) {
            String methodName = methodDefinitions.get(0).getMethod().getName();
            String message = messageResources.getMessageWithDefault("ambiguousActionMethodSignature",
                    "ActionMethod ''{0}'' has ambiguous signature among methods ''{1}''", methodName, methods);
            throw new AmbiguousActionMethodSignatureException(message);
        } else if (methodDefinitions.isEmpty()) {
            String message = messageResources.getMessageWithDefault("noMatchingMethodFound",
                    "No matching methods for name ''{0}''", methods.get(0).getName());
            throw new NoMatchingActionMethodException(message, null);
        }

        return methodDefinitions.get(0); // TODO ... should we cache the method?
    }

    private MethodDefinition buildMethodDefinition(HttpServletRequest request, HttpServletResponse response,
                                                   Method method, List<Object> arguments,
                                                   MessagesContext messageContext) {
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
                    methodDefinition.addMethodArgument(messageContext);
                } else if (iterator.hasNext()) {
                    methodDefinition.addMethodArgument(iterator.next());
                } else {
                    // no valid method found
                    throw new NoValidActionMethodException(method.getName());
                }
            }

            if (hasEquivalentParameterTypes(methodDefinition, stringTransmuter)) {
                return methodDefinition;
            }
        }

        throw new NoValidActionMethodException(method.getName());
    }

    // TODO mward ------ Why is StringTransmuter being passed?
    protected boolean hasEquivalentParameterTypes(MethodDefinition methodDefinition, StringTransmuter stringTransmuter) {
        Type[] methodParameterTypes = methodDefinition.getMethod().getGenericParameterTypes();
        List<Object> methodArguments = methodDefinition.getMethodArguments();

        if (methodParameterTypes.length != methodArguments.size()) {
            return false; // different signatures lengths
        }

        for (int i = 0; i < methodParameterTypes.length; i++) {
            Type methodParameterType = methodParameterTypes[i];

            // the types must be assignable to be considered a valid method (assume true if actualParameterType is null)
            if (methodArguments.get(i) != null) {
                Class<?> methodArgumentType = methodArguments.get(i).getClass();

                if (!isAssignableFrom(methodParameterType, methodArgumentType)) {
                    if (String.class.equals(methodArgumentType)) {
                        try {
                            // Can the String be converted to the parameter type? If so convert it...
                            String value = (String) methodArguments.get(i);
                            methodArguments.set(i, stringTransmuter.transmute(value, methodParameterType));
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

    private boolean isAssignableFrom(Type methodParameterType, Class<?> methodArgumentType) {
        if (methodParameterType instanceof Class) {
            return ((Class<?>) methodParameterType).isAssignableFrom(methodArgumentType);
        } else if (methodParameterType instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) methodParameterType).getRawType();
            return ((Class<?>) rawType).isAssignableFrom(methodArgumentType);
        }
        return false;
    }

    // Protected methods, accessible by subclasses

    /**
     * Wraps value in curly brackets to fit with default handling
     * 
     * @param value the argument value
     * @return A formatted argument
     */
    protected String formatArgument(String value) {
        return format(ARGUMENT_FORMAT, value);
    }

    /**
     * Resolves arguments by name
     * 
     * @param request the HttpServletRequest
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
     * Returns the resolved list of method arguments.
     * 
     * @param method the action method to be invoked
     * @param request the HttpServetRequest
     * @return the resolved list of arguments needed to satisfy the action method invocation
     */
    protected abstract List<Object> getArguments(Method method, HttpServletRequest request);

    /**
     * Returns the methods matching the type and name
     * 
     * @param type the Class in which to look for the method
     * @param methodName the method name
     * @return A List of methods
     * @throws NoMatchingActionMethodException if no methods match
     */
    protected abstract List<Method> findMethods(Class<?> type, String methodName);

}
