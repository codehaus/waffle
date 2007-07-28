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
import javax.servlet.http.HttpServletResponse;

/**
 * Finds method definitions in the controller using the parameters specified in the request
 * 
 * @author Michael Ward
 */
public interface MethodDefinitionFinder {

    /**
     * Returns a method definition of a given controller
     * 
     * @param controller the controller Object
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @return The MethodDefinition found
     */
    MethodDefinition find(Object controller,
                          HttpServletRequest request,
                          HttpServletResponse response);

}
