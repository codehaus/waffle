/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

/**
 * Allows for a component dependency to be resolved from another component registered with Waffle.  This allows more
 * than one implementation to be registered with Waffle without causing ambiguity issues.
 *
 * @author Michael Ward
 */
public class ComponentReference extends AbstractReference {

    /**
     * @param key is the key the dependent component was registered under (usually a {@code String} or {@code Class})
     */
    public ComponentReference(Object key) {
        super(key);
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     *
     * @param key is the key the dependent component was registered under (usually a {@code String} or {@code Class})
     */
    public static ComponentReference component(Object key) {
        return new ComponentReference(key);
    }

}
