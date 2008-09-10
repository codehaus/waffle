/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import static ognl.OgnlRuntime.getMethods;
import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.monitor.ActionMonitor;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Abstract method definition finder that uses Ognl to find methods
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractOgnlMethodDefinitionFinder extends AbstractMethodDefinitionFinder {

    public AbstractOgnlMethodDefinitionFinder(ServletContext servletContext,
                                           ArgumentResolver argumentResolver,
                                           MethodNameResolver methodNameResolver,
                                           StringTransmuter stringTransmuter,
                                           ActionMonitor actionMonitor) {
        super(servletContext, argumentResolver, methodNameResolver, stringTransmuter, actionMonitor);
    }

    /**
     * Inspects the class (aka Type) and finds all methods with that name.  
     *
     * @param type       the Class in which to look for the method
     * @param methodName the method name
     * @return A List of methods
     * @throws NoMatchingActionMethodException if no methods match
     */
    @SuppressWarnings({"unchecked"})
    protected List<Method> findMethods(Class<?> type, String methodName) {
        List<Method> methods = getMethods(type, methodName, false);
        if (methods == null) {
            throw new NoMatchingActionMethodException("No matching methods for "+methodName, type);
        }
        return methods;
    }
    
}
