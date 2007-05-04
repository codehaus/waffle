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
package org.codehaus.waffle.context;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.servlet.PicoWaffleComponentRegistry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class WaffleContextListener implements ServletContextListener, HttpSessionListener {
    private ContextContainerFactory contextContainerFactory;

    /**
     * The ServletContext is being initialized Waffle needs to instantiate a WaffleComponentRegistry
     * and register to the ServletContext so that it will be avcilable through out.  Next the Waffle
     * ContextManager is retreived from the componentRegistry and the initialize message is delegated
     * to it.
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        // build instance and register it with the ServletContext
        WaffleComponentRegistry componentRegistry = buildWaffleComponentRegistry(servletContext);
        servletContext.setAttribute(WaffleComponentRegistry.class.getName(), componentRegistry);

        contextContainerFactory = componentRegistry.getContextContainerFactory();
        contextContainerFactory.initialize(servletContext);
    }

    /**
     * The servlet context is being destroyed it is time to cleanup.
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        contextContainerFactory.destroy();
    }

    protected WaffleComponentRegistry buildWaffleComponentRegistry(ServletContext servletContext) {
        return new PicoWaffleComponentRegistry(servletContext);
    }

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();

        ContextContainer sessionContextContainer = contextContainerFactory.buildSessionLevelContainer();
        sessionContextContainer.registerComponentInstance(session);
        session.setAttribute(Constants.SESSION_CONTAINER_KEY, sessionContextContainer);
        sessionContextContainer.start();
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();

        ContextContainer sessionContextContainer = (ContextContainer) session.getAttribute(Constants.SESSION_CONTAINER_KEY);
        sessionContextContainer.stop();
        sessionContextContainer.dispose();
    }

}
