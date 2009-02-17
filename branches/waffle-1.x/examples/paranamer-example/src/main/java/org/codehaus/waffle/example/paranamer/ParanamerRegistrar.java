package org.codehaus.waffle.example.paranamer;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.example.paranamer.action.CalculatorController;
import org.codehaus.waffle.example.paranamer.action.HelloWorldController;
import org.codehaus.waffle.example.paranamer.action.PersonController;
import org.codehaus.waffle.example.paranamer.dao.SimplePersonDAO;

public class ParanamerRegistrar extends AbstractRegistrar {

    public ParanamerRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(SimplePersonDAO.class);
        register("helloworld", HelloWorldController.class);
        register("ajaxexample", AjaxExample.class);
        register("people/person", PersonController.class);
    }

    @Override
    public void session() {
        register("calculator", CalculatorController.class);
    }
}
