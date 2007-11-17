package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.bind.RequestAttributeBinder;

import javax.servlet.http.HttpServletRequest;

public class StubRequestAttributeBinder implements RequestAttributeBinder {
    public void bind(HttpServletRequest request, Object controller) {
    }
}
