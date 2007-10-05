package org.codehaus.waffle.action;

/**
 * Thrown when missing methods are identified.
 *
 * @author Mauro Talevi
 */
public class MissingActionMethodException extends ActionMethodInvocationException {

    public MissingActionMethodException(String message) {
        super(message);
    }

}
