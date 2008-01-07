package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.testmodel.DependsOnValue;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.servlet.http.HttpSession;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class SessionAttributeParameterTest {
    private Mockery mockery = new Mockery();

    @Test
    public void componentDependsOnSessionAttribute() {
        // Mock HttpSession
        final HttpSession session = mockery.mock(HttpSession.class);
        mockery.checking(new Expectations() {
            {
                exactly(2).of(session).getAttribute("foobar");
                will(returnValue("helloWorld"));
            }
        });

        Parameter[] parameters = { new SessionAttributeParameter("foobar") };

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(session);
        pico.registerComponentImplementation("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("x");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }
}
