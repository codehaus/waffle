package org.codehaus.waffle.example.jruby;

import org.codehaus.waffle.example.jruby.dao.SimplePersonDAO;
import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.jruby.Ruby;

import java.util.ArrayList;
import java.util.List;

public class JRubyRegistrar extends AbstractRegistrar {
    private PicoRegistrar picoRegistrar;
    Ruby runtime = Ruby.getDefaultInstance();

    public JRubyRegistrar(Registrar delegate) {
        super(delegate);
        picoRegistrar = (PicoRegistrar) delegate;
    }

    @Override
    public void application() {
        register(SimplePersonDAO.class);
        registerInstance("chicago", "bears");

        List myList = new ArrayList();
        myList.add(15);
        registerInstance(List.class, myList);

        picoRegistrar.registerRubyScript("foobar", "FooBar"); // register the controller!
        picoRegistrar.registerRubyScript("hello", "HelloController"); // register the controller!
        picoRegistrar.registerRubyScript("person", "PersonController"); // register the controller!
    }

    @Override
    public void request() {
        register(RubyScriptReloader.class);
    }
}