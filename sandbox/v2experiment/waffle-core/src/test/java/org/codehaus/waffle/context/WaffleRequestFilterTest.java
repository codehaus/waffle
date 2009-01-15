package org.codehaus.waffle.context;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.testmodel.StubActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.i18n.MessageResources;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;


/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class WaffleRequestFilterTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canInit() throws ServletException {

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);

        // Mock ComponentRegistry
        final ComponentRegistry registry = new ComponentRegistry(servletContext) {
            protected void register(Object key, Class<?> defaultClass, ServletContext servletContext) throws WaffleException {
            }

            @SuppressWarnings("unchecked")
            protected void registerOtherComponents(ServletContext servletContext) {
            }
        };

        mockery.checking(new Expectations() {
            {
                one(servletContext).getAttribute(ComponentRegistry.class.getName());
                will(returnValue(registry));
            }
        });

        // Mock FilterConfig
        final FilterConfig filterConfig = mockery.mock(FilterConfig.class);
        mockery.checking(new Expectations() {
            {
                one(filterConfig).getServletContext();
                will(returnValue(servletContext));
            }
        });

        Filter filter = new WaffleRequestFilter();
        filter.init(filterConfig);
    }

    @Test
    public void canDestroy() throws Exception {
        Filter filter = new WaffleRequestFilter();

        Field field = WaffleRequestFilter.class.getDeclaredField("contextContainerFactory");
        field.setAccessible(true);
        field.set(filter, new ContextContainerFactory(null, null, null, null));

        assertNotNull(field.get(filter));
        filter.destroy();
        assertNull(field.get(filter));
    }

    @Test
    public void canDoFilter() throws Exception {
        CurrentHttpServletRequest.set(null); // ensure clear

        // Mock ContextContainer
        final MutablePicoContainer container = mockery.mock(MutablePicoContainer.class);
        mockery.checking(new Expectations() {
            {
                one(container).start();
                one(container).addComponent(with(an(HttpServletRequest.class)));
                will(returnValue(null));
                one(container).addComponent(with(an(HttpServletResponse.class)));
                will(returnValue(null));
                one(container).stop();
                one(container).dispose();
            }
        });

        // Mock ContextContainerFactory
        final ContextContainerFactory contextContainerFactory = new ContextContainerFactory(mockery.mock(MessageResources.class), mockery.mock(ContextMonitor.class), mockery.mock(RegistrarMonitor.class), mockery.mock(ParameterResolver.class)) {
            public MutablePicoContainer buildRequestLevelContainer(HttpServletRequest request) {
                return container;
            }
        };

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock FilterChain
        final FilterChain filterChain = mockery.mock(FilterChain.class);
        mockery.checking(new Expectations() {
            {
                one(filterChain).doFilter(with(same(request)), with(same(response)));
            }
        });

        Filter filter = new WaffleRequestFilter();
        Field field = WaffleRequestFilter.class.getDeclaredField("contextContainerFactory");
        field.setAccessible(true);
        field.set(filter, contextContainerFactory);

        filter.doFilter(request, response, filterChain);

        Assert.assertSame("Filter should have set request", request, CurrentHttpServletRequest.get());
    }

}
