package org.codehaus.waffle.servlet;

import javax.servlet.ServletContext;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.testmodel.StubComponentRegistry;
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
public class ServletContextHelperTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canGetComponentRegistry() {

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getInitParameterNames();
                will(returnValue(null));
                atLeast(1).of(servletContext).getInitParameter(with(any(String.class)));
                will(returnValue(null));
            }
        });
        mockery.checking(new Expectations() {
            {
                one(servletContext).getAttribute(ComponentRegistry.class.getName());
                will(returnValue(new StubComponentRegistry(servletContext)));
            }
        });
        ServletContextHelper.getComponentRegistry(servletContext);
    }

    @Test(expected = WaffleException.class)
    public void cannotGetComponentRegistryIfNotRegistered() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getAttribute(ComponentRegistry.class.getName());
                will(returnValue(null));
            }
        });
        ServletContextHelper.getComponentRegistry(servletContext);
    }
}
