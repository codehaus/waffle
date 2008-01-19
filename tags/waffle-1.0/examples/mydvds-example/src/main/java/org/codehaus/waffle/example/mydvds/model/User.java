package org.codehaus.waffle.example.mydvds.model;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class User {

    public static final String SESSION_KEY = "currentUser";

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min = 3, max = 20)
    private String login;

    @NotNull
    @Length(min = 6, max = 20)
    private String password;

    @NotNull
    @Length(min = 3, max = 100)
    private String name;

    @ManyToMany(mappedBy = "users")
    @Sort(type = SortType.NATURAL)
    private SortedSet<Dvd> dvds = new TreeSet<Dvd>();

    public User() {
    }

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Set<Dvd> getDvds() {
        return dvds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login: " + this.login + ", Name: " + this.name + ", Password: " + this.password;
    }
}
