package org.codehaus.waffle.testmodel;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.HierarchicalArgumentResolver.Scope;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.ValidationMonitor;
import org.codehaus.waffle.monitor.ViewMonitor;
import org.codehaus.waffle.monitor.Monitor;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.view.RedirectView;
import org.codehaus.waffle.view.ResponderView;
import org.codehaus.waffle.view.View;
import org.picocontainer.MutablePicoContainer;

public class StubMonitor implements ActionMonitor, BindMonitor, ContextMonitor, ControllerMonitor,
        ServletMonitor, ValidationMonitor, ViewMonitor, Monitor {

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

    public void controllerBindFailed(Object controller, BindErrorMessage errorMessage, Exception cause) {
    }

    public void viewBindFailed(Object controller, Exception cause) {
    }

    public void viewValueBound(String name, Object value, Object controller) {
    }

    public void controllerValueBound(String name, Object value, Object controller) {
    }

    public void genericParameterTypeFound(Type type, Method method) {
    }

    public void genericParameterTypeNotFound(Method method) {
    }

    public void valueConverterFound(Type type, ValueConverter converter) {
    }

    public void valueConverterNotFound(Type type) {
    }

    public void contextInitialized() {
    }

    public void contextInitializationFailed(WaffleException cause) {
    }

    public void applicationContextContainerDestroyed() {
    }

    public void applicationContextContainerStarted() {
    }

    public void requestContextContainerCreated(MutablePicoContainer sessionContextContainer) {
    }

    public void sessionContextContainerCreated(MutablePicoContainer applicationContextContainer) {
    }

    public void controllerNameResolved(String name, String path) {
    }

    public void controllerNotFound(String name) {
    }

    public void methodDefinitionNotFound(String controllerName) {
    }

    public void requestContextContainerNotFound() {
    }

    public void actionMethodInvocationFailed(Exception cause) {
    }

    public void servletInitialized(Servlet servlet) {
    }

    public void servletServiceFailed(Exception cause) {
    }

    public void servletServiceRequested(Map<String, List<String>> parameters) {
    }

    public void controllerValidatorNotFound(String controllerValidatorName, String controllerName) {
    }

    public void methodDefinitionNotFound(ControllerDefinition controllerDefinition) {
    }

    public void validationFailed(Exception cause) {
    }

    public void viewForwarded(String path) {
    }

    public void viewRedirected(RedirectView redirectView) {
    }

    public void viewResponded(ResponderView responderView) {
    }

}
