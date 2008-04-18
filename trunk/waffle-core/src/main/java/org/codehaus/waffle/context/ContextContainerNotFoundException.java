package org.codehaus.waffle.context;

import org.codehaus.waffle.WaffleException;

/**
 * Thrown when context controller is not found in request
 * 
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class ContextContainerNotFoundException extends WaffleException {
 
    public ContextContainerNotFoundException(String message) {
        super(message);
    }
 
}
