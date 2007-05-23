package org.codehaus.waffle.example.jruby;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.codehaus.waffle.registrar.pico.RubyScriptComponentAdapter;
import org.jruby.Ruby;
import org.picocontainer.ComponentAdapter;

public class JRubyRegistrar extends AbstractRegistrar {
    private PicoRegistrar picoRegistrar;
    Ruby runtime = Ruby.getDefaultInstance();

    public JRubyRegistrar(Registrar delegate) {
        super(delegate);
        picoRegistrar = (PicoRegistrar) delegate;
    }

    @Override
    public void application() {

        String script =
                "class Foo\n" +
                "  def index\n" +
                "    'HELLO WORLD from the index method'\n" +
                "  end\n" +
                "  def bar\n" +
                "    'HELLO WORLD'\n" +
                "  end\n" +
                "end\n";
        
        ComponentAdapter componentAdapter = new RubyScriptComponentAdapter("Foo", script);
        picoRegistrar.registerComponentAdapter(componentAdapter);
    }
}
