package org.codehaus.waffle.bind;

import ognl.TypeConverter;
import org.codehaus.waffle.validation.ErrorsContext;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;

public class RubyDataBinder extends OgnlDataBinder {

    // needs to bind, request, session and parameter to instance variables
    public RubyDataBinder(TypeConverter typeConverter, BindErrorMessageResolver bindErrorMessageResolver) {
        super(typeConverter, bindErrorMessageResolver);
    }

    public void bind(HttpServletRequest request, ErrorsContext errorsContext, Object model) {
        if(model instanceof IRubyObject) {
            // does nothing for the moment
        } else {
            super.bind(request, errorsContext, model);
        }
    }
}
