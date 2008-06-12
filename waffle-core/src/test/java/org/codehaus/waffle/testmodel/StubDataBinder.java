package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.bind.ControllerDataBinder;
import org.codehaus.waffle.validation.ErrorsContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StubDataBinder implements ControllerDataBinder {

    public void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object model) {
        // does nothing
    }
}
