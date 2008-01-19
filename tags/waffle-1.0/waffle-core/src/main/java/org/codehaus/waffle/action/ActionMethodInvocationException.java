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

import org.codehaus.waffle.WaffleException;

/**
 * Thrown when Waffle is unable to invoke the Action method.
 *
 * @author Michael Ward
 */
@SuppressWarnings("serial")
public class ActionMethodInvocationException extends WaffleException {

    public ActionMethodInvocationException(String message) {
        super(message);
    }

    public ActionMethodInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

}
