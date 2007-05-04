package com.thoughtworks.waffle.registrar.pico;

import com.thoughtworks.waffle.testmodel.DependsOnValue;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.servlet.ServletContext;

public class ServletContextAttributeParameterTest extends MockObjectTestCase {

    public void testComponentDependsOnServletContextAttribute() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(exactly(2))
                .method("getAttribute")
                .with(eq("foobar"))
                .will(returnValue("helloWorld"));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        Parameter[] parameters = {new ServletContextAttributeParameter("foobar")};

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(servletContext);
        pico.registerComponentImplementation("blah", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("blah");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }
}
