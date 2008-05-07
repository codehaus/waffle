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
