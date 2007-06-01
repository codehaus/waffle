package org.codehaus.waffle.example.jruby;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.jruby.Ruby;

public class JRubyRegistrar extends AbstractRegistrar {
    private PicoRegistrar picoRegistrar;
    Ruby runtime = Ruby.getDefaultInstance();

    public JRubyRegistrar(Registrar delegate) {
        super(delegate);
        picoRegistrar = (PicoRegistrar) delegate;
    }

    @Override
    public void application() {
        registerInstance("chicago", "bears");

        picoRegistrar.registerRubyScript("foobar", "FooBar"); // register the controller!
    }

    @Override
    public void request() {
        register(RubyScriptReloader.class);
    }
}
