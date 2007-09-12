package org.codehaus.waffle.example.jruby;

import org.codehaus.waffle.example.jruby.dao.SimplePersonDAO;
import org.codehaus.waffle.registrar.AbstractRubyAwareRegistrar;
import org.codehaus.waffle.registrar.Registrar;

import java.util.ArrayList;
import java.util.List;

public class JRubyRegistrar extends AbstractRubyAwareRegistrar {

    public JRubyRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(SimplePersonDAO.class);
        registerInstance("chicago", "bears");

        List myList = new ArrayList();
        myList.add(15);
        registerInstance(List.class, myList);

        registerRubyScript("foobar", "FooBar"); // register the controller!
        registerRubyScript("hello", "HelloController"); // register the controller!
        registerRubyScript("person", "PersonController"); // register the controller!
    }

    @Override
    public void request() {
        register(RubyScriptReloader.class);
    }
}
