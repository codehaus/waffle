/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
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
 * Allows for a component dependency to be resolved from a {@code ServletContext} attribute.
 *
 * @author Michael Ward
 */
public class ServletContextAttributeReference extends AbstractReference {

    /**
     * @param key is the <code>String</code> specifying the name of the {@code ServletContext} attribute.
     */
    public ServletContextAttributeReference(String key) {
        super(key);
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     *
     * @param key is the <code>String</code> specifying the name of the {@code ServletContext} attribute.
     */
    public static ServletContextAttributeReference servletContextAttribute(String key) {
        return new ServletContextAttributeReference(key);
    }
}
