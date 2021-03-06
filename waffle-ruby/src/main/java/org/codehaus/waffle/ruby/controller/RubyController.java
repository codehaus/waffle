/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.ruby.controller;

import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.controller.ScriptedController;
import org.jruby.Ruby;
import org.jruby.exceptions.RaiseException;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Ruby wrapper that uses the underlying Ruby runtime to invoke the scripted controller execute method.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RubyController implements ScriptedController {
    private String methodName;
    private final IRubyObject rubyObject;

    public RubyController(IRubyObject rubyObject) {
        this.rubyObject = rubyObject;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public IRubyObject getScriptObject() {
        return rubyObject;
    }

    /**
     * This will invoke the method on the ruby object instance this controller is maintaining.
     * 
     * @return the result from the method invocation.
     */
    public Object execute() {
        Ruby runtime = rubyObject.getRuntime();
        String[] strings = methodName.split("\\|");

        try {
            IRubyObject result = callMethod(runtime, strings);
            return JavaUtil.convertRubyToJava(result);
        } catch (RaiseException e) {
            throw new ActionMethodInvocationException(e.getException().message.toString());
        }
    }

    private IRubyObject callMethod(Ruby runtime, String[] strings) {
        IRubyObject result;
        if (strings.length == 1) {
            result = rubyObject.callMethod(runtime.getCurrentContext(), methodName);
        } else {
            Iterator<String> iterator = Arrays.asList(strings).iterator();

            methodName = iterator.next();
            List<IRubyObject> arguments = new ArrayList<IRubyObject>();

            while (iterator.hasNext()) {
                arguments.add(JavaEmbedUtils.javaToRuby(runtime, iterator.next()));
            }

            result = rubyObject.callMethod(runtime.getCurrentContext(), methodName, arguments
                    .toArray(new IRubyObject[arguments.size()]));
        }
        return result;
    }
}
