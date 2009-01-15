package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.ruby.context.RubyContextContainerFactory;
import org.codehaus.waffle.ruby.context.pico.RubyScriptLoader;
import org.jruby.Ruby;
import org.jruby.RubyBoolean;
import org.jruby.RubyInteger;
import org.jruby.javasupport.JavaEmbedUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;

public class RubyPicoContextContainerFactoryTest  {

    @Test
    public void canBuildApplicationContextContainer() {
        RubyContextContainerFactory factory
                = new RubyContextContainerFactory(null, new SilentMonitor(), new SilentMonitor(), null);
        MutablePicoContainer contextContainer = factory.buildApplicationContextContainer();
        Ruby runtime = contextContainer.getComponent(Ruby.class);
        assertNotNull(runtime);

        assertNotNull(contextContainer.getComponentAdapter(RubyScriptLoader.class));

        // ensure mixin occurred
        RubyBoolean rubyBoolean = (RubyBoolean) runtime.evalScriptlet("Waffle::Controller.is_a? Module");
        assertTrue((Boolean) JavaEmbedUtils.rubyToJava(runtime, rubyBoolean, Boolean.class));

        // ensure we can load ruby script
        RubyInteger rubyInteger = (RubyInteger)runtime.evalScriptlet("$LOAD_PATH.size");
        Integer count = (Integer) JavaEmbedUtils.rubyToJava(runtime, rubyInteger, Integer.class);
        assertTrue(count > 0);
    }

}
