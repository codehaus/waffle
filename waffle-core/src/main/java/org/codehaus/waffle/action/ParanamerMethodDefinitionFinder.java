/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ActionMonitor;

import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;
import com.thoughtworks.paranamer.AdaptiveParanamer;

/**
 * <p>
 * Pananamer-based method definition finder, which can be used in alternative to other definition finders, eg
 * {@link AnnotatedMethodDefinitionFinder}.
 * </p>
 * <p>
 * <b>Note</b>: Pragmatic method calls will always take precedence.
 * </p>
 * 
 * @author Paul Hammant
 * @see AnnotatedMethodDefinitionFinder
 */
public class ParanamerMethodDefinitionFinder extends AbstractOgnlMethodDefinitionFinder {
    private final Paranamer paranamer = new CachingParanamer(new AdaptiveParanamer());

    public ParanamerMethodDefinitionFinder(ServletContext servletContext, ArgumentResolver argumentResolver,
            MethodNameResolver methodNameResolver, StringTransmuter stringTransmuter, ActionMonitor actionMonitor,
            MessageResources messageResources) {
        super(servletContext, argumentResolver, methodNameResolver, stringTransmuter, actionMonitor, messageResources);
    }

    /**
     * Overriden to allow Paranamer to not use generic parameter types.
     */
    protected boolean hasEquivalentParameterTypes(MethodDefinition methodDefinition, StringTransmuter stringTransmuter) {
        Class<?>[] methodParameterTypes = methodDefinition.getMethod().getParameterTypes();
        List<Object> methodArguments = methodDefinition.getMethodArguments();

        if (methodParameterTypes.length != methodArguments.size()) {
            return false; // different signatures lengths
        }

        for (int i = 0; i < methodParameterTypes.length; i++) {
            Class<?> methodParameterType = methodParameterTypes[i];

            // the types must be assignable to be considered a valid method (assume true if actualParameterType is null)
            if (methodArguments.get(i) != null) {
                Class<?> methodArgumentType = methodArguments.get(i).getClass();

                if (!methodParameterType.isAssignableFrom(methodArgumentType)) {
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

    /**
     * Uses {@link Paranamer} to determine the parameter names to use to resolve the argument values.
     * 
     * @param method the action method to be invoked
     * @param request the HttpServetRequest
     * @return the resolved list of arguments needed to satisfy the action method invocation
     */
    protected List<Object> getArguments(Method method, HttpServletRequest request) {

        try {
            return resolveArgs(request, method.getParameterTypes(), paranamer.lookupParameterNames(method));
        } catch (ParameterNamesNotFoundException e) {
            String message = messageResources.getMessageWithDefault("noParameterNamesListFound",
                    "No parameter names list found for method ''{0}'', class ''{1}''", method.toString(), method.getDeclaringClass().getName());
            throw new MatchingActionMethodException(message);
        }
    }

    private List<Object> resolveArgs(HttpServletRequest request, Class<?>[] parameterTypes, String[] parameterNames) {
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
                arguments.add(formatArgument(parameterNames[i]));
            }
        }

        return resolveArguments(request, arguments.iterator());
    }

}
