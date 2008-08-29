/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.Constants;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Abstract context and session listener that uses a Waffle ComponentRegistry to retrieve the ContextContainerFactory
 * used to manage the components registered at each webapp scope.
 * 
 * @author Mike Ward
 * @author Mauro Talevi
 */
public abstract class WaffleContextListener implements ServletContextListener, HttpSessionListener {
    private ContextContainerFactory contextContainerFactory;

    /**
     * As the servlet context is being initialized Waffle needs to instantiate a component registry and add it to the
     * context so that it will be available through out. Next the Waffle context container factory is retrieved from the
     * registry and initialized with the servlet context.
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        // build component registry instance and add it to the ServletContext
        ComponentRegistry componentRegistry = buildComponentRegistry(servletContext);
        servletContext.setAttribute(ComponentRegistry.class.getName(), componentRegistry);

        contextContainerFactory = componentRegistry.getContextContainerFactory();
        contextContainerFactory.initialize(servletContext);
    }

    /**
     * As the servlet context is being destroyed, the context container factory is also destroyed.
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        contextContainerFactory.destroy();
    }

    /**
     * As the session is created a session-level context container is also created and started.
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        ContextContainer sessionContextContainer = contextContainerFactory.buildSessionLevelContainer();
        sessionContextContainer.registerComponentInstance(session);
        session.setAttribute(Constants.SESSION_CONTAINER_KEY, sessionContextContainer);
        sessionContextContainer.start();
    }

    /**
     * As the session is created the session-level context container is also stopped and disposed.
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        ContextContainer sessionContextContainer = (ContextContainer) session
                .getAttribute(Constants.SESSION_CONTAINER_KEY);
        sessionContextContainer.stop();
        sessionContextContainer.dispose();
    }

    /**
     * Concrete subclasses to provide a Waffle ComponentRegistry instance
     * 
     * @param servletContext
     * @return A ComponentRegistry
     */
    protected abstract ComponentRegistry buildComponentRegistry(ServletContext servletContext);

}
