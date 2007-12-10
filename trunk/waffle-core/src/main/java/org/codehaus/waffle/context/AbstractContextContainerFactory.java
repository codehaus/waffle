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

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;

import javax.servlet.ServletContext;
import static java.text.MessageFormat.format;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractContextContainerFactory implements ContextContainerFactory {
    protected final MessageResources messageResources;
    protected RegistrarAssistant registrarAssistant;
    protected ContextContainer applicationContextContainer;
    private final ContextMonitor contextMonitor;

    public AbstractContextContainerFactory(MessageResources messagesResources, ContextMonitor contextMonitor) {
        this.messageResources = messagesResources;
        this.contextMonitor = contextMonitor;
    }

    public RegistrarAssistant getRegistrarAssistant() {
        return registrarAssistant;
    }

    public void initialize(ServletContext servletContext) throws WaffleException {
        initializeRegistrar(servletContext);
        servletContext.setAttribute(ContextContainerFactory.class.getName(), this); // register self to context
        contextMonitor.contextInitialized();
    }

    /**
     * Create the Registrar from the ServletContext's InitParameter.
     *
     * @param servletContext
     */
    private void initializeRegistrar(ServletContext servletContext) {
        String registrarClassName = servletContext.getInitParameter(Registrar.class.getName());

        try {
            ClassLoader loader = this.getClass().getClassLoader();
            Class<?> registrarClass = loader.loadClass(registrarClassName);
            registrarAssistant = new RegistrarAssistant(registrarClass);
        } catch (ClassNotFoundException e) {
            contextMonitor.registrarNotFound(registrarClassName);
            String message = format("Unable to load the Registrar [{0}] defined as context-param in web.xml",
                            registrarClassName);
            throw new WaffleException(message, e);
        }

        // build application context container
        applicationContextContainer = buildApplicationContextContainer();
        applicationContextContainer.registerComponentInstance(servletContext);
        applicationContextContainer.registerComponentInstance(messageResources);

        buildApplicationLevelRegistry();
        applicationContextContainer.start();
        contextMonitor.applicationContextContainerStarted();
    }

    public void destroy() {
        if (applicationContextContainer != null) {
            applicationContextContainer.stop();
            applicationContextContainer.dispose();
            applicationContextContainer = null;
            contextMonitor.applicationContextContainerDestroyed();
        }
    }

    private void buildApplicationLevelRegistry() {
        Registrar registrar = createRegistrar(applicationContextContainer);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
    }

    public ContextContainer getApplicationContextContainer() {
        return applicationContextContainer;
    }

    protected ContextMonitor getContextMonitor(){
        return contextMonitor;
    }
    
    protected abstract ContextContainer buildApplicationContextContainer();

    protected abstract Registrar createRegistrar(ContextContainer contextContainer);

}