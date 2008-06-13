package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.jruby.Ruby;
import org.jruby.RubyBoolean;
import org.jruby.RubyInteger;
import org.jruby.javasupport.JavaEmbedUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;

public class RubyPicoContextContainerFactoryTest  {

    @Test
    public void canBuildApplicationContextContainer() {
        RubyPicoContextContainerFactory factory
                = new RubyPicoContextContainerFactory(null, new SilentMonitor(), new SilentMonitor(), null);
        ContextContainer contextContainer = factory.buildApplicationContextContainer();
        PicoContainer picoContainer = (MutablePicoContainer)contextContainer.getDelegate();
        Ruby runtime = (Ruby) picoContainer.getComponent(Ruby.class);
        assertNotNull(runtime);

        assertNotNull(picoContainer.getComponentAdapter(RubyScriptLoader.class));

        // ensure mixin occurred
        RubyBoolean rubyBoolean = (RubyBoolean) runtime.evalScript("Waffle::Controller.is_a? Module");
        assertTrue((Boolean) JavaEmbedUtils.rubyToJava(runtime, rubyBoolean, Boolean.class));

        // ensure we can load ruby script
        RubyInteger rubyInteger = (RubyInteger)runtime.evalScript("$LOAD_PATH.size");
        Integer count = (Integer) JavaEmbedUtils.rubyToJava(runtime, rubyInteger, Integer.class);
        assertTrue(count > 0);
    }

}
