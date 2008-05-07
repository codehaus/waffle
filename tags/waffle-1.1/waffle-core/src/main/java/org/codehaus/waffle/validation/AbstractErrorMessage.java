/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all error messages. Handles the error cause and the retrieval of stack messages.
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractErrorMessage implements ErrorMessage {

    protected Throwable cause;

    protected AbstractErrorMessage(Throwable cause) {
        this.cause = cause;
    }

    public List<String> getStackMessages() {
        List<String> messages = new ArrayList<String>();
        addStackMessages(cause, messages);
        return messages;
    }

    public Throwable getCause() {
        return cause;
    }

    void addStackMessages(Throwable cause, List<String> messages) {
        if (cause != null) {
            messages.add(cause.getMessage());
            addStackMessages(cause.getCause(), messages);
        }
    }

}
