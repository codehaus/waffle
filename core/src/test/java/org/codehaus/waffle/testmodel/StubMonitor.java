package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.monitor.ActionMonitor;

import java.util.Set;

public class StubMonitor implements ActionMonitor {

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

    public void actionMethodReturnedException(Exception exception) {
    }
}
