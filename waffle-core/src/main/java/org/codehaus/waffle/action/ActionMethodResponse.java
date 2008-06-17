/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
