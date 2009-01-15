/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.Startable;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.codehaus.waffle.testmodel.ApplicationLevelComponent;
import org.codehaus.waffle.testmodel.CustomRegistrar;
import org.codehaus.waffle.testmodel.FakeBean;
import org.codehaus.waffle.testmodel.RequestLevelComponent;
import org.codehaus.waffle.testmodel.SessionLevelComponent;
import org.codehaus.waffle.testmodel.StubParameterResolver;
import org.codehaus.waffle.validation.ErrorsContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.containers.EmptyPicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class PicoContextContainerFactoryTest {

    private Mockery mockery = new Mockery();
    
    private final MessageResources messageResources = new DefaultMessageResources();

    @Test
    public void canBuildEachContextLevelContainer() {
        final ContextContainerFactory contextContainerFactory
                = new ContextContainerFactory(messageResources, new SilentMonitor(), new SilentMonitor(), null);

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).setAttribute(with(same(ContextContainerFactory.class.getName())), with(same(contextContainerFactory)));
                one(servletContext).getInitParameter(with(same(Registrar.class.getName())));
                will(returnValue(CustomRegistrar.class.getName()));
            }
        });
        contextContainerFactory.initialize(servletContext);

        // Application
        MutablePicoContainer applicationContainer = contextContainerFactory.getApplicationContextContainer();
        Assert.assertNotNull(applicationContainer.getComponent(ServletContext.class));
        assertSame(messageResources, applicationContainer.getComponent(MessageResources.class));
        ApplicationLevelComponent applicationLevelComponent =
                (ApplicationLevelComponent) applicationContainer.getComponent("application");
        assertNotNull(applicationLevelComponent);
        assertTrue(applicationLevelComponent.isStarted());
        assertFalse(applicationLevelComponent.isStopped());

        // Session
        final MutablePicoContainer sessionContainer = contextContainerFactory.buildSessionLevelContainer();
        SessionLevelComponent sessionLevelComponent = (SessionLevelComponent) sessionContainer.getComponent("session");
        assertNotNull(sessionLevelComponent);
        assertFalse("Start is the responsibility of HttpSessionListener", sessionLevelComponent.isStarted());
        assertFalse(sessionLevelComponent.isStopped());
        assertEquals(applicationLevelComponent, sessionLevelComponent.getApplicationLevelComponent());

        // Mock HttpSession
        final HttpSession httpSession = mockery.mock(HttpSession.class);
        mockery.checking(new Expectations() {
            {
                one(httpSession).getAttribute(Constants.SESSION_CONTAINER_KEY);
                will(returnValue(sessionContainer));
            }
        });

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getSession();
                will(returnValue(httpSession));
                allowing(request).getLocale(); // allow, rather than mandate
                will(returnValue(Locale.US));
            }
        });

        // Request
        MutablePicoContainer requestContainer = contextContainerFactory.buildRequestLevelContainer(request);
        RequestLevelComponent requestLevelComponent = (RequestLevelComponent) requestContainer.getComponent("request");
        assertNotNull(requestLevelComponent);
        assertFalse("Start is the responsibility of ServletRequestListener", requestLevelComponent.isStarted());
        assertFalse(requestLevelComponent.isStopped());
        assertEquals(applicationLevelComponent, requestLevelComponent.getApplicationLevelComponent());
        assertEquals(sessionLevelComponent, requestLevelComponent.getSessionLevelComponent());

        // Destroy Application Context
        contextContainerFactory.destroy();
        assertTrue(applicationLevelComponent.isStopped());
        assertFalse("session component should NOT be stopped", sessionLevelComponent.isStopped());
        assertFalse("request component should NOT be stopped", requestLevelComponent.isStopped());
    }

    @Test
    public void canInitializeContext() {
        final ContextContainerFactory contextContainerFactory
                = new ContextContainerFactory(messageResources, new SilentMonitor(), new SilentMonitor(), null);

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).setAttribute(with(same(ContextContainerFactory.class.getName())), with(same(contextContainerFactory)));
                one(servletContext).getInitParameter(with(same(Registrar.class.getName())));
                will(returnValue(CustomRegistrar.class.getName()));
            }
        });
        contextContainerFactory.initialize(servletContext);

        MutablePicoContainer container = contextContainerFactory.getApplicationContextContainer();
        ApplicationLevelComponent applicationLevelComponent =
                container.getComponent(ApplicationLevelComponent.class);
        assertTrue(applicationLevelComponent.isStarted());
    }

    /**
     * Testing that the underlying containers do provide the start and stop support we are looking for.
     * <p/>
     * Want to ensure start and stop only affect the container being started or stopped and that it does
     * not propogate to child or parent containers.
     */
    @Test
    public void canStopChildContainerWithoutStoppingParent() {
        NullComponentMonitor ncm = new NullComponentMonitor();
        MutablePicoContainer applicationContainer = new DefaultPicoContainer(new Caching(), new WaffleLifecycleStrategy(ncm), new EmptyPicoContainer(), ncm);
        applicationContainer.addComponent(ApplicationLevelComponent.class);

        MutablePicoContainer sessionContainer = new DefaultPicoContainer(new Caching(), new WaffleLifecycleStrategy(ncm), applicationContainer, ncm);
        sessionContainer.addComponent(SessionLevelComponent.class);

        MutablePicoContainer requestContainer = new DefaultPicoContainer(new Caching(), new WaffleLifecycleStrategy(ncm), sessionContainer, ncm);
        requestContainer.addComponent(RequestLevelComponent.class);

        ApplicationLevelComponent applicationLevelComponent = (ApplicationLevelComponent)
                applicationContainer.getComponent(ApplicationLevelComponent.class);
        SessionLevelComponent sessionLevelComponent = (SessionLevelComponent)
                sessionContainer.getComponent(SessionLevelComponent.class);
        RequestLevelComponent requestLevelComponent = (RequestLevelComponent)
                requestContainer.getComponent(RequestLevelComponent.class);

        // assert non are started
        assertFalse(applicationLevelComponent.isStarted());
        assertFalse(sessionLevelComponent.isStarted());
        assertFalse(requestLevelComponent.isStarted());

        // starting parent does not start children
        applicationContainer.start();
        assertTrue(applicationLevelComponent.isStarted());
        assertFalse(sessionLevelComponent.isStarted());
        assertFalse(requestLevelComponent.isStarted());

        // start child does not start parent
        requestContainer.start();
        assertFalse(sessionLevelComponent.isStarted());
        assertTrue(requestLevelComponent.isStarted());

        // start session container
        sessionContainer.start();
        assertTrue(applicationLevelComponent.isStarted());
        assertTrue(sessionLevelComponent.isStarted());
        assertTrue(requestLevelComponent.isStarted());

        // stopping parent does not stop child
        applicationContainer.stop();
        assertFalse(applicationLevelComponent.isStarted());
        assertTrue(applicationLevelComponent.isStopped());
        assertTrue(sessionLevelComponent.isStarted());
        assertFalse(sessionLevelComponent.isStopped());
        assertTrue(requestLevelComponent.isStarted());
        assertFalse(requestLevelComponent.isStopped());

        // stopping child does not stop parent
        requestContainer.stop();
        assertTrue(sessionLevelComponent.isStarted());
        assertFalse(requestLevelComponent.isStarted());
    }

    @Test
    public void canGetAllComponentInstancesOfType() {
        MutablePicoContainer grandParent = new DefaultPicoContainer(new Caching());
        grandParent.addComponent("A", new FakeBean());

        MutablePicoContainer parent = new DefaultPicoContainer(new Caching(), grandParent);
        parent.addComponent("B", new FakeBean());

        ContextContainer container = new ContextContainer(parent, messageResources);
        container.addComponent(new FakeBean());

        Collection<FakeBean> fakeBeans = container.getComponents(FakeBean.class);
        assertEquals(3, fakeBeans.size());
    }

    @Test
    public void canBuildApplicationContextContainerWithLifecycle() {
        ContextContainerFactory contextContainerFactory
                = new ContextContainerFactory(messageResources, new SilentMonitor(), new SilentMonitor(), null);
        MutablePicoContainer container = contextContainerFactory.buildApplicationContextContainer();

        StubStartable startable = new StubStartable();
        container.addComponent(startable);

        // Test lifecycle
        container.start();

        assertTrue(startable.started);
        assertFalse(startable.stopped);

        container.stop();
        assertTrue(startable.stopped);
    }

    @Test
    public void canBuildSessionLevelContainerWithLifecycle() {
        final ContextContainerFactory contextContainerFactory
                = new ContextContainerFactory(messageResources, new SilentMonitor(), new SilentMonitor(), null);

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).setAttribute(with(same(ContextContainerFactory.class.getName())), with(same(contextContainerFactory)));
                one(servletContext).getInitParameter(with(same(Registrar.class.getName())));
                will(returnValue(CustomRegistrar.class.getName()));
            }
        });
        contextContainerFactory.initialize(servletContext);

        MutablePicoContainer container = contextContainerFactory.buildSessionLevelContainer();

        StubStartable startable = new StubStartable();
        container.addComponent(startable);

        container.start();

        assertTrue(startable.started);
        assertFalse(startable.stopped);

        container.stop();
        assertTrue(startable.stopped);
    }

    @Test
    public void canBuildRequestLevelContainerWithLifecycle() {
        final ContextContainerFactory contextContainerFactory
                = new ContextContainerFactory(messageResources, new SilentMonitor(), new SilentMonitor(), null);

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).setAttribute(with(same(ContextContainerFactory.class.getName())), with(same(contextContainerFactory)));
                one(servletContext).getInitParameter(with(same(Registrar.class.getName())));
                will(returnValue(CustomRegistrar.class.getName()));
            }
        });
        contextContainerFactory.initialize(servletContext);

        final MutablePicoContainer sessionContextContainer = contextContainerFactory.buildSessionLevelContainer();


        // Mock HttpSession
        final HttpSession httpSession = mockery.mock(HttpSession.class);
        mockery.checking(new Expectations() {
            {
                one(httpSession).getAttribute(Constants.SESSION_CONTAINER_KEY);
                will(returnValue(sessionContextContainer));
            }
        });

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getSession();
                will(returnValue(httpSession));
                allowing(request).getLocale(); // allow, rather than mandate
                will(returnValue(Locale.US));
            }
        });
        
        MutablePicoContainer container = contextContainerFactory.buildRequestLevelContainer(request);

        StubStartable startable = new StubStartable();
        container.addComponent(startable);

        // Remove ErrorsContext and MessagesContext prior to starting... (assert they existed)
        assertNotNull(container.removeComponent(ErrorsContext.class));
        assertNotNull(container.removeComponent(MessagesContext.class));
        container.start();

        assertTrue(startable.started);
        assertFalse(startable.stopped);

        container.stop();
        assertTrue(startable.stopped);
    }

    /**
     * WAFFLE-52 : ensure PicoRegistrar is not constructed without passing ParameterResolver
     */
    @Test
    public void canBuildRegistrar() throws Exception {
        ParameterResolver parameterResolver = new StubParameterResolver();
        SilentMonitor registrarMonitor = new SilentMonitor();
        final ContextContainerFactory contextContainerFactory
                = new ContextContainerFactory(messageResources, new SilentMonitor(), registrarMonitor, parameterResolver);

        // Mock ContextContainer
        final MutablePicoContainer contextContainer = mockery.mock(MutablePicoContainer.class);

        Registrar registrar = contextContainerFactory.createRegistrar(contextContainer);

        // Ensure 'picoContainer' was set
        Field picoContainerField = PicoRegistrar.class.getDeclaredField("picoContainer");
        picoContainerField.setAccessible(true);
        assertSame(contextContainer, picoContainerField.get(registrar));

        // ensure 'parameterResolver' was set
        Field parameterResolverField = PicoRegistrar.class.getDeclaredField("parameterResolver");
        parameterResolverField.setAccessible(true);
        assertSame(parameterResolver, parameterResolverField.get(registrar));

        // ensure 'picoLifecycleStrategy' is not Null
        Field lifecycleStrategyField = PicoRegistrar.class.getDeclaredField("lifecycleStrategy");
        lifecycleStrategyField.setAccessible(true);
        assertTrue(lifecycleStrategyField.get(registrar) instanceof WaffleLifecycleStrategy);

        // ensure 'registrarMonitor' was set
        Field registrarMonitorField = PicoRegistrar.class.getDeclaredField("registrarMonitor");
        registrarMonitorField.setAccessible(true);
        assertSame(registrarMonitor, registrarMonitorField.get(registrar));
    }

    public class StubStartable implements Startable {
        boolean started = false;
        boolean stopped = false;


        public void start() {
            started = true;
        }

        public void stop() {
            stopped = true;
        }
    }
}
