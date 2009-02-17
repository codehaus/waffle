/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.mock;

import static org.codehaus.waffle.context.ContextLevel.APPLICATION;
import static org.codehaus.waffle.context.ContextLevel.REQUEST;
import static org.codehaus.waffle.context.ContextLevel.SESSION;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;
import org.jmock.Mockery;

/**
 * Abstract Mockery for asserting Registrars are defined correctly.
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

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(registrarClass, messageResources);
        registrarAssistant.executeDelegatingRegistrar(registrar, APPLICATION);

        container.validateComponentInstances(); // ensure dependencies are satisfied
    }

    public void assertSessionContext(Class<?> registrarClass) {
        ContextContainer container = createContextContainer();
        container.registerComponentInstance(mockServletContext());
        container.registerComponentInstance(mockHttpSession());
        container.registerComponentInstance(mockMessageResources());
        Registrar registrar = createRegistrar(container);

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(registrarClass, messageResources);
        registrarAssistant.executeDelegatingRegistrar(registrar, APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, SESSION);

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

        RegistrarAssistant registrarAssistant = new RegistrarAssistant(customRegistrarClass, messageResources);
        registrarAssistant.executeDelegatingRegistrar(registrar, APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, SESSION);
        registrarAssistant.executeDelegatingRegistrar(registrar, REQUEST);

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
