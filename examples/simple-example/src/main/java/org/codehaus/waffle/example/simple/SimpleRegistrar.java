package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.example.simple.action.CalculatorController;
import org.codehaus.waffle.example.simple.action.HelloWorldController;
import org.codehaus.waffle.example.simple.action.PersonController;
import org.codehaus.waffle.example.simple.dao.SimplePersonDAO;

public class SimpleRegistrar extends AbstractRegistrar {

    public SimpleRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(SimplePersonDAO.class);
        register("helloworld", HelloWorldController.class);
        register("ajaxexample", AjaxExample.class);
    }

    @Override
    public void session() {
        register("calculator", CalculatorController.class);
        register("automobile", AutomobileController.class);
        //validation for automobile controller done in the controller itself
        //uncomment registration of validator if you prefer to override it
        //register("automobileValidator", AutomobileControllerValidator.class);
        register("person", PersonController.class);
    }
}
