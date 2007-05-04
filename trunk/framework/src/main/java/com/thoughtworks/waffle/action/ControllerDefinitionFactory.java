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
package com.thoughtworks.waffle.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementations of this interface are responsible for creating and
 * maintaining the "controller" objects (pojo's).
 *
 * @author Michael Ward
 */
public interface ControllerDefinitionFactory {

    /**
     * Implementors of this method should decipher the Servlet request passed
     * in and provide the associated controller instance (pojo).  The instance of
     * the Controller object and the key the controller was retrieved with are returned
     * in an ControllerDefinition.
     */
    ControllerDefinition getControllerDefinition(HttpServletRequest servletRequest,
                                                 HttpServletResponse response);
}
