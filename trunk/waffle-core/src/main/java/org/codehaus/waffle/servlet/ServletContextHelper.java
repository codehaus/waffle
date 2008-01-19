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
package org.codehaus.waffle.servlet;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.WaffleContextListener;

import javax.servlet.ServletContext;
import java.text.MessageFormat;

/**
 * @author Michael Ward
 */
public class ServletContextHelper {

    private ServletContextHelper() {
        // should not be instantiated
    }

    /**
     * Allows access to Waffle core components
     *
     * @param servletContext
     * @return the ComponentRegistry for the running application
     */
    public static ComponentRegistry getComponentRegistry(ServletContext servletContext) {
        ComponentRegistry componentRegistry = (ComponentRegistry) servletContext
                .getAttribute(ComponentRegistry.class.getName());

        if (componentRegistry == null) {
            String error = MessageFormat.format(
                    "Unable to locate a {0} from the ServletContext, make sure that {1} is registered as a listener in the web.xml",
                    ComponentRegistry.class.getName(),
                    WaffleContextListener.class.getName());
            throw new WaffleException(error);
        }

        return componentRegistry;
    }

}
