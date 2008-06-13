package org.codehaus.waffle.bind.ognl;

import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.controller.RubyController;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.validation.ErrorsContext;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RubyControllerDataBinder extends OgnlControllerDataBinder {
    private final ArgumentResolver argumentResolver;

    public RubyControllerDataBinder(ValueConverterFinder valueConverterFinder,
                          BindErrorMessageResolver bindErrorMessageResolver,
                          ArgumentResolver argumentResolver,
                          BindMonitor bindMonitor) {
        super(valueConverterFinder, bindErrorMessageResolver, bindMonitor);
        this.argumentResolver = argumentResolver;
    }

    public void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object controller) {
        if (controller instanceof RubyController) {
            IRubyObject rubyObject = ((RubyController) controller).getRubyObject();
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
        } else {
            // default to standard binding
            super.bind(request, response, errorsContext, controller);
        }
    }
}
