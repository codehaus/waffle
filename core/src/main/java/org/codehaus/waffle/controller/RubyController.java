package org.codehaus.waffle.controller;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a wrapper for the underlying ruby script
 */
public class RubyController {
    private String methodName;
    private final IRubyObject rubyObject;

    public RubyController(IRubyObject rubyObject) {
        this.rubyObject = rubyObject;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public IRubyObject getRubyObject() {
        return rubyObject;
    }

    // todo need to ensure this doesn't allow non-public methods to be called
    public Object execute(HttpServletRequest request, HttpServletResponse response) {
        Ruby runtime = rubyObject.getRuntime();
        IRubyObject result = rubyObject.callMethod(runtime.getCurrentContext(), methodName);

        return JavaUtil.convertRubyToJava(result);
    }
}
