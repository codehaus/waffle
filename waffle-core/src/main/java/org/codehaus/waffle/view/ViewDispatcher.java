/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Responsible for dispatching a view
 * 
 * @author Michael Ward
 */
public interface ViewDispatcher {

    void dispatch(HttpServletRequest request, HttpServletResponse response, View view) throws IOException,
            ServletException;
}
