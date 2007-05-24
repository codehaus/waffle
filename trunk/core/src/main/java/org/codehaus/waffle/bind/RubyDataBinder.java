package org.codehaus.waffle.bind;

import ognl.TypeConverter;
import org.codehaus.waffle.controller.RubyController;
import org.codehaus.waffle.validation.ErrorsContext;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;

public class RubyDataBinder extends OgnlDataBinder {

    // needs to bind, request, session and parameter to instance variables
    public RubyDataBinder(TypeConverter typeConverter, BindErrorMessageResolver bindErrorMessageResolver) {
        super(typeConverter, bindErrorMessageResolver);
    }

    public void bind(HttpServletRequest request, ErrorsContext errorsContext, Object controller) {
        if(controller instanceof RubyController) {
            IRubyObject rubyObject = ((RubyController)controller).getRubyObject();
            Ruby runtime = rubyObject.getRuntime();

            // set request on the controller
            JavaEmbedUtils.invokeMethod(runtime, rubyObject, "request=", new HttpServletRequest[] {request}, Object.class);
            
        } else {
            // default to standard binding
            super.bind(request, errorsContext, controller);
        }
    }
}
