package com.thoughtworks.waffle.example.freemarker;

import com.thoughtworks.waffle.registrar.AbstractRegistrar;
import com.thoughtworks.waffle.registrar.RegisterWithApplication;
import com.thoughtworks.waffle.registrar.RegisterWithSession;
import com.thoughtworks.waffle.registrar.Registrar;
import com.thoughtworks.waffle.example.freemarker.action.PersonController;
import com.thoughtworks.waffle.example.freemarker.dao.SimplePersonDAO;

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
