/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.validation;


/**
 * ErrorMessage associated to field validations.
 * 
 * @author Mauro Talevi
 */
public class FieldErrorMessage extends AbstractErrorMessage {
    
    private String name;
    private String value;
    private String message;

    public FieldErrorMessage(String name, String value, String message) {
        this(name, value, message, null);
    }

    public FieldErrorMessage(String name, String value, String message, Throwable cause) {
        super(cause);
        this.name = name;
        this.value = value;
        this.message = message;
    }

    public Type getType() {
        return Type.FIELD;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[FieldErrorMessage name=");
        sb.append(name);
        sb.append(", value=");
        sb.append(value);
        sb.append(", message=");
        sb.append(message);
        sb.append(", stackMessages=");
        sb.append(getStackMessages());
        sb.append("]");
        return sb.toString();
    }

}
