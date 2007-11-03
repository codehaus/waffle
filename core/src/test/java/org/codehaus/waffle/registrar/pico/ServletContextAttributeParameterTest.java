package org.codehaus.waffle.registrar.pico;

import javax.servlet.ServletContext;

import org.codehaus.waffle.testmodel.DependsOnValue;
import org.jmock.Expectations;
import org.jmock.MockObjectTestCase;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class ServletContextAttributeParameterTest extends MockObjectTestCase {

    private Mockery mockery = new Mockery();

    @Test
    public void componentDependsOnServletContextAttribute() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                exactly(2).of(servletContext).getAttribute("foobar");
                will(returnValue("helloWorld"));
            }
        });
        
        Parameter[] parameters = {new ServletContextAttributeParameter("foobar")};

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(servletContext);
        pico.registerComponentImplementation("blah", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("blah");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }
}
