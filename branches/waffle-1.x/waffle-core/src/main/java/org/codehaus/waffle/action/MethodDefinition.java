/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

/**
 * Holder for the method and values to be executed.
 * 
 * @author Michael Ward
 */
public class MethodDefinition {
    private final Method method;
    private final List<Object> arguments = new ArrayList<Object>();

    public MethodDefinition(Method method) {
        this.method = method;
    }

    /**
     * The method that is to be invoked on the controller
     */
    public Method getMethod() {
        return method;
    }

    /**
     * The argument values that will be used to satisfy the invocation of the action method
     */
    public List<Object> getMethodArguments() {
        return arguments;
    }

    /**
     * Allows resolved argument to be added.  Arguments should be added in order.
     */
    public void addMethodArgument(Object argument) {
        arguments.add(argument);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[MethodDefinition method=")
                .append(method)
                .append(", arguments=")
                .append(arguments).append("]");
        return sb.toString();
    }
}
