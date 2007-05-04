package com.thoughtworks.waffle.action.method;

/**
 * Thrown when matching methods are hard to identify for invocation.
 *
 * @author Paul Hammant
 */
public class MatchingMethodException extends MethodInvocationException {

    public MatchingMethodException(String message) {
        super(message);
    }

}
