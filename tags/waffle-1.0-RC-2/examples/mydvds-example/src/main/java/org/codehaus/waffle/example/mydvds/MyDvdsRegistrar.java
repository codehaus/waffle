package org.codehaus.waffle.example.mydvds;

import org.codehaus.waffle.example.mydvds.action.DvdsController;
import org.codehaus.waffle.example.mydvds.action.UsersController;
import org.codehaus.waffle.example.mydvds.action.UsersControllerValidator;
import org.codehaus.waffle.example.mydvds.interceptor.AuthorizationInterceptor;
import org.codehaus.waffle.example.mydvds.model.Passport;
import org.codehaus.waffle.example.mydvds.persistence.HibernateSessionFactory;
import org.codehaus.waffle.example.mydvds.persistence.PersistenceManager;
import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;

public class MyDvdsRegistrar extends AbstractRegistrar {

    public MyDvdsRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(HibernateSessionFactory.class);
    }

    @Override
    public void session() {
        register(Passport.class);
    }

    @Override
    public void request() {
        register(PersistenceManager.class);
        register(AuthorizationInterceptor.class);
        register("dvds", DvdsController.class);
        register("users", UsersController.class);
        register("usersValidator", UsersControllerValidator.class);
    }
}
