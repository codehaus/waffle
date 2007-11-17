package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class StubViewDispatcher implements ViewDispatcher {
    public void dispatch(HttpServletRequest request, HttpServletResponse response, View view)
            throws IOException, ServletException {
    }
}
