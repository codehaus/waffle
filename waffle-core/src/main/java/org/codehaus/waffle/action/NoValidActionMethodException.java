/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;


/**
 * Thrown when method is no valid action method is found
 *
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class NoValidActionMethodException extends MissingActionMethodException {

    public NoValidActionMethodException(String message) {
        super(message);
    }

}
