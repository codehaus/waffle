/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.validation;

import java.util.List;

/**
 * Represents a validation error message
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface ErrorMessage {

    enum Type { BIND, FIELD, GLOBAL }
    
    /**
     * Returns the type of error message
     * 
     * @return The Type
     */
    Type getType();

    /**
     * Returns the formatted message
     * 
     * @return The message
     */
    String getMessage();

    /**
     * Returns the list of messages for the exception stack that cause the error,
     * ie the list of Throwable.getMessage() from the stack.
     * 
     * @return The List of stack messages
     */
    List<String> getStackMessages();

    /**
     * Returns the throwable that caused the error
     * 
     * @return The Throwable cause
     */
    Throwable getCause();
    
}
