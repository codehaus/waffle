package org.codehaus.waffle.testmodel;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.validation.DefaultErrorsContext;

public class CustomErrorsContext extends DefaultErrorsContext {

    public CustomErrorsContext(HttpServletRequest request) {
        super(request);
    }

}
