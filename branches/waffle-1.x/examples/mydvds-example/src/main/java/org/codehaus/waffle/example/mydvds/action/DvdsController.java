package org.codehaus.waffle.example.mydvds.action;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.example.mydvds.model.Dvd;
import org.codehaus.waffle.example.mydvds.model.Passport;
import org.codehaus.waffle.example.mydvds.model.User;
import org.codehaus.waffle.example.mydvds.persistence.PersistenceManager;

public class DvdsController {

    private final PersistenceManager factory;
    private final Passport passport;

    private Dvd dvd = new Dvd();

    public DvdsController(PersistenceManager factory, Passport passport) {
        this.factory = factory;
        this.passport = passport;
    }

    // just to be intercepted
    @ActionMethod(asDefault=true)
    public void index() {
    }

    public void save() {
        this.factory.beginTransaction();
        System.out.println("dvd = " + dvd);
        User currentUser = this.passport.getUser();
        this.dvd.getUsers().add(currentUser);
        currentUser.getDvds().add(this.dvd);
        this.factory.getDvdDao().add(this.dvd);
        this.factory.commit();
        
        this.dvd = new Dvd();
    }

    public Dvd getDvd() {
        return dvd;
    }

    public Passport getPassport() {
        return passport;
    }
}
