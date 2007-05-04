package com.thoughtworks.waffle.example.paranamer;

import com.thoughtworks.waffle.registrar.AbstractRegistrar;
import com.thoughtworks.waffle.registrar.RegisterWithApplication;
import com.thoughtworks.waffle.registrar.RegisterWithSession;
import com.thoughtworks.waffle.registrar.Registrar;
import com.thoughtworks.waffle.example.paranamer.action.CalculatorController;
import com.thoughtworks.waffle.example.paranamer.action.HelloWorldController;
import com.thoughtworks.waffle.example.paranamer.action.PersonController;
import com.thoughtworks.waffle.example.paranamer.dao.SimplePersonDAO;

public class MyRegistrar extends AbstractRegistrar {

    public MyRegistrar(Registrar delegate) {
        super(delegate);
    }

    @RegisterWithApplication
    public void application() {
        register(SimplePersonDAO.class);
        register("helloworld", HelloWorldController.class);
        register("ajaxexample", AjaxExample.class);
    }

    @RegisterWithSession
    public void session() {
        register("calculator", CalculatorController.class);
        register("people/person", PersonController.class);
    }
}
