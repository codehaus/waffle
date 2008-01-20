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
package org.codehaus.waffle.view;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.monitor.ViewMonitor;

import java.io.IOException;

/**
 * The ViewDispatcher handles redirecting/forwarding to the view
 *
 * @author Michael Ward
 * @author Paulo Silveira
 * @author Mauro Talevi
 */
public class DefaultViewDispatcher implements ViewDispatcher {
    private final ViewResolver viewResolver;
    private final ViewMonitor viewMonitor;

    public DefaultViewDispatcher(ViewResolver viewResolver, ViewMonitor viewMonitor) {
        this.viewResolver = viewResolver;
        this.viewMonitor = viewMonitor;
    }

    // todo may need to handle ... http://java.sun.com/products/servlet/Filters.html for Character Encoding from request
    public void dispatch(HttpServletRequest request,
                         HttpServletResponse response,
                         View view) throws IOException, ServletException {
        String path = viewResolver.resolve(view);

        if (view instanceof RedirectView) {
            RedirectView redirectView = (RedirectView) view;
            response.setStatus(redirectView.getStatusCode());
            response.setHeader("Location", path);
            viewMonitor.viewRedirected(redirectView);
        } else if (view instanceof ResponderView) {
            ResponderView responderView = (ResponderView) view;
            responderView.respond(request, response);
            viewMonitor.viewResponded(responderView);
        } else {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);
            viewMonitor.viewForwarded(path);
        }
    }
}
