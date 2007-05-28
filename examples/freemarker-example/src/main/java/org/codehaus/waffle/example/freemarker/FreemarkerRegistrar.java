package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.example.freemarker.action.PersonController;
import org.codehaus.waffle.example.freemarker.dao.SimplePersonDAO;

public class FreemarkerRegistrar extends AbstractRegistrar {

    public FreemarkerRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(SimplePersonDAO.class);
        register("people/person", PersonController.class);
    }

    @Override
    public void session() {
        // TODO it fails if registered at session with 
        // Waffle could not find session-level context container.
        //register("people/person", PersonController.class);
    }
}
