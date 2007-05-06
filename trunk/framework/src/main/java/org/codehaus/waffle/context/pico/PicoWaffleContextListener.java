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
package org.codehaus.waffle.context.pico;

import javax.servlet.ServletContext;

import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.context.WaffleContextListener;

/**
 * Pico-based WaffleContextListener that uses PicoWaffleComponentRegistry instances.
 * 
 * @author Mauro Talevi
 */
public class PicoWaffleContextListener extends WaffleContextListener {

    protected WaffleComponentRegistry buildWaffleComponentRegistry(ServletContext servletContext) {
        return new PicoWaffleComponentRegistry(servletContext);
    }

}
