package com.thoughtworks.waffle.servlet;

import com.thoughtworks.waffle.WaffleComponentRegistry;
import com.thoughtworks.waffle.WaffleException;
import com.thoughtworks.waffle.testmodel.StubWaffleComponentRegistry;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.ServletContext;

public class ServletContextHelperTest extends MockObjectTestCase {

    public void testGetWaffleComponentManager() {
        Mock mockServletContext = mock(ServletContext.class);
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        mockServletContext.expects(once())
                .method("getInitParameterNames")
                .will(returnValue(null));
        mockServletContext.expects(atLeastOnce())
                .method("getInitParameter")
                .will(returnValue(null));
        mockServletContext.expects(once()).method("getAttribute")
                .with(eq(WaffleComponentRegistry.class.getName()))
                .will(returnValue(new StubWaffleComponentRegistry(servletContext)));

        ServletContextHelper.getWaffleComponentRegistry(servletContext);
    }

    public void testWaffleComponentManagerRegistrationRequired() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getAttribute")
                .with(eq(WaffleComponentRegistry.class.getName()))
                .will(returnValue(null));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        try {
            ServletContextHelper.getWaffleComponentRegistry(servletContext);
            fail("WaffleException expected");
        } catch (WaffleException expected) {
            // expected
        }
    }
}
