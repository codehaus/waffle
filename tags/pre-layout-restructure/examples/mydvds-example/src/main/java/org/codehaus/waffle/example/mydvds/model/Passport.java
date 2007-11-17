package org.codehaus.waffle.example.mydvds.model;

/**
 * @Author Fabio Kung
 */
public class Passport {
    private User user;

    public boolean isValid() {
        return this.user != null && this.user.getLogin().trim().length() != 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void invalidate() {
        this.user = null;
    }
}
