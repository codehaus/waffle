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

    public RedirectView(String path) {
        this(path, null, HttpServletResponse.SC_SEE_OTHER);
    }
  
    public RedirectView(String path, Object controller, int statusCode) {
        super((String)path);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
}
