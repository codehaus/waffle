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
package org.codehaus.waffle;

/**
 * Basic Exception type in the Waffle framework.
 *
 * @author Michael Ward
 */
@SuppressWarnings("serial")
public class WaffleException extends RuntimeException {
    public WaffleException() {
        super();
    }

    public WaffleException(String message) {
        super(message);
    }

    public WaffleException(String message, Throwable cause) {
        super(message, cause);
    }

    public WaffleException(Throwable cause) {
        super(cause);
    }
}
