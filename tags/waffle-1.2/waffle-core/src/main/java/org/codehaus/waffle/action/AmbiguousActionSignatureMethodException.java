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
public class AmbiguousActionSignatureMethodException extends MatchingActionMethodException {

    public AmbiguousActionSignatureMethodException(String message) {
        super(message);
    }

}
