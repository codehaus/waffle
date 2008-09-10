/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

/**
 * Thrown when missing methods are identified.
 *
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class MissingActionMethodException extends ActionMethodInvocationException {

    public MissingActionMethodException(String message) {
        super(message);
    }

}
