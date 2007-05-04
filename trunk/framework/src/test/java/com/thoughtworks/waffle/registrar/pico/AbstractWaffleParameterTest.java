package com.thoughtworks.waffle.registrar.pico;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;

public class AbstractWaffleParameterTest extends MockObjectTestCase {

    public void testAccept() {
        AbstractWaffleParameter parameter = new AbstractWaffleParameter("foobar") {
            public Object resolveInstance(PicoContainer picoContainer, ComponentAdapter componentAdapter, Class aClass) {
                throw new UnsupportedOperationException("don't call");
            }
        };

        // Mock PicoVisitor
        Mock mockPicoVisitor = mock(PicoVisitor.class);
        mockPicoVisitor.expects(once())
                .method("visitParameter")
                .with(same(parameter));
        PicoVisitor visitor = (PicoVisitor) mockPicoVisitor.proxy();

        parameter.accept(visitor);
    }
}
