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
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;

import javax.servlet.ServletContext;
import java.text.MessageFormat;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractContextContainerFactory implements ContextContainerFactory {
    protected final MessageResources messageResources;
    protected RegistrarAssistant registrarAssistant;
    protected ContextContainer applicationContextContainer;

    public AbstractContextContainerFactory(MessageResources messagesResources) {
        this.messageResources = messagesResources;
    }

    public RegistrarAssistant getRegistrarAssistant() {
        return registrarAssistant;
    }

    public void initialize(ServletContext servletContext) throws WaffleException {
        handleRegistrar(servletContext);
        servletContext.setAttribute(ContextContainerFactory.class.getName(), this); // register self to context
    }

    /**
     * Locate the Registrar from the ServletContext's InitParameter.
     *
     * @param servletContext
     */
    private void handleRegistrar(ServletContext servletContext) {
        String registrarClassName = servletContext.getInitParameter(Registrar.class.getName());

        try {
            ClassLoader loader = this.getClass().getClassLoader();
            Class clazz = loader.loadClass(registrarClassName);
            registrarAssistant = new RegistrarAssistant(clazz);
        } catch (ClassNotFoundException e) {
            String message = MessageFormat
                    .format("Unable to load the Registrar [{0}] defined as context-param in web.xml",
                            registrarClassName);
            throw new WaffleException(message, e);
        }

        // build application context container
        applicationContextContainer = buildApplicationContextContainer();
        applicationContextContainer.registerComponentInstance(servletContext);
        applicationContextContainer.registerComponentInstance(messageResources);
        buildApplicationLevelRegistry();
        applicationContextContainer.start();
    }

    public void destroy() {
        if (applicationContextContainer != null) {
            applicationContextContainer.stop();
            applicationContextContainer.dispose();
            applicationContextContainer = null;
        }
    }

    private void buildApplicationLevelRegistry() {
        Registrar registrar = createRegistrar(applicationContextContainer);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
    }

    public ContextContainer getApplicationContextContainer() {
        return applicationContextContainer;
    }

    protected abstract ContextContainer buildApplicationContextContainer();

    protected abstract Registrar createRegistrar(ContextContainer contextContainer);

}