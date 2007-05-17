package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.example.freemarker.action.PersonController;
import org.codehaus.waffle.example.freemarker.dao.SimplePersonDAO;

public class MyRegistrar extends AbstractRegistrar {

    public MyRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(SimplePersonDAO.class);
    }

    @Override
    public void session() {
        register("people/person", PersonController.class);
    }
}
