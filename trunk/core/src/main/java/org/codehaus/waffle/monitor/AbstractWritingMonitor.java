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
import static org.codehaus.waffle.monitor.MonitorLevel.DEBUG;
import static org.codehaus.waffle.monitor.MonitorLevel.INFO;

import java.util.Set;

/**
 * Abstract implementation of Monitor that delegates writing to concrete subclasses.
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractWritingMonitor implements ActionMonitor {

    /**
     * Writes message for a given level. Concrete implementations should provide writing functionality.
     * 
     * @param level
     * @param message
     */
    protected abstract void write(MonitorLevel level, String message);

    /**
     * Traces an exception. Concrete implementations should provide writing functionality.
     *
     * @param exception
     */
    protected abstract void trace(Exception exception);

    public void defaultActionMethodFound(MethodDefinition methodDefinition) {
        write(INFO, "Default ActionMethod found: " + methodDefinition);
    }

    public void defaultActionMethodCached(Class<?> controllerType, MethodDefinition methodDefinition) {
        write(DEBUG, "Default ActionMethod cached for controller " + controllerType.getName() + ": " + methodDefinition);
    }

    public void pragmaticActionMethodFound(MethodDefinition methodDefinition) {
        write(INFO, "Pragmatic ActionMethod found: " + methodDefinition);
    }

    public void actionMethodFound(MethodDefinition methodDefinition) {
        write(INFO, "ActionMethod found:  " + methodDefinition);
    }

    public void methodNameResolved(String methodName, String methodKey, Set<String> keys) {
        write(INFO, "Method name '" + methodName + "' found for key '" + methodKey + "' among keys " + keys);
    }

    public void actionMethodExecutionFailed(Exception exception) {
        trace(exception);
    }
}
