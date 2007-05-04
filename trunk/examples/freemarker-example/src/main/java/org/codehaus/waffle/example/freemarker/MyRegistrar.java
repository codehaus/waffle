package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.RegisterWithApplication;
import org.codehaus.waffle.registrar.RegisterWithSession;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.example.freemarker.action.PersonController;
import org.codehaus.waffle.example.freemarker.dao.SimplePersonDAO;

public class MyRegistrar extends AbstractRegistrar {

    public MyRegistrar(Registrar delegate) {
        super(delegate);
    }

    @RegisterWithApplication
    public void application() {
        register(SimplePersonDAO.class);
    }

    @RegisterWithSession
    public void session() {
        register("people/person", PersonController.class);
    }
}
