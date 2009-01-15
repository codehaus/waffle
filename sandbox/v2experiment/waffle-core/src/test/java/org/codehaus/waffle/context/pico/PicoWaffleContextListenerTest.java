package org.codehaus.waffle.context.pico;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.testmodel.StubParameterResolver;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.context.WaffleContextListener;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class PicoWaffleContextListenerTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canInvokeServletContextListenerMethods() {

        final StringBuilder sb = new StringBuilder();
        
        final ContextContainerFactory contextContainerFactory = new ContextContainerFactory(mockery.mock(MessageResources.class), mockery.mock(ContextMonitor.class), mockery.mock(RegistrarMonitor.class), new StubParameterResolver()) {
            public void initialize(ServletContext servletContext) throws WaffleException {
                sb.append("init;");
            }

            public void destroy() {
                sb.append("destroy;");
            }
        };

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);

        // Mock ComponentRegistry
        final ComponentRegistry registry = new ComponentRegistry(servletContext) {
            protected void register(Object key, Class<?> defaultClass, ServletContext servletContext) throws WaffleException {
            }

            @SuppressWarnings("unchecked")
            protected void registerOtherComponents(ServletContext servletContext) {
            }
            
            public ContextContainerFactory getContextContainerFactory() {
                return contextContainerFactory;
            }

        };
        
        WaffleContextListener waffleContextListener = new WaffleContextListener() {
            protected ComponentRegistry buildComponentRegistry(ServletContext servletContext) {
                return registry;
            }
        };

        mockery.checking(new Expectations() {
            {
                String name = ComponentRegistry.class.getName();
                one(servletContext).setAttribute(with(same(name)), with(same(registry)));
            }
        });

        ServletContextEvent event = new ServletContextEvent(servletContext);

        // test the init
        waffleContextListener.contextInitialized(event);

        // test the destroy
        waffleContextListener.contextDestroyed(event);

        assertEquals("init;destroy;", sb.toString());
    }

    @Test
    public void canInvokeHttpSessionListenerMethods() throws Exception {
        WaffleContextListener waffleContextListener = new PicoWaffleContextListener();

        // Mock ContextContainer
        final MutablePicoContainer container = mockery.mock(MutablePicoContainer.class);
        mockery.checking(new Expectations() {
            {
                one(container).start();
                one(container).addComponent(with(an(HttpSession.class)));
                one(container).stop();
                one(container).dispose();
            }
        });

        final StringBuilder sb = new StringBuilder();

        final ContextContainerFactory contextContainerFactory = new ContextContainerFactory(mockery.mock(MessageResources.class), mockery.mock(ContextMonitor.class), mockery.mock(RegistrarMonitor.class), new StubParameterResolver()) {
            public MutablePicoContainer buildSessionLevelContainer() {
                sb.append("bs;");
                return container;
            }
        };
        setContextManager(waffleContextListener, contextContainerFactory);

        // Mock HttpSession
        final HttpSession httpSession = mockery.mock(HttpSession.class);
        mockery.checking(new Expectations() {
            {
                one(httpSession).setAttribute(Constants.SESSION_CONTAINER_KEY, container);
                one(httpSession).getAttribute(Constants.SESSION_CONTAINER_KEY);
                will(returnValue(container));
            }
        });

        // created
        waffleContextListener.sessionCreated(new HttpSessionEvent(httpSession));

        // destroy
        waffleContextListener.sessionDestroyed(new HttpSessionEvent(httpSession));
        assertEquals("bs;", sb.toString());
    }

    /**
     * set the private field so we don't need to add an accessors simply for testing.
     */
    private void setContextManager(WaffleContextListener listener, ContextContainerFactory containerFactory)
            throws Exception {
        Field field = WaffleContextListener.class.getDeclaredField("contextContainerFactory");
        field.setAccessible(true);
        field.set(listener, containerFactory);
    }
}
