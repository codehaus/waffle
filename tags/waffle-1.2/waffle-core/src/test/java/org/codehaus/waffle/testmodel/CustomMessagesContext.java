package org.codehaus.waffle.testmodel;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.i18n.DefaultMessagesContext;

public class CustomMessagesContext extends DefaultMessagesContext {

    public CustomMessagesContext(HttpServletRequest request) {
        super(request);
    }

}
