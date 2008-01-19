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
package org.codehaus.waffle.registrar.pico;

import org.picocontainer.Parameter;

/**
 * Implementations of this interface will find the correct PicoContainer Parameter needed based on the
 * argument being resolved
 *
 * @author Michael Ward
 */
public interface ParameterResolver {

    /**
     * Find the correct Parameter
     *
     * @param arg the argument to be resolved
     * @return the correct Parameter.
     */
    Parameter resolve(Object arg);
}
