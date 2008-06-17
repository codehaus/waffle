/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
