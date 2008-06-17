/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.monitor.ControllerMonitor;

/**
 * A scriptspecific extension to the {@link ContextControllerDefinitionFactory} ..
 *
 * @author Michael Ward
 */
public abstract class ScriptedControllerDefinitionFactory extends ContextControllerDefinitionFactory {
    private final MethodNameResolver methodNameResolver;
    private Method executeMethod;

    public ScriptedControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder,
                                           ControllerNameResolver controllerNameResolver,
                                           MethodNameResolver methodNameResolver,
                                           ControllerMonitor controllerMonitor) {
        super(methodDefinitionFinder, controllerNameResolver, controllerMonitor);
        this.methodNameResolver = methodNameResolver;
    }

    @Override
    protected MethodDefinition findMethodDefinition(Object controller, HttpServletRequest request, HttpServletResponse response) {
        if (controller instanceof ScriptedController) {
            ScriptedController scriptedController = (ScriptedController) controller;
            String methodName = methodNameResolver.resolve(request);

            if(methodName == null || methodName.equals("")) {
                methodName = "index"; // default to the index method
            }

            scriptedController.setMethodName(methodName);
            executeMethod = findExecuteMethod();
            return new MethodDefinition(executeMethod);
        }
        return super.findMethodDefinition(controller, request, response);
    }

    protected abstract Method findExecuteMethod();
    
}
