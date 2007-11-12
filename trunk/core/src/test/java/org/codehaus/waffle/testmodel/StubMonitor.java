package org.codehaus.waffle.testmodel;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.HierarchicalArgumentResolver.Scope;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.view.View;

public class StubMonitor implements ActionMonitor, BindMonitor {

    public void defaultActionMethodFound(MethodDefinition methodDefinition) {
    }

    public void defaultActionMethodCached(Class controllerType, MethodDefinition methodDefinition) {
    }

    public void pragmaticActionMethodFound(MethodDefinition methodDefinition) {
    }

    public void actionMethodFound(MethodDefinition methodDefinition) {        
    }

    public void methodNameResolved(String methodName, String methodKey, Set<String> keys) {
    }

    public void actionMethodExecutionFailed(Exception exception) {
    }

    public void argumentNameNotMatched(String name, String pattern) {
    }

    public void argumentNameResolved(String name, Object value, Scope scope) {
    }

    public void bindFailed(Object bindModel, BindErrorMessage errorMessage) {
    }

    public void bindFailed(Object controller, Throwable cause) {
    }

    public void responseIsCommitted(HttpServletResponse response) {
    }

    public void viewDispatched(View view) {
    }

}
