/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
