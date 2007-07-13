package org.codehaus.waffle.example.mydvds.action;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.example.mydvds.persistence.PersistenceManager;
import org.codehaus.waffle.example.mydvds.model.Passport;
import org.codehaus.waffle.example.mydvds.model.User;
import org.codehaus.waffle.view.RedirectView;
import org.codehaus.waffle.view.View;

import java.util.HashMap;

public class UsersController {

    private final PersistenceManager factory;
    private final Passport passport;

    public UsersController(PersistenceManager factory, Passport passport) {
        this.factory = factory;
        this.passport = passport;
    }

    @ActionMethod(parameters = {"login", "password"})
    public View login(String login, String password) {

        User user = this.factory.getUserDao().search(login, password);
        this.passport.setUser(user);

        return new RedirectView("dvds.waffle", this, new HashMap());
    }

    @ActionMethod(parameters = {"name", "login", "password"})
    public View add(String name, String login, String password) {
        this.factory.beginTransaction();
        this.factory.getUserDao().add(new User(name, login, password));
        this.factory.commit();

        return new View("user-added.jspx", this);
    }

    public View logout() {
        this.passport.invalidate();
        return new View("index.jspx", this);
    }
}
