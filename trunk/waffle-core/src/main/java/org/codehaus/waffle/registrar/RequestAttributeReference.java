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
