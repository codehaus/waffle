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
package org.codehaus.waffle.context.pico;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.Startable;
import org.codehaus.waffle.context.AbstractContextContainerFactory;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.testmodel.ApplicationLevelComponent;
import org.codehaus.waffle.testmodel.CustomRegistrar;
import org.codehaus.waffle.testmodel.FakeBean;
import org.codehaus.waffle.testmodel.RequestLevelComponent;
import org.codehaus.waffle.testmodel.SessionLevelComponent;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;

public class PicoContextContainerFactoryTest extends MockObjectTestCase {
    private final MessageResources messageResources = new DefaultMessageResources();

    public void testBuildEachContextLevelContainer() {
        PicoContextContainerFactory contextContainerFactory = new PicoContextContainerFactory(messageResources);

        // Mock ServletContext
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once()).method("setAttribute")
                .with(eq(ContextContainerFactory.class.getName()), eq(contextContainerFactory));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(Registrar.class.getName()))
                .will(returnValue(CustomRegistrar.class.getName()));

        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        contextContainerFactory.initialize(servletContext);

        // Application
        ContextContainer applicationContainer = contextContainerFactory.getApplicationContextContainer();
        assertNotNull(applicationContainer.getComponentInstanceOfType(ServletContext.class));
        assertSame(messageResources, applicationContainer.getComponentInstanceOfType(MessageResources.class));
        ApplicationLevelComponent applicationLevelComponent =
                (ApplicationLevelComponent) applicationContainer.getComponentInstance("application");
        assertNotNull(applicationLevelComponent);
        assertTrue(applicationLevelComponent.isStarted());
        assertFalse(applicationLevelComponent.isStopped());

        // Session
        ContextContainer sessionContainer = contextContainerFactory.buildSessionLevelContainer();
        SessionLevelComponent sessionLevelComponent = (SessionLevelComponent) sessionContainer.getComponentInstance("session");
        assertNotNull(sessionLevelComponent);
        assertFalse("Start is the responsibility of HttpSessionListener", sessionLevelComponent.isStarted());
        assertFalse(sessionLevelComponent.isStopped());
        assertEquals(applicationLevelComponent, sessionLevelComponent.getApplicationLevelComponent());

        // Mock HttpSession
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession.expects(once()).method("getAttribute")
                .with(eq(Constants.SESSION_CONTAINER_KEY))
                .will(returnValue(sessionContainer));
        HttpSession httpSession = (HttpSession) mockHttpSession.proxy();

        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest.expects(once())
                .method("getSession")
                .will(returnValue(httpSession));
        mockHttpServletRequest.expects(once())
                .method("getLocale")
                .will(returnValue(Locale.US));
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();

        // Request
        ContextContainer requestContainer = contextContainerFactory.buildRequestLevelContainer(request);
        RequestLevelComponent requestLevelComponent = (RequestLevelComponent) requestContainer.getComponentInstance("request");
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

    public void testContextInitialized() {
        AbstractContextContainerFactory contextContainerFactory = new PicoContextContainerFactory(messageResources);

        // Mock ServletContext
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("setAttribute")
                .with(eq(ContextContainerFactory.class.getName()), eq(contextContainerFactory));
        mockServletContext.expects(once())
                .method("getInitParameter")
                .with(eq(Registrar.class.getName()))
                .will(returnValue(CustomRegistrar.class.getName()));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        contextContainerFactory.initialize(servletContext);

        ContextContainer container = contextContainerFactory.getApplicationContextContainer();
        ApplicationLevelComponent applicationLevelComponent =
                container.getComponentInstanceOfType(ApplicationLevelComponent.class);
        assertTrue(applicationLevelComponent.isStarted());
    }

    /**
     * Testing that the underlying containers do provide the start and stop support we are looking for.
     * <p/>
     * Want to ensure start and stop only affect the container being started or stopped and that it does
     * not propogate to child or parent containers.
     */
    public void testStoppingChildContainerDoesNotStopParent() {
        NullComponentMonitor ncm = new NullComponentMonitor();
        MutablePicoContainer applicationContainer = new DefaultPicoContainer(ncm, new PicoLifecycleStrategy(ncm), null);
        applicationContainer.registerComponentImplementation(ApplicationLevelComponent.class);

        MutablePicoContainer sessionContainer = new DefaultPicoContainer(ncm, new PicoLifecycleStrategy(ncm), applicationContainer);
        sessionContainer.registerComponentImplementation(SessionLevelComponent.class);

        MutablePicoContainer requestContainer = new DefaultPicoContainer(ncm, new PicoLifecycleStrategy(ncm), sessionContainer);
        requestContainer.registerComponentImplementation(RequestLevelComponent.class);

        ApplicationLevelComponent applicationLevelComponent = (ApplicationLevelComponent)
                applicationContainer.getComponentInstanceOfType(ApplicationLevelComponent.class);
        SessionLevelComponent sessionLevelComponent = (SessionLevelComponent)
                sessionContainer.getComponentInstance(SessionLevelComponent.class);
        RequestLevelComponent requestLevelComponent = (RequestLevelComponent)
                requestContainer.getComponentInstance(RequestLevelComponent.class);

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

    public void testGetAllComponentInstancesOfType() {
        MutablePicoContainer grandParent = new DefaultPicoContainer();
        grandParent.registerComponentInstance("A", new FakeBean());

        MutablePicoContainer parent = new DefaultPicoContainer(grandParent);
        parent.registerComponentInstance("B", new FakeBean());

        PicoContextContainer container = new PicoContextContainer(parent);
        container.registerComponentInstance(new FakeBean());

        Collection<FakeBean> fakeBeans = container.getAllComponentInstancesOfType(FakeBean.class);
        assertEquals(3, fakeBeans.size());
    }

    public void testBuildApplicationContextContainerSupportsLifecycle() {
        PicoContextContainerFactory contextContainerFactory = new PicoContextContainerFactory(messageResources);
        ContextContainer container = contextContainerFactory.buildApplicationContextContainer();

        StubStartable startable = new StubStartable();
        container.registerComponentInstance(startable);

        // Test lifecycle
        PicoContainer picoContainer = (PicoContainer) container.getDelegate();
        picoContainer.start();

        assertTrue(startable.started);
        assertFalse(startable.stopped);

        picoContainer.stop();
        assertTrue(startable.stopped);
    }

    public void testBuildSessionLevelContainerSupportsLifecycle() {
        PicoContextContainerFactory contextContainerFactory = new PicoContextContainerFactory(messageResources);

        // Mock ServletContext
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once()).method("setAttribute")
                .with(eq(ContextContainerFactory.class.getName()), eq(contextContainerFactory));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(Registrar.class.getName()))
                .will(returnValue(CustomRegistrar.class.getName()));

        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        contextContainerFactory.initialize(servletContext);

        ContextContainer container = contextContainerFactory.buildSessionLevelContainer();

        StubStartable startable = new StubStartable();
        container.registerComponentInstance(startable);

        // Test lifecycle
        PicoContainer picoContainer = (PicoContainer) container.getDelegate();
        picoContainer.start();

        assertTrue(startable.started);
        assertFalse(startable.stopped);

        picoContainer.stop();
        assertTrue(startable.stopped);
    }

    public void testBuildRequestLevelContainerSupportsLifecycle() {
        PicoContextContainerFactory contextContainerFactory = new PicoContextContainerFactory(messageResources);

        // Mock ServletContext
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once()).method("setAttribute")
                .with(eq(ContextContainerFactory.class.getName()), eq(contextContainerFactory));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(Registrar.class.getName()))
                .will(returnValue(CustomRegistrar.class.getName()));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        contextContainerFactory.initialize(servletContext);

        ContextContainer sessionContextContainer = contextContainerFactory.buildSessionLevelContainer();

        // Mock HttpSession
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession.expects(once()).method("getAttribute")
                .with(eq(Constants.SESSION_CONTAINER_KEY))
                .will(returnValue(sessionContextContainer));
        HttpSession httpSession = (HttpSession) mockHttpSession.proxy();

        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest.expects(once())
                .method("getSession")
                .will(returnValue(httpSession));
        mockHttpServletRequest.expects(once())
                .method("getLocale")
                .will(returnValue(Locale.US));
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();

        ContextContainer container = contextContainerFactory.buildRequestLevelContainer(request);

        StubStartable startable = new StubStartable();
        container.registerComponentInstance(startable);

        // Test lifecycle
        PicoContainer picoContainer = (PicoContainer) container.getDelegate();
        picoContainer.start();

        assertTrue(startable.started);
        assertFalse(startable.stopped);

        picoContainer.stop();
        assertTrue(startable.stopped);
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
