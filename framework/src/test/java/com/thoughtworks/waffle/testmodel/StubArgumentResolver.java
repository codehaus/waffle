package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.action.method.ArgumentResolver;

import javax.servlet.http.HttpServletRequest;

public class StubArgumentResolver implements ArgumentResolver {
    public Object resolve(HttpServletRequest request, String name) {
        throw new UnsupportedOperationException();
    }
}
