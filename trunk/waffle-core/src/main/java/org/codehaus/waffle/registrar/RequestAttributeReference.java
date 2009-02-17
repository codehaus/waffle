/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

/**
 * <p>Allows for a component dependency to be resolved from a {@code ServletRequest} attribute.</p>
 * <br/>
 * <p><b>NOTE:</b> This should only be utilized from {@link org.codehaus.waffle.registrar.Registrar#request()}.</p>
 *
 * @author Michael Ward
 */
public class RequestAttributeReference extends AbstractReference {

    /**
     * @param key is the <code>String</code> specifying the name of the request attribute.
     */
    public RequestAttributeReference(String key) {
        super(key);
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     *
     * @param key is the <code>String</code> specifying the name of the request attribute.
     */
    public static RequestAttributeReference requestAttribute(String key) {
        // TODO mward: need to determine current context and if NOT 'request' an exception should be thrown!
        return new RequestAttributeReference(key);
    }
    
}
