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
package org.codehaus.waffle.action;


/**
 * Thrown when no default action method is found
 *
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class NoDefaultActionMethodException extends MissingActionMethodException {

    public NoDefaultActionMethodException(String message) {
        super(message);
    }

}
