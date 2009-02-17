package org.codehaus.waffle.registrar;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.testmodel.DependsOnValue;
import org.codehaus.waffle.registrar.RequestAttributeParameter;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;

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

        MutablePicoContainer pico = new DefaultPicoContainer(new Caching());
        pico.addComponent(request);
        pico.addComponent("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponent("x");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }

}
