/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import org.codehaus.waffle.WaffleException;

/**
 * Thrown when a registrar class defined in the application web.xml is invalid.
 * 
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class InvalidRegistrarException extends WaffleException {

    public InvalidRegistrarException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRegistrarException(Throwable cause) {
        super(cause);
    }

}
