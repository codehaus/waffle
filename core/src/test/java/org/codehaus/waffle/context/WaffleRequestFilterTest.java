package org.codehaus.waffle.context;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.testmodel.StubContextContainerFactory;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

public class WaffleRequestFilterTest extends MockObjectTestCase {

    public void testInit() throws ServletException {
        Mock mockRegistry = mock(ComponentRegistry.class);
        mockRegistry.expects(once())
                .method("getContextContainerFactory")
                .will(returnValue(new StubContextContainerFactory()));
        ComponentRegistry componentRegistry = (ComponentRegistry) mockRegistry.proxy();

        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getAttribute")
                .with(eq(ComponentRegistry.class.getName()))
                .will(returnValue(componentRegistry));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        // Mock FilterConfig
        Mock mockFilterConfig = mock(FilterConfig.class);
        mockFilterConfig.expects(once())
                .method("getServletContext")
                .will(returnValue(servletContext));
        FilterConfig filterConfig = (FilterConfig) mockFilterConfig.proxy();

        Filter filter = new WaffleRequestFilter();
        filter.init(filterConfig);
    }

    public void testDestroy() throws Exception {
        Filter filter = new WaffleRequestFilter();

        Field field = WaffleRequestFilter.class.getDeclaredField("contextContainerFactory");
        field.setAccessible(true);
        field.set(filter, new StubContextContainerFactory());

        assertNotNull(field.get(filter));
        filter.destroy();
        assertNull(field.get(filter));
    }

    public void testDoFilter() throws Exception {
        // Mock ContextContainer
        Mock mockContainer = mock(ContextContainer.class);
        mockContainer.expects(once())
                .method("start");
        mockContainer.expects(once())
                .method("registerComponentInstance")
                .with(isA(HttpServletRequest.class));
        mockContainer.expects(once())
                .method("registerComponentInstance")
                .with(isA(HttpServletResponse.class));
        mockContainer.expects(once()).method("stop");
        mockContainer.expects(once()).method("dispose");
        ContextContainer container = (ContextContainer) mockContainer.proxy();

        // Mock ContextContainerFactory
        Mock mockContextContainerFactory = mock(ContextContainerFactory.class);
        mockContextContainerFactory.expects(once())
                .method("buildRequestLevelContainer").will(returnValue(container));
        ContextContainerFactory contextContainerFactory = (ContextContainerFactory) mockContextContainerFactory.proxy();

        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();

        // Mock
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock FilterChain
        Mock mockFilterChain = mock(FilterChain.class);
        mockFilterChain.expects(once())
                .method("doFilter")
                .with(same(request), same(response));
        FilterChain filterChain = (FilterChain) mockFilterChain.proxy();

        Filter filter = new WaffleRequestFilter();
        Field field = WaffleRequestFilter.class.getDeclaredField("contextContainerFactory");
        field.setAccessible(true);
        field.set(filter, contextContainerFactory);

        filter.doFilter(request, response, filterChain);
    }

}
