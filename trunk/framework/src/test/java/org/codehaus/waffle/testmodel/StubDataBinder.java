package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.validation.ErrorsContext;

import javax.servlet.http.HttpServletRequest;

public class StubDataBinder implements DataBinder {

    public void bind(HttpServletRequest request, ErrorsContext errorsContext, Object model) {
        // does nothing
    }
}
