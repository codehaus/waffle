package org.codehaus.waffle.example.jruby;

import org.codehaus.waffle.example.jruby.dao.SimplePersonDAO;
import org.codehaus.waffle.registrar.AbstractScriptedRegistrar;
import org.codehaus.waffle.registrar.Registrar;

import java.util.ArrayList;
import java.util.List;

public class JRubyRegistrar extends AbstractScriptedRegistrar {

    public JRubyRegistrar(Registrar delegate) {
        super(delegate);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void application() {
        register("the_dao", SimplePersonDAO.class);
        registerInstance("chicago", "bears");

        List myList = new ArrayList();
        myList.add(15);
        registerInstance(List.class, myList);

        registerScript("foobar", "FooBar"); // register the controller!
        registerScript("hello", "HelloController"); // register the controller!
        registerScript("person", "PersonController"); // register the controller!
    }

    @Override
    public void request() {
        register(RubyScriptReloader.class);
    }
}
