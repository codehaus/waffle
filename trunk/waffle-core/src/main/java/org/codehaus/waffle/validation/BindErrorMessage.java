/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.validation;

/**
 * ErrorMessage associated to bind validations.
 * 
 * @author Mauro Talevi
 */
public class BindErrorMessage extends FieldErrorMessage {

    public BindErrorMessage(String name, String value, String message) {
        this(name, value, message, null);
    }
    
    public BindErrorMessage(String name, String value, String message, Throwable cause) {
        super(name, value, message, cause);
    }
    
    public Type getType() {
        return Type.BIND;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[BindErrorMessage name=");
        sb.append(getName());
        sb.append(", value=");
        sb.append(getValue());
        sb.append(", message=");
        sb.append(getMessage());
        sb.append(", stackMessages=");
        sb.append(getStackMessages());
        sb.append("]");
        return sb.toString();
    }

}
