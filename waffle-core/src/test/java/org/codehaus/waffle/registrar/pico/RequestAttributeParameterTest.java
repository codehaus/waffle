package org.codehaus.waffle.registrar.pico;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.testmodel.DependsOnValue;
import org.jmock.Expectations;
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
public class RequestAttributeParameterTest {
    private Mockery mockery = new Mockery();

    @Test
    public void componentDependsOnRequestAttribute() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                exactly(2).of(request).getAttribute("foobar");
                will(returnValue("helloWorld"));
            }
        });
        
        Parameter[] parameters = {new RequestAttributeParameter("foobar")};

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(request);
        pico.registerComponentImplementation("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("x");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }

}
