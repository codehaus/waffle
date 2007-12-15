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
package org.codehaus.waffle.bind;

import org.codehaus.waffle.validation.ErrorsContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementor of this interface are responsible for binding the values from
 * the request to the model.
 *
 * @author Michael Ward
 */
public interface DataBinder {
    
    void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object model);
}
