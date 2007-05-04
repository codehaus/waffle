package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.view.DispatchAssistant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class StubDispatchAssistant implements DispatchAssistant {
    public void redirect(HttpServletRequest request, HttpServletResponse response, Map model, String path) throws IOException {

    }

    public void forward(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException {

    }
}
