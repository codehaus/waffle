package org.codehaus.waffle.servlet;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.testmodel.StubComponentRegistry;
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
                .with(eq(ComponentRegistry.class.getName()))
                .will(returnValue(new StubComponentRegistry(servletContext)));

        ServletContextHelper.getComponentRegistry(servletContext);
    }

    public void testWaffleComponentManagerRegistrationRequired() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getAttribute")
                .with(eq(ComponentRegistry.class.getName()))
                .will(returnValue(null));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        try {
            ServletContextHelper.getComponentRegistry(servletContext);
            fail("WaffleException expected");
        } catch (WaffleException expected) {
            // expected
        }
    }
}
