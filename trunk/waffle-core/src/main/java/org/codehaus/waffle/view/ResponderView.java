/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A ResponderView is able to use the servlet response and write directly to it,
 * instead of dispatching it.
 * 
 * @author Paulo Silveira
 */
public abstract class ResponderView extends View {

    public ResponderView() {
        super((String)null);
    }

    /**
     * Renders the output directly into servlet response
     *
     * @param request the ServletRequest
     * @param response the HttpServletResponse
     * @throws IOException
     */
    public abstract void respond(ServletRequest request, HttpServletResponse response)
            throws IOException;
}
