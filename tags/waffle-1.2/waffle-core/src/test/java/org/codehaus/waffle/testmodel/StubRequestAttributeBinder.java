package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.bind.ViewDataBinder;

import javax.servlet.http.HttpServletRequest;

public class StubRequestAttributeBinder implements ViewDataBinder {
    public void bind(HttpServletRequest request, Object controller) {
    }
}
