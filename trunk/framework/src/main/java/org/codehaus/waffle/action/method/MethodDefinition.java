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
    private final List<Object> methodArguments = new ArrayList<Object>();

    public MethodDefinition(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public List<Object> getMethodArguments() {
        return methodArguments;
    }

    public void addMethodArgument(Object argument) {
        methodArguments.add(argument);
    }
}
