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
 * A holder object which represents the return value from the action method fired
 *
 * @author Michael Ward
 */
public class ActionMethodResponse {
    private Object returnValue;

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[ActionMethodResponse returnValue=")
                .append(returnValue)
                .append("]")
                .toString();
    }
}
