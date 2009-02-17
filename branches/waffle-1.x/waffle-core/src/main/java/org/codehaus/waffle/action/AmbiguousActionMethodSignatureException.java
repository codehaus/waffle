/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;


/**
 * Thrown when unable to determine which method to invoke.
 *
 * @author Michael Ward
 */
@SuppressWarnings("serial")
public class AmbiguousActionMethodSignatureException extends MatchingActionMethodException {

    public AmbiguousActionMethodSignatureException(String message) {
        super(message);
    }

}
