/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
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
 * ErrorMessage that requires a name/value combination associate to the message.
 *
 * @author Mauro Talevi
 */
public class FieldErrorMessage implements ErrorMessage {
    private String name;
    private String value;
    private String message;

    public FieldErrorMessage(String name, String value, String message) {
        this.name = name;
        this.value = value;
        this.message = message;
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
        sb.append("]");
        return sb.toString();
    }
}
