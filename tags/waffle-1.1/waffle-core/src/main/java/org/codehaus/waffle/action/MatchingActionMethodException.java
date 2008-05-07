package org.codehaus.waffle.action;

/**
 * Thrown when matching methods are hard to identify for invocation.
 *
 * @author Paul Hammant
 */
@SuppressWarnings("serial")
public class MatchingActionMethodException extends ActionMethodInvocationException {

    public MatchingActionMethodException(String message) {
        super(message);
    }

}
