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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * The ViewDispatcher handles redirecting/forwarding to the view
 *
 * @author Michael Ward
 * @author Paulo Silveira
 */
public class DefaultViewDispatcher implements ViewDispatcher {
    private final ViewResolver viewResolver;
    private final DispatchAssistant dispatchAssistant;

    public DefaultViewDispatcher(ViewResolver viewResolver, DispatchAssistant dispatchAssistant) {
        this.viewResolver = viewResolver;
        this.dispatchAssistant = dispatchAssistant;
    }

    // todo may need to handle ... http://java.sun.com/products/servlet/Filters.html for Character Encoding from request
    public void dispatch(HttpServletRequest request,
                         HttpServletResponse response,
                         View view) throws IOException, ServletException {
        String url = viewResolver.resolve(view);

        if (view instanceof RedirectView) {
            Map model = ((RedirectView)view).getModel();
            dispatchAssistant.redirect(request, response, model, url);
        } else if (view instanceof ResponderView) {
        	((ResponderView)view).respond(request, response);
        }else {
            dispatchAssistant.forward(request, response, url);
        }
    }
}
