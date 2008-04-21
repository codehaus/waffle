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



/**
 * ErrorMessage associated to global validations.
 *
 * @author Mauro Talevi
 */
public class GlobalErrorMessage extends AbstractErrorMessage implements ErrorMessage {
    private String message;
    
    public GlobalErrorMessage(String message) {
        this(message, null);
    }

    public GlobalErrorMessage(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public Type getType() {
        return Type.GLOBAL;
    }
    
    public String getMessage() {
        return message;
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
