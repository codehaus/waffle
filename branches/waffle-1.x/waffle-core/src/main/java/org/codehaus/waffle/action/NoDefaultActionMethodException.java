/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
