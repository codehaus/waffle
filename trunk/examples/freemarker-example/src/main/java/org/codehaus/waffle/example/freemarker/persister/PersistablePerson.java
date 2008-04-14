package org.codehaus.waffle.example.freemarker.persister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.waffle.example.freemarker.model.Person;

public class PersistablePerson implements Person {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;
    private List<String> skills;

    public PersistablePerson() {
        id = new Long(0);
        firstName = "";
        lastName = "";
        email = "";
        dateOfBirth = new Date();
        skills = new ArrayList<String>();
    }

    public PersistablePerson(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.dateOfBirth = person.getDateOfBirth();
        this.skills = person.getSkills();
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

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    
    @Override
    public String toString() {
        return "Person "+firstName+" has skills "+skills;
    }
    
}
