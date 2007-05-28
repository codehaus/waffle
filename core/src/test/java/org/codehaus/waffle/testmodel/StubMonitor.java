package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.monitor.Monitor;

public class StubMonitor implements Monitor {

    public void defaultActionMethodFound(MethodDefinition methodDefinition) {
    }

    public void defaultActionMethodCached(Class controllerType, MethodDefinition methodDefinition) {
    }

    public void pragmaticActionMethodFound(MethodDefinition methodDefinition) {
    }

    public void actionMethodFound(MethodDefinition methodDefinition) {        
    }


}
