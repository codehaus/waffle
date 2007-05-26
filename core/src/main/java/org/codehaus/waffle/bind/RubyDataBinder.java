package org.codehaus.waffle.bind;

import ognl.TypeConverter;
import org.codehaus.waffle.controller.RubyController;
import org.codehaus.waffle.validation.ErrorsContext;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

public class RubyDataBinder extends OgnlDataBinder {

    public RubyDataBinder(TypeConverter typeConverter, BindErrorMessageResolver bindErrorMessageResolver) {
        super(typeConverter, bindErrorMessageResolver);
    }

    public void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object controller) {
        if(controller instanceof RubyController) {
            IRubyObject rubyObject = ((RubyController)controller).getRubyObject();
            Ruby runtime = rubyObject.getRuntime();

            // inject following onto the controller
            JavaEmbedUtils.invokeMethod(runtime, rubyObject, "request=", new HttpServletRequest[] {request}, Object.class);
            JavaEmbedUtils.invokeMethod(runtime, rubyObject, "response=", new HttpServletResponse[] {response}, Object.class);
            JavaEmbedUtils.invokeMethod(runtime, rubyObject, "session=", new HttpSession[] {request.getSession(false)}, Object.class);

            // inspect rubyObject instance variables ... resolve values from (params, req, session, application) ... and set them onto the instance

        } else {
            // default to standard binding
            super.bind(request, response, errorsContext, controller);
        }
    }
}
