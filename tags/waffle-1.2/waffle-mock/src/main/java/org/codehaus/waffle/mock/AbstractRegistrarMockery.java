/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.mock;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;
import org.jmock.Mockery;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    
    public MessageResources mockMessageResources(){
        return messageResources;
    }

    public void assertConfiguration(Class<?> registrarClass) {
        assertApplicationContext(registrarClass);
        assertSessionContext(registrarClass);
        assertRequestContext(registrarClass);
    }
  
    public void assertApplicationContext(Class<?> registrarClass) {
        ContextContainer container = createContextContainer();
        container.registerComponentInstance(mockServletContext());
        container.registerComponentInstance(mockMessageResources());
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(registrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }

    public void assertSessionContext(Class<?> registrarClass) {
        ContextContainer container = createContextContainer();
        container.registerComponentInstance(mockServletContext());
        container.registerComponentInstance(mockHttpSession());
        container.registerComponentInstance(mockMessageResources());
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(registrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.SESSION);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }

    public void assertRequestContext(Class<?> customRegistrarClass) {
        ContextContainer container = createContextContainer();
        container.registerComponentInstance(mockServletContext());
        container.registerComponentInstance(mockHttpSession());
        container.registerComponentInstance(mockHttpServletRequest());
        container.registerComponentInstance(mockHttpServletResponse());
        container.registerComponentInstance(mockMessageResources());
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(customRegistrarClass);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.SESSION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.REQUEST);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }
    
    protected abstract ContextContainer createContextContainer();

    protected abstract Registrar createRegistrar(ContextContainer container);

    protected RegistrarMonitor getRegistrarMonitor(ContextContainer container) {
        RegistrarMonitor registrarMonitor = container.getComponent(RegistrarMonitor.class);
        if ( registrarMonitor == null ){
            registrarMonitor = new SilentMonitor();
            // TODO monitor it
        }
        return registrarMonitor;
    }

}