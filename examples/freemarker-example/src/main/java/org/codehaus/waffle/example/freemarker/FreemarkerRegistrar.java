package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.example.freemarker.controller.PersonController;
import org.codehaus.waffle.example.freemarker.persister.SimplePersonPersister;

public class FreemarkerRegistrar extends AbstractRegistrar {

    public FreemarkerRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(SimplePersonPersister.class);
    }

    @Override
    public void session() {
        register("people/manage", PersonController.class);
    }
}
