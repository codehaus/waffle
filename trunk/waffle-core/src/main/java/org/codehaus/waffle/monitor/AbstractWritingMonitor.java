/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.monitor;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.codehaus.waffle.monitor.Monitor.Level.DEBUG;
import static org.codehaus.waffle.monitor.Monitor.Level.INFO;
import static org.codehaus.waffle.monitor.Monitor.Level.WARN;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.HierarchicalArgumentResolver.Scope;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.view.RedirectView;
import org.codehaus.waffle.view.ResponderView;
import org.codehaus.waffle.view.View;

/**
 * Abstract implementation of Monitor that delegates writing to concrete subclasses.
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractWritingMonitor implements ActionMonitor, BindMonitor, ContextMonitor, ControllerMonitor,
        RegistrarMonitor, ServletMonitor, ValidationMonitor, ViewMonitor {

    private Map<String, Level> levels;
    private Map<String, String> messages;
    
    protected AbstractWritingMonitor(){
        this.levels = monitorLevels();
        this.messages = monitorMessages();
    }
   
    /**
     * Creates the default map of monitor levels, keyed on the event name. 
     * Subclasses may override any of these by retrieving the levels via
     * <code>super.monitorLevels()</code>, overwriting any entry and returning
     * the map.
     * 
     * @return A Map<String, Level>
     */
    protected Map<String, Level> monitorLevels() {
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
        levels.put("responseIsCommitted", INFO);
        levels.put("viewDispatched", INFO);
        levels.put("attributeBindFailed", WARN);
        levels.put("attributeValueBoundFromController", DEBUG);
        levels.put("dataBindFailed", WARN);
        levels.put("dataValueBoundToController", DEBUG);
        levels.put("registrarCreated", INFO);
        levels.put("registrarNotFound", WARN);
        levels.put("contextInitialized", DEBUG);
        levels.put("applicationContextContainerStarted", DEBUG);
        levels.put("applicationContextContainerDestroyed", DEBUG);
        levels.put("sessionContextContainerCreated", DEBUG);
        levels.put("requestContextContainerCreated", DEBUG);
        levels.put("controllerNameResolved", DEBUG);
        levels.put("controllerNotFound", DEBUG);
        levels.put("methodDefinitionNotFound", WARN);
        levels.put("requestContextContainerNotFound", WARN);
        levels.put("componentRegistered", DEBUG);
        levels.put("instanceRegistered", DEBUG);
        levels.put("nonCachingComponentRegistered", DEBUG);
        levels.put("actionMethodInvocationFailed", WARN);
        levels.put("servletInitialized", INFO);
        levels.put("servletServiceFailed", WARN);
        levels.put("servletServiceRequested", DEBUG);
        levels.put("controllerValidatorNotFound", WARN);
        levels.put("methodDefinitionNotFound", WARN);        
        levels.put("validationFailed", WARN);  
        levels.put("viewForwarded", DEBUG);        
        levels.put("viewRedirected", DEBUG);
        levels.put("viewResponded", DEBUG);        
        return levels;
    }

    /**
     * Creates the default map of monitor message templates, keyed on the event name. 
     * Subclasses may override any of these by retrieving the messages via
     * <code>super.monitorMessages()</code>, overwriting any entry and returning
     * the map.  Message templates need to be maintained in a format compatible with 
     * {@link java.text.MessageFormat} and will expect the same number of arguments as the event
     * (with the argument index reflecting the argument order of the event).
     * 
     * @return A Map<String, String>
     */
    protected Map<String, String> monitorMessages() {
        Map<String, String> messages = new HashMap<String, String>();
        messages.put("defaultActionMethodFound", "Default ActionMethod found: {0}");
        messages.put("defaultActionMethodCached", "Default ActionMethod cached for controller ''{0}'': {1}");
        messages.put("pragmaticActionMethodFound", "Pragmatic ActionMethod found: {0}");
        messages.put("actionMethodFound", "ActionMethod found: {0}");
        messages.put("actionMethodExecuted", "ActionMethod executed with response: {0}");
        messages.put("actionMethodExecutionFailed", "ActionMethod failed: {0}");
        messages.put("methodNameResolved", "Method name ''{0}'' found for key ''{1}'' among keys ''{2}''");
        messages.put("methodIntercepted", "Method ''{0}'' intercepted with arguments {1} and returned value ''{2}''");
        messages.put("argumentNameResolved", "Argument name ''{0}'' resolved to ''{1}'' in scope ''{2}''");
        messages.put("argumentNameNotMatched", "Argument name ''{0}'' not matched by pattern ''{1}''");
        messages.put("responseIsCommitted", "Response is committed for response: {0}");
        messages.put("viewDispatched", "View dispatched: {0}");
        messages.put("attributeBindFailed", "Attribute bind failed from controller ''{0}'': {1}");
        messages.put("attributeValueBoundFromController", "Attribute value ''{1}'' bound for name ''{0}'' from controller ''{2}''");        
        messages.put("dataBindFailed", "Data bind failed to controller ''{0}'' with message {1}: {2}");
        messages.put("dataValueBoundToController", "Data value ''{1}'' bound for name ''{0}'' to controller ''{2}''");        
        messages.put("registrarCreated", "Registrar ''{0}'' created  with monitor ''{1}''");
        messages.put("registrarNotFound", "Registrar ''{0}'' not found");
        messages.put("contextInitialized", "Context initialized");
        messages.put("applicationContextContainerStarted", "Application context container started");
        messages.put("applicationContextContainerDestroyed", "Application context container destroyed");
        messages.put("sessionContextContainerCreated", "Session context container created with parent application container ''{0}''");
        messages.put("requestContextContainerCreated", "Request context container created with parent session container ''{0}''");
        messages.put("controllerNameResolved", "Controller name resolved to ''{0}'' from path ''{1}''");        
        messages.put("controllerNotFound", "Controller not found for name ''{0}''");
        messages.put("methodDefinitionNotFound", "Method definition not found for controller name ''{0}''");
        messages.put("requestContextContainerNotFound", "Request level context container not found");
        messages.put("componentRegistered", "Registered component of type ''{1}'' with key ''{0}'' and parameters ''{2}''");
        messages.put("instanceRegistered", "Registered instance ''{1}'' with key ''{0}''");
        messages.put("nonCachingComponentRegistered", "Registered non-caching component of type ''{1}'' with key ''{0}'' and parameters ''{2}''");
        messages.put("actionMethodInvocationFailed", "ActionMethod invocation failed: {0}");
        messages.put("servletInitialized", "Servlet initialized: {0}");
        messages.put("servletServiceFailed", "Servlet service failed: {0}");
        messages.put("servletServiceRequested", "Servlet service requested with parameters: {0}");
        messages.put("controllerValidatorNotFound", "Controller validator ''{0}'' not found: defaulting to controller ''{1}''");
        messages.put("methodDefinitionNotFound", "Method definition not found in controller definition ''{0}''");        
        messages.put("validationFailed", "Validation failed: {0}");  
        messages.put("viewForwarded", "View forwarded to path ''{0}''");        
        messages.put("viewRedirected", "View redirected: {0}");
        messages.put("viewResponded", "View responded: {0}");
        return messages;
    }

    private Level level(String event) {
        if ( !levels.containsKey(event) ){
            return Level.INFO;
        }
        return levels.get(event);
    }

    private String template(String event) {
        if ( !messages.containsKey(event) ){
            throw new NoSuchElementException(event);
        }
        return messages.get(event);
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
    
    public void actionMethodExecutionFailed(Exception cause) {
        write("actionMethodExecutionFailed", cause); 
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
    
    public void responseIsCommitted(HttpServletResponse response) {
        write("responseIsCommitted", response);        
    }

    public void viewDispatched(View view) {
        write("viewDispatched", view);        
    }

    public void attributeBindFailed(Object controller, Exception cause){
        write("attributeBindFailed", controller, cause);
    }

    public void attributeValueBoundFromController(String name, Object value, Object controller) {
        write("attributeValueBoundFromController", name, value, controller);
    }

    public void dataBindFailed(Object controller, BindErrorMessage errorMessage, Exception cause){
        write("dataBindFailed", controller, errorMessage, cause);
    }
    
    public void dataValueBoundToController(String name, Object value, Object controller) {
        write("dataValueBoundToController", name, value, controller);
    }

    public void registrarCreated(Registrar registrar, RegistrarMonitor registrarMonitor) {
        write("registrarCreated", registrar, registrarMonitor);         
    }

    public void registrarNotFound(String registrarClassName) {
        write("registrarNotFound", registrarClassName); 
    }

    public void contextInitialized() {
        write("contextInitialized");        
    }
    
    public void applicationContextContainerStarted() {
        write("applicationContextContainerStarted");        
    }

    public void applicationContextContainerDestroyed() {
        write("applicationContextContainerDestroyed");                
    }

    public void sessionContextContainerCreated(ContextContainer applicationContextContainer) {
        write("sessionContextContainerCreated", applicationContextContainer);                        
    }

    public void requestContextContainerCreated(ContextContainer sessionContextContainer){
        write("requestContextContainerCreated", sessionContextContainer);                                
    }

    public void controllerNameResolved(String name, String path) {
        write("controllerNameResolved", name, path);        
    }
    
    public void controllerNotFound(String name){
        write("controllerNotFound", name);                
    }

    public void methodDefinitionNotFound(String controllerName){
        write("methodDefinitionNotFound", controllerName);                
    }

    public void requestContextContainerNotFound(){
        write("requestContextContainerNotFound");                        
    }

    public void componentRegistered(Object key, Class<?> type, Object[] parameters) {
        write("componentRegistered", key, type, asList(parameters));
    }

    public void instanceRegistered(Object key, Object instance) {
        write("instanceRegistered", key, instance);
    }

    public void nonCachingComponentRegistered(Object key, Class<?> type, Object[] parameters) {
        write("nonCachingComponentRegistered", key, type, asList(parameters));
    }

    public void actionMethodInvocationFailed(Exception cause){
        write("actionMethodInvocationFailed", cause);
    }

    public void servletInitialized(Servlet servlet) {
        write("servletInitialized", servlet);
    }
    
    public void servletServiceFailed(Exception cause){
        write("servletServiceFailed", cause);
    }
    
    public void servletServiceRequested(Map<String, List<String>> parameters){
        write("servletServiceRequested", parameters);        
    }

    public void controllerValidatorNotFound(String controllerValidatorName, String controllerName) {
        write("controllerValidatorNotFound", controllerValidatorName, controllerName);
    }

    public void methodDefinitionNotFound(ControllerDefinition controllerDefinition) {
        write("methodDefinitionNotFound", controllerDefinition);        
    }

    public void validationFailed(Exception cause) {
        write("validationFailed", cause);
    }

    public void viewForwarded(String path) {
        write("viewForwarded", path);        
    }

    public void viewRedirected(RedirectView redirectView) {
        write("viewRedirected", redirectView);        
    }

    public void viewResponded(ResponderView responderView) {
        write("viewResponded", responderView);        
    }
}
