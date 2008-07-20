/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.controller;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.monitor.ControllerMonitor;

import java.lang.reflect.Method;

/**
 * A JRuby specific extension to the {@link ScriptedControllerDefinitionFactory}.
 *
 * @author Michael Ward
 */
public class RubyControllerDefinitionFactory extends ScriptedControllerDefinitionFactory {
   
    public RubyControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder,
            ControllerNameResolver controllerNameResolver, MethodNameResolver methodNameResolver,
            ControllerMonitor controllerMonitor) {
        super(methodDefinitionFinder, controllerNameResolver, methodNameResolver, controllerMonitor);
    }
   
    @Override
    protected Method findExecuteMethod() {
        try {
            return RubyController.class.getMethod("execute");
        } catch (NoSuchMethodException e) {
            throw new WaffleException("FATAL: Waffle's RubyController does not define an execute() method.");
        }
    }
    
}
