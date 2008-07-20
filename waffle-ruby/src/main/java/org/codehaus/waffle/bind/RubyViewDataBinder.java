/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import org.codehaus.waffle.controller.ScriptedController;
import org.codehaus.waffle.monitor.BindMonitor;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.builtin.InstanceVariables;
import org.jruby.runtime.builtin.Variable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ScriptedViewDataBinder implementation which handles request via IRubyObject instance_variables.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RubyViewDataBinder extends ScriptedViewDataBinder {

    public RubyViewDataBinder(BindMonitor bindMonitor) {
        super(bindMonitor);
    }

    @SuppressWarnings( { "unchecked" })
    protected void handleScriptController(HttpServletRequest request, ScriptedController rubyController) {
        IRubyObject iRubyObject = (IRubyObject) rubyController.getScriptObject();
        InstanceVariables instanceVariables = iRubyObject.getInstanceVariables();
        List<Variable<IRubyObject>> instanceVariableList = instanceVariables.getInstanceVariableList();

        // request attributes are renamed from @foo => foo
        for (Variable<IRubyObject> rubyObjectVariable : instanceVariableList) {
            Object value = JavaEmbedUtils.rubyToJava(iRubyObject.getRuntime(), rubyObjectVariable.getValue(), Object.class);
            request.setAttribute(rubyObjectVariable.getName().substring(1), value);
        }
    }
}
