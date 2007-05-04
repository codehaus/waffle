package com.thoughtworks.waffle.registrar.pico;

import com.thoughtworks.waffle.testmodel.DependsOnValue;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.servlet.http.HttpServletRequest;

public class RequestAttributeParameterTest extends MockObjectTestCase {

    public void testComponentDependsOnRequestAttribute() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(exactly(2))
                .method("getAttribute")
                .with(eq("foobar"))
                .will(returnValue("helloWorld"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        Parameter[] parameters = {new RequestAttributeParameter("foobar")};

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(request);
        pico.registerComponentImplementation("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("x");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }

}
