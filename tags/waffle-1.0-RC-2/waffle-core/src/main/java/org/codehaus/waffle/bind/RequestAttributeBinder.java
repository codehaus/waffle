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

import javax.servlet.http.HttpServletRequest;

/**
 * Implementors of this class allow for properties from a controller to be exposed as request attributes.  This
 * simplifies view development.
 * 
 * @author Michael Ward
 */
public interface RequestAttributeBinder {

    void bind(HttpServletRequest request, Object controller);
}
