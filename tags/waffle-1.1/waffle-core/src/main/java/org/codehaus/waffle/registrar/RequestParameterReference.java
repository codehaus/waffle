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
 * <p>Allows for a component dependency to be resolved from a {@code ServletRequest} parameter</p>
 * <br/>
 * <p><b>NOTE:</b> This should only be utilized from {@link org.codehaus.waffle.registrar.Registrar#request()}.</p>
 *
 * @author Michael Ward
 */
public class RequestParameterReference extends AbstractReference {
    private final Object defaultValue;

    /**
     * @param key is the <code>String</code> specifying the name of the request parameter.
     */
    public RequestParameterReference(String key) {
        super(key);
        defaultValue = null;
    }

    /**
     * @param key is the <code>String</code> specifying the name of the request parameter.
     * @param defaultValue the default value to use if the parameter is not found, primitives types should NOT set a
     *        default value since the standard default value will be returned (e.g. int => 0, double => 0.0)
     */
    public RequestParameterReference(String key, Object defaultValue) {
        super(key);
        this.defaultValue = defaultValue;
    }

    /**
     * @return the default value to use if the parameter is not found, primitives types should NOT set a
     *         default value since the standard default value will be returned (e.g. int => 0, double => 0.0)
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     *
     * @param key is the <code>String</code> specifying the name of the request parameter.
     */
    public static RequestParameterReference requestParameter(String key) {
        // TODO mward: need to determine current context and if NOT 'request' an exception should be thrown!
        return new RequestParameterReference(key);
    }

    /**
     * This method can be statically imported into an Application's Registrar allowing
     * for a more fluent interface to define components and their dependencies
     *
     * @param key is the <code>String</code> specifying the name of the request parameter.
     * @param defaultValue the default value to use if the parameter is not found, primitives types should NOT set a
     *        default value since the standard default value will be returned (e.g. int => 0, double => 0.0)
     */
    public static RequestParameterReference requestParameter(String key, Object defaultValue) {
        // TODO mward: need to determine current context and if NOT 'request' an exception should be thrown!
        return new RequestParameterReference(key, defaultValue);
    }
}