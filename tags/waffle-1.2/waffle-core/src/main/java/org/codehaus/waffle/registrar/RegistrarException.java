/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import org.codehaus.waffle.WaffleException;

@SuppressWarnings("serial")
public class RegistrarException extends WaffleException {

    public RegistrarException(String message) {
        super(message);
    }

    public RegistrarException(String message, Throwable cause) {
        super(message, cause);
    }

}
