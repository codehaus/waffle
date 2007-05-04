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
package com.thoughtworks.waffle.context;

import com.thoughtworks.waffle.WaffleException;
import com.thoughtworks.waffle.i18n.MessageResources;
import com.thoughtworks.waffle.registrar.Registrar;
import com.thoughtworks.waffle.registrar.RegistrarAssistant;

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
            Class clazz = this.getClass().getClassLoader().loadClass(registrarClassName);
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

    public abstract ContextContainer buildApplicationContextContainer();

    public abstract Registrar createRegistrar(ContextContainer contextContainer);

}