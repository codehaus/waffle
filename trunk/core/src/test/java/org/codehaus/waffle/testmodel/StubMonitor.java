package org.codehaus.waffle.testmodel;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.HierarchicalArgumentResolver.Scope;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.view.View;

public class StubMonitor implements ActionMonitor, BindMonitor, ContextMonitor, RegistrarMonitor, ServletMonitor {

    public void defaultActionMethodFound(MethodDefinition methodDefinition) {
    }

    public void defaultActionMethodCached(Class<?> controllerType, MethodDefinition methodDefinition) {
    }

    public void pragmaticActionMethodFound(MethodDefinition methodDefinition) {
    }

    public void actionMethodFound(MethodDefinition methodDefinition) {        
    }

    public void actionMethodExecuted(ActionMethodResponse actionMethodResponse) {
    }  

    public void actionMethodExecutionFailed(Exception exception) {
    }

    public void methodNameResolved(String methodName, String methodKey, Set<String> keys) {
    }
    
    public void methodIntercepted(Method method, Object[] arguments, Object returnValue) {
    }

    public void argumentNameNotMatched(String name, String pattern) {
    }

    public void argumentNameResolved(String name, Object value, Scope scope) {
    }
    
    public void responseIsCommitted(HttpServletResponse response) {
    }

    public void viewDispatched(View view) {
    }

    public void bindFailedForModel(Object bindModel, BindErrorMessage errorMessage) {
    }

    public void bindFailedForController(Object controller, Throwable cause) {
    }

    public void contextInitialized() {
    }

    public void applicationContextContainerDestroyed() {
    }

    public void applicationContextContainerStarted() {
    }

    public void registrarCreated(Registrar registrar, RegistrarMonitor registrarMonitor) {
    }

    public void registrarNotFound(String registrarClassName) {
    }

    public void requestContextContainerCreated(ContextContainer sessionContextContainer) {
    }

    public void sessionContextContainerCreated(ContextContainer applicationContextContainer) {
    }

    public void componentRegistered(Object key, Class<?> clazz, Object[] parameters) {
    }

    public void instanceRegistered(Object key, Object instance) {
    }

    public void nonCachingComponentRegistered(Object key, Class<?> clazz, Object[] parameters) {
    }

    public void servletServiceFailed(Exception cause) {
    }

}
