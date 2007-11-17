/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.view;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Michael Ward
 */
public class RedirectView extends View {
    private int statusCode;

    public RedirectView(String value, Object fromController) {
        this(value, fromController, HttpServletResponse.SC_SEE_OTHER);
    }

    public RedirectView(String value, Object fromController, int statusCode) {
        super(value, fromController);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
