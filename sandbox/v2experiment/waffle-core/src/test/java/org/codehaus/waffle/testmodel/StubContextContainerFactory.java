package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.context.ContextContainerFactory;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class StubContextContainerFactory implements ContextContainerFactory {
    public void initialize(ServletContext servletContext) {

    }

    public void destroy() {

    }

    public MutablePicoContainer buildSessionLevelContainer() {
        return null;
    }

    public MutablePicoContainer buildRequestLevelContainer(HttpServletRequest request) {
        return null;
    }
}
