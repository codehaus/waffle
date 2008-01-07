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
 * ErrorMessage associated to bind validations.
 * 
 * @author Mauro Talevi
 */
public class BindErrorMessage extends FieldErrorMessage {

    public BindErrorMessage(String name, String value, String message) {
        super(name, value, message);
    }
    
    public Type getType() {
        return Type.BIND;
    }
}
