/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import org.codehaus.waffle.WaffleException;

/**
 * <p>This is a specialized exception that will be thrown directly from an ActionMethod.  Exceptions of this type
 * will set the response statusCode code and response body.</p>
 */
@SuppressWarnings("serial")
public class ActionMethodException extends WaffleException {
    private final int statusCode;
    private final String message;

    /**
     * An expected error has occurred the requestor should be notified with the appropriate status and message.
     *
     * @param statusCode the value the response status should be set to
     * @param message the text to be returned
     */
    public ActionMethodException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * Returns the the {@code javax.servlet.http.HttpServletResponse} status should be set to.
     */
    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

}
