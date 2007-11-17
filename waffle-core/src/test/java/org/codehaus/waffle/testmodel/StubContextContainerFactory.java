package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextContainerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class StubContextContainerFactory implements ContextContainerFactory {
    public void initialize(ServletContext servletContext) {

    }

    public void destroy() {

    }

    public ContextContainer buildSessionLevelContainer() {
        return null;
    }

    public ContextContainer buildRequestLevelContainer(HttpServletRequest request) {
        return null;
    }
}
