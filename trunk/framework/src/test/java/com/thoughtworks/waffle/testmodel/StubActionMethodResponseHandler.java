package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.action.method.ActionMethodResponseHandler;
import com.thoughtworks.waffle.action.method.ActionMethodResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class StubActionMethodResponseHandler implements ActionMethodResponseHandler {
    public void handle(HttpServletRequest request, HttpServletResponse response, ActionMethodResponse actionMethodResponse)
            throws IOException, ServletException {
        // do nothing
    }
}
