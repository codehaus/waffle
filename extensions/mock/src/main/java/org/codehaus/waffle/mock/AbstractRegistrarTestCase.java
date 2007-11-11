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

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;

/**
 * Abstract jMock 1.x MockObjectTestCase used for asserting Registrars are defined correctly. 
 * Concrete subclasses need to define method to create the ContextContainer and the Registrar.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractRegistrarTestCase extends MockObjectTestCase {
    private Mock mockServletContext = mock(ServletContext.class);
    private Mock mockHttpSession = mock(HttpSession.class);
    private Mock mockHttpServletRequest = mock(HttpServletRequest.class);
    private Mock mockHttpServletResponse = mock(HttpServletResponse.class);
    private Mock mockMessageResources = mock(MessageResources.class);

    public Mock getMockServletContext() {
        return mockServletContext;
    }

    public Mock getMockHttpSession() {
        return mockHttpSession;
    }

    public Mock getMockHttpServletRequest() {
        return mockHttpServletRequest;
    }

    public Mock getMockHttpServletResponse() {
        return mockHttpServletResponse;
    }

    public void setMockHttpServletResponse(Mock mockHttpServletResponse) {
        this.mockHttpServletResponse = mockHttpServletResponse;
    }

    public void assertConfiguration(Class<?> registrarClass) {
        assertApplicationContext(registrarClass);
        assertSessionContext(registrarClass);
        assertRequestContext(registrarClass);
    }

    public void assertApplicationContext(Class<?> customRegistrarClass) {
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        ContextContainer container = createContextContainer();
        container.registerComponentInstance(servletContext);
        container.registerComponentInstance(mockMessageResources.proxy());
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(customRegistrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }

    public void assertSessionContext(Class<?> customRegistrarClass) {
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        HttpSession httpSession = (HttpSession) mockHttpSession.proxy();

        ContextContainer container = createContextContainer();
        container.registerComponentInstance(servletContext);
        container.registerComponentInstance(httpSession);
        container.registerComponentInstance(mockMessageResources.proxy());
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(customRegistrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.SESSION);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }

    public void assertRequestContext(Class<?> customRegistrarClass) {
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        HttpSession httpSession = (HttpSession) mockHttpSession.proxy();
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();
        HttpServletResponse response = (HttpServletResponse) mockHttpServletResponse.proxy();

        ContextContainer container = createContextContainer();
        container.registerComponentInstance(servletContext);
        container.registerComponentInstance(httpSession);
        container.registerComponentInstance(request);
        container.registerComponentInstance(response);
        container.registerComponentInstance(mockMessageResources.proxy());
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
