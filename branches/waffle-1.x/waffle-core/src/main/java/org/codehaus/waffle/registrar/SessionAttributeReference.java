/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

/**
 * Allows for a component dependency to be resolved from a {@code HttpSession} attribute.
 *
 * @author Michael Ward
 */
public class SessionAttributeReference extends AbstractReference {

    /**
     * @param key is the <code>String</code> specifying the name of the {@code HttpSession} attribute.
     */
    public SessionAttributeReference(String key) {
        super(key);
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     *
     * @param key is the <code>String</code> specifying the name of the {@code HttpSession} attribute.
     */
    public static SessionAttributeReference sessionAttribute(String key) {
        return new SessionAttributeReference(key);
    }
}
