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

public class RequestParameterReference extends AbstractReference {
    private final Object defaultValue;

    public RequestParameterReference(String key) {
        super(key);
        defaultValue = null;
    }

    public RequestParameterReference(String key, Object defaultValue) {
        super(key);
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     */
    public static RequestParameterReference requestParameter(String key) {
        return new RequestParameterReference(key);
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     *
     * @param key the parameter name to resolve against
     * @param defaultValue the default value to use if the parameter is not found, primitives types should NOT set a
     *        default value since the standard default value will be returned (e.g. int => 0, double => 0.0)
     */
    public static RequestParameterReference requestParameter(String key, Object defaultValue) {
        return new RequestParameterReference(key, defaultValue);
    }
}