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
package org.codehaus.waffle.mock;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;
import org.jmock.Mockery;

/**
 * Abstract jMock 2.x Mockery for asserting Registrars are defined correctly.
 * Concrete subclasses need to define method to create the ContextContainer
 * and the Registrar.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractRegistrarMockery extends Mockery {
    
    private ServletContext servletContext = mock(ServletContext.class);
    private HttpSession httpSession = mock(HttpSession.class);
    private HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    private HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    private MessageResources messageResources = mock(MessageResources.class);

    public ServletContext mockServletContext() {
        return servletContext;
    }

    public HttpSession mockHttpSession() {
        return httpSession;
    }

    public HttpServletRequest mockHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse mockHttpServletResponse() {
        return httpServletResponse;
    }

    public void assertConfiguration(Class<?> registrarClass) {
        assertApplicationContext(registrarClass);
        assertSessionContext(registrarClass);
        assertRequestContext(registrarClass);
    }
  
    public void assertApplicationContext(Class<?> registrarClass) {
        ContextContainer container = createContextContainer();
        container.registerComponentInstance(servletContext);
        container.registerComponentInstance(messageResources);
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(registrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }

    public void assertSessionContext(Class<?> registrarClass) {
        ContextContainer container = createContextContainer();
        container.registerComponentInstance(servletContext);
        container.registerComponentInstance(httpSession);
        container.registerComponentInstance(messageResources);
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(registrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.SESSION);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }

    public void assertRequestContext(Class<?> customRegistrarClass) {

        ContextContainer container = createContextContainer();
        container.registerComponentInstance(servletContext);
        container.registerComponentInstance(httpSession);
        container.registerComponentInstance(httpServletRequest);
        container.registerComponentInstance(httpServletResponse);
        container.registerComponentInstance(messageResources);
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(customRegistrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.SESSION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.REQUEST);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }
    
    protected abstract ContextContainer createContextContainer();

    protected abstract Registrar createRegistrar(ContextContainer container);

}
