package com.thoughtworks.waffle.example.paranamer.action;

import com.thoughtworks.waffle.example.paranamer.dao.PersonDAO;
import com.thoughtworks.waffle.example.paranamer.model.Person;

import java.util.Collection;

public class PersonController {
    private final PersonDAO personDAO;
    private Person person;

    public PersonController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public Collection<Person> getPersons() {
        return personDAO.findAll();
    }

    public void remove(Long personId) {
        personDAO.delete(personId);
    }

    public void select(Long id) {
        this.person = personDAO.findById(id);
    }

    public void save() {
        personDAO.save(person);
    }
    
    public void create() {
        person = new Person();
    }

    public void cancel() {
        person = null;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
