package org.codehaus.waffle.controller;

import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a wrapper for the underlying ruby script
 */
public class RubyController {
    private final IRubyObject rubyObject;
    private String methodName;

    public RubyController(IRubyObject rubyObject) {
        this.rubyObject = rubyObject;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object execute(HttpServletRequest request, HttpServletResponse response) {
        IRubyObject result = rubyObject.callMethod(rubyObject.getRuntime().getCurrentContext(), methodName);

        return JavaUtil.convertRubyToJava(result);
    }
}
