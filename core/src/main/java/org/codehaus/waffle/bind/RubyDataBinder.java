package org.codehaus.waffle.bind;

import ognl.TypeConverter;
import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.controller.RubyController;
import org.codehaus.waffle.validation.ErrorsContext;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RubyDataBinder extends OgnlDataBinder {
    private final ArgumentResolver argumentResolver;

    public RubyDataBinder(TypeConverter typeConverter,
                          BindErrorMessageResolver bindErrorMessageResolver,
                          ArgumentResolver argumentResolver) {
        super(typeConverter, bindErrorMessageResolver);
        this.argumentResolver = argumentResolver;
    }

    public void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object controller) {
        if (controller instanceof RubyController) {
            IRubyObject rubyObject = ((RubyController) controller).getRubyObject();
            Ruby runtime = rubyObject.getRuntime();

            JavaEmbedUtils.invokeMethod(runtime, rubyObject, "__argument_resolver=",
                    new Object[]{JavaEmbedUtils.javaToRuby(runtime, argumentResolver)},
                    Object.class);

            JavaEmbedUtils.invokeMethod(
                    runtime,
                    rubyObject,
                    "__set_all_contexts",
                    new Object[]{
                            JavaEmbedUtils.javaToRuby(runtime, request),
                            JavaEmbedUtils.javaToRuby(runtime, response)
                    },
                    Object.class);
        } else {
            // default to standard binding
            super.bind(request, response, errorsContext, controller);
        }
    }
}
