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
package org.codehaus.waffle.registrar.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;

import javax.servlet.ServletContext;

/**
 * Will resolve value from the {@code ServletContext} attribute.
 *
 * @author Michael Ward
 */
class ServletContextAttributeParameter extends AbstractWaffleParameter {

    protected ServletContextAttributeParameter(String key) {
        super(key);
    }

    public Object resolveInstance(PicoContainer picoContainer, ComponentAdapter componentAdapter, Class type) {
        ServletContext servletContext = (ServletContext) picoContainer
                .getComponentInstanceOfType(ServletContext.class);
        return servletContext.getAttribute(getKey());
    }
}
