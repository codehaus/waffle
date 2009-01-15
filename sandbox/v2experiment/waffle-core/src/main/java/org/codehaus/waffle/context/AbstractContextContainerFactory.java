/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import javax.servlet.ServletContext;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;
import org.picocontainer.MutablePicoContainer;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractContextContainerFactory implements ContextContainerFactory {
    protected final MessageResources messageResources;
    protected RegistrarAssistant registrarAssistant;
    protected ContextContainer applicationContextContainer;
    private final ContextMonitor contextMonitor;

    public AbstractContextContainerFactory(MessageResources messageResources, ContextMonitor contextMonitor) {
        this.messageResources = messageResources;
        this.contextMonitor = contextMonitor;
    }

    public RegistrarAssistant getRegistrarAssistant() {
        return registrarAssistant;
    }

    public void initialize(ServletContext servletContext) throws WaffleException {
        try {
            initializeRegistrar(servletContext);
            servletContext.setAttribute(ContextContainerFactory.class.getName(), this); // register self to context
            contextMonitor.contextInitialized();
        } catch (WaffleException e) {
            contextMonitor.contextInitializationFailed(e);
            throw e; // re-throwing exception after failure event has been monitored
        }
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
            registrarAssistant = new RegistrarAssistant(registrarClass, messageResources);
        } catch (ClassNotFoundException e) {
            contextMonitor.registrarNotFound(registrarClassName);
            String message = messageResources.getMessageWithDefault("registrarNotFound",
                    "Registrar ''{0}'' defined as context-param in web.xml could not be found.", registrarClassName);
            throw new WaffleException(message, e);
        }

        // build application context container
        applicationContextContainer = buildApplicationContextContainer();
        applicationContextContainer.addComponent(servletContext);
        applicationContextContainer.addComponent(messageResources);

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

    protected ContextMonitor getContextMonitor() {
        return contextMonitor;
    }

    protected abstract ContextContainer buildApplicationContextContainer();

    protected abstract Registrar createRegistrar(MutablePicoContainer contextContainer);

}
