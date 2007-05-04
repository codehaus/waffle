package com.thoughtworks.waffle.action.method;

/**
 * Thrown when unable to find any matching methods to invoke.
 *
 * @author Paul Hammant
 */
public class NoMatchingMethodException extends MatchingMethodException {
    private final String methodName;
    private final Class actionClass;

    public NoMatchingMethodException(String methodName, Class actionClass) {
        super("no matching methods for: " + methodName);
        this.methodName = methodName;
        this.actionClass = actionClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class getActionClass() {
        return actionClass;
    }
}
