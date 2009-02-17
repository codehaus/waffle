package org.codehaus.waffle.example.mydvds;

import org.codehaus.waffle.example.mydvds.action.DvdsController;
import org.codehaus.waffle.example.mydvds.action.UsersController;
import org.codehaus.waffle.example.mydvds.action.UsersControllerValidator;
import org.codehaus.waffle.example.mydvds.interceptor.AuthorizationInterceptor;
import org.codehaus.waffle.example.mydvds.model.Passport;
import org.codehaus.waffle.example.mydvds.persistence.HibernateSessionFactory;
import org.codehaus.waffle.example.mydvds.persistence.PersistenceManager;
import org.codehaus.waffle.context.WaffleWebappComposer;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;

public class MyDvdsWebappComposer extends WaffleWebappComposer {


    @Override
    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        super.composeApplication(picoContainer, servletContext);
        picoContainer.addComponent(HibernateSessionFactory.class);
    }

    @Override
    public void composeSession(MutablePicoContainer picoContainer) {
        super.composeSession(picoContainer);
        picoContainer.addComponent(Passport.class);
    }

    @Override
    public void composeRequest(MutablePicoContainer picoContainer) {
        super.composeRequest(picoContainer);
        picoContainer.addComponent(PersistenceManager.class);
        picoContainer.addComponent(AuthorizationInterceptor.class);
        picoContainer.addComponent("dvds", DvdsController.class);
        picoContainer.addComponent("users", UsersController.class);
        picoContainer.addComponent("usersValidator", UsersControllerValidator.class);
    }

}
