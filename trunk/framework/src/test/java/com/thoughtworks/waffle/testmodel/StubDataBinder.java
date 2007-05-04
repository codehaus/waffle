package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.bind.DataBinder;
import com.thoughtworks.waffle.validation.ErrorsContext;

import javax.servlet.http.HttpServletRequest;

public class StubDataBinder implements DataBinder {

    public void bind(HttpServletRequest request, ErrorsContext errorsContext, Object model) {
        // does nothing
    }
}
