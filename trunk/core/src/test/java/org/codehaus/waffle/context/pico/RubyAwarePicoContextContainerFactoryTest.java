package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.context.ContextContainer;
import org.jmock.MockObjectTestCase;
import org.jruby.Ruby;
import org.jruby.RubyBoolean;
import org.jruby.javasupport.JavaEmbedUtils;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;

public class RubyAwarePicoContextContainerFactoryTest extends MockObjectTestCase {

    public void testBuildApplicationContextContainer() {
        RubyAwarePicoContextContainerFactory factory = new RubyAwarePicoContextContainerFactory(null);
        ContextContainer contextContainer = factory.buildApplicationContextContainer();
        PicoContainer picoContainer = (MutablePicoContainer)contextContainer.getDelegate();
        Ruby runtime = (Ruby) picoContainer.getComponentInstance(Ruby.class);
        assertNotNull(runtime);

        // ensure mixin occurred
        RubyBoolean rubyBoolean = (RubyBoolean) runtime.evalScript("String.respond_to? :camelize");
        assertTrue((Boolean)JavaEmbedUtils.rubyToJava(runtime, rubyBoolean, Boolean.class));
    }

}