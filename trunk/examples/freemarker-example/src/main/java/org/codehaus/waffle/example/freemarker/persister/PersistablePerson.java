package org.codehaus.waffle.example.freemarker.persister;

import java.util.Date;

import org.codehaus.waffle.example.freemarker.model.Person;

public class PersistablePerson implements Person {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;

    public PersistablePerson() {
        id = new Long(0);
        firstName = "";
        lastName = "";
        email = "";
        dateOfBirth = new Date();
    }

    public PersistablePerson(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.dateOfBirth = person.getDateOfBirth();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
