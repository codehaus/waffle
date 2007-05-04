package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.context.ContextContainer;
import com.thoughtworks.waffle.context.ContextContainerFactory;

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
