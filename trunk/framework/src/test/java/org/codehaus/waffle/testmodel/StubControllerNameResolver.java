package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.ControllerNameResolver;

import javax.servlet.http.HttpServletRequest;

public class StubControllerNameResolver implements ControllerNameResolver {

    public String findControllerName(HttpServletRequest request) {
        return null;
    }
}
