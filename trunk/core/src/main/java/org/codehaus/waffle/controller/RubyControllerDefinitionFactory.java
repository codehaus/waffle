package org.codehaus.waffle.controller;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RubyControllerDefinitionFactory extends DefaultControllerDefinitionFactory {
    private final MethodNameResolver methodNameResolver;
    private static final Method executeMethod;

    static {
        try {
            executeMethod = RubyController.class.getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            throw new WaffleException("FATAL: Waffle's RubyController does not define an execute() method.");
        }
    }

    public RubyControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder,
                                           ControllerNameResolver controllerNameResolver,
                                           MethodNameResolver methodNameResolver) {
        super(methodDefinitionFinder, controllerNameResolver);
        this.methodNameResolver = methodNameResolver;
    }

    protected Object findController(String name, HttpServletRequest request) {
        Object controller = super.findController(name, request);

        if (controller instanceof IRubyObject) {
            return new RubyController((IRubyObject) controller);
        }

        return controller;
    }

    protected MethodDefinition findMethodDefinition(Object controller, HttpServletRequest request, HttpServletResponse response) {
        if (controller instanceof RubyController) {
            RubyController rubyController = (RubyController) controller;
            String methodName = methodNameResolver.resolve(request);

            if(methodName == null || methodName.equals("")) {
                methodName = "index"; // default to the index method
            }

            rubyController.setMethodName(methodName);
            MethodDefinition methodDefinition = new MethodDefinition(executeMethod);
            methodDefinition.addMethodArgument(request);
            methodDefinition.addMethodArgument(response);
            return methodDefinition;
        }
        return super.findMethodDefinition(controller, request, response);
    }
}
