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
package org.codehaus.waffle.action;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of this interface will be able to determine the action method name that is to be invoked.
 */
public interface MethodNameResolver {

    /**
     * Find the method name to be invoked
     *
     * @param request is the current request
     * @return the name of the method that is to be invoked
     */
    String resolve(HttpServletRequest request);
}
