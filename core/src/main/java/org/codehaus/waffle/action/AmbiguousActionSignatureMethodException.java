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
package org.codehaus.waffle.action;


/**
 * Thrown when unable to determine which method to invoke.
 *
 * @author Michael Ward
 */
public class AmbiguousActionSignatureMethodException extends MatchingActionMethodException {

    public AmbiguousActionSignatureMethodException(String message) {
        super(message);
    }

}
