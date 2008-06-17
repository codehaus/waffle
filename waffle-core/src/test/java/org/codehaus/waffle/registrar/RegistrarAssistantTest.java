/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.i18n.DefaultMessagesContext;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.codehaus.waffle.testmodel.CustomErrorsContext;
import org.codehaus.waffle.testmodel.CustomMessagesContext;
import org.codehaus.waffle.testmodel.CustomRegistrar;
import org.codehaus.waffle.testmodel.CustomRegistrarWithContexts;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.monitors.NullComponentMonitor;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.containers.EmptyPicoContainer;
import org.picocontainer.lifecycle.NullLifecycleStrategy;
import org.picocontainer.behaviors.Caching;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

@RunWith(JMock.class)
public class RegistrarAssistantTest {
    private LifecycleStrategy lifecycleStrategy = new PicoLifecycleStrategy(new NullComponentMonitor());
    private Mockery mockery = new Mockery();
    ComponentMonitor cm = new NullComponentMonitor();

    @Test
    public void canExecuteWithDefaultErrorsAndMessagesContexts() {
        DefaultPicoContainer picoContainer = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        Registrar registrar = new PicoRegistrar(picoContainer, null, lifecycleStrategy, new SilentMonitor(), cm);
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        registrar.registerInstance(request);
        RegistrarAssistant registrarAssistant = new RegistrarAssistant(CustomRegistrar.class);

        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.SESSION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.REQUEST);

        assertNotNull(picoContainer.getComponent("application"));
        assertNotNull(picoContainer.getComponent("session"));
        assertNotNull(picoContainer.getComponent("request"));
        assertNotNull(picoContainer.getComponent(DefaultErrorsContext.class));
        assertNotNull(picoContainer.getComponent(DefaultMessagesContext.class));
    }
    
    @Test
    public void canExecuteWithCustomErrorsAndMessagesContexts() {
        DefaultPicoContainer picoContainer = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        Registrar registrar = new PicoRegistrar(picoContainer, null, lifecycleStrategy, new SilentMonitor(), cm);
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        registrar.registerInstance(request);
        RegistrarAssistant registrarAssistant = new RegistrarAssistant(CustomRegistrarWithContexts.class);

        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.SESSION);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.REQUEST);

        assertNotNull(picoContainer.getComponent("application"));
        assertNotNull(picoContainer.getComponent("session"));
        assertNotNull(picoContainer.getComponent("request"));
        assertNotNull(picoContainer.getComponent(CustomErrorsContext.class));
        assertNotNull(picoContainer.getComponent(CustomMessagesContext.class));
    }

    @Test(expected = WaffleException.class)
    public void cannotCreateForBadClass() {
        new RegistrarAssistant(Hashtable.class); // bad class
    }
}
