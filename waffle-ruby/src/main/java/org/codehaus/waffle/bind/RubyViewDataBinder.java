package org.codehaus.waffle.bind;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.controller.ScriptedController;
import org.codehaus.waffle.monitor.BindMonitor;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

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
        Map<String, IRubyObject> iVars = iRubyObject.getInstanceVariables();
        Set<Map.Entry<String, IRubyObject>> entries = iVars.entrySet();

        for (Map.Entry<String, IRubyObject> entry : entries) {
            Object value = JavaEmbedUtils.rubyToJava(iRubyObject.getRuntime(), entry.getValue(), Object.class);
            request.setAttribute(entry.getKey().substring(1), value);
        }
    }
}
