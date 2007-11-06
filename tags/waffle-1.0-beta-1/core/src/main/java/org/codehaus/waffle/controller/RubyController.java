package org.codehaus.waffle.controller;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

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

    // todo need to ensure this doesn't allow non-public methods to be called ... NEED test in general
    public Object execute(HttpServletRequest request, HttpServletResponse response) {
        Ruby runtime = rubyObject.getRuntime();
        IRubyObject result;

        String[] strings = methodName.split("\\|");

        if(strings.length == 0) {
            result = rubyObject.callMethod(runtime.getCurrentContext(), methodName);
        } else {
            Iterator<String> iterator = Arrays.asList(strings).iterator();

            methodName = iterator.next();
            List<IRubyObject> arguments = new ArrayList<IRubyObject>();

            while (iterator.hasNext()) {
                arguments.add(JavaEmbedUtils.javaToRuby(runtime, iterator.next()));
            }
            
            result = rubyObject.callMethod(runtime.getCurrentContext(), methodName, arguments.toArray(new IRubyObject[0]));
        }

        return JavaUtil.convertRubyToJava(result);
    }
}