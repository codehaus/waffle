package org.codehaus.waffle.action;

/**
 * Thrown when missing methods are identified.
 *
 * @author Mauro Talevi
 */
public class MissingMethodException extends MethodInvocationException {

    public MissingMethodException(String message) {
        super(message);
    }

}
