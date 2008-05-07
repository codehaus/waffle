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
