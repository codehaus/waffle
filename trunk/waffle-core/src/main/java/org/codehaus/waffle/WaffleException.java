/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle;

/**
 * <p>The base Exception that all other Waffle exceptions extend.</p>
 *
 * @author Michael Ward
 */
@SuppressWarnings("serial")
public class WaffleException extends RuntimeException {
    public WaffleException() {
        super();
    }

    public WaffleException(String message) {
        super(message);
    }

    public WaffleException(String message, Throwable cause) {
        super(message, cause);
    }

    public WaffleException(Throwable cause) {
        super(cause);
    }
}
