/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.ComponentRegistry;

/**
 * Abstract context and session listener that uses a Waffle ComponentRegistry to 
 * retrieve the ContextContainerFactory used to manage the components registered at each webapp scope.
 * 
 * @author Mike Ward
 * @author Mauro Talevi
 */
public abstract class WaffleContextListener implements ServletContextListener, HttpSessionListener {
    private ContextContainerFactory contextContainerFactory;

    /**
     * The ServletContext is being initialized Waffle needs to instantiate a Waffle ComponentRegistry
     * and register to the ServletContext so that it will be avcilable through out.  Next the Waffle
     * ContextManager is retreived from the componentRegistry and the initialize message is delegated
     * to it.
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        // build instance and register it with the ServletContext
        ComponentRegistry componentRegistry = buildComponentRegistry(servletContext);
        servletContext.setAttribute(ComponentRegistry.class.getName(), componentRegistry);

        contextContainerFactory = componentRegistry.getContextContainerFactory();
        contextContainerFactory.initialize(servletContext);
    }

    /**
     * The servlet context is being destroyed it is time to cleanup.
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        contextContainerFactory.destroy();
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

    /**
     * Concrete subclasses to provide a Waffle ComponentRegistry instance
     * @param servletContext 
     * @return A ComponentRegistry
     */
    protected abstract ComponentRegistry buildComponentRegistry(ServletContext servletContext);


}
