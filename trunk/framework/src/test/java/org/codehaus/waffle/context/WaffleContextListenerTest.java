package org.codehaus.waffle.context;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleComponentRegistry;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.lang.reflect.Field;

public class WaffleContextListenerTest extends MockObjectTestCase {

    public void testServletContextListenerMethods() {
        // Mock ContextContainerFactory
        Mock mockContextContainerFactory = mock(ContextContainerFactory.class);
        mockContextContainerFactory.expects(once())
                .method("initialize")
                .with(isA(ServletContext.class));
        mockContextContainerFactory.expects(once())
                .method("destroy");
        ContextContainerFactory contextContainerFactory = (ContextContainerFactory) mockContextContainerFactory.proxy();

        // Mock WaffleComponentRegistry
        Mock mockRegistry = mock(WaffleComponentRegistry.class);
        mockRegistry.expects(once())
                .method("getContextContainerFactory")
                .will(returnValue(contextContainerFactory));
        final WaffleComponentRegistry registry = (WaffleComponentRegistry) mockRegistry.proxy();

        WaffleContextListener waffleContextListener = new WaffleContextListener() {
            protected WaffleComponentRegistry buildWaffleComponentRegistry(ServletContext servletContext) {
                return registry;
            }
        };

        // Mock ServletContext
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("setAttribute")
                .with(eq(WaffleComponentRegistry.class.getName()), same(registry));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        ServletContextEvent event = new ServletContextEvent(servletContext);

        // test the init
        waffleContextListener.contextInitialized(event);

        // test the destroy
        waffleContextListener.contextDestroyed(event);
    }

    public void testHttpSessionListenerMethods() throws Exception {
        WaffleContextListener waffleContextListener = new WaffleContextListener();

        // Mock ContextContainer
        Mock mockContainer = mock(ContextContainer.class);
        mockContainer.expects(once()).method("registerComponentInstance").with(isA(HttpSession.class));
        mockContainer.expects(once()).method("start");
        mockContainer.expects(once()).method("stop");
        mockContainer.expects(once()).method("dispose");
        ContextContainer container = (ContextContainer) mockContainer.proxy();

        // Mock WaffleContextContainerFactory
        Mock mockWaffleContextContainerFactory = mock(ContextContainerFactory.class);
        mockWaffleContextContainerFactory.expects(once()).method("buildSessionLevelContainer")
                .will(returnValue(container));
        ContextContainerFactory contextContainerFactory = (ContextContainerFactory) mockWaffleContextContainerFactory.proxy();

        setContextManager(waffleContextListener, contextContainerFactory);

        //  Mock HttpSession
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession.expects(once()).method("setAttribute")
                .with(eq(Constants.SESSION_CONTAINER_KEY), eq(container));
        mockHttpSession.expects(once()).method("getAttribute")
                .with(eq(Constants.SESSION_CONTAINER_KEY))
                .will(returnValue(container));
        HttpSession httpSession = (HttpSession) mockHttpSession.proxy();

        // created
        waffleContextListener.sessionCreated(new HttpSessionEvent(httpSession));

        // destroy
        waffleContextListener.sessionDestroyed(new HttpSessionEvent(httpSession));
    }

    /**
     * set the private field so we don't need to add an accessors simply for testing.
     */
    private void setContextManager(WaffleContextListener listener, ContextContainerFactory containerFactory) throws Exception {
        Field field = WaffleContextListener.class.getDeclaredField("contextContainerFactory");
        field.setAccessible(true);
        field.set(listener, containerFactory);
    }
}
