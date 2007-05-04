package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.action.ControllerNameResolver;

import javax.servlet.http.HttpServletRequest;

public class StubControllerNameResolver implements ControllerNameResolver {

    public String findControllerName(HttpServletRequest request) {
        return null;
    }
}
