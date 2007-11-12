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

import java.util.Set;

import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.HierarchicalArgumentResolver.Scope;

/**
 * A monitor for action-related events
 * 
 * @author Mauro Talevi
 */
public interface ActionMonitor extends Monitor {

    void defaultActionMethodFound(MethodDefinition methodDefinition);

    void defaultActionMethodCached(Class<?> controllerType, MethodDefinition methodDefinition);

    void pragmaticActionMethodFound(MethodDefinition methodDefinition);

    void actionMethodFound(MethodDefinition methodDefinition);

    void actionMethodExecutionFailed(Exception cause);

    void argumentResolved(String name, Object value, Scope scope);

    void argumentNameNotMatched(String name, String pattern);

    void methodNameResolved(String methodName, String methodKey, Set<String> keys);

}
