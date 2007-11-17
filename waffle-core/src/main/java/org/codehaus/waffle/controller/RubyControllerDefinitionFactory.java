package org.codehaus.waffle.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.jruby.runtime.builtin.IRubyObject;

public class RubyControllerDefinitionFactory extends ContextControllerDefinitionFactory {
    private final MethodNameResolver methodNameResolver;
    private static final Method executeMethod;

    static {
        try {
            executeMethod = RubyController.class.getMethod("execute");
        } catch (NoSuchMethodException e) {
            throw new WaffleException("FATAL: Waffle's RubyController does not define an execute() method.");
        }
    }

    public RubyControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder,
                                           ControllerNameResolver controllerNameResolver,
                                           MethodNameResolver methodNameResolver, ControllerMonitor controllerMonitor) {
        super(methodDefinitionFinder, controllerNameResolver, controllerMonitor);
        this.methodNameResolver = methodNameResolver;
    }

    @Override
    protected Object findController(String name, HttpServletRequest request) {
        Object controller = super.findController(name, request);

        if (controller instanceof IRubyObject) {
            return new RubyController((IRubyObject) controller);
        }

        return controller;
    }

    @Override
    protected MethodDefinition findMethodDefinition(Object controller, HttpServletRequest request, HttpServletResponse response) {
        if (controller instanceof RubyController) {
            RubyController rubyController = (RubyController) controller;
            String methodName = methodNameResolver.resolve(request);

            if(methodName == null || methodName.equals("")) {
                methodName = "index"; // default to the index method
            }

            rubyController.setMethodName(methodName);
            return new MethodDefinition(executeMethod);
        }
        return super.findMethodDefinition(controller, request, response);
    }
    
}
