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
package org.codehaus.waffle.action;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of this interface are responsible for resolving an action methods argument value by name.  The means
 * by which argument resolution is handled is to the discretion of the implementation
 * 
 * @author Michael Ward
 */
public interface ArgumentResolver {

    /**
     * Find the associated value for the argument name
     *
     * @param request is the current request
     * @param name the name of the argument being resolved
     * @return the arguments resolved value, or {@code null} if not resolved
     */
    Object resolve(HttpServletRequest request, String name);
}
