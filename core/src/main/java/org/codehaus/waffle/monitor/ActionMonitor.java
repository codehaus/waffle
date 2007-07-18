/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.monitor;

import org.codehaus.waffle.action.MethodDefinition;

import java.util.Set;

/**
 * Defines events that need to be monitored, eg for debugging purposes.
 * 
 * Each implementing class can opt to disregard some of these events or handle them with 
 * different priorities.
 * 
 * @author Mauro Talevi
 */
public interface ActionMonitor {

    void defaultActionMethodFound(MethodDefinition methodDefinition);

    void defaultActionMethodCached(Class<?> controllerType, MethodDefinition methodDefinition);

    void pragmaticActionMethodFound(MethodDefinition methodDefinition);

    void actionMethodFound(MethodDefinition methodDefinition);

    void methodNameResolved(String methodName, String methodKey, Set<String> keys);

    void actionMethodReturnedException(Exception exception);
}
