/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.ruby.controller;

import java.lang.reflect.Method;

import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.codehaus.waffle.controller.ScriptedControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNameResolver;

/**
 * A JRuby specific extension to the {@link org.codehaus.waffle.controller.ScriptedControllerDefinitionFactory}.
 * 
 * @author Michael Ward
 */
public class RubyControllerDefinitionFactory extends ScriptedControllerDefinitionFactory {

    public RubyControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder,
            ControllerNameResolver controllerNameResolver, MethodNameResolver methodNameResolver,
            ControllerMonitor controllerMonitor, MessageResources messageResources) {
        super(methodDefinitionFinder, controllerNameResolver, methodNameResolver, controllerMonitor, messageResources);
    }

    @Override
    protected Method findExecuteMethod() {
        return findMethod("execute", RubyController.class);
    }

}
