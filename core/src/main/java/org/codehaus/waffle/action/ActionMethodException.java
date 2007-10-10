/*****************************************************************************
 * Copyright (C) 2005 - 2007 Michael Ward                                    *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.action;

import org.codehaus.waffle.WaffleException;

/**
 * This is a specialized exception that will be thrown directly from an ActionMethod.  Exceptions of this type
 * will set the response statusCode code and response body.
 */
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

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

}
