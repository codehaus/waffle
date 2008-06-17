/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
