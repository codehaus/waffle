/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.context;

import javax.servlet.http.HttpServletRequest;

/**
 * This class uses a ThreadLocal to allow access to the current HttpServletRequest.  This is needed so that components
 * registered with waffle can have dependencies on the HttpServletRequest and HttpSession.
 *
 * @author Michael Ward
 */
public class CurrentHttpServletRequest {
    private final static ThreadLocal<HttpServletRequest> CURRENT_REQUEST = new ThreadLocal<HttpServletRequest>();

    private CurrentHttpServletRequest() {
    }

    public static HttpServletRequest get() {
        return CURRENT_REQUEST.get();
    }

    public static void set(HttpServletRequest request) {
        CURRENT_REQUEST.set(request);
    }

}
