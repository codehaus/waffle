/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

import javax.servlet.http.HttpServletResponse;

/**
 * Indicates that the view should be redirected to.
 *
 * @author Michael Ward
 */
public class RedirectView extends View {
    private int statusCode;

    public RedirectView(String path, Object controller) {
        this(path, controller, HttpServletResponse.SC_SEE_OTHER);
    }

    public RedirectView(String path, Object controller, int statusCode) {
        super(path, controller);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
}
