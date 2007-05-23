package org.codehaus.waffle.context.pico;

import org.jmock.MockObjectTestCase;
import org.codehaus.waffle.context.ContextContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.jruby.Ruby;

public class RubyAwarePicoContextContainerFactoryTest extends MockObjectTestCase {

    public void testBuildApplicationContextContainer() {
        RubyAwarePicoContextContainerFactory factory = new RubyAwarePicoContextContainerFactory(null);
        ContextContainer contextContainer = factory.buildApplicationContextContainer();
        PicoContainer picoContainer = (MutablePicoContainer)contextContainer.getDelegate();
        assertNotNull(picoContainer.getComponentInstance(Ruby.class));
    }

}
