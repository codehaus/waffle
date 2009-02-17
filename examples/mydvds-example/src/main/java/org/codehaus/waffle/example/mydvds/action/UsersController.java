package org.codehaus.waffle.example.mydvds.action;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.example.mydvds.model.Passport;
import org.codehaus.waffle.example.mydvds.model.User;
import org.codehaus.waffle.example.mydvds.persistence.PersistenceManager;
import org.codehaus.waffle.view.RedirectView;
import org.codehaus.waffle.view.View;

public class UsersController {

    private final PersistenceManager persistenceManager;
    private final Passport passport;

    public UsersController(PersistenceManager persistenceManager, Passport passport) {
        this.persistenceManager = persistenceManager;
        this.passport = passport;
    }

    @ActionMethod(parameters = {"login", "password"})
    public View login(String login, String password) {
        User user = this.persistenceManager.getUserDao().search(login, password);
        this.passport.setUser(user);

        return new RedirectView("dvds.waffle");
    }

    @ActionMethod(parameters = {"name", "login", "password"})
    public View add(String name, String login, String password) {
        this.persistenceManager.beginTransaction();
        this.persistenceManager.getUserDao().add(new User(name, login, password));
        this.persistenceManager.commit();

        return new View("user-added");
    }

    public View logout() {
        this.passport.invalidate();
        return new RedirectView("users.waffle");
    }
}
