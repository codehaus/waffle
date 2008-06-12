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
package org.codehaus.waffle.bind;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementors of this class allow for properties from a controller to be exposed to the view as request attributes.
 * 
 * @author Michael Ward
 */
public interface ViewDataBinder {

    void bind(HttpServletRequest request, Object controller);
}
