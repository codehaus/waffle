/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.ognl;

import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.controller.ScriptedController;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.validation.ErrorsContext;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RubyControllerDataBinder extends ScriptedControllerDataBinder {

    public RubyControllerDataBinder(ValueConverterFinder valueConverterFinder,
                          BindErrorMessageResolver bindErrorMessageResolver,
                          ArgumentResolver argumentResolver,
                          BindMonitor bindMonitor) {
        super(valueConverterFinder, bindErrorMessageResolver, argumentResolver, bindMonitor);
    }

    protected void bindScriptedController(HttpServletRequest request, HttpServletResponse response,
            ErrorsContext errorsContext, ScriptedController controller) {
        IRubyObject rubyObject = (IRubyObject)controller.getScriptObject();
        Ruby runtime = rubyObject.getRuntime();

        JavaEmbedUtils.invokeMethod(runtime, rubyObject, "__argument_resolver=",
                new Object[]{JavaEmbedUtils.javaToRuby(runtime, argumentResolver)},
                Void.class);

        JavaEmbedUtils.invokeMethod(runtime, rubyObject, "__errors=",
                new Object[]{JavaEmbedUtils.javaToRuby(runtime, errorsContext)},
                Void.class);

        JavaEmbedUtils.invokeMethod(runtime,
                rubyObject,
                "__set_all_contexts",
                new Object[]{
                        JavaEmbedUtils.javaToRuby(runtime, request),
                        JavaEmbedUtils.javaToRuby(runtime, response)
                },
                Void.class);
    }

}
