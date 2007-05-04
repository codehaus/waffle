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
package org.codehaus.waffle.servlet;

import javax.servlet.ServletContext;

import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.WaffleContextListener;

import java.text.MessageFormat;

public class ServletContextHelper {

    private ServletContextHelper() {
        // should not be instantiated
    }

    public static WaffleComponentRegistry getWaffleComponentRegistry(ServletContext servletContext) {
        WaffleComponentRegistry componentRegistry = (WaffleComponentRegistry) servletContext
                .getAttribute(WaffleComponentRegistry.class.getName());

        if (componentRegistry == null) {
            String error = MessageFormat.format(
                    "Unable to locate a {0} from the ServletContext, make sure that {1} is registered as a listener in the web.xml",
                    WaffleComponentRegistry.class.getName(),
                    WaffleContextListener.class.getName());
            throw new WaffleException(error);
        }

        return componentRegistry;
    }

}
