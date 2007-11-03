package org.codehaus.waffle.context.pico;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.codehaus.waffle.context.ContextContainer;
import org.jruby.Ruby;
import org.jruby.RubyBoolean;
import org.jruby.javasupport.JavaEmbedUtils;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;

public class RubyAwarePicoContextContainerFactoryTest  {

    @Test
    public void canBuildApplicationContextContainer() {
        RubyAwarePicoContextContainerFactory factory = new RubyAwarePicoContextContainerFactory(null);
        ContextContainer contextContainer = factory.buildApplicationContextContainer();
        PicoContainer picoContainer = (MutablePicoContainer)contextContainer.getDelegate();
        Ruby runtime = (Ruby) picoContainer.getComponentInstance(Ruby.class);
        assertNotNull(runtime);

        assertNotNull(picoContainer.getComponentAdapter(RubyScriptLoader.class));

        // ensure mixin occurred
        RubyBoolean rubyBoolean = (RubyBoolean) runtime.evalScript("Waffle::Controller.is_a? Module");
        assertTrue((Boolean) JavaEmbedUtils.rubyToJava(runtime, rubyBoolean, Boolean.class));
    }

}
