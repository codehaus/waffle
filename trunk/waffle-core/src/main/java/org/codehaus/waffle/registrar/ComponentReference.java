/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.registrar;

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
