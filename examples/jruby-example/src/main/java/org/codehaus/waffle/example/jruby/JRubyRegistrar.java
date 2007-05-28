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
        registerInstance("chicago", "bears");

        String script =
                "class FooBar\n" +
                "  def index\n" +
                "    request[:foo] = 'bar'\n" +
                "    session[:bar] = 'foo'\n" +
                "    p session\n" +
                "    begin\n" +
                "      \"HELLO WORLD from the index method \nlook up from pico: #{find_chicago} \nrequest: #{request} \nsession: #{session} \n#{session['waffle.session.container']} \"\n" +
                "    rescue Exception => e\n" +
                "      return e\n" +
                "    end\n" +
                "  end\n" +
                "  def bar\n" +
                "    \"HELLO WORLD #{request.local_name} #{request.local_port} \"\n" +
                "  end\n" +
                "end\n";

        // TODO ... update Registrar to have a "registerRubyScript(key, String)" .. and registerRubyScriptLocation(folder)
        ComponentAdapter componentAdapter = new RubyScriptComponentAdapter("foo_bar", script);
        picoRegistrar.registerComponentAdapter(componentAdapter);
    }
}
