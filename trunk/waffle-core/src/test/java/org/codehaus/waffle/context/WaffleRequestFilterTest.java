package org.codehaus.waffle.context;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.testmodel.StubContextContainerFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class WaffleRequestFilterTest {
    
    private Mockery mockery = new Mockery();
    
    @Test
    public void canInit() throws ServletException {
        // Mock ComponentRegistry
        final ComponentRegistry registry = mockery.mock(ComponentRegistry.class);
        mockery.checking(new Expectations() {
            {
                one(registry).getContextContainerFactory();
                will(returnValue(new StubContextContainerFactory()));
            }
        });

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
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
        field.set(filter, new StubContextContainerFactory());

        assertNotNull(field.get(filter));
        filter.destroy();
        assertNull(field.get(filter));
    }

    @Test
    public void canDoFilter() throws Exception {
        // Mock ContextContainer
        final ContextContainer container = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(container).start();
                one(container).registerComponentInstance(with(an(HttpServletRequest.class)));
                one(container).registerComponentInstance(with(an(HttpServletResponse.class)));                
                one(container).stop();
                one(container).dispose();
            }
        });

        // Mock ContextContainerFactory
        final ContextContainerFactory contextContainerFactory = mockery.mock(ContextContainerFactory.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainerFactory).buildRequestLevelContainer((HttpServletRequest) with(an(HttpServletRequest.class)));
                will(returnValue(container));
            }
        });

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
    }

}