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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

    private Map<String, Level> levels;
    private Map<String, String> templates;
    
    protected AbstractWritingMonitor(){
        this.levels = eventLevels();
        this.templates = eventTemplates();
    }
   
    protected Map<String, Level> eventLevels() {
        Map<String, Level> levels = new HashMap<String, Level>();
        levels.put("defaultActionMethodFound", INFO);
        levels.put("defaultActionMethodCached", DEBUG);
        levels.put("pragmaticActionMethodFound", INFO);
        levels.put("actionMethodFound", INFO);
        levels.put("actionMethodExecuted", INFO);
        levels.put("actionMethodExecutionFailed", WARN);
        levels.put("methodNameResolved", INFO);
        levels.put("methodIntercepted", INFO);
        levels.put("argumentNameResolved", INFO);
        levels.put("argumentNameNotMatched", INFO);
        levels.put("bindFailedForModel", INFO);
        levels.put("bindFailedForController", INFO);
        levels.put("responseIsCommitted", INFO);
        levels.put("viewDispatched", INFO);
        return levels;
    }

    protected Map<String, String> eventTemplates() {
        Map<String, String> templates = new HashMap<String, String>();
        templates.put("defaultActionMethodFound", "Default ActionMethod found: {0}");
        templates.put("defaultActionMethodCached", "Default ActionMethod cached for controller {0}: {1}");
        templates.put("pragmaticActionMethodFound", "Pragmatic ActionMethod found: {0}");
        templates.put("actionMethodFound", "ActionMethod found: {0}");
        templates.put("actionMethodExecuted", "ActionMethod executed with response: {0}");
        templates.put("actionMethodExecutionFailed", "ActionMethod failed: {0}");
        templates.put("methodNameResolved", "Method name ''{0}'' found for key ''{1}'' among keys {2}");
        templates.put("methodIntercepted", "Method ''{0}'' intercepted with arguments {1} and returned value ''{2}''");
        templates.put("argumentNameResolved", "Argument name ''{0}'' resolved to ''{1}'' in scope ''{2}''");
        templates.put("argumentNameNotMatched", "Argument name ''{0}'' not matched by pattern ''{1}''");
        templates.put("bindFailedForModel", "Bind failed for model ''{0}'': {1}");
        templates.put("bindFailedForController", "Bind failed for controller ''{0}'': {1}");
        templates.put("responseIsCommitted", "Response is committed for response: {0}");
        templates.put("viewDispatched", "View dispatched: {0}");
        return templates;
    }

    private Level level(String event) {
        if ( !levels.containsKey(event) ){
            return Level.INFO;
        }
        return levels.get(event);
    }

    private String template(String event) {
        if ( !templates.containsKey(event) ){
            throw new NoSuchElementException(event);
        }
        return templates.get(event);
    }

    protected String format(String template, Object ... arguments) {
        return MessageFormat.format(template, arguments);
    }

    protected void write(String event, Object... arguments) {
        String message = format(template(event), arguments);
        write(level(event), message);
        for ( Exception exception : findExceptions(arguments) ){
            trace(exception);
        }
    }

    protected List<Exception> findExceptions(Object[] arguments) {
        List<Exception> exceptions = new ArrayList<Exception>();
        for ( Object argument : arguments){
            if ( argument instanceof Exception ){
                exceptions.add((Exception)argument);
            }
        }
        return exceptions;
    }

    public void defaultActionMethodFound(MethodDefinition methodDefinition) {
        write("defaultActionMethodFound", methodDefinition);
    }

    public void defaultActionMethodCached(Class<?> controllerType, MethodDefinition methodDefinition) {
        write("defaultActionMethodCached", controllerType, methodDefinition);
    }

    public void pragmaticActionMethodFound(MethodDefinition methodDefinition) {
        write("pragmaticActionMethodFound", methodDefinition);
    }

    public void actionMethodFound(MethodDefinition methodDefinition) {
        write("actionMethodFound", methodDefinition);
    }

    public void actionMethodExecuted(ActionMethodResponse actionMethodResponse) {
        write("actionMethodExecuted", actionMethodResponse);        
    }
    
    public void actionMethodExecutionFailed(Exception exception) {
        write("actionMethodExecutionFailed", exception);    
    }
    
    public void methodNameResolved(String methodName, String methodKey, Set<String> keys) {
        write("methodNameResolved", methodName, methodKey, keys);
    }

    public void methodIntercepted(Method method, Object[] arguments, Object returnValue) {
        write("methodIntercepted", method, asList(arguments), returnValue);
    }
    
    public void argumentNameResolved(String name, Object value, Scope scope) {
        write("argumentNameResolved", name, value, scope);        
    }

    public void argumentNameNotMatched(String name, String pattern) {
        write("argumentNameNotMatched", name, pattern);                
    }
    
    public void bindFailedForModel(Object bindModel, BindErrorMessage errorMessage){
        write("bindFailedForModel", bindModel, errorMessage);
    }
    
    public void bindFailedForController(Object controller, Throwable cause){
        write("bindFailedForController", controller, cause);
    }
    
    public void responseIsCommitted(HttpServletResponse response) {
        write("responseIsCommitted", response);        
    }

    public void viewDispatched(View view) {
        write("viewDispatched", view);
    }

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
     * @param exception the Throwable to trace
     */
    protected abstract void trace(Throwable exception);

}
