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
package org.codehaus.waffle.action;

import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This handler will make decisions based on what is returned from the action method. For example:
 * <p/>
 * - A View response indicates which view the user should be directed (either redirected or forwarded) to.
 * <p/>
 * - An exception sets the response status to 400 and sends the mesage directly (perfect for ajax)
 * <p/>
 * - otherwise the response value will be sent directly to the browser as a String via Object.toString().
 *
 * @author Michael Ward
 */
public class DefaultActionMethodResponseHandler implements ActionMethodResponseHandler {
    private final ViewDispatcher viewDispatcher;

    public DefaultActionMethodResponseHandler(ViewDispatcher viewDispatcher) {
        if (viewDispatcher == null) {
            throw new IllegalArgumentException("ViewDispatcher cannot be null");
        }

        this.viewDispatcher = viewDispatcher;
    }

    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       ActionMethodResponse actionMethodResponse) throws IOException, ServletException {
        if (response.isCommitted()) {
            return; // do NOT go any further
        }

        Object returnValue = actionMethodResponse.getReturnValue();

        if (returnValue instanceof View) {
            View view = (View) returnValue;
            viewDispatcher.dispatch(request, response, view);
        } else if (returnValue instanceof Exception) {
            Exception exception = (Exception) returnValue;
            // todo log this occurance
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            handleResponse(response, exception.getMessage());
        } else {
            handleResponse(response, returnValue.toString());
        }
    }

    protected void handleResponse(HttpServletResponse response, String message) throws IOException {
        response.getOutputStream().print(message);
        response.flushBuffer();
    }

}
