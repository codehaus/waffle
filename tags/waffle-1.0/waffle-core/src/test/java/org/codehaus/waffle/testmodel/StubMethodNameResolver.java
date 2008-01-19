package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.MethodNameResolver;

import javax.servlet.http.HttpServletRequest;

public class StubMethodNameResolver implements MethodNameResolver {
    public String resolve(HttpServletRequest request) {
        return null;
    }
}
