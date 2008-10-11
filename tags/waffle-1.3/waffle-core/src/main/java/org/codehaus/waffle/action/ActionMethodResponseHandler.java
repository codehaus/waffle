/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Custom implementation of this interface will be able to act in response to the values returned from an
 * ActionMethod.
 *
 * @author Michael Ward
 */
public interface ActionMethodResponseHandler {

    /**
     * Handles response from an action method invocation. Regardless of the implementation an ActionMethodResponse
     * value of {@code null} indicates that the user should be return to the referring page.
     *
     * @param request
     * @param response
     * @param actionMethodResponse
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    void handle(HttpServletRequest request,
                HttpServletResponse response,
                ActionMethodResponse actionMethodResponse) throws IOException, ServletException;
}
