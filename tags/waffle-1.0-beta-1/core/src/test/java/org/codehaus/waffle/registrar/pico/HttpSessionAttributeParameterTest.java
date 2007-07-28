package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.testmodel.DependsOnValue;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.servlet.http.HttpSession;

public class HttpSessionAttributeParameterTest extends MockObjectTestCase {

    public void testComponentDependsOnHttpSessionAttribute() {
        Mock mockSession = mock(HttpSession.class);
        mockSession.expects(exactly(2))
                .method("getAttribute")
                .with(eq("foobar"))
                .will(returnValue("helloWorld"));
        HttpSession session = (HttpSession) mockSession.proxy();

        Parameter[] parameters = {new HttpSessionAttributeParameter("foobar")};

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(session);
        pico.registerComponentImplementation("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("x");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }
}
