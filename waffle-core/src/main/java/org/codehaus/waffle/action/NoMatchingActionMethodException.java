package org.codehaus.waffle.action;

/**
 * Thrown when unable to find any matching methods to invoke.
 *
 * @author Paul Hammant
 */
@SuppressWarnings("serial")
public class NoMatchingActionMethodException extends MatchingActionMethodException {
    private final String methodName;
    private final Class<?> actionClass;

    public NoMatchingActionMethodException(String methodName, Class<?> actionClass) {
        super("no matching methods for: " + methodName);
        this.methodName = methodName;
        this.actionClass = actionClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }
}
