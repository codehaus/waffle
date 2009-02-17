/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
