package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.ArgumentResolver;

import javax.servlet.http.HttpServletRequest;

public class StubArgumentResolver implements ArgumentResolver {
    public Object resolve(HttpServletRequest request, String name) {
        throw new UnsupportedOperationException();
    }
}
