/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import org.codehaus.waffle.WaffleException;

@SuppressWarnings("serial")
public class BindException extends WaffleException {
    public BindException() {
    }

    public BindException(String message) {
        super(message);
    }

    public BindException(String message, Throwable cause) {
        super(message, cause);
    }

    public BindException(Throwable cause) {
        super(cause);
    }
}
