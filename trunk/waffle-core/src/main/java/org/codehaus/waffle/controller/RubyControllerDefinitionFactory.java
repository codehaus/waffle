package org.codehaus.waffle.controller;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * A JRuby specific extension to the {@link ContextControllerDefinitionFactory} if the controller found is an instance
 * of {@code IRubyObject} then that object will be wrapped with a {@link RubyController}.
 *
 * @author Michael Ward
 */
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
                                           MethodNameResolver methodNameResolver,
                                           ControllerMonitor controllerMonitor) {
        super(methodDefinitionFinder, controllerNameResolver, controllerMonitor);
        this.methodNameResolver = methodNameResolver;
    }

    /**
     * Delegates the lookup of the controller to its super class the result will be wrapped as {@code RubyController} if
     * the type is a {@code IRubyObject}
     *
     * @param name the name of the controller being requested
     * @param request the current request
     * @return the controller requested.
     */
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
