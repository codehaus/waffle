/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.monitor.ActionMonitor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handler that will make decisions based on what is returned from the action method:
 * 
 * <ol>
 *  <li>A View response will be directed (either redirected or forwarded)</li>
 *  <li>A ActionMethodException will set the response status and sends the message directly (perfect for ajax).</li>
 *  <li>Otherwise the response value will be sent directly to the browser as a String via Object.toString() method.</li>
 * </ol>
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultActionMethodResponseHandler implements ActionMethodResponseHandler {
    private final ViewDispatcher viewDispatcher;
    private final ActionMonitor actionMonitor;

    public DefaultActionMethodResponseHandler(ViewDispatcher viewDispatcher, ActionMonitor actionMonitor) {
        if (viewDispatcher == null) {
            throw new IllegalArgumentException("ViewDispatcher cannot be null");
        }
        if (actionMonitor == null) {
            throw new IllegalArgumentException("ActionMonitor cannot be null");
        }

        this.viewDispatcher = viewDispatcher;
        this.actionMonitor = actionMonitor;
    }

    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       ActionMethodResponse actionMethodResponse) throws IOException, ServletException {
        if (response.isCommitted()) {
            actionMonitor.responseIsCommitted(response);
            return; // do NOT go any further
        }

        Object returnValue = actionMethodResponse.getReturnValue();

        if (returnValue instanceof View) {
            View view = (View) returnValue;
            viewDispatcher.dispatch(request, response, view);
            actionMonitor.viewDispatched(view);
        } else if (returnValue instanceof ActionMethodException) {
            ActionMethodException exception = (ActionMethodException) returnValue;
            actionMonitor.actionMethodExecutionFailed(exception); 
            // todo ... this isn't really necessarily a true failure
            response.setStatus(exception.getStatusCode());
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
