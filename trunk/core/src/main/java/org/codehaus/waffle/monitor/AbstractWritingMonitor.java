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

import static java.util.Arrays.asList;
import static org.codehaus.waffle.monitor.Monitor.Level.DEBUG;
import static org.codehaus.waffle.monitor.Monitor.Level.INFO;
import static org.codehaus.waffle.monitor.Monitor.Level.WARN;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.HierarchicalArgumentResolver.Scope;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.view.View;

/**
 * Abstract implementation of Monitor that delegates writing to concrete subclasses.
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractWritingMonitor implements ActionMonitor, BindMonitor {

    /**
     * Writes message for a given level. Concrete implementations should provide writing functionality.
     * 
     * @param level the Level
     * @param message the message to write 
     */
    protected abstract void write(Level level, String message);

    /**
     * Traces an exception. Concrete implementations should provide writing functionality.
     *
     * @param exception the Exception to trace
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

    public void actionMethodExecuted(ActionMethodResponse actionMethodResponse) {
        write(INFO, "ActionMethod executed with response:  " + actionMethodResponse);        
    }
    
    public void actionMethodExecutionFailed(Exception exception) {
        trace(exception);
    }
    
    public void methodNameResolved(String methodName, String methodKey, Set<String> keys) {
        write(INFO, "Method name '" + methodName + "' found for key '" + methodKey + "' among keys " + keys);
    }

    public void methodIntercepted(Method method, Object[] arguments, Object returnValue) {
        write(INFO, "Method '" + method + "' intercepted with arguments '" + asList(arguments)
                + "' and returned value '" + returnValue + "'");
    }
    
    public void argumentNameResolved(String name, Object value, Scope scope) {
        write(INFO, "Argument name '" + name + "' resolved to '" + value + "' in scope " + scope);        
    }

    public void argumentNameNotMatched(String name, String pattern) {
        write(WARN, "Argument name '" + name + "' not matched by pattern '" + pattern + "'" );                
    }
    
    public void bindFailed(Object bindModel, BindErrorMessage errorMessage){
        write(WARN, "Bind failed for model '" + bindModel + "': " + errorMessage);
    }
    
    public void bindFailed(Object controller, Throwable cause){
        write(WARN, "Bind failed for controller '" + controller + "': " + cause);
    }
    
    public void responseIsCommitted(HttpServletResponse response) {
        write(INFO, "Reponse is already committed: "+response);        
    }

    public void viewDispatched(View view) {
        write(DEBUG, "Dispached view "+view);
    }

}
