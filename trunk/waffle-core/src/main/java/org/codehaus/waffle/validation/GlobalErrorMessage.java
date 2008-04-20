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

import java.util.ArrayList;
import java.util.List;


/**
 * ErrorMessage associated to global validations.
 *
 * @author Mauro Talevi
 */
public class GlobalErrorMessage implements ErrorMessage {
    private String message;
    private Throwable cause;

    public GlobalErrorMessage(String message) {
        this(message, null);
    }

    public GlobalErrorMessage(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    public Type getType() {
        return Type.GLOBAL;
    }
    
    public String getMessage() {
        return message;
    }
    
    public List<String> getStackMessages(){
        List<String> messages = new ArrayList<String>();
        addStackMessages(cause, messages);
        return messages;        
    }

    public Throwable getCause(){
        return cause;
    }
    
    private void addStackMessages(Throwable cause, List<String> messages) {
        if ( cause != null ){
            messages.add(cause.getMessage());
            addStackMessages(cause.getCause(), messages);
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[GlobalErrorMessage message=");
        sb.append(message);
        sb.append(", stackMessages=");
        sb.append(getStackMessages());
        sb.append("]");
        return sb.toString();
    }

}
